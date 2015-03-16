package br.com.zbra.androidlinq;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import br.com.zbra.androidlinq.delegate.Comparator;
import br.com.zbra.androidlinq.delegate.Selector;

class OrderByDescendingIterable<T, R> extends OrderByIterable<T, R> implements Iterable<T> {

    OrderByDescendingIterable(Stream<T> stream, Selector<T, R> fieldSelector, Comparator<R> comparator) {
        super(stream, fieldSelector, comparator);
    }

    @Override
    public Iterator<T> iterator() {
        List<T> list = getStream().toList();
        Collections.sort(list, Collections.reverseOrder((T t1, T t2) -> getComparator().compare(getSelector().select(t1), getSelector().select(t2))));
        return list.iterator();
    }
}
