package br.com.zbra.androidlinq;

import java.util.Iterator;

class TakeIterable<T> implements Iterable<T> {
    private final Stream<T> stream;
    private final int count;

    TakeIterable(Stream<T> stream, int count) {
        if (count <= 0)
            throw new IllegalArgumentException("count must be greater than 0: " + count);

        this.stream = stream;
        this.count = count;
    }

    @Override
    public Iterator<T> iterator() {

        Iterator<T> wrapped = stream.iterator();
        return new Iterator<T>() {
            long index = 0;

            @Override
            public boolean hasNext() {
                return index < count && wrapped.hasNext();
            }

            @Override
            public T next() {
                index++;
                return wrapped.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
