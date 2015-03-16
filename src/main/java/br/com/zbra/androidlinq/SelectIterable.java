package br.com.zbra.androidlinq;

import java.util.Iterator;

import br.com.zbra.androidlinq.delegate.Selector;

class SelectIterable<T, TSelected> implements Iterable<TSelected> {
    private final Stream<T> stream;
    private final Selector<T, TSelected> selector;

    SelectIterable(Stream<T> stream, Selector<T, TSelected> selector) {
        this.stream = stream;
        this.selector = selector;
    }

    @Override
    public Iterator<TSelected> iterator() {
        Iterator<T> wrapped = stream.iterator();
        return new Iterator<TSelected>() {
            @Override
            public boolean hasNext() {
                return wrapped.hasNext();
            }

            @Override
            public TSelected next() {
                return selector.select(wrapped.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
