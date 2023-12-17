package application;

import utility.Item;

public class Playground {
  public static void main(String[] args) {
    RadixSortWithArray radixSort = new RadixSortWithArray();

    Item i1 = new Item(111, new Object());
    Item i2 = new Item(222, new Object());
    Item i3 = new Item(333, new Object());
    Item i4 = new Item(444, new Object());
    Item i5 = new Item(555, new Object());

    Item[] arr = new Item[]{i1, i5, i3, i4, i2, i4, i1};

    Item[] res = radixSort.sort(arr);

    // Arrays.stream(res).forEach(item -> System.out.println(item.sortKey()));

  }
}
