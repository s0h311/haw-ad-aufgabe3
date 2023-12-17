package application;

import utility.Item;

import java.util.ArrayList;
import java.util.Arrays;

public class RadixSortWithArray {

  private static final int sliceSize = 4;
  private static final int sliceCount = 64 / sliceSize;     // 64/4 = 16 ,  long has 64bit and nibbles/slices have 4 bits => 16slices resp. runs
  private static final int bucketCount = 1 << sliceSize;    //  2^4 = 16 "buckets" (resp. queues) are required
  private static final int sliceMask = bucketCount - 1;     // 16-1 = 0xF is mask for identification of "related" slice (resp. bucket/queue)

  public Item[] sort(Item[] arrayToSort) {
    @SuppressWarnings("unchecked")
    ArrayList<Item>[][] arrayMatrix = (ArrayList<Item>[][]) new ArrayList[2][bucketCount];

    Arrays.fill(arrayMatrix[0], new ArrayList<Item>());
    Arrays.fill(arrayMatrix[1], new ArrayList<Item>());

    int oldArrayIndex = 0b0;
    int currentArrayIndex = 0b1;
    int shift = 0;

    for (Item item : arrayToSort) {
      int currentBucketIndex = (item.sortKey() >>> shift) & sliceMask;
      System.err.println("ItemSortKey: " + item.sortKey() + " currentBucketIndex: " + currentBucketIndex + "\n");
      arrayMatrix[currentArrayIndex][currentBucketIndex].add(item);
    }

    for (int stillToDo = sliceCount - 1; stillToDo > 0; stillToDo--) {
      shift += sliceSize;
      oldArrayIndex ^= 0b1;
      currentArrayIndex ^= 0b1;

      for (int bucketIndexOldArray = 0; bucketIndexOldArray < bucketCount; bucketIndexOldArray++) {
        while (!arrayMatrix[oldArrayIndex][bucketIndexOldArray].isEmpty()) {
          Item currentItem = arrayMatrix[oldArrayIndex][bucketIndexOldArray].remove(0);
          int currentBucketIndex = (currentItem.sortKey() >>> shift) & sliceMask;
          System.out.println("ItemSortKey: " + currentItem.sortKey() + " currentBucketIndex: " + currentBucketIndex + "\n");
          arrayMatrix[currentArrayIndex][currentBucketIndex].add(currentItem);
        }
      }
    }

    int arrayIndex = 0;
    for (int currentBucketIndex = 0; currentBucketIndex < bucketCount; currentBucketIndex++) {
      while (!arrayMatrix[currentArrayIndex][currentBucketIndex].isEmpty()) {
        arrayToSort[arrayIndex] = arrayMatrix[currentArrayIndex][currentBucketIndex].remove(0);
        arrayIndex++;
      }
    }

    return arrayToSort;
  }
}