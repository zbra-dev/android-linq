package br.com.zbra.androidlinq;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class ReverseListIterator<T> implements Iterator<T> {
    ListIterator<T> iterator;
    public ReverseListIterator(List<T> list) {
        iterator = list.listIterator(list.size());
    }

    public boolean hasNext() {
        return iterator.hasPrevious();
    }

    public T next() {
        return iterator.previous();
    }
}