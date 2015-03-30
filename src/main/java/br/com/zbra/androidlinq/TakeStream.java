package br.com.zbra.androidlinq;

import java.util.Iterator;
import java.util.NoSuchElementException;

class TakeStream<T> extends AbstractStream<T> {

    private final int count;
    private final Stream<T> stream;

    TakeStream(Stream<T> stream, int count) {
        this.stream = stream;

        if (count <= 0)
            throw new IllegalArgumentException("count must be greater than 0: " + count);

        this.count = count;
    }

    @Override
    public Iterator<T> iterator() {
        return new TakeIterator<>(stream.iterator(), count);
    }

    private static class TakeIterator<T> implements Iterator<T> {
        private final long count;
        private final Iterator<T> wrapped;

        long index = 0;

        public TakeIterator(Iterator<T> wrapped,  long count) {
            this.count = count;
            this.wrapped = wrapped;
        }

        @Override
        public boolean hasNext() {
            return index < count && wrapped.hasNext();
        }

        @Override
        public T next() {
            index++;
            if (index > count) throw new NoSuchElementException();
            return wrapped.next();
        }
    }
}
