package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Comparator;
import br.com.zbra.androidlinq.delegate.Selector;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class OrderByStream<T, TComparable> extends AbstractStream<T> {

    private final Stream<T> stream;
    private final java.util.Comparator<T>  comparator;

    OrderByStream(Stream<T> stream, Selector<T, TComparable> selector, Comparator<TComparable> comparator) {
        this.stream = stream;
        this.comparator = (T t1, T t2) -> comparator.compare(selector.select(t1), selector.select(t2));
    }

    @Override
    public int count() {
        return stream.count();
    }

    @Override
    public Iterator<T> iterator() {
        return getIterator(comparator);
    }

    @Override
    protected Iterator<T> reverseIterator() {
        return getIterator(Collections.reverseOrder(comparator));
    }

    private Iterator<T> getIterator(java.util.Comparator<T> comparator) {
        List<T> list = stream.toList();
        Collections.sort(list, comparator);
        return list.iterator();
    }
}
