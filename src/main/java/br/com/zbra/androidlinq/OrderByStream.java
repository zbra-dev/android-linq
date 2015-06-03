package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Comparator;
import br.com.zbra.androidlinq.delegate.Selector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class OrderByStream<T> extends AbstractStream<T> implements OrderedStream<T> {

    private final Stream<T> stream;
    private final QueuedComparators<T> queuedComparators;

    static <T, TComparable> OrderedStream<T> createAscending(Stream<T> stream, Selector<T, TComparable> selector, Comparator<TComparable> comparator) {
        OrderByStream<T> orderByStream = new OrderByStream<>(stream);
        orderByStream.thenBy(selector, comparator);
        return orderByStream;
    }

    static <T, TComparable> OrderedStream<T> createDescending(Stream<T> stream, Selector<T, TComparable> selector, Comparator<TComparable> comparator) {
        OrderByStream<T> orderByStream = new OrderByStream<>(stream);
        orderByStream.thenByDescending(selector, comparator);
        return orderByStream;
    }

    private OrderByStream(Stream<T> stream) {
        this.stream = stream;
        this.queuedComparators = new QueuedComparators<>();
    }

    @Override
    public int count() {
        return stream.count();
    }

    @Override
    public Iterator<T> iterator() {
        return getIterator(queuedComparators);
    }

    @Override
    protected Iterator<T> reverseIterator() {
        return getIterator(Collections.reverseOrder(queuedComparators));
    }

    private Iterator<T> getIterator(java.util.Comparator<T> comparator) {
        List<T> list = stream.toList();
        Collections.sort(list, comparator);
        return list.iterator();
    }

    @Override
    public <TKey> OrderedStream<T> thenBy(Selector<T, TKey> keySelector, Comparator<TKey> comparator) {
        this.queuedComparators.addComparator((T t1, T t2) -> comparator.compare(keySelector.select(t1), keySelector.select(t2)));
        return this;
    }

    @Override
    public <TKey extends Comparable<TKey>> OrderedStream<T> thenBy(Selector<T, TKey> keySelector) {
        thenBy(keySelector, TKey::compareTo);
        return this;
    }

    @Override
    public <TKey> OrderedStream<T> thenByDescending(Selector<T, TKey> keySelector, Comparator<TKey> comparator) {
        this.queuedComparators.addComparator((T t1, T t2) -> comparator.compare(keySelector.select(t1), keySelector.select(t2)) * -1);
        return this;
    }

    @Override
    public <TKey extends Comparable<TKey>> OrderedStream<T> thenByDescending(Selector<T, TKey> keySelector) {
        thenByDescending(keySelector, TKey::compareTo);
        return this;
    }

    private static class QueuedComparators<T> implements java.util.Comparator<T> {
        private List<java.util.Comparator<T>> comparators = new ArrayList<>();

        @Override
        public int compare(T o1, T o2) {
            int size = comparators.size();
            int compare = 0;
            for (int i = 0; i < size && compare == 0; i++) {
                compare = comparators.get(i).compare(o1, o2);
            }
            return compare;
        }

        public void addComparator(java.util.Comparator<T> comparator) {
            comparators.add(comparator);
        }
    }
}
