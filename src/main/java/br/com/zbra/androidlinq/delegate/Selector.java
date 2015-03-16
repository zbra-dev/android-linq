package br.com.zbra.androidlinq.delegate;

public interface Selector<T, R> {
    public R select(T value);
}
