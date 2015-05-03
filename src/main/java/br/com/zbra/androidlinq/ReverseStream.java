package br.com.zbra.androidlinq;

import java.util.Iterator;

class ReverseStream<T> extends AbstractStream<T> {

    private final AbstractStream<T> stream;

    ReverseStream(AbstractStream<T> stream) {
        this.stream = stream;
    }

    @Override
    public Iterator<T> iterator() {
        return stream.reverseIterator();
    }

    @Override
    protected Iterator<T> reverseIterator() {
        return stream.iterator();
    }
}
