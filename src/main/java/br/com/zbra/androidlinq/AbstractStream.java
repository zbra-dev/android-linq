package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.*;
import br.com.zbra.androidlinq.delegate.Comparator;
import br.com.zbra.androidlinq.exception.MultipleElementsFoundException;

import java.math.BigDecimal;
import java.util.*;

abstract class AbstractStream<T> implements Stream<T> {

    protected Iterator<T> reverseIterator() {
        Deque<T> deque = new LinkedList<>();
        for (T t : this) deque.addFirst(t);
        return deque.iterator();
    }

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
    public <R> OrderedStream<T> orderBy(Selector<T, R> keySelector, Comparator<R> comparator) {
        return OrderByStream.createAscending(this, keySelector, comparator);
    }

    @Override
    public <R extends Comparable<R>> OrderedStream<T> orderBy(Selector<T, R> keySelector) {
        return orderBy(keySelector, R::compareTo);
    }

    @Override
    public <R> OrderedStream<T> orderByDescending(Selector<T, R> keySelector, Comparator<R> comparator) {
        return OrderByStream.createDescending(this, keySelector, comparator);
    }

    @Override
    public <R extends Comparable<R>> OrderedStream<T> orderByDescending(Selector<T, R> keySelector) {
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
    public Stream<T> skip(int count) {
        return new SkipStream<>(this, count);
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
        return iterator().next();
    }

    @Override
    public T first(Predicate<T> predicate) {
        return where(predicate).first();
    }

    @Override
    public T firstOrNull() {
        return firstOrDefault(null);
    }

    @Override
    public T firstOrNull(Predicate<T> predicate) {
        return firstOrDefault(predicate, null);
    }

    @Override
    public T firstOrDefault(T defaultValue) {
        Iterator<T> iterator = iterator();
        return iterator.hasNext() ? iterator.next() : defaultValue;
    }

    @Override
    public T firstOrDefault(Predicate<T> predicate, T defaultValue) {
        return where(predicate).firstOrDefault(defaultValue);
    }

    @Override
    public T last() {
        return reverse().first();
    }

    @Override
    public T last(Predicate<T> predicate) {
        return reverse().first(predicate);
    }

    @Override
    public T lastOrNull() {
        return lastOrDefault(null);
    }

    @Override
    public T lastOrNull(Predicate<T> predicate) {
        return lastOrDefault(predicate, null);
    }

    @Override
    public T lastOrDefault(T defaultValue) {
        return reverse().firstOrDefault(defaultValue);
    }

    @Override
    public T lastOrDefault(Predicate<T> predicate, T defaultValue) {
        return reverse().firstOrDefault(predicate, defaultValue);
    }

    @Override
    public T single() throws MultipleElementsFoundException {
        Iterator<T> iterator = iterator();
        T result = iterator.next();
        if (iterator.hasNext())
            throw new MultipleElementsFoundException();
        return result;
    }

    @Override
    public T single(Predicate<T> predicate) throws MultipleElementsFoundException {
        return where(predicate).single();
    }

    @Override
    public T singleOrNull() {
        return singleOrDefault(null);
    }

    @Override
    public T singleOrNull(Predicate<T> predicate) {
        return singleOrDefault(predicate, null);
    }

    @Override
    public T singleOrDefault(T defaultValue) {
        Iterator<T> iterator = iterator();
        if (!iterator.hasNext())
            return defaultValue;
        T result = iterator.next();
        if (iterator.hasNext())
            throw new MultipleElementsFoundException();
        return result;
    }

    @Override
    public T singleOrDefault(Predicate<T> predicate, T defaultValue) {
        return where(predicate).singleOrDefault(defaultValue);
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
