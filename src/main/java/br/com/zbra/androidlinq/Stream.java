package br.com.zbra.androidlinq;

import java.math.BigDecimal;
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

/**
 * Decorates {@code Iterable<T>} objects to enable use of Linq like expressions.
 *
 * @param <T> the type of the wrapped {@code Iterable<T>}
 */
public interface Stream<T> extends Iterable<T> {
    /**
     * Filters a sequence of values based on a predicate.
     *
     * @param predicate A function to test each element for a condition.
     * @return An Stream of type T that contains elements from the input sequence that satisfy the condition.
     */
    public Stream<T> where(Predicate<T> predicate);

    /**
     * Projects each element of a sequence into a new form.
     *
     * @param selector  A transform function to apply to each element.
     * @param <TResult> The type of the value returned by selector.
     * @return An Stream of type TResult whose elements are the result of invoking the transform function on each element of source.
     */
    public <TResult> Stream<TResult> select(Selector<T, TResult> selector);

    /**
     * Projects each element of a sequence to an {@link Iterable Iterable&lt;TResult&gt;} and flattens the resulting sequences into one sequence.
     *
     * @param selector  A transform function to apply to each element.
     * @param <TResult> The type of the elements of the sequence returned by selector.
     * @return An Stream of type TResult whose elements are the result of invoking the one-to-many transform function on each element of the input sequence.
     */
    public <TResult> Stream<TResult> selectMany(Selector<T, Iterable<TResult>> selector);

    /**
     * Groups the elements of a sequence according to a specified key selector function.
     *
     * @param keySelector     A function to extract the key for each source element.
     * @param elementSelector A function to map each source element to an element into a Grouping.
     * @param <TKey>          The type of the key returned by keySelector.
     * @param <TElement>      The type of the element returned by elementSelector.
     * @return An Stream of type Grouping where each Grouping object contains a sequence of objects and a key (TKey, TResult respectively).
     */
    public <TKey, TElement> Stream<Grouping<TKey, TElement>> groupBy(Selector<T, TKey> keySelector, Selector<T, TElement> elementSelector);

    /**
     * Groups the elements of a sequence according to a specified key selector function.
     *
     * @param keySelector A function to extract the key for each source element.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type Grouping where each Grouping object contains a sequence of objects and a key (TKey, TResult respectively).
     */
    public <TKey> Stream<Grouping<TKey, T>> groupBy(Selector<T, TKey> keySelector);

    /**
     * Sorts the elements of a sequence in ascending order according to a key.
     *
     * @param keySelector A function to extract a key from an element.
     * @param comparator  An Comparator to compare keys.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    public <TKey> Stream<T> orderBy(Selector<T, TKey> keySelector, Comparator<TKey> comparator);

    /**
     * Sorts the elements of a sequence in ascending order according to a key.
     *
     * @param keySelector A function to extract a key from an element.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    public <TKey extends Comparable<TKey>> Stream<T> orderBy(Selector<T, TKey> keySelector);

    /**
     * Sorts the elements of a sequence in descending order according to a key.
     *
     * @param keySelector A function to extract a key from an element.
     * @param comparator  An Comparator to compare keys.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    public <TKey> Stream<T> orderByDescending(Selector<T, TKey> keySelector, Comparator<TKey> comparator);

    /**
     * Sorts the elements of a sequence in descending order according to a key.
     *
     * @param keySelector A function to extract a key from an element.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    public <TKey extends Comparable<TKey>> Stream<T> orderByDescending(Selector<T, TKey> keySelector);

    /**
     * Applies an accumulator function over a sequence.
     * The specified seed value is used as the initial accumulator value,
     * and the specified function is used to select the result value.
     *
     * @param seed          The initial accumulator value.
     * @param aggregator    An accumulator function to be invoked on each element.
     * @param <TAccumulate> The type of the accumulator value.
     * @return The transformed final accumulator value.
     */
    public <TAccumulate> TAccumulate aggregate(TAccumulate seed, Aggregator<T, TAccumulate> aggregator);

    /**
     * Returns a specified number of contiguous elements from the start of a sequence.
     *
     * @param count The number of elements to return.
     * @return An Stream of type T that contains the specified number of elements from the start of the input sequence.
     */
    public Stream<T> take(int count);

    /**
     * Returns distinct elements from a sequence by using the object {@code equals()} to compare values.
     *
     * @return An Stream of type T that contains distinct elements from the source sequence.
     */
    public Stream<T> distinct();

    /**
     * Computes the sum of the sequence of Byte values that are obtained by invoking
     * a transform function on each element of the input sequence.
     *
     * @param selector A transform function to apply to each element.
     * @return The sum of the projected values.
     */
    public Byte sum(SelectorByte<T> selector);

    /**
     * Computes the sum of the sequence of Short values that are obtained by invoking
     * a transform function on each element of the input sequence.
     *
     * @param selector A transform function to apply to each element.
     * @return The sum of the projected values.
     */
    public Short sum(SelectorShort<T> selector);

    /**
     * Computes the sum of the sequence of Integer values that are obtained by invoking
     * a transform function on each element of the input sequence.
     *
     * @param selector A transform function to apply to each element.
     * @return The sum of the projected values.
     */
    public Integer sum(SelectorInteger<T> selector);

    /**
     * Computes the sum of the sequence of Long values that are obtained by invoking
     * a transform function on each element of the input sequence.
     *
     * @param selector A transform function to apply to each element.
     * @return The sum of the projected values.
     */
    public Long sum(SelectorLong<T> selector);

    /**
     * Computes the sum of the sequence of Float values that are obtained by invoking
     * a transform function on each element of the input sequence.
     *
     * @param selector A transform function to apply to each element.
     * @return The sum of the projected values.
     */
    public Float sum(SelectorFloat<T> selector);

    /**
     * Computes the sum of the sequence of Double values that are obtained by invoking
     * a transform function on each element of the input sequence.
     *
     * @param selector A transform function to apply to each element.
     * @return The sum of the projected values.
     */
    public Double sum(SelectorDouble<T> selector);

    /**
     * Computes the sum of the sequence of BigDecimal values that are obtained by invoking
     * a transform function on each element of the input sequence.
     *
     * @param selector A transform function to apply to each element.
     * @return The sum of the projected values.
     */
    public BigDecimal sum(SelectorBigDecimal<T> selector);

    /**
     * Determines whether a sequence contains any elements.
     *
     * @return {@code true} if the source sequence contains any elements; otherwise, {@code false}.
     */
    public boolean any();

    /**
     * Determines whether any element of a sequence satisfies a condition.
     *
     * @param predicate A function to test each element for a condition
     * @return {@code true} if any elements in the source sequence pass the test in the specified predicate; otherwise,  {@code false}.
     */
    public boolean any(Predicate<T> predicate);

    /**
     * Returns the number of elements in a sequence.
     *
     * @return The number of elements in the input sequence.
     */
    public int count();

    /**
     * Returns the first element of a sequence.
     *
     * @return The first element in the specified sequence or null if the sequence is empty.
     */
    public T first();

    /**
     * Returns the first element in a sequence that satisfies a specified condition.
     *
     * @param predicate A function to test each element for a condition.
     * @return The first element in the sequence that passes the test in the specified predicate function or null if none does.
     */
    public T first(Predicate<T> predicate);

    /**
     * Returns the only element of a sequence, or a default value if the sequence is empty; this method throws
     * an exception if there is more than one element in the sequence.
     *
     * @return The single element of the input sequence, or default(TSource) if the sequence contains no elements.
     * @throws MultipleElementsFoundException The input sequence contains more than one matching element.
     */
    public T single() throws MultipleElementsFoundException;

    /**
     * Returns the only element of a sequence that satisfies a specified condition or null if
     * no such element exists; this method throws an exception if more than one element satisfies the condition.
     *
     * @param predicate A function to test an element for a condition.
     * @return The single element of the input sequence that satisfies the condition, or null if no such element is found.
     * @throws MultipleElementsFoundException The input sequence contains more than one matching element.
     */
    public T single(Predicate<T> predicate) throws MultipleElementsFoundException;

    /**
     * Creates a List from a Stream.
     *
     * @return A List of type T that contains elements from the input sequence.
     */
    public List<T> toList();

    /**
     * Creates a Map&lt;TKey, T&gt; from a Stream&lt;T&gt; according to a specified key selector function.
     *
     * @param keySelector A function to extract a key from each element.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return A Map that contains keys and values derived from the Stream.
     */
    public <TKey> Map<TKey, T> toMap(Selector<T, TKey> keySelector);

    /**
     * Creates a Map&lt;TKey, T&gt; from a Stream&lt;T&gt; according to specified key selector and value selector functions.
     *
     * @param keySelector   A function to extract a key from each element.
     * @param valueSelector A transform function to produce a result element value from each element.
     * @param <TKey>        The type of the key returned by keySelector.
     * @param <TValue>      The type of the value returned by elementSelector.
     * @return A Map that contains keys and values derived from the Stream.
     */
    public <TKey, TValue> Map<TKey, TValue> toMap(Selector<T, TKey> keySelector, Selector<T, TValue> valueSelector);
}
