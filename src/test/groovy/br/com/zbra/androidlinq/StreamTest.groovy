package br.com.zbra.androidlinq

import br.com.zbra.androidlinq.delegate.*
import br.com.zbra.androidlinq.exception.MultipleElementsFoundException

import static br.com.zbra.androidlinq.Linq.stream as stream

class StreamTest extends GroovyTestCase {

    void testWhere() {
        assert [0, 2, 4, 6, 8] ==
                stream(0..9)
                        .where({ int n -> n % 2 == 0 })
                        .toList()
    }

    void testSelect() {
        def integers = 0..9
        assert integers.collect { it * 2 } ==
                stream(integers)
                        .select({ n -> n * 2 })
                        .toList()
    }

    void testSelectMany() {
        assert 1..9 ==
                stream([1..3, [4], 5..6, [7], 8..9])
                        .selectMany({ c -> c })
                        .toList()

        assert [] ==
                stream([[], [], [], [], [], [], []])
                        .selectMany({ n -> n })
                        .toList()

        assert [null, null, null] ==
                stream([[null], [null], [null]])
                        .selectMany({ n -> n })
                        .toList()

        shouldFail(NullPointerException.class, {
            stream([[]])
                    .selectMany({ n -> null })
                    .toList()
        });
    }

    void testGroupBy() {
        def result
        def integers = 0..9
        def keySelector = { int n -> n % 2 == 0 ? "Even" : "Odd" } as Selector
        def elementSelector = { int n -> n * 10 } as Selector

        // groupBy with keySelector only
        result = stream(integers)
                .groupBy(keySelector)
                .toList()

        assert result.size() == 2

        assert result.get(0).key == "Even"
        assert result.get(0).elements.toList() == [0, 2, 4, 6, 8]

        assert result.get(1).key == "Odd"
        assert result.get(1).elements.toList() == [1, 3, 5, 7, 9]

        // groupBy with key and element selectors
        result = stream(integers)
                .groupBy(keySelector, elementSelector)
                .toList()

        assert result.size() == 2

        assert result.get(0).key == "Even"
        assert result.get(0).elements.toList() == [00, 20, 40, 60, 80]

        assert result.get(1).key == "Odd"
        assert result.get(1).elements.toList() == [10, 30, 50, 70, 90]
    }

    void testOrderBy() {
        def integers = 0..9
        def integersDescending = 9..0
        def shuffledItems = []

        shuffledItems.addAll(integers)
        Collections.shuffle(shuffledItems);

        // orderBy ascending
        assert integers ==
                stream(shuffledItems)
                        .orderBy({ n -> n })
                        .toList()

        // orderBy ascending with comparator
        assert integers ==
                stream(shuffledItems)
                        .orderBy({ n -> n }, { n1, n2 -> n1 - n2 })
                        .toList()

        // orderBy descending
        assert integersDescending ==
                stream(shuffledItems)
                        .orderByDescending({ n -> n })
                        .toList()

        // orderBy descending with comparator
        assert integersDescending ==
                stream(shuffledItems)
                        .orderByDescending({ n -> n }, { n1, n2 -> n1 - n2 })
                        .toList()
    }

    void testAggregate() {
        def integers = 0..9
        assert integers.sum() ==
                stream(integers).aggregate(0, { accumulate, n -> accumulate + n })
    }

    void testTake() {
        def integers = 0..9

        // fail: param cannot be <= 0
        shouldFail(IllegalArgumentException.class, {
            stream(integers).take(-1).toList();
        })

        shouldFail(IllegalArgumentException.class, {
            stream(integers).take(0).toList();
        })

        shouldFail(NoSuchElementException.class, {
            def iterator = stream(integers).take(1).iterator();
            iterator.next();
            iterator.next();
        })

        // takes 5 items
        assert stream(integers).take(5).toList().size() == 5
        // takes 10 items (as many as are into the collection)
        assert stream(integers).take(10).toList().size() == 10
        // takes 100 items (get all items)
        assert stream(integers).take(100).toList().size() == 10
    }

    void testDistinct() {
        def integers = 0..9

        // no dup items
        assert integers ==
                stream(integers).distinct().toList()

        // every item has a dup
        def range = new ArrayList<Integer>()
        range.addAll(integers)
        range.addAll(integers)

        assert integers ==
                stream(range).distinct().toList()
    }

    void testSumByte() {
        def list = 0..9
        def sum = list.sum()

        // sum Bytes
        assert sum == stream(list).sum({ n -> (byte) n } as SelectorByte)

        // sum Shorts
        assert sum == stream(list).sum({ n -> (short) n } as SelectorShort)

        // sum Integers
        assert sum == stream(list).sum({ n -> (int) n } as SelectorInteger)

        // sum Longs
        assert sum == stream(list).sum({ n -> (long) n } as SelectorLong)

        // sum Floats
        assert sum == stream(list).sum({ n -> (float) n } as SelectorFloat)

        // sum Doubles
        assert sum == stream(list).sum({ n -> (double) n } as SelectorDouble)

        // sum BigDecimals
        assert sum == stream(list).sum({ int n -> new BigDecimal(n) } as SelectorBigDecimal)
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    void testAny() {
        def integers = 0..9

        // any with no parameters
        assert stream([]).any() == false
        assert stream([1]).any() == true
        assert stream(integers).any() == true

        // any passing Predicate
        assert stream([]).any({ n -> n == 0 } as Predicate) == false
        assert stream([1]).any({ n -> n == 1 } as Predicate) == true
        assert stream(integers).any({ n -> n == 5 } as Predicate) == true
        assert stream(integers).any({ n -> n == 100 } as Predicate) == false
    }

    void testCount() {
        assert stream([]).count() == 0
        assert stream(0..9).count() == 10
    }

    void testFirst() {
        def integers = 0..9

        // first with no parameters
        assert stream([]).first() == null
        assert stream(integers).first() == 0

        // first passing Predicate
        assert stream(integers).first({ n -> n > 5 }) == 6
        assert stream(integers).first({ n -> n > 10 }) == null
    }


    void testSingle() {
        def integers = 0..9

        // single with no parameters
        assert stream([]).single() == null
        shouldFail(MultipleElementsFoundException.class, {
            stream(integers).single()
        })

        // single passing Predicate
        assert stream(integers).single({ n -> n == 5 }) == 5
        assert stream(integers).single({ n -> n == 10 }) == null
        shouldFail(MultipleElementsFoundException.class, {
            stream(integers).single({ n -> n > 5 })
        })
    }

    void testToList() {
        def range = 0..9
        def result = stream(range).toList()
        assert result.size() == 10
        assert result instanceof List
        assert result == range
    }

    void testToMap() {
        def map
        def integers = 0..9
        def keySelector = { n -> n } as Selector
        def elementSelector = { int n -> n * 10 } as Selector

        // map with key selector only
        map = stream(integers).toMap(keySelector)

        assert map.keySet().size() == integers.size()
        assert map.values().toList() == (integers)

        // map with key and element selectors
        map = stream(integers).toMap(keySelector, elementSelector)

        assert map.keySet().size() == integers.size()
        assert map.values().toList() == integers.collect { it * 10 }

        // empty Iterable generate empty maps
        assert stream([]).toMap(keySelector).isEmpty()
        assert stream([]).toMap(keySelector, elementSelector).isEmpty()
    }
}
