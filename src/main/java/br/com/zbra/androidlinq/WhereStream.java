package br.com.zbra.androidlinq;

import br.com.zbra.androidlinq.delegate.Predicate;

import java.util.Iterator;
import java.util.NoSuchElementException;

class WhereStream<T> extends AbstractStream<T> {
    private final AbstractStream<T> stream;
    private final Predicate<T> predicate;

    WhereStream(AbstractStream<T> stream, Predicate<T> predicate) {
        this.stream = stream;
        this.predicate = predicate;
    }

    @Override
    public Iterator<T> iterator() {
        return new WhereIterator<>(stream.iterator(), predicate);
    }

    @Override
    protected Iterator<T> reverseIterator() {
        return new WhereIterator<>(stream.reverseIterator(), predicate);
    }

    private static class WhereIterator<T> implements Iterator<T> {
        private final Iterator<T> wrapped;
        private final Predicate<T> predicate;

        private T next;
        private boolean nextEvaluated;

        public WhereIterator(Iterator<T> wrapped, Predicate<T> predicate) {
            this.wrapped = wrapped;
            this.predicate = predicate;
        }

        @Override
        public boolean hasNext() {
            evaluateNext();
            return next != null;
        }

        @Override
        public T next() {
            evaluateNext();

            if (next == null)
                throw new NoSuchElementException();

            nextEvaluated = false;

            return next;
        }

        private void evaluateNext() {
            if (nextEvaluated)
                return;

            next = null;
            while (wrapped.hasNext() && next == null) {
                T entry = wrapped.next();
                if (predicate.apply(entry))
                    next = entry;
            }

            nextEvaluated = true;
        }
    }
}
