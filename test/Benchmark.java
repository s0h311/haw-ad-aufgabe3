package test;

import application.ArraySorter;
import application.RadixSortArray;
import application.RadixSortQueue;
import utility.Beautician;
import utility.Item;
import utility.OptimizationBlocker;
import utility.UnsortedItemArrayGenerator;

import java.util.concurrent.*;

public class Benchmark {
  @SuppressWarnings("unchecked")
  private static final ArraySorter<Item>[] sorter =
      (ArraySorter<Item>[]) (new ArraySorter[]{
          new RadixSortArray<>(),
          new RadixSortQueue<>(),
      });

  public static void main(final String... unused) {
    final OptimizationBlocker ob = new OptimizationBlocker();

    //configure test parameters
    final int runCnt = 10;
    final int[] numberOfItemsToBeSorted = {10, 100, 1_000, 10_000, 100_000}; //TODO 1_000_000

    System.out.printf("Start measurement at : %s\n", Beautician.getPimpedTime());
    System.out.printf("\n");
    // actual measurement
    for (final ArraySorter<Item> currentSorter : sorter) {
      for (final int amountOfItems : numberOfItemsToBeSorted) {
        final int excludingMaximum = amountOfItems * 999 / 1000;
        final UnsortedItemArrayGenerator uiag = new UnsortedItemArrayGenerator(amountOfItems, excludingMaximum);
        long duration = 0;
        for (int rc = runCnt; --rc >= 0; ) {
          final Item[] arrayToBeSorted = uiag.createUnsortedItemArray();
          final long startTime = System.nanoTime();

          ExecutorService executorService = Executors.newSingleThreadExecutor();
          try {
            executorService.submit(() -> {
              currentSorter.sort(arrayToBeSorted);
            }).get(10, TimeUnit.MINUTES);
          } catch (InterruptedException | ExecutionException | TimeoutException e) {
            break;
          } finally {
            executorService.shutdown();
          }

          //
          final long endTime = System.nanoTime();
          duration += (endTime - startTime);
          ob.bo(arrayToBeSorted);
        }//for
        System.out.printf(
            "%s   %12d ~ %-12d : %40s\n",
            currentSorter.getClass().getSimpleName(),
            amountOfItems,
            excludingMaximum,
            Beautician.nanoSecondBasedTimeToString(duration / runCnt)
        );
      }//for
      System.out.printf("\n");
    }//for

    System.out.printf(">>>>>>>>>>>>>>>>>>>>----\n");
    System.out.printf("don't care about :  %x", ob.getSignature());
  }//method()
}
