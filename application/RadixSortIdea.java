package application;


import java.util.LinkedList;
import java.util.Queue;


// RadixSort for long[] configured for 4bit-slices/nibbles
// Demo to explain RadixSort idea via source code
//
// abbreviations
// -------------
// bIdxOldQl ::= bucket index (of) old queue_list
// bIdxCurQl ::= bucket index (of) current queue_list
public class RadixSortIdea {

  // this implementation works with 4bit slices (nibbles) resulting in 2^4=16 "buckets" resp. queues
  private static final int sliceSize = 4;
  private static final int sliceCnt = 64 / sliceSize;     // 64/4 = 16 ,  long has 64bit and nibbles/slices have 4 bits => 16slices resp. runs
  private static final int bucketCnt = 1 << sliceSize;    //  2^4 = 16 "buckets" (resp. queues) are required
  private static final int sliceMask = bucketCnt - 1;     // 16-1 = 0xF is mask for identification of "related" slice (resp. bucket/queue)


  public long[] sort(long[] arrayToBeSorted) {

    //TODO Die QueueMatrix mit LinkedLists befüllen
    @SuppressWarnings("unchecked")
    Queue<Long>[][] queueMatrix = (Queue<Long>[][]) (new Queue[2][bucketCnt]);
    for (int queueListSelectionIdx = 2; --queueListSelectionIdx >= 0; ) {
      for (int bc = bucketCnt; --bc >= 0; ) {
        queueMatrix[queueListSelectionIdx][bc] = new LinkedList<>();    // double linked list !!!
      }//for
    }//for


    int oldQueueList = 0b0;
    int curQueueList = 0b1;
    int shift = 0;

    // 1st sort step
    for (long curValue : arrayToBeSorted) {
      int bucketIndexCurrQueueList = (int) ((curValue >>> shift) & sliceMask);
      // TODO 1 & 15 = 1, 2 & 15 = 2, 15 & 15 = 15, 16 & 15 = 0
      queueMatrix[curQueueList][bucketIndexCurrQueueList].add(curValue);
    }//for

    // other sort steps
    for (int stillToDo = sliceCnt; --stillToDo > 0; ) {
      // prepare for next sort step
      shift += sliceSize;
      oldQueueList ^= 0b1;    // old queue list containing values from last run
      curQueueList ^= 0b1;    // new/current queue list taking values from current run
      // TODO zwischen alter und neuer Liste switchen

      // current sort step
      for (int bucketIndexOldQueueList = 0; bucketIndexOldQueueList < bucketCnt; bucketIndexOldQueueList++) {
        while (!queueMatrix[oldQueueList][bucketIndexOldQueueList].isEmpty()) {
          long curValue = queueMatrix[oldQueueList][bucketIndexOldQueueList].remove();
          int bucketIndexCurrQueueList = (int) ((curValue >>> shift) & sliceMask);
          queueMatrix[curQueueList][bucketIndexCurrQueueList].add(curValue);
        }//while
      }//for
    }//for

    // put sorted values back into array
    int arrayIdx = 0;
    for (int bucketIndexCurrQueueList = 0; bucketIndexCurrQueueList < bucketCnt; bucketIndexCurrQueueList++) {
      while (!queueMatrix[curQueueList][bucketIndexCurrQueueList].isEmpty()) {
        arrayToBeSorted[arrayIdx++] = queueMatrix[curQueueList][bucketIndexCurrQueueList].remove();
      }//while
    }//for

    return arrayToBeSorted;

  }//method()

}//class
