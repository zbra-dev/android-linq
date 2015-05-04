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
        return new ArrayIterator<>(source, 0, source.length - 1);
    }


    @Override
    protected Iterator<T> reverseIterator() {
        return new ArrayIterator<>(source, source.length - 1, 0);
    }

    private static class ArrayIterator<T> implements Iterator<T> {
        private int index;
        private final int increment;
        private final int stopOn;
        private final T[] source;

        public ArrayIterator(T[] source, int from, int to) {
            this.index = from;
            this.increment = from < to ? 1 : -1;
            this.stopOn = source.length == 0 ? from : to + increment;
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return index != stopOn;
        }

        @Override
        public T next() {
            if (index == stopOn) throw new NoSuchElementException();
            T nextElement = source[index];
            index += increment;
            return nextElement;
        }
    }
}
