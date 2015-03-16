package br.com.zbra.androidlinq;

import java.util.Iterator;
import java.util.NoSuchElementException;

import br.com.zbra.androidlinq.delegate.Predicate;

class WhereIterable<T> implements Iterable<T> {
    private final Stream<T> stream;
    private final Predicate<T> predicate;

    WhereIterable(Stream<T> stream, Predicate<T> predicate) {
        this.stream = stream;
        this.predicate = predicate;
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<T> wrapped = stream.iterator();
        return new Iterator<T>() {
            private T next;
            private boolean nextEvaluated;

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

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
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
        };
    }
}
