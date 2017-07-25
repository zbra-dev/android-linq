package br.com.zbra.androidlinq.delegate;

public interface Aggregator<T, R> {
    R aggregate(R r, T t);
}
