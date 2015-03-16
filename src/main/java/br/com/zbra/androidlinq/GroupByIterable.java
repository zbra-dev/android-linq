package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


class GroupByIterable<T, TKey, TElement> implements Iterable<Grouping<TKey, TElement>> {

    private final Stream<T> stream;
    private final Selector<T, TKey> keySelector;
    private final Selector<T, TElement> elementSelector;

    GroupByIterable(Stream<T> stream, Selector<T, TKey> keySelector, Selector<T, TElement> elementSelector) {
        this.stream = stream;
        this.keySelector = keySelector;
        this.elementSelector = elementSelector;
    }

    @Override
    public Iterator<Grouping<TKey, TElement>> iterator() {
        HashMap<TKey, List<TElement>> map = new HashMap<>();
        for (T t : stream) {
            TKey key = keySelector.select(t);
            TElement element = elementSelector.select(t);

            List<TElement> elements = map.get(key);
            if (elements == null) {
                elements = new ArrayList<>();
                map.put(key, elements);
            }

            elements.add(element);
        }

        List<Grouping<TKey, TElement>> groupings = new ArrayList<>();
        for (Map.Entry<TKey, List<TElement>> entry : map.entrySet()) {
            groupings.add(new GroupingImpl<>(entry.getKey(), Linq.stream(entry.getValue())));
        }

        return groupings.iterator();
    }


    public static class GroupingImpl<TKey, TElement> implements Grouping<TKey, TElement> {
        private final TKey key;
        private final Stream<TElement> elements;

        private GroupingImpl(TKey key, Stream<TElement> elements) {
            this.key = key;
            this.elements = elements;
        }

        public TKey getKey() {
            return key;
        }

        public Stream<TElement> getElements() {
            return elements;
        }
    }
}
