package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Selector;

import java.util.Iterator;

class SelectStream<T, TSelected> extends AbstractStream<TSelected> {
    private final Stream<T> stream;
    private final Selector<T, TSelected> selector;

    SelectStream(Stream<T> stream, Selector<T, TSelected> selector) {
        this.stream = stream;
        this.selector = selector;
    }

    @Override
    public Iterator<TSelected> iterator() {
        return new SelectIterator<>(selector, stream.iterator());
    }

    private static class SelectIterator<T, TSelected> implements Iterator<TSelected> {
        private final Iterator<T> iterator;
        private final Selector<T, TSelected> selector;

        public SelectIterator(Selector<T, TSelected> selector, Iterator<T> iterator) {
            this.iterator = iterator;
            this.selector = selector;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public TSelected next() {
            return selector.select(iterator.next());
        }
    }
}
