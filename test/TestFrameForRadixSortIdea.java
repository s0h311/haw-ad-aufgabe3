package test;

import application.RadixSortIdea;
import utility.Beautician;

public class TestFrameForRadixSortIdea {

  public static void main(final String... unused) {
        /*
        Runtime runtime = Runtime.getRuntime();
        System.out.printf( "%16d ~ %16x\n",  runtime.freeMemory(),  runtime.freeMemory() );
        System.out.printf( "%16d ~ %16x\n",  runtime.totalMemory(), runtime.totalMemory() );
        System.out.printf( "%16d ~ %16x\n",  runtime.maxMemory(),   runtime.maxMemory() );
        System.out.printf( "\n\n" );
        */


    final int amountOfDataToBeSorted = 10_000_000;
    long[] theArray = new long[amountOfDataToBeSorted];
    for (int i = theArray.length; --i >= 0; ) theArray[i] = (long) (Long.MAX_VALUE * Math.random());

    RadixSortIdea rs = new RadixSortIdea();
    final long timeStamp1 = System.nanoTime();
    rs.sort(theArray);
    final long timeStamp2 = System.nanoTime();
    final long delta = timeStamp2 - timeStamp1;

    for (int i = theArray.length - 1; --i >= 0; ) {
      if (theArray[i + 1] < theArray[i]) throw new AssertionError("invalid order detected => data is NOT sorted");
    }//for

    System.out.printf("THE END after %s", Beautician.nanoSecondBasedTimeToString(delta));
    System.out.flush();
  }//main()

}//class
