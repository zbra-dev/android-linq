package br.com.zbra.androidlinq.delegate;

public interface Aggregator<T, R> {
    public R aggregate(R r, T t);
}
