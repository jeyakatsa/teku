/*
 * Copyright 2020 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tech.pegasys.teku.protoarray;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static tech.pegasys.teku.protoarray.ProtoNodeValidationStatus.INVALID;
import static tech.pegasys.teku.protoarray.ProtoNodeValidationStatus.OPTIMISTIC;
import static tech.pegasys.teku.protoarray.ProtoNodeValidationStatus.VALID;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tuweni.bytes.Bytes32;
import tech.pegasys.teku.infrastructure.exceptions.FatalServiceFailureException;
import tech.pegasys.teku.infrastructure.unsigned.UInt64;

public class ProtoArray {
  private static final Logger LOG = LogManager.getLogger();

  private int pruneThreshold;

  private UInt64 justifiedEpoch;
  private UInt64 finalizedEpoch;
  // The epoch of our initial startup state
  // When starting from genesis, this value is zero (genesis epoch)
  private final UInt64 initialEpoch;

  /**
   * Lists all the known nodes. It is guaranteed that a node will be after its parent in the list.
   *
   * <p>The list may contain nodes which have been removed from the indices collection either
   * because they are now before the finalized checkpoint but pruning has not yet occurred or
   * because they extended from a now-invalid chain and were removed. This avoids having to update
   * the indices to entries in the list too often.
   */
  private final List<ProtoNode> nodes = new ArrayList<>();

  /**
   * protoArrayIndices allows root lookup to retrieve indices of protoNodes without looking through
   * the nodes list
   *
   * <p>Needs to be Maintained when nodes are added or removed from the nodes list.
   */
  private final ProtoArrayIndices indices = new ProtoArrayIndices();

  ProtoArray(
      int pruneThreshold, UInt64 justifiedEpoch, UInt64 finalizedEpoch, UInt64 initialEpoch) {
    this.pruneThreshold = pruneThreshold;
    this.justifiedEpoch = justifiedEpoch;
    this.finalizedEpoch = finalizedEpoch;
    this.initialEpoch = initialEpoch;
  }

  public static ProtoArrayBuilder builder() {
    return new ProtoArrayBuilder();
  }

  public boolean contains(final Bytes32 root) {
    return indices.contains(root);
  }

  public Optional<Integer> getIndexByRoot(final Bytes32 root) {
    return indices.get(root);
  }

  public Optional<ProtoNode> getProtoNode(final Bytes32 root) {
    return indices
        .get(root)
        .flatMap(
            blockIndex -> {
              if (blockIndex < getTotalTrackedNodeCount()) {
                return Optional.of(getNodeByIndex(blockIndex));
              }
              return Optional.empty();
            });
  }

  public List<ProtoNode> getNodes() {
    return nodes;
  }

  public void setPruneThreshold(int pruneThreshold) {
    this.pruneThreshold = pruneThreshold;
  }

  /**
   * Register a block with the fork choice. It is only sane to supply a `None` parent for the
   * genesis block.
   */
  public void onBlock(
      UInt64 blockSlot,
      Bytes32 blockRoot,
      Bytes32 parentRoot,
      Bytes32 stateRoot,
      UInt64 justifiedEpoch,
      UInt64 finalizedEpoch,
      Bytes32 executionBlockHash,
      boolean optimisticallyProcessed) {
    if (indices.contains(blockRoot)) {
      return;
    }

    int nodeIndex = getTotalTrackedNodeCount();

    ProtoNode node =
        new ProtoNode(
            blockSlot,
            stateRoot,
            blockRoot,
            parentRoot,
            indices.get(parentRoot),
            justifiedEpoch,
            finalizedEpoch,
            executionBlockHash,
            UInt64.ZERO,
            Optional.empty(),
            Optional.empty(),
            optimisticallyProcessed ? OPTIMISTIC : VALID);

    indices.add(blockRoot, nodeIndex);
    nodes.add(node);

    updateBestDescendantOfParent(node, nodeIndex);
  }

  /**
   * Follows the best-descendant links to find the best-block (i.e., head-block). This excludes any
   * optimistic nodes.
   *
   * @param justifiedRoot the root of the justified checkpoint
   * @return the best node according to fork choice
   */
  public Optional<ProtoNode> findHead(
      Bytes32 justifiedRoot, UInt64 justifiedEpoch, UInt64 finalizedEpoch) {
    return findHead(justifiedRoot, justifiedEpoch, finalizedEpoch, ProtoNode::isFullyValidated);
  }

  /**
   * Follows the best-descendant links to find the best-block (i.e., head-block), including any
   * optimistic nodes which have not yet been fully validated.
   *
   * @param justifiedRoot the root of the justified checkpoint
   * @return the best node according to fork choice
   */
  public ProtoNode findOptimisticHead(
      Bytes32 justifiedRoot, UInt64 justifiedEpoch, UInt64 finalizedEpoch) {
    return findHead(justifiedRoot, justifiedEpoch, finalizedEpoch, node -> !node.isInvalid())
        .orElseThrow(fatalException("Finalized block was found to be invalid."));
  }

  private Supplier<FatalServiceFailureException> fatalException(final String message) {
    return () -> new FatalServiceFailureException("fork choice", message);
  }

  private Optional<ProtoNode> findHead(
      final Bytes32 justifiedRoot,
      final UInt64 justifiedEpoch,
      final UInt64 finalizedEpoch,
      final Predicate<ProtoNode> hasSuitableValidationState) {
    if (!this.justifiedEpoch.equals(justifiedEpoch)
        || !this.finalizedEpoch.equals(finalizedEpoch)) {
      this.justifiedEpoch = justifiedEpoch;
      this.finalizedEpoch = finalizedEpoch;
      // Justified or finalized epoch changed so we have to re-evaluate all best descendants.
      applyToNodes(this::updateBestDescendantOfParent);
    }
    int justifiedIndex =
        indices
            .get(justifiedRoot)
            .orElseThrow(fatalException("Invalid or unknown justified root: " + justifiedRoot));

    ProtoNode justifiedNode = getNodeByIndex(justifiedIndex);

    if (!hasSuitableValidationState.test(justifiedNode)) {
      return Optional.empty();
    }

    int bestDescendantIndex = justifiedNode.getBestDescendantIndex().orElse(justifiedIndex);
    ProtoNode bestNode = getNodeByIndex(bestDescendantIndex);

    // Normally the best descendant index would point straight to chain head, but onBlock only
    // updates the parent, not all the ancestors. When applyScoreChanges runs it propagates the
    // change back up and everything works, but we run findHead to determine if the new block should
    // become the best head so need to follow down the chain.
    while (bestNode.getBestDescendantIndex().isPresent()
        && hasSuitableValidationState.test(bestNode)) {
      bestDescendantIndex = bestNode.getBestDescendantIndex().get();
      bestNode = getNodeByIndex(bestDescendantIndex);
    }

    // Walk backwards to find the last fully validated node in the chain
    while (!hasSuitableValidationState.test(bestNode)) {
      final Optional<Integer> maybeParentIndex = bestNode.getParentIndex();
      if (maybeParentIndex.isEmpty()) {
        // No node on this chain with sufficient validity.
        return Optional.empty();
      }
      final int parentIndex = maybeParentIndex.get();
      bestNode = getNodeByIndex(parentIndex);
    }

    // Perform a sanity check that the node is indeed valid to be the head.
    if (!nodeIsViableForHead(bestNode) && !bestNode.equals(justifiedNode)) {
      throw new RuntimeException("ProtoArray: Best node is not viable for head");
    }
    return Optional.of(bestNode);
  }

  public void markNodeValid(final Bytes32 blockRoot) {
    final Optional<ProtoNode> maybeNode = getProtoNode(blockRoot);
    if (maybeNode.isEmpty()) {
      // Most likely just pruned prior to the validation result being received.
      LOG.debug("Couldn't mark block {} valid because it was unknown", blockRoot);
      return;
    }
    final ProtoNode node = maybeNode.get();
    node.setValidationStatus(VALID);
    Optional<Integer> parentIndex = node.getParentIndex();
    while (parentIndex.isPresent()) {
      final ProtoNode parentNode = getNodeByIndex(parentIndex.get());
      if (parentNode.isFullyValidated()) {
        break;
      }
      parentNode.setValidationStatus(VALID);
      parentIndex = parentNode.getParentIndex();
    }
  }

  public void markNodeInvalid(final Bytes32 blockRoot) {
    final Optional<Integer> maybeIndex = indices.get(blockRoot);
    if (maybeIndex.isEmpty()) {
      LOG.debug("Couldn't update status for block {} because it was unknown", blockRoot);
      return;
    }
    final int index = maybeIndex.get();
    final ProtoNode node = getNodeByIndex(index);
    node.setValidationStatus(INVALID);
    removeBlockRoot(blockRoot);
    markDescendantsAsInvalid(index);
    // Applying zero deltas causes the newly marked INVALID nodes to have their weight set to 0
    applyDeltas(new ArrayList<>(Collections.nCopies(getTotalTrackedNodeCount(), 0L)));
  }

  private void markDescendantsAsInvalid(final int index) {
    final IntSet invalidParents = new IntOpenHashSet();
    invalidParents.add(index);
    // Need to mark all nodes extending from this one as invalid
    // Descendant nodes must be later in the array so can start from next index
    for (int i = index + 1; i < nodes.size(); i++) {
      final ProtoNode possibleDescendant = getNodeByIndex(i);
      if (possibleDescendant.getParentIndex().isEmpty()) {
        continue;
      }
      if (invalidParents.contains((int) possibleDescendant.getParentIndex().get())) {
        possibleDescendant.setValidationStatus(INVALID);
        removeBlockRoot(possibleDescendant.getBlockRoot());
        invalidParents.add(i);
      }
    }
  }

  /**
   * Iterate backwards through the array, touching all nodes and their parents and potentially the
   * bestChildIndex of each parent.
   *
   * <p>The structure of the `nodes` array ensures that the child of each node is always touched
   * before its parent.
   *
   * <p>For each node, the following is done:
   *
   * <ul>
   *   <li>Update the node's weight with the corresponding delta.
   *   <li>Back-propagate each node's delta to its parents delta.
   *   <li>Compare the current node with the parents best child, updating it if the current node
   *       should become the best child.
   *   <li>If required, update the parents best descendant with the current node or its best
   *       descendant.
   * </ul>
   *
   * @param deltas
   * @param justifiedEpoch
   * @param finalizedEpoch
   */
  public void applyScoreChanges(List<Long> deltas, UInt64 justifiedEpoch, UInt64 finalizedEpoch) {
    checkArgument(
        deltas.size() == getTotalTrackedNodeCount(),
        "ProtoArray: Invalid delta length expected %s but got %s",
        getTotalTrackedNodeCount(),
        deltas.size());

    if (!justifiedEpoch.equals(this.justifiedEpoch)
        || !finalizedEpoch.equals(this.finalizedEpoch)) {
      this.justifiedEpoch = justifiedEpoch;
      this.finalizedEpoch = finalizedEpoch;
    }

    applyDeltas(deltas);
  }

  public int getTotalTrackedNodeCount() {
    return nodes.size();
  }

  /**
   * Update the tree with new finalization information. The tree is only actually pruned if both of
   * the two following criteria are met:
   *
   * <ul>
   *   <li>The supplied finalized epoch and root are different to the current values.
   *   <li>The number of nodes in `this` is at least `this.pruneThreshold`.
   * </ul>
   *
   * @param finalizedRoot
   */
  public void maybePrune(Bytes32 finalizedRoot) {
    int finalizedIndex =
        indices
            .get(finalizedRoot)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "ProtoArray: Finalized root is unknown " + finalizedRoot.toHexString()));

    if (finalizedIndex < pruneThreshold) {
      // Pruning at small numbers incurs more cost than benefit.
      return;
    }

    // Remove the `indices` key/values for all the to-be-deleted nodes.
    for (int nodeIndex = 0; nodeIndex < finalizedIndex; nodeIndex++) {
      Bytes32 root = getNodeByIndex(nodeIndex).getBlockRoot();
      indices.remove(root);
    }

    // Drop all the nodes prior to finalization.
    nodes.subList(0, finalizedIndex).clear();

    indices.offsetIndexes(finalizedIndex);

    // Iterate through all the existing nodes and adjust their indices to match the
    // new layout of nodes.
    for (ProtoNode node : nodes) {
      node.getParentIndex()
          .ifPresent(
              parentIndex -> {
                // If node.parentIndex is less than finalizedIndex, set is to None.
                if (parentIndex < finalizedIndex) {
                  node.setParentIndex(Optional.empty());
                } else {
                  node.setParentIndex(Optional.of(parentIndex - finalizedIndex));
                }
              });

      node.getBestChildIndex()
          .ifPresent(
              bestChildIndex -> {
                int newBestChildIndex = bestChildIndex - finalizedIndex;
                checkState(
                    newBestChildIndex >= 0, "ProtoArray: New best child index is less than 0");
                node.setBestChildIndex(Optional.of(newBestChildIndex));
              });

      node.getBestDescendantIndex()
          .ifPresent(
              bestDescendantIndex -> {
                int newBestDescendantIndex = bestDescendantIndex - finalizedIndex;
                checkState(
                    newBestDescendantIndex >= 0,
                    "ProtoArray: New best descendant index is less than 0");
                node.setBestDescendantIndex(Optional.of(newBestDescendantIndex));
              });
    }
  }

  /**
   * Observe the parent at `parentIndex` with respect to the child at `childIndex` and potentially
   * modify the `parent.bestChild` and `parent.bestDescendant` values.
   *
   * <p>## Detail
   *
   * <p>There are four outcomes:
   *
   * <ul>
   *   <li>The child is already the best child but it's now invalid due to a FFG change and should
   *       be removed.
   *   <li>The child is already the best child and the parent is updated with the new best
   *       descendant.
   *   <li>The child is not the best child but becomes the best child.
   *   <li>The child is not the best child and does not become the best child.
   * </ul>
   *
   * @param parentIndex
   * @param childIndex
   */
  @SuppressWarnings("StatementWithEmptyBody")
  private void maybeUpdateBestChildAndDescendant(int parentIndex, int childIndex) {
    ProtoNode child = getNodeByIndex(childIndex);
    ProtoNode parent = getNodeByIndex(parentIndex);

    boolean childLeadsToViableHead = nodeLeadsToViableHead(child);

    parent
        .getBestChildIndex()
        .ifPresentOrElse(
            bestChildIndex -> {
              if (bestChildIndex.equals(childIndex) && !childLeadsToViableHead) {
                // If the child is already the best-child of the parent but it's not viable for
                // the head, remove it.
                changeToNone(parent);
              } else if (bestChildIndex.equals(childIndex)) {
                // If the child is the best-child already, set it again to ensure that the
                // best-descendant of the parent is updated.
                changeToChild(parent, childIndex);
              } else {
                ProtoNode bestChild = getNodeByIndex(bestChildIndex);

                boolean bestChildLeadsToViableHead = nodeLeadsToViableHead(bestChild);

                if (childLeadsToViableHead && !bestChildLeadsToViableHead) {
                  // The child leads to a viable head, but the current best-child doesn't.
                  changeToChild(parent, childIndex);
                } else if (!childLeadsToViableHead && bestChildLeadsToViableHead) {
                  // The best child leads to a viable head, but the child doesn't.
                  // No change.
                } else if (child.getWeight().equals(bestChild.getWeight())) {
                  // Tie-breaker of equal weights by root.
                  if (child
                          .getBlockRoot()
                          .toHexString()
                          .compareTo(bestChild.getBlockRoot().toHexString())
                      >= 0) {
                    changeToChild(parent, childIndex);
                  } else {
                    // No change.
                  }
                } else {
                  // Choose the winner by weight.
                  if (child.getWeight().compareTo(bestChild.getWeight()) >= 0) {
                    changeToChild(parent, childIndex);
                  } else {
                    // No change.
                  }
                }
              }
            },
            () -> {
              if (childLeadsToViableHead) {
                // There is no current best-child and the child is viable.
                changeToChild(parent, childIndex);
              } else {
                // There is no current best-child but the child is not not viable.
                // No change.
              }
            });
  }

  /**
   * Helper for maybeUpdateBestChildAndDescendant
   *
   * @param parent
   * @param childIndex
   */
  private void changeToChild(ProtoNode parent, int childIndex) {
    ProtoNode child = getNodeByIndex(childIndex);
    parent.setBestChildIndex(Optional.of(childIndex));
    parent.setBestDescendantIndex(Optional.of(child.getBestDescendantIndex().orElse(childIndex)));
  }

  /**
   * Helper for maybeUpdateBestChildAndDescendant
   *
   * @param parent
   */
  private void changeToNone(ProtoNode parent) {
    parent.setBestChildIndex(Optional.empty());
    parent.setBestDescendantIndex(Optional.empty());
  }

  /**
   * Indicates if the node itself is viable for the head, or if it's best descendant is viable for
   * the head.
   *
   * @param node
   * @return
   */
  private boolean nodeLeadsToViableHead(ProtoNode node) {
    boolean bestDescendantIsViableForHead =
        node.getBestDescendantIndex()
            .map(this::getNodeByIndex)
            .map(this::nodeIsViableForHead)
            .orElse(false);

    return bestDescendantIsViableForHead || nodeIsViableForHead(node);
  }

  /**
   * This is the equivalent to the <a
   * href="https://github.com/ethereum/eth2.0-specs/blob/v0.10.0/specs/phase0/fork-choice.md#filter_block_tree">filter_block_tree</a>
   * function in the eth2 spec:
   *
   * <p>Any node that has a different finalized or justified epoch should not be viable for the
   * head.
   *
   * @param node
   * @return
   */
  public boolean nodeIsViableForHead(ProtoNode node) {
    return (node.getJustifiedEpoch().equals(justifiedEpoch) || justifiedEpoch.equals(initialEpoch))
        && (node.getFinalizedEpoch().equals(finalizedEpoch) || finalizedEpoch.equals(initialEpoch));
  }

  public UInt64 getJustifiedEpoch() {
    return justifiedEpoch;
  }

  public UInt64 getFinalizedEpoch() {
    return finalizedEpoch;
  }

  public UInt64 getInitialEpoch() {
    return initialEpoch;
  }

  /**
   * Removes a block root from the lookup map. The actual node is not removed from the protoarray to
   * avoid recalculating indices. As a result, looking up the block by root will not find it but it
   * may still be "found" when iterating through all nodes or following links to parent or ancestor
   * nodes.
   *
   * @param blockRoot the block root to remove from the lookup map.
   */
  public void removeBlockRoot(final Bytes32 blockRoot) {
    indices.remove(blockRoot);
  }

  private void applyDeltas(final List<Long> deltas) {
    applyToNodes((node, nodeIndex) -> applyDelta(deltas, node, nodeIndex));
    applyToNodes(this::updateBestDescendantOfParent);
  }

  private void updateBestDescendantOfParent(final ProtoNode node, final int nodeIndex) {
    node.getParentIndex()
        .ifPresent(parentIndex -> maybeUpdateBestChildAndDescendant(parentIndex, nodeIndex));
  }

  private void applyDelta(final List<Long> deltas, final ProtoNode node, final int nodeIndex) {
    // If the node is invalid, remove any existing weight.
    long nodeDelta = node.isInvalid() ? -node.getWeight().longValue() : deltas.get(nodeIndex);
    node.adjustWeight(nodeDelta);

    if (node.getParentIndex().isPresent()) {
      int parentIndex = node.getParentIndex().get();
      deltas.set(parentIndex, deltas.get(parentIndex) + nodeDelta);
    }
  }

  private void applyToNodes(final NodeVisitor action) {
    for (int nodeIndex = getTotalTrackedNodeCount() - 1; nodeIndex >= 0; nodeIndex--) {
      final ProtoNode node = getNodeByIndex(nodeIndex);

      // No point processing the genesis block.
      if (node.getBlockRoot().equals(Bytes32.ZERO)) {
        continue;
      }
      action.onNode(node, nodeIndex);
    }
  }

  public Map<Bytes32, Integer> getRootIndices() {
    return indices.getRootIndices();
  }

  private ProtoNode getNodeByIndex(final int index) {
    return checkNotNull(nodes.get(index), "Missing node %s", index);
  }

  private interface NodeVisitor {
    void onNode(ProtoNode node, int nodeIndex);
  }
}
