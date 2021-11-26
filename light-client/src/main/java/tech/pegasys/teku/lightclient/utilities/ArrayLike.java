package tech.pegasys.teku.lightclient.utilities;;

//Generic Array
public class ArrayLike<T>
{
    private final Object[] arr;
    public final int number;
 
    // constructor
    public ArrayLike(int number)
    {
        // Creates a new object array of the specified length
        arr = new Object[number];
        this.number = number;
    }
 
    // Method to get object present at index `i` in the array
    T get(int i) {
        @SuppressWarnings("unchecked")
        final T e = (T)arr[i];
        return e;
    }
 
    // Method to set a value `e` at index `i` in the array
    void set(int i, T e) {
        arr[i] = e;
    }

}
