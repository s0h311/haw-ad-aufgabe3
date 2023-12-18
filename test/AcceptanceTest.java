package test;

import application.ArraySorter;
import application.RadixSortArray;
import application.RadixSortQueue;

import java.util.ArrayList;
import java.util.List;

public class AcceptanceTest {
  @SuppressWarnings("unchecked")
  private static final ArraySorter<Integer>[] sorter =
      (ArraySorter<Integer>[]) (new ArraySorter[]{
          new RadixSortArray<Integer>(),
          new RadixSortQueue<Integer>(),
      });

  public static void main(final String... unused) {
    // checklist for later review of the results
    List<Integer> checkList = new ArrayList<Integer>();

    // configure test parameters
    final int amountOfValues = 9_999;
    final int excludingMaxValue = amountOfValues * 999 / 1000;
    final int runCnt = 13;

    // start of actual test
    //
    // for each implemented sorter resp. each sorting algorithm
    for (ArraySorter<Integer> currentSorter : sorter) {
      // do it "sometimes"
      for (int rc = runCnt; --rc >= 0; ) {
        // generate array to be sorted (and check list)
        final Integer[] theArray = new Integer[amountOfValues];
        for (int i = amountOfValues; --i >= 0; ) {
          final int item = (int) (excludingMaxValue * Math.random());
          theArray[i] = item;
          checkList.add(item);
        }//for

        //sort array
        currentSorter.sort(theArray);

        // check array, if sorted & valid
        for (int i = 1; i < amountOfValues; i++) {
          if (0 < Integer.compare(theArray[i - 1], theArray[i])) {
            System.out.printf("%s   %d   %d\n", currentSorter.getClass().getSimpleName(), theArray[i - 1], theArray[i]);
            throw new AssertionError();
          }//if
        }//for
        for (int i = theArray.length; --i >= 0; ) {
          if (!checkList.remove(theArray[i])) throw new AssertionError();
        }//for
        if (!checkList.isEmpty()) throw new AssertionError();
      }//for
    }//for
  }//method()

}
