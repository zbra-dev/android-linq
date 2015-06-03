package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Comparator;
import br.com.zbra.androidlinq.delegate.Selector;

public interface OrderedStream<T> extends Stream<T> {

    /**
     * Performs a subsequent ordering of the elements in a sequence in ascending order by using a specified comparator.
     *
     * @param keySelector A function to extract a key from an element.
     * @param comparator  An Comparator to compare keys.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    public <TKey> OrderedStream<T> thenBy(Selector<T, TKey> keySelector, Comparator<TKey> comparator);

    /**
     * Performs a subsequent ordering of the elements in a sequence in ascending order according to a key.
     *
     * @param keySelector A function to extract a key from an element.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    public <TKey extends Comparable<TKey>> OrderedStream<T> thenBy(Selector<T, TKey> keySelector);

    /**
     * Performs a subsequent ordering of the elements in a sequence in descending order by using a specified comparer.
     *
     * @param keySelector A function to extract a key from an element.
     * @param comparator  An Comparator to compare keys.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    public <TKey> OrderedStream<T> thenByDescending(Selector<T, TKey> keySelector, Comparator<TKey> comparator);

    /**
     * Performs a subsequent ordering of the elements in a sequence in descending order, according to a key.
     *
     * @param keySelector A function to extract a key from an element.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    public <TKey extends Comparable<TKey>> OrderedStream<T> thenByDescending(Selector<T, TKey> keySelector);

}
