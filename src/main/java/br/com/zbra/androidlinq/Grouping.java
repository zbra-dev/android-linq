package br.com.zbra.androidlinq;

/**
 * Represents a collection of TElements that had been grouped through the TKey.
 *
 * @param <TKey> type used to group the TElements
 * @param <TElement> type of the elements grouped through the TKey
 * @see br.com.zbra.androidlinq.Stream#groupBy(br.com.zbra.androidlinq.delegate.Selector)
 * @see br.com.zbra.androidlinq.Stream#groupBy(br.com.zbra.androidlinq.delegate.Selector, br.com.zbra.androidlinq.delegate.Selector)
 */
public interface Grouping<TKey, TElement> {
    /**
     * @return the key used to perform the grouping.
     */
    TKey getKey();

    /**
     * @return a Stream of grouped elements.
     */
    Stream<TElement> getElements();
}
