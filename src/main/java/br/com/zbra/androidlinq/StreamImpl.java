package br.com.zbra.androidlinq;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.zbra.androidlinq.delegate.Aggregator;
import br.com.zbra.androidlinq.delegate.Comparator;
import br.com.zbra.androidlinq.delegate.Predicate;
import br.com.zbra.androidlinq.delegate.Selector;
import br.com.zbra.androidlinq.delegate.SelectorBigDecimal;
import br.com.zbra.androidlinq.delegate.SelectorByte;
import br.com.zbra.androidlinq.delegate.SelectorDouble;
import br.com.zbra.androidlinq.delegate.SelectorFloat;
import br.com.zbra.androidlinq.delegate.SelectorInteger;
import br.com.zbra.androidlinq.delegate.SelectorLong;
import br.com.zbra.androidlinq.delegate.SelectorShort;
import br.com.zbra.androidlinq.exception.MultipleElementsFoundException;

import static br.com.zbra.androidlinq.Linq.stream;

class StreamImpl<T> implements Stream<T> {

    private final Iterable<T> iterable;

    StreamImpl(Iterable<T> iterable) {
        if (iterable == null)
            throw new IllegalArgumentException("Cannot create stream for 'null'");

        this.iterable = iterable;
    }

    @Override
    public Stream<T> where(Predicate<T> predicate) {
        return stream(new WhereIterable<>(this, predicate));
    }

    @Override
    public <R> Stream<R> select(Selector<T, R> selector) {
        return stream(new SelectIterable<>(this, selector));
    }

    @Override
    public <R> Stream<R> selectMany(Selector<T, Iterable<R>> selector) {
        return stream(new SelectManyIterable<>(this, selector));
    }

    @Override
    public <K, E> Stream<Grouping<K, E>> groupBy(Selector<T, K> keySelector, Selector<T, E> elementSelector) {
        return stream(new GroupByIterable<>(this, keySelector, elementSelector));
    }

    @Override
    public <K> Stream<Grouping<K, T>> groupBy(Selector<T, K> keySelector) {
        return groupBy(keySelector, t -> t);
    }

    @Override
    public <R> Stream<T> orderBy(Selector<T, R> keySelector, Comparator<R> comparator) {
        return stream(new OrderByIterable<>(this, keySelector, comparator));
    }

    @Override
    public <R extends Comparable<R>> Stream<T> orderBy(Selector<T, R> keySelector) {
        return orderBy(keySelector, R::compareTo);
    }

    @Override
    public <R> Stream<T> orderByDescending(Selector<T, R> keySelector, Comparator<R> comparator) {
        return stream(new OrderByDescendingIterable<>(this, keySelector, comparator));
    }

    @Override
    public <R extends Comparable<R>> Stream<T> orderByDescending(Selector<T, R> keySelector) {
        return orderByDescending(keySelector, R::compareTo);
    }

    @Override
    public <R> R aggregate(R seed, Aggregator<T, R> aggregator) {
        for (T t : this)
            seed = aggregator.aggregate(seed, t);
        return seed;
    }

    @Override
    public Stream<T> take(int count) {
        return stream(new TakeIterable<>(this, count));
    }

    @Override
    public Stream<T> distinct() {
        return stream(toMap(t -> t).keySet());
    }

    @Override
    public Byte sum(SelectorByte<T> selector) {
        return aggregate((byte)0, (Byte v, T t) -> (byte)(v + selector.select(t)));
    }

    @Override
    public Short sum(SelectorShort<T> selector) {
        return aggregate((short)0, (Short v, T t) -> (short)(v + selector.select(t)));
    }

    @Override
    public Integer sum(SelectorInteger<T> selector) {
        return aggregate(0, (Integer v, T t) -> v + selector.select(t));
    }

    @Override
    public Long sum(SelectorLong<T> selector) {
        return aggregate(0l, (Long v, T t) -> v + selector.select(t));
    }

    @Override
    public Float sum(SelectorFloat<T> selector) {
        return aggregate(0f, (Float v, T t) -> v + selector.select(t));
    }

    @Override
    public Double sum(SelectorDouble<T> selector) {
        return aggregate(0d, (Double v, T t) -> v + selector.select(t));
    }

    @Override
    public BigDecimal sum(SelectorBigDecimal<T> selector) {
        return aggregate(new BigDecimal(0), (BigDecimal v, T t) -> v.add(selector.select(t)));
    }

    @Override
    public boolean any() {
        return first() != null;
    }

    @Override
    public boolean any(Predicate<T> predicate) {
        return first(predicate) != null;
    }

    @Override
    public int count() {
        return aggregate(0, (a, t) -> a += 1);
    }

    @Override
    public T first() {
        Iterator<T> iterator = iterable.iterator();
        if (iterator.hasNext())
            return iterator.next();
        return null;
    }

    @Override
    public T first(Predicate<T> predicate) {
        for (T entry : iterable) {
            if (predicate.apply(entry))
                return entry;
        }
        return null;
    }

    @Override
    public T single() throws MultipleElementsFoundException {
        T result = null;
        for (T entry : iterable) {
            if (result != null)
                throw new MultipleElementsFoundException();
            result = entry;
        }
        return result;
    }

    @Override
    public T single(Predicate<T> predicate) throws MultipleElementsFoundException {
        return where(predicate).single();
    }

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        for (T entry : iterable)
            list.add(entry);
        return list;
    }

    @Override
    public <K> Map<K, T> toMap(Selector<T, K> keySelector) {
        return toMap(keySelector, t -> t);
    }

    @Override
    public <K, V> Map<K, V> toMap(Selector<T, K> keySelector, Selector<T, V> valueSelector) {
        Map<K, V> map = new HashMap<>();
        for (T entry : iterable)
            map.put(keySelector.select(entry), valueSelector.select(entry));
        return map;
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }
}
