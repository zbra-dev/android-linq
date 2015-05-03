package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Comparator;
import br.com.zbra.androidlinq.delegate.Selector;

import java.util.Iterator;

class OrderByDescendingStream<T, R> extends OrderByStream<T, R> {

    OrderByDescendingStream(Stream<T> stream, Selector<T, R> fieldSelector, Comparator<R> comparator) {
        super(stream, fieldSelector, comparator);
    }

    @Override
    public Iterator<T> iterator() {
        return super.reverseIterator();
    }

    @Override
    public Iterator<T> reverseIterator() {
        return super.iterator();
    }
}
