package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Selector;

import java.util.Iterator;
import java.util.NoSuchElementException;

class SelectManyIterable<T, TSelected> implements Iterable<TSelected> {
    private final Stream<T> stream;
    private final Selector<T, Iterable<TSelected>> selector;

    SelectManyIterable(Stream<T> stream, Selector<T, Iterable<TSelected>> selector) {
        this.stream = stream;
        this.selector = selector;
    }

    @Override
    public Iterator<TSelected> iterator() {

        Iterator<T> streamIterator = stream.iterator();
        return new Iterator<TSelected>() {

            public Boolean hasNext;
            private Iterator<TSelected> selectedIterator;

            @Override
            public boolean hasNext() {
                if (hasNext != null)
                    return hasNext;

                if (selectedIterator != null && selectedIterator.hasNext())
                    return hasNext = true;

                while(streamIterator.hasNext()) {
                    Iterable<TSelected> selectorIterable = selector.select(streamIterator.next());
                    if (!(selectedIterator = selectorIterable.iterator()).hasNext())
                        continue;

                    return hasNext = true;
                }

                return hasNext = false;
            }

            @Override
            public TSelected next() {
                if (!hasNext()) throw new NoSuchElementException();
                hasNext = null;
                return selectedIterator.next();
            }
        };
    }
}
