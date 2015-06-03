package br.com.zbra.androidlinq

import java.lang.reflect.Constructor

import static br.com.zbra.androidlinq.Linq.stream

class LinqTest extends GroovyTestCase {

    void testConstructor() {
        Constructor<Linq> constructor = Linq.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        shouldFailWithCause(UnsupportedOperationException.class, {
            constructor.newInstance();
        });
    }

    void testArrayStream() {
        def array = 0..9 as int[]
        def arrayStream = stream(array)

        assert arrayStream.count() == array.length
        assert arrayStream.toList() == array
        assert arrayStream.reverse().toList() == 9..0 as int[]

        assert stream(new int[0]).toList() == []
        assert stream(new int[0]).reverse().toList() == []
    }

    void testListStream() {
        def list = 0..9 as List<Integer>
        def listStream = stream(list)

        assert listStream.count() == list.size()
        assert listStream.toList() == list
        assert listStream.reverse().toList() == 9..0 as int[]

        assert stream(new ArrayList<Integer>()).toList() == []
        assert stream(new ArrayList<Integer>()).reverse().toList() == []
    }

    void testIterableStream() {
        def iterable = 0..9 as Set<Integer>
        def iterableStream = stream(iterable)

        assert iterableStream.count() == iterable.size()
        assert iterableStream.toList() == iterable.toList()
        assert iterableStream.reverse().toList() == 9..0 as List<Integer>

        assert stream(new HashSet<Integer>()).toList() == []
        assert stream(new HashSet<Integer>()).reverse().toList() == []
    }

    void testMapStream() {

        def map = [ 1: "One", 2: "Two", 3: "Three", 4: "Four", 5: "Five"]
        def entrySetStream = stream(map)

        assert entrySetStream.select({ n -> n.key }).toList() == 1..5
        assert entrySetStream.select({ n -> n.value }).toList() == [ "One", "Two", "Three", "Four", "Five" ]
        assert entrySetStream.toMap({n -> n.key }, { n -> n.value }) == map

        assert stream(new HashMap<String, Integer>()).toList() == []
        assert stream(new HashMap<String, Integer>()).reverse().toList() == []

        assert stream(map).count() == map.size()
        assert stream(new HashMap<String, Integer>()).count() == 0

    }
}
