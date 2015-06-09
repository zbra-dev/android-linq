package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static br.com.zbra.androidlinq.Linq.stream;


class GroupByStream<T, TKey, TElement> extends AbstractStream<Grouping<TKey, TElement>> {

    private final AbstractStream<T> stream;
    private final Selector<T, TKey> keySelector;
    private final Selector<T, TElement> elementSelector;

    GroupByStream(AbstractStream<T> stream, Selector<T, TKey> keySelector, Selector<T, TElement> elementSelector) {
        this.stream = stream;
        this.keySelector = keySelector;
        this.elementSelector = elementSelector;
    }

    @Override
    public Iterator<Grouping<TKey, TElement>> iterator() {
        return getGroupingIterator(stream.iterator());
    }

    @Override
    protected Iterator<Grouping<TKey, TElement>> reverseIterator() {
        return super.reverseIterator();
    }

    private Iterator<Grouping<TKey, TElement>> getGroupingIterator(Iterator<T> iterator) {
        HashMap<TKey, GroupingImpl<TKey, TElement>> map = new HashMap<>();
        List<Grouping<TKey, TElement>> groupings = new ArrayList<>();

        while (iterator.hasNext()) {
            T t = iterator.next();
            TKey key = keySelector.select(t);
            TElement element = elementSelector.select(t);

            GroupingImpl<TKey, TElement> grouping = map.get(key);
            if (grouping == null) {
                grouping = new GroupingImpl<>(key);
                map.put(key, grouping);
                groupings.add(grouping);
            }

            grouping.source.add(element);
        }

        return groupings.iterator();
    }

    private static class GroupingImpl<TKey, TElement> implements Grouping<TKey, TElement> {
        private final TKey key;
        private final List<TElement> source;

        private GroupingImpl(TKey key) {
            this.key = key;
            this.source = new ArrayList<>();
        }

        public TKey getKey() {
            return key;
        }

        public Stream<TElement> getElements() {
            return stream(source);
        }
    }
}
