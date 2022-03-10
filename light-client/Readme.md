# Experimental Light Client for Teku

### [Step-By-Step Guide](https://hackmd.io/ZFINvY5fRUGrLK-BteZrug?view)

### Useful Link: [Research & Development](https://github.com/jeyakatsa/Altair----Minimal-Light-Client-Prototype/blob/main/Teku-Light-Client-Server-R%26D.md)

*Brief Description:* The Light-Client for Teku is created with Java. This process is ever so evolving as need be but should provide a straight forward methodical approach into building it (open to more efficient methods).

### Step 1: Download/Install Necessities

| Language Needed   | Links                   |
| ------------------|:----------------------- |
| Java 11           | https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html |

| Build/Test Tool Needed   | Links                   |
| -------------------------|:----------------------- |
| Gradle             | https://gradle.org/install/ |

| Coding Tool Needed   | Links                   |
| -----------------------|:----------------------- |
| IntelliJ         | https://www.jetbrains.com/idea/ |

### Step 2: Fork Teku repo & clone.

Teku repo: https://github.com/ConsenSys/teku.git

```shell script
git clone https://github.com/ConsenSys/teku.git
```

### Step 3: Build

Before building, run `./gradlew spotlessApply`. Then run,`./gradlew`. Then run, `./gradlew distTar installDist`.
