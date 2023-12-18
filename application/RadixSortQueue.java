package application;

import java.util.LinkedList;
import java.util.Queue;

public class RadixSortQueue<T> implements ArraySorter<T> {

  private final int sliceSize = 8;
  private final int sliceCount = 64 / sliceSize;
  private final int bucketCount = 1 << sliceSize;
  private final int sliceMask = bucketCount - 1;
  private SortKeyGetter<T> sortKeyGetter;

  public RadixSortQueue() {
    this.sortKeyGetter = new SortKeyGetter<>();
  }

  public void sort(T[] arrayToBeSorted) {

    // TODO Die QueueMatrix mit LinkedLists befüllen
    @SuppressWarnings("unchecked")
    Queue<T>[][] queueMatrix = new Queue[2][bucketCount];
    for (int queueListSelectionIdx = 2; --queueListSelectionIdx >= 0; ) {
      for (int bc = bucketCount; --bc >= 0; ) {
        queueMatrix[queueListSelectionIdx][bc] = new LinkedList<>();
      }
    }

    int oldQueueList = 0b0;
    int curQueueList = 0b1;
    int shift = 0;

    for (T item : arrayToBeSorted) {
      int bucketIndexCurrQueueList = (sortKeyGetter.getSortKey(item) >>> shift) & sliceMask;
      // TODO 1 & 15 = 1, 2 & 15 = 2, 15 & 15 = 15, 16 & 15 = 0
      queueMatrix[curQueueList][bucketIndexCurrQueueList].add(item);
    }

    for (int stillToDo = sliceCount - 1; stillToDo > 0; stillToDo--) {
      shift += sliceSize;
      // TODO zwischen alter und neuer Liste switchen
      oldQueueList ^= 0b1;
      curQueueList ^= 0b1;

      for (int bucketIndexOldQueueList = 0; bucketIndexOldQueueList < bucketCount; bucketIndexOldQueueList++) {
        while (!queueMatrix[oldQueueList][bucketIndexOldQueueList].isEmpty()) {
          T currentItem = queueMatrix[oldQueueList][bucketIndexOldQueueList].remove();
          int bucketIndexCurrQueueList = (sortKeyGetter.getSortKey(currentItem) >>> shift) & sliceMask;
          queueMatrix[curQueueList][bucketIndexCurrQueueList].add(currentItem);
        }
      }
    }

    int arrayIndex = 0;
    for (int bucketIndexCurrQueueList = 0; bucketIndexCurrQueueList < bucketCount; bucketIndexCurrQueueList++) {
      while (!queueMatrix[curQueueList][bucketIndexCurrQueueList].isEmpty()) {
        arrayToBeSorted[arrayIndex++] = queueMatrix[curQueueList][bucketIndexCurrQueueList].remove();
      }
    }
  }
}
