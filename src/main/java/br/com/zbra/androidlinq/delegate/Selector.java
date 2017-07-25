package br.com.zbra.androidlinq.delegate;

public interface Selector<T, R> {
    R select(T value);
}
