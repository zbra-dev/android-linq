package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Selector;

import java.util.Iterator;

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
        };
    }
}
