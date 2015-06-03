package br.com.zbra.androidlinq;

import java.util.Iterator;
import java.util.Map;

public class MapStream<K, V> extends AbstractStream<Map.Entry<K, V>> {

    private Map<K, V> map;

    public MapStream(Map<K, V> map) {
        this.map = map;
    }

    @Override
    public int count() {
        return map.size();
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    protected Iterator<Map.Entry<K, V>> reverseIterator() {
        return iterator();
    }

}
