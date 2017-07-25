package br.com.zbra.androidlinq

import br.com.zbra.androidlinq.delegate.*
import br.com.zbra.androidlinq.exception.MultipleElementsFoundException

import static br.com.zbra.androidlinq.Linq.stream as stream

@SuppressWarnings("GroovyUnusedDeclaration")
class StreamTest extends GroovyTestCase {

    void testWhere() {
        def stream = stream(0..9).where({ int n -> n % 2 == 0 })

        assert [0, 2, 4, 6, 8] == stream.toList()
        assert [8, 6, 4, 2, 0] == stream.reverse().toList()
    }

    void testSelect() {
        def integers = 0..9
        def stream = stream(integers).select({ n -> n * 2 })

        assert integers.collect { it * 2 } == stream.toList()
        assert integers.reverse().collect { it * 2 } == stream.reverse().toList()

        assert integers.size() == stream.count()
    }

    void testSelectMany() {
        assert 1..9 ==
                stream([1..3, [4], 5..6, [7], 8..9])
                        .selectMany({ c -> c })
                        .toList()

        assert [9, 8, 7, 6, 5, 4, 3, 2, 1] ==
                stream([1..3, [4], 5..6, [7], 8..9])
                        .selectMany({ c -> c })
                        .reverse()
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
        })
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
        def groupByStream = stream(integers)
                .groupBy(keySelector, elementSelector)

        result = groupByStream.toList()

        assert result.size() == 2

        assert result.get(0).key == "Even"
        assert result.get(0).elements.toList() == [00, 20, 40, 60, 80]

        assert result.get(1).key == "Odd"
        assert result.get(1).elements.toList() == [10, 30, 50, 70, 90]

        // test reverse
        def reverseResult = groupByStream.reverse().toList()
        assert reverseResult.get(0).key == "Odd"
        assert reverseResult.get(0).elements.toList() == [10, 30, 50, 70, 90]

        assert reverseResult.get(1).key == "Even"
        assert reverseResult.get(1).elements.toList() == [00, 20, 40, 60, 80]
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    void testOrderBy() {
        def integers = 0..9
        def integersDescending = 9..0
        def numbersComparator = { n1, n2 -> n1 - n2 }
        def shuffledItems = []

        shuffledItems.addAll(integers)
        Collections.shuffle(shuffledItems)

        // orderBy ascending
        assert integers ==
                stream(shuffledItems)
                        .orderBy({ n -> n })
                        .toList()

        // orderBy ascending with comparator
        assert integers ==
                stream(shuffledItems)
                        .orderBy({ n -> n }, numbersComparator)
                        .toList()

        // orderBy count()
        assert shuffledItems.size() ==
                stream(shuffledItems)
                        .orderBy({ n -> n }, numbersComparator)
                        .count()

        // orderBy descending
        assert integersDescending ==
                stream(shuffledItems)
                        .orderByDescending({ n -> n })
                        .toList()

        // orderBy descending with comparator
        assert integersDescending ==
                stream(shuffledItems)
                        .orderByDescending({ n -> n }, numbersComparator)
                        .toList()

        // reverse cohesion
        assert stream(shuffledItems).orderBy({ n -> n }).toList() ==
                stream(shuffledItems).orderByDescending({ n -> n }).reverse().toList()

        assert stream(shuffledItems).orderByDescending({ n -> n }).toList() ==
                stream(shuffledItems).orderBy({ n -> n }).reverse().toList()

        // thenBy & thenByDescending over orderBy
        def matrix = [[1, 1], [1, 2], [1, 3], [2, 1], [2, 2], [2, 3], [3, 1], [3, 2], [3, 3]]
        def shuffledMatrix = []

        shuffledMatrix.addAll(matrix)
        Collections.shuffle(shuffledMatrix)

        assert [[1, 1], [1, 2], [1, 3], [2, 1], [2, 2], [2, 3], [3, 1], [3, 2], [3, 3]] ==
                stream(shuffledMatrix)
                        .orderBy({ n -> n[0] })
                        .thenBy({ n -> n[1] })
                        .toList()

        assert [[1, 3], [1, 2], [1, 1], [2, 3], [2, 2], [2, 1], [3, 3], [3, 2], [3, 1]] ==
                stream(shuffledMatrix)
                        .orderBy({ n -> n[0] })
                        .thenByDescending({ n -> n[1] })
                        .toList()


        assert [[1, 1], [1, 2], [1, 3], [2, 1], [2, 2], [2, 3], [3, 1], [3, 2], [3, 3]] ==
                stream(shuffledMatrix)
                        .orderBy({ n -> n[0] })
                        .thenBy({ n -> n[1] }, numbersComparator)
                        .toList()

        assert [[1, 3], [1, 2], [1, 1], [2, 3], [2, 2], [2, 1], [3, 3], [3, 2], [3, 1]] ==
                stream(shuffledMatrix)
                        .orderBy({ n -> n[0] })
                        .thenByDescending({ n -> n[1] }, numbersComparator)
                        .toList()

        // thenBy & thenByDescending over orderByDescending
        assert [[3, 1], [3, 2], [3, 3], [2, 1], [2, 2], [2, 3], [1, 1], [1, 2], [1, 3]] ==
                stream(shuffledMatrix)
                        .orderByDescending({ n -> n[0] })
                        .thenBy({ n -> n[1] })
                        .toList()

        assert [[3, 3], [3, 2], [3, 1], [2, 3], [2, 2], [2, 1], [1, 3], [1, 2], [1, 1]] ==
                stream(shuffledMatrix)
                        .orderByDescending({ n -> n[0] })
                        .thenByDescending({ n -> n[1] })
                        .toList()


        assert [[3, 1], [3, 2], [3, 3], [2, 1], [2, 2], [2, 3], [1, 1], [1, 2], [1, 3]] ==
                stream(shuffledMatrix)
                        .orderByDescending({ n -> n[0] })
                        .thenBy({ n -> n[1] }, numbersComparator)
                        .toList()

        assert [[3, 3], [3, 2], [3, 1], [2, 3], [2, 2], [2, 1], [1, 3], [1, 2], [1, 1]] ==
                stream(shuffledMatrix)
                        .orderByDescending({ n -> n[0] })
                        .thenByDescending({ n -> n[1] }, numbersComparator)
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
            stream(integers).take(-1).toList()
        })

        shouldFail(NoSuchElementException.class, {
            def iterator = stream(integers).take(1).iterator()
            iterator.next()
            iterator.next()
        })

        // takes 5 items
        assert stream(integers).take(5).toList().size() == 5
        // takes 10 items (as many as are into the collection)
        assert stream(integers).take(10).toList().size() == 10
        // takes 100 items (get all items)
        assert stream(integers).take(100).toList().size() == 10
        // takes 5 and count
        assert stream(integers).take(5).count() == 5
        // takes 5 and reverses
        assert stream(integers).take(5).reverse().toList() == [4, 3, 2, 1, 0]
    }

    void testSkip() {
        def integers = 0..9 as int[]

        // fail: param cannot be <= 0
        shouldFail(IllegalArgumentException.class, {
            stream(integers).skip(-1).toList()
        })

        shouldFail(NoSuchElementException.class, {
            stream(integers).skip(10).iterator().next()
        })

        // skips 5 items
        assert stream(integers).skip(5).toList().size() == 5
        // skips 10 items (as many as are into the collection)
        assert stream(integers).skip(10).toList().size() == 0
        // skips 100 items (more than present in the collection)
        assert stream(integers).skip(100).toList().size() == 0
        // skips 5 and count
        assert stream(integers).skip(5).count() == 5
        // skips 5 and reverses
        assert stream(integers).skip(5).reverse().toList().toArray() as int[] == [9, 8, 7, 6, 5] as int []
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

    void testSum() {
        def list = 0..9 as List<Integer>
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

    void testMax() {

        // max for numbers
        def numbers = [3, 6, 8, 4, 2, 0, 1, 9, 7, 5]
        assert stream(numbers).max({ n -> n }) == 9

        // max for dates
        def dates = [
                new Date(2017, 10, 20),
                new Date(2018, 1, 1),
                new Date(2012, 9, 7),
                new Date(2015, 7, 27)
        ]
        assert stream(dates).max({ n -> n }) == new Date(2018, 1, 1)

        // max for strings
        def strings = ["Shift", "Alê", "Falavinha", "Tomoiti",  "Milton", "Michele", "Fillipe", "Zé"]
        assert stream(strings).max({ n -> n }) == "Zé"

        // max with custom comparator
        def objects = [ [id: 322], [id: 420], [id: 3154], [id: 123] ]
        assert stream(objects).max({ n -> n.id }, { n1, n2 -> n1 - n2 }).id == 3154
    }

    void testMin() {

        // max for numbers
        def numbers = [3, 6, 8, 4, 2, 0, 1, 9, 7, 5]
        assert stream(numbers).min({ n -> n }) == 0

        // max for dates
        def dates = [
                new Date(2017, 10, 20),
                new Date(2018, 1, 1),
                new Date(2012, 9, 7),
                new Date(2015, 7, 27)
        ]
        assert stream(dates).min({ n -> n }) == new Date(2012, 9, 7)

        // max for strings
        def strings = ["Shift", "Alê", "Falavinha", "Tomoiti",  "Milton", "Michele", "Fillipe", "Zé"]
        assert stream(strings).min({ n -> n }) == "Alê"

        // max with custom comparator
        def objects = [ [id: 322], [id: 420], [id: 3154], [id: 123] ]
        assert stream(objects).min({ n -> n.id }, { n1, n2 -> n1 - n2 }).id == 123
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
        assert stream(integers).first() == 0
        shouldFail(NoSuchElementException.class, {
            assert stream([]).first()
        })

        // first passing Predicate
        assert stream(integers).first({ n -> n > 5 }) == 6
        shouldFail(NoSuchElementException.class, {
            assert stream(integers).first({ n -> n > 10 })
        })

        // firstOrDefault
        assert stream([]).firstOrDefault(10) == 10
        assert stream(integers).firstOrDefault(10) == 0

        // firstOrDefault passing Predicate
        assert stream(integers).firstOrDefault({ n -> n > 5 }, 10) == 6
        assert stream(integers).firstOrDefault({ n -> n == 100 }, -1) == -1

        // firstOrNull
        assert stream([]).firstOrNull() == null
        assert stream(integers).firstOrNull() == 0

        // firstOrDefault passing Predicate
        assert stream(integers).firstOrNull({ n -> n > 5 }) == 6
        assert stream(integers).firstOrNull({ n -> n == 100 }) == null
    }

    void testLast() {
        def integers = 0..9

        // last with no parameters
        assert stream(integers).last() == 9
        shouldFail(NoSuchElementException.class, {
            assert stream([]).last()
        })

        // last passing Predicate
        assert stream(integers).last({ n -> n < 5 }) == 4
        shouldFail(NoSuchElementException.class, {
            assert stream(integers).last({ n -> n > 10 })
        })

        // lastOrDefault
        assert stream([]).lastOrDefault(10) == 10
        assert stream(integers).lastOrDefault(10) == 9

        // lastOrDefault passing Predicate
        assert stream(integers).lastOrDefault({ n -> n < 5 }, 10) == 4
        assert stream(integers).lastOrDefault({ n -> n == 100 }, -1) == -1

        // lastOrNull
        assert stream([]).lastOrNull() == null
        assert stream(integers).lastOrNull() == 9

        // lastOrDefault passing Predicate
        assert stream(integers).lastOrNull({ n -> n < 5 }) == 4
        assert stream(integers).lastOrNull({ n -> n == 100 }) == null
    }

    void testSingle() {
        def integers = 0..9

        // single with no parameters
        assert stream([5]).single() == 5
        shouldFail(NoSuchElementException.class, {
            stream([]).single()
        })
        shouldFail(MultipleElementsFoundException.class, {
            stream(integers).single()
        })

        // single passing Predicate
        assert stream(integers).single({ n -> n == 5 }) == 5
        shouldFail(NoSuchElementException.class, {
            stream(integers).single({ n -> n == 10 })
        })
        shouldFail(MultipleElementsFoundException.class, {
            stream(integers).single({ n -> n > 5 })
        })

        // singleOrDefault with no parameters
        assert stream([5]).singleOrDefault(10) == 5
        assert stream([]).singleOrDefault(10) == 10
        shouldFail(MultipleElementsFoundException.class, {
            stream(integers).singleOrDefault(10)
        })

        // singleOrDefault passing Predicate
        assert stream(integers).singleOrDefault({ n -> n == 5 }, -1) == 5
        assert stream(integers).singleOrDefault({ n -> n == 10 }, -1) == -1
        shouldFail(MultipleElementsFoundException.class, {
            stream(integers).singleOrDefault({ n -> n > 5 }, -1)
        })

        // singleOrNull with no parameters
        assert stream([5]).singleOrNull() == 5
        assert stream([]).singleOrNull() == null
        shouldFail(MultipleElementsFoundException.class, {
            stream(integers).singleOrNull()
        })

        // singleOrNull passing Predicate
        assert stream(integers).singleOrNull({ n -> n == 5 }) == 5
        assert stream(integers).singleOrNull({ n -> n == 10 }) == null
        shouldFail(MultipleElementsFoundException.class, {
            stream(integers).singleOrNull({ n -> n > 5 })
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

    void testReverse() {
        def integers = 0..9

        assert integers ==
                stream(integers).reverse().reverse().toList()
    }
}

