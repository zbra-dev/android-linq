package br.com.zbra.androidlinq;

import java.util.Iterator;
import java.util.NoSuchElementException;

import br.com.zbra.androidlinq.delegate.Selector;

class SelectManyIterable<T, TSelected> implements Iterable<TSelected> {
    private final Stream<T> stream;
    private final Selector<T, Iterable<TSelected>> selector;

    SelectManyIterable(Stream<T> stream, Selector<T, Iterable<TSelected>> selector) {
        this.stream = stream;
        this.selector = selector;
    }

    @Override
    public Iterator<TSelected> iterator() {
        Iterator<T> wrapped = stream.iterator();
        return new Iterator<TSelected>() {
            private boolean nextEvaluated;
            private TSelected next;
            private Iterator<TSelected> currentIterator;

            public boolean hasNext() {
                evaluateNext();
                return next != null;
            }

            @Override
            public TSelected next() {
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
                if (currentIterator != null && currentIterator.hasNext()) {
                    next = currentIterator.next();
                } else {
                    while (wrapped.hasNext() && next == null) {
                        T entry = wrapped.next();
                        Iterable<TSelected> many = selector.select(entry);
                        currentIterator = many.iterator();
                        if (currentIterator.hasNext())
                            next = currentIterator.next();
                    }
                }

                nextEvaluated = true;
            }
        };
    }
}
