package br.com.zbra.androidlinq;

import java.util.Iterator;
import java.util.List;

class ListStream<T> extends AbstractStream<T> {

    private List<T> source;

    ListStream(List<T> source) {
        this.source = source;
    }

    @Override
    public int count() {
        return source.size();
    }

    @Override
    public Iterator<T> iterator() {
        return source.iterator();
    }
}
