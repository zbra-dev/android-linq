package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.*;
import br.com.zbra.androidlinq.delegate.Comparator;
import br.com.zbra.androidlinq.exception.MultipleElementsFoundException;

import java.math.BigDecimal;
import java.math.MathContext;
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
        return groupBy(keySelector, new Selector<T, T>() {
            @Override
            public T select(T t) {
                return t;
            }
        });
    }

    @Override
    public <R> OrderedStream<T> orderBy(Selector<T, R> keySelector, Comparator<R> comparator) {
        return OrderByStream.createAscending(this, keySelector, comparator);
    }

    @Override
    public <R extends Comparable<R>> OrderedStream<T> orderBy(Selector<T, R> keySelector) {
        return orderBy(keySelector, new Comparator<R>() {
            @Override
            public int compare(R r, R o) {
                return r.compareTo(o);
            }
        });
    }

    @Override
    public <R> OrderedStream<T> orderByDescending(Selector<T, R> keySelector, Comparator<R> comparator) {
        return OrderByStream.createDescending(this, keySelector, comparator);
    }

    @Override
    public <R extends Comparable<R>> OrderedStream<T> orderByDescending(final Selector<T, R> keySelector) {
        return orderByDescending(keySelector, new Comparator<R>() {
            @Override
            public int compare(R r, R o) {
                return r.compareTo(o);
            }
        });
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
        final HashSet<T> set = new HashSet<>();
        return where(new Predicate<T>() {
            @Override
            public boolean apply(T e) {
                return set.add(e);
            }
        });
    }

    @Override
    public Byte sum(final SelectorByte<T> selector) {
        return aggregate((byte)0, new Aggregator<T, Byte>() {
            @Override
            public Byte aggregate(Byte v, T t) {
                return (byte) (v + selector.select(t));
            }
        });
    }

    @Override
    public Short sum(final SelectorShort<T> selector) {
        return aggregate((short)0, new Aggregator<T, Short>() {
            @Override
            public Short aggregate(Short v, T t) {
                return (short) (v + selector.select(t));
            }
        });
    }

    @Override
    public Integer sum(final SelectorInteger<T> selector) {
        return aggregate(0, new Aggregator<T, Integer>() {
            @Override
            public Integer aggregate(Integer v, T t) {
                return v + selector.select(t);
            }
        });
    }

    @Override
    public Long sum(final SelectorLong<T> selector) {
        return aggregate(0L, new Aggregator<T, Long>() {
            @Override
            public Long aggregate(Long v, T t) {
                return v + selector.select(t);
            }
        });
    }

    @Override
    public Float sum(final SelectorFloat<T> selector) {
        return aggregate(0f, new Aggregator<T, Float>() {
            @Override
            public Float aggregate(Float v, T t) {
                return v + selector.select(t);
            }
        });
    }

    @Override
    public Double sum(final SelectorDouble<T> selector) {
        return aggregate(0d, new Aggregator<T, Double>() {
            @Override
            public Double aggregate(Double v, T t) {
                return v + selector.select(t);
            }
        });
    }

    @Override
    public BigDecimal sum(final SelectorBigDecimal<T> selector) {
        return aggregate(new BigDecimal(0), new Aggregator<T, BigDecimal>() {
            @Override
            public BigDecimal aggregate(BigDecimal v, T t) {
                return v.add(selector.select(t));
            }
        });
    }

    @Override
    public Byte average( SelectorByte<T> selector) {
        return (byte)(sum(selector) / count());
    }

    @Override
    public Short average(SelectorShort<T> selector) {
        return (short)(sum(selector) / count());
    }

    @Override
    public Integer average(SelectorInteger<T> selector) {
        return sum(selector) / count();
    }

    @Override
    public Long average(SelectorLong<T> selector) {
        return sum(selector) / count();
    }

    @Override
    public Float average(SelectorFloat<T> selector) {
        return sum(selector) / count();
    }

    @Override
    public Double average(SelectorDouble<T> selector) {
        return sum(selector) / count();
    }

    @Override
    public BigDecimal average(SelectorBigDecimal<T> selector) {
        //noinspection BigDecimalMethodWithoutRoundingCalled
        return sum(selector).divide(new BigDecimal(count()));
    }

    @Override
    public BigDecimal average(SelectorBigDecimal<T> selector, MathContext mathContext) {
        return sum(selector).divide(new BigDecimal(count()), mathContext);
    }

    @Override
    public <TResult extends Comparable<TResult>> T min(Selector<T, TResult> selector) {
        return orderBy(selector).first();
    }

    @Override
    public <TResult> T min(Selector<T, TResult> selector, Comparator<TResult> comparator) {
        return orderBy(selector, comparator).first();
    }

    @Override
    public <TResult extends Comparable<TResult>> T max(Selector<T, TResult> selector) {
        return orderBy(selector).last();
    }

    @Override
    public <TResult> T max(Selector<T, TResult> selector, Comparator<TResult> comparator) {
        return orderBy(selector, comparator).last();
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
    public boolean contains(final T element) {
        return contains(element, new EqualityComparator<T>() {
            @Override
            public boolean compare(T value1, T value2) {
                return value1.equals(value2);
            }
        });
    }

    @Override
    public boolean contains(final T element, final EqualityComparator<T> comparator) {
        return any(new Predicate<T>() {
            @Override
            public boolean apply(T value) {
                return comparator.compare(value, element);
            }
        });
    }

    @Override
    public int count() {
        return aggregate(0, new Aggregator<T, Integer>() {
            @Override
            public Integer aggregate(Integer a, T t) {
                return a + 1;
            }
        });
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
        return toMap(keySelector, new Selector<T, T>() {
            @Override
            public T select(T t) {
                return t;
            }
        });
    }

    @Override
    public <K, V> Map<K, V> toMap(Selector<T, K> keySelector, Selector<T, V> valueSelector) {
        Map<K, V> map = new HashMap<>();
        for (T entry : this)
            map.put(keySelector.select(entry), valueSelector.select(entry));
        return map;
    }
}
