package br.com.zbra.androidlinq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class IterableStream<T> extends AbstractStream<T> {

    private final Iterable<T> iterable;

    public IterableStream(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }
}
