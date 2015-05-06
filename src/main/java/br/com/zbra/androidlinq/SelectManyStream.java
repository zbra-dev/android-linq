package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Selector;

import java.util.Iterator;
import java.util.NoSuchElementException;

class SelectManyStream<T, TSelected> extends AbstractStream<TSelected> {

    private final AbstractStream<T> stream;
    private final Selector<T, Iterable<TSelected>> selector;

    SelectManyStream(AbstractStream<T> stream, Selector<T, Iterable<TSelected>> selector) {
        this.stream = stream;
        this.selector = selector;
    }

    @Override
    public Iterator<TSelected> iterator() {
        return new SelectManyIterator<>(selector, stream.iterator());
    }

    @Override
    protected Iterator<TSelected> reverseIterator() {
        return super.reverseIterator();
    }

    private static class SelectManyIterator<T, TSelected> implements Iterator<TSelected> {
        private Boolean hasNext;
        private Iterator<TSelected> selectedIterator;
        private final Iterator<T> sourceIterator;
        private final Selector<T, Iterable<TSelected>> selector;

        public SelectManyIterator(Selector<T, Iterable<TSelected>> selector, Iterator<T> sourceIterator) {
            this.sourceIterator = sourceIterator;
            this.selector = selector;
        }

        @Override
        public boolean hasNext() {
            if (hasNext != null)
                return hasNext;

            if (selectedIterator != null && selectedIterator.hasNext())
                return hasNext = true;

            while(sourceIterator.hasNext()) {
                Iterable<TSelected> selectedIterable = selector.select(sourceIterator.next());
                if ((selectedIterator = selectedIterable.iterator()).hasNext())
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
    }
}
