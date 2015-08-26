package br.com.zbra.androidlinq;

import java.util.Iterator;
import java.util.NoSuchElementException;

class TakeStream<T> extends AbstractStream<T> {

    private final int count;
    private final AbstractStream<T> stream;

    TakeStream(AbstractStream<T> stream, int count) {
        if (count < 0) throw new IllegalArgumentException("count must be greater than 0: " + count);

        this.stream = stream;
        this.count = count;
    }

    @Override
    public int count() {
        return count == 0 ? 0 : Math.min(count, stream.count());
    }

    @Override
    public Iterator<T> iterator() {
        return new TakeIterator<>(stream.iterator(), count);
    }

    @Override
    protected Iterator<T> reverseIterator() {
        return super.reverseIterator();
    }

    private static class TakeIterator<T> implements Iterator<T> {
        private long index = 0;
        private final long count;
        private final Iterator<T> wrapped;

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

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
}
