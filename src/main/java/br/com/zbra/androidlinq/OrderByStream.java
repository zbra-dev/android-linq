package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Comparator;
import br.com.zbra.androidlinq.delegate.Selector;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class OrderByStream<T, TComparable> extends AbstractStream<T> {

    private final Stream<T> stream;
    private final Selector<T, TComparable> selector;
    private final Comparator<TComparable> comparator;

    OrderByStream(Stream<T> stream, Selector<T, TComparable> selector, Comparator<TComparable> comparator) {
        this.stream = stream;
        this.selector = selector;
        this.comparator = comparator;
    }

    /**
     * @return the wrapped {@code stream}
     */
    protected Stream<T> getStream() {
        return stream;
    }

    /**
     * @return the passed {@code selector}
     */
    protected Selector<T, TComparable> getSelector() {
        return selector;
    }

    /**
     * @return the passed {@code comparator}
     */
    protected Comparator<TComparable> getComparator() {
        return comparator;
    }

    @Override
    public int count() {
        return stream.count();
    }

    @Override
    public Iterator<T> iterator() {
        List<T> list = stream.toList();
        Collections.sort(list, (T t1, T t2) -> comparator.compare(selector.select(t1), selector.select(t2)));
        return list.iterator();
    }
}
