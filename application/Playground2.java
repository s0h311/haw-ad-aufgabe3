package application;

import java.util.Arrays;

public class Playground2 {
  public static void main(String[] args) {
    int[][] oldArray = new int[10][30];
    int[][] currentArray = new int[10][30];
    int[][][] arrays = new int[][][]{oldArray, currentArray};

    int indexOfOldArray = 0b0;
    int indexOfCurrentArray = 0b1;
    int shift = 0;

    for (int i = 0; i < 10; i++) {
      int bucketIndexCurrentArray = (i >>> shift) & 9;
      arrays[indexOfCurrentArray][bucketIndexCurrentArray][i] = i;
    }

    System.err.println(Arrays.deepToString(currentArray));
  }
}
