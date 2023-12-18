package application;

import utility.Item;

public class SortKeyGetter<T> {
  public int getSortKey(T itemWithSortKey) {
    if (itemWithSortKey instanceof Item item) return item.sortKey();
    if (itemWithSortKey instanceof Integer item) return item;
    if (itemWithSortKey instanceof Long item) return item.intValue();

    throw new IllegalArgumentException("item should be type of Item, int or long");
  }
}
