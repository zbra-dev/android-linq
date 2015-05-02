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
        return new ArrayIterator<>(source);
    }

    private static class ArrayIterator<T> implements Iterator<T> {
        private int index;
        private T[] source;

        public ArrayIterator(T[] source) {
            this.index = 0;
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return index < source.length;
        }

        @Override
        public T next() {
            if (index >= source.length) throw new NoSuchElementException();
            return source[index++];
        }
    }
}
