package br.com.zbra.androidlinq.delegate;

public interface EqualityComparator<T> {
    boolean compare(T value1, T value2);
}
