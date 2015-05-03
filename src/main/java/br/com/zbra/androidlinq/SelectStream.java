package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Selector;

import java.util.Iterator;

class SelectStream<T, TSelected> extends AbstractStream<TSelected> {
    private final AbstractStream<T> stream;
    private final Selector<T, TSelected> selector;

    SelectStream(AbstractStream<T> stream, Selector<T, TSelected> selector) {
        this.stream = stream;
        this.selector = selector;
    }

    @Override
    public int count() {
        return stream.count();
    }

    @Override
    public Iterator<TSelected> iterator() {
        return new SelectIterator<>(selector, stream.iterator());
    }

    @Override
    protected Iterator<TSelected> reverseIterator() {
        return new SelectIterator<>(selector, stream.reverseIterator());
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
