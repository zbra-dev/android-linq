package br.com.zbra.androidlinq.delegate;

public interface Predicate<T> {
    public boolean apply(T value);
}