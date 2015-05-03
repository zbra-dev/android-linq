package br.com.zbra.androidlinq;

import java.util.Iterator;
import java.util.Stack;

class IterableStream<T> extends AbstractStream<T> {

    private final Iterable<T> iterable;

    public IterableStream(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    @Override
    protected Iterator<T> reverseIterator() {
        Stack<T> stack = new Stack<>();
        for (T t : iterable)
            stack.add(t);
        return stack.iterator();
    }
}
