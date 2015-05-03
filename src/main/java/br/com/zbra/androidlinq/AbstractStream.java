package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.*;
import br.com.zbra.androidlinq.delegate.Comparator;
import br.com.zbra.androidlinq.exception.MultipleElementsFoundException;

import java.math.BigDecimal;
import java.util.*;

import static br.com.zbra.androidlinq.Linq.stream;

abstract class AbstractStream<T> implements Stream<T> {

    protected abstract Iterator<T> reverseIterator();

    @Override
    public Stream<T> where(Predicate<T> predicate) {
        return new WhereStream<>(this, predicate);
    }

    @Override
    public <R> Stream<R> select(Selector<T, R> selector) {
        return new SelectStream<>(this, selector);
    }

    @Override
    public <R> Stream<R> selectMany(Selector<T, Iterable<R>> selector) {
        return new SelectManyStream<>(this, selector);
    }

    @Override
    public <K, E> Stream<Grouping<K, E>> groupBy(Selector<T, K> keySelector, Selector<T, E> elementSelector) {
        return new GroupByStream<>(this, keySelector, elementSelector);
    }

    @Override
    public <K> Stream<Grouping<K, T>> groupBy(Selector<T, K> keySelector) {
        return groupBy(keySelector, t -> t);
    }

    @Override
    public <R> Stream<T> orderBy(Selector<T, R> keySelector, Comparator<R> comparator) {
        return new OrderByStream<>(this, keySelector, comparator);
    }

    @Override
    public <R extends Comparable<R>> Stream<T> orderBy(Selector<T, R> keySelector) {
        return orderBy(keySelector, R::compareTo);
    }

    @Override
    public <R> Stream<T> orderByDescending(Selector<T, R> keySelector, Comparator<R> comparator) {
        return stream(new OrderByDescendingStream<>(this, keySelector, comparator));
    }

    @Override
    public <R extends Comparable<R>> Stream<T> orderByDescending(Selector<T, R> keySelector) {
        return orderByDescending(keySelector, R::compareTo);
    }

    @Override
    public Stream<T> reverse() {
        return new ReverseStream<>(this);
    }

    @Override
    public <R> R aggregate(R seed, Aggregator<T, R> aggregator) {
        for (T t : this)
            seed = aggregator.aggregate(seed, t);
        return seed;
    }

    @Override
    public Stream<T> take(int count) {
        return new TakeStream<>(this, count);
    }

    @Override
    public Stream<T> distinct() {
        HashSet<T> set = new HashSet<>();
        return where(set::add);
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
        return iterator().hasNext();
    }

    @Override
    public boolean any(Predicate<T> predicate) {
        return where(predicate).any();
    }

    @Override
    public int count() {
        return aggregate(0, (a, t) -> a + 1);
    }

    @Override
    public T first() {
        Iterator<T> iterator = iterator();
        if (iterator.hasNext())
            return iterator.next();
        return null;
    }

    @Override
    public T first(Predicate<T> predicate) {
        for (T entry : this) {
            if (predicate.apply(entry))
                return entry;
        }
        return null;
    }

    @Override
    public T single() throws MultipleElementsFoundException {
        T result = null;
        for (T entry : this) {
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
        for (T entry : this)
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
        for (T entry : this)
            map.put(keySelector.select(entry), valueSelector.select(entry));
        return map;
    }
}
