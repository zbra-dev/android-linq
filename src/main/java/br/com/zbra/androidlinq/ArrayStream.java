package br.com.zbra.androidlinq;

import java.util.Iterator;
import java.util.NoSuchElementException;


class ArrayStream<T> extends AbstractStream<T> {

    private final T[] source;

    ArrayStream(T[] source) {
        this.source = source;
    }

    @Override
    public int count() {
        return source.length;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(source, 0, 1, source.length);
    }


    @Override
    protected Iterator<T> reverseIterator() {
        return new ArrayIterator<>(source, source.length - 1, -1, -1);
    }

    private static class ArrayIterator<T> implements Iterator<T> {
        private int index;
        private final int increment;
        private final int limit;
        private final T[] source;

        public ArrayIterator(T[] source, int index, int increment, int limit) {
            this.index = index;
            this.increment = increment;
            this.limit = limit;
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return index < source.length;
        }

        @Override
        public T next() {
            if (index == limit) throw new NoSuchElementException();
            T nextElement = source[index];
            index += increment;
            return nextElement;
        }
    }
}
