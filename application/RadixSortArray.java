package application;

public class RadixSortArray<T> implements ArraySorter<T> {

  private final int sliceSize = 8;
  private final int integerSize = 32; // Integer has 32 bits in Java
  private final int bucketCount = 1 << sliceSize; // 256
  private final int sliceMask = bucketCount - 1;
  private SortKeyGetter<T> sortKeyGetter;

  public RadixSortArray() {
    this.sortKeyGetter = new SortKeyGetter<>();
  }

  public void sort(T[] arrayToSort) {
    this.sortKeyGetter = sortKeyGetter;

    @SuppressWarnings("unchecked")
    T[] tempArray = (T[]) (new Object[arrayToSort.length]);

    for (int shift = 0; shift < integerSize; shift += sliceSize) {
      countSort(arrayToSort, tempArray, shift);
      System.arraycopy(tempArray, 0, arrayToSort, 0, arrayToSort.length);
    }
  }

  private void countSort(T[] array, T[] output, int shift) {
    int[] count = new int[bucketCount];

    for (T item : array) {
      int bucketIndex = (sortKeyGetter.getSortKey(item) >>> shift) & sliceMask;
      count[bucketIndex]++;
    }

    for (int i = 1; i < bucketCount; i++) {
      count[i] += count[i - 1];
    }

    for (int i = array.length - 1; i >= 0; i--) {
      T item = array[i];
      int bucketIndex = (sortKeyGetter.getSortKey(item) >>> shift) & sliceMask;
      output[count[bucketIndex] - 1] = item;
      count[bucketIndex]--;
    }
    System.arraycopy(output, 0, array, 0, array.length);
  }
}