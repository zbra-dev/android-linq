package br.com.zbra.androidlinq;

import java.util.Map;

/**
 * Factory for {@link br.com.zbra.androidlinq.Stream Stream} objects.
 */
public final class Linq {

    /**
     * Decorates the passed {@code iterable} with a Stream.
     *
     * @param iterable a generic typed iterable (usually implementing the {@link java.util.Collection Collection} interface)
     * @param <T>      the generic type of the {@code iterable}
     * @return a new Stream object that decorates the passed {@code iterable}
     */
    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return new StreamImpl<>(iterable);
    }

    /**
     * Decorates the passed {@code map} with a Stream.
     *
     * @param map      a generic typed map.
     * @param <TKey>   the generic type of the {@code map} key
     * @param <TValue> the generic type for the {@code map} value
     * @return a new Stream object that decorates the passed {@code map} entry set
     * @see java.util.Map#entrySet()
     */
    public static <TKey, TValue> Stream<Map.Entry<TKey, TValue>> stream(Map<TKey, TValue> map) {
        return new StreamImpl<>(map.entrySet());
    }
}
