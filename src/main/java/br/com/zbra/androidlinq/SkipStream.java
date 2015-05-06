package br.com.zbra.androidlinq;

import java.util.Iterator;
import java.util.NoSuchElementException;

class SkipStream<T> extends AbstractStream<T> {

    private final int count;
    private final AbstractStream<T> stream;

    SkipStream(AbstractStream<T> stream, int count) {
        this.stream = stream;
        this.count = count;
    }

    @Override
    public int count() {
        int originalCount = stream.count();
        return count >= originalCount ? 0 : originalCount - count;
    }

    @Override
    public Iterator<T> iterator() {
        return new SkipIterator<>(stream.iterator(), count);
    }

    @Override
    protected Iterator<T> reverseIterator() {
        return super.reverseIterator();
    }

    private static class SkipIterator<T> implements Iterator<T> {
        private final long count;
        private final Iterator<T> wrapped;

        public SkipIterator(Iterator<T> wrapped, long count) {
            this.count = count;
            this.wrapped = wrapped;

            for (int i = 0; i < count && wrapped.hasNext(); i++)
                wrapped.next();
        }

        @Override
        public boolean hasNext() {
            return wrapped.hasNext();
        }

        @Override
        public T next() {
            return wrapped.next();
        }
    }
}
