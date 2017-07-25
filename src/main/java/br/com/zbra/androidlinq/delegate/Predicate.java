package br.com.zbra.androidlinq.delegate;

public interface Predicate<T> {
    boolean apply(T value);
}