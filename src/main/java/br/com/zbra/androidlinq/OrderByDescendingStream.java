package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Comparator;
import br.com.zbra.androidlinq.delegate.Selector;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class OrderByDescendingStream<T, R> extends OrderByStream<T, R> {

    OrderByDescendingStream(Stream<T> stream, Selector<T, R> fieldSelector, Comparator<R> comparator) {
        super(stream, fieldSelector, comparator);
    }

    @Override
    public Iterator<T> iterator() {
        List<T> list = getStream().toList();
        Collections.sort(list, Collections.reverseOrder((T t1, T t2) -> getComparator().compare(getSelector().select(t1), getSelector().select(t2))));
        return list.iterator();
    }
}
