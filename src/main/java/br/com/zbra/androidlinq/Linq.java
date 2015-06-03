package br.com.zbra.androidlinq;

import java.util.List;
import java.util.Map;

/**
 * Factory for {@link br.com.zbra.androidlinq.Stream Stream} objects.
 */
public final class Linq {

    private Linq() {
        throw new UnsupportedOperationException();
    }

    /**
     * Decorates the passed {@code iterable} with a Stream.
     *
     * @param array a generic typed iterable (usually implementing the {@link java.util.Collection Collection} interface)
     * @param <T>      the generic type of the {@code array}
     * @return a new Stream object that decorates the passed {@code array}
     */
    public static <T> Stream<T> stream(T[] array) {
        return new ArrayStream<>(array);
    }

    /**
     * Decorates the passed {@code list} with a Stream.
     *
     * @param list a generic typed list (usually implementing the {@link java.util.List List} interface)
     * @param <T>      the generic type of the {@code list}
     * @return a new Stream object that decorates the passed {@code list}
     */
    public static <T> Stream<T> stream(List<T> list) {
        return new ListStream<>(list);
    }

    /**
     * Decorates the passed {@code iterable} with a Stream.
     *
     * @param iterable a generic typed iterable (usually implementing the {@link java.util.Collection Collection} interface)
     * @param <T>      the generic type of the {@code iterable}
     * @return a new Stream object that decorates the passed {@code iterable}
     */
    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return new IterableStream<>(iterable);
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
        return new MapStream<>(map);
    }
}
