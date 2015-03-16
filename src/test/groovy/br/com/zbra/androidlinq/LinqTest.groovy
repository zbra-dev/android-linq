package br.com.zbra.androidlinq

import static br.com.zbra.androidlinq.Linq.stream

class LinqTest extends GroovyTestCase {
    void testStreamMap() {

        def map = [ 1: "One", 2: "Two", 3: "Three", 4: "Four", 5: "Five"]
        def entrySetStream = stream(map)

        assert entrySetStream.select({ n -> n.key }).toList() == 1..5
        assert entrySetStream.select({ n -> n.value }).toList() == [ "One", "Two", "Three", "Four", "Five" ]
        assert entrySetStream.toMap({n -> n.key }, { n -> n.value }) == map
    }
}
