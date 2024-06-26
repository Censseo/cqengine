package network.rhizome.cqengine.resultset;

import org.junit.Test;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.resultset.ResultSet;
import network.rhizome.cqengine.resultset.stored.StoredSetBasedResultSet;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static network.rhizome.cqengine.query.QueryFactory.*;
import static org.junit.Assert.*;

/**
 * Unit tests for the base {@link ResultSet}.
 */
public class ResultSetTest {

    @Test
    public void testStream() {
        List<String> input = asList("a", "b", "c", "d");
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        indexedCollection.addAll(input);

        ResultSet<String> resultSet = indexedCollection.retrieve(all(String.class), queryOptions(orderBy(ascending(selfAttribute(String.class)))));

        Stream<String> stream = resultSet.stream();

        List<String> output = stream.collect(toList());
        assertEquals(input, output);
    }

    @Test
    public void testStreamClose() {
        AtomicBoolean closeCalled = new AtomicBoolean();
        ResultSet<Integer> resultSet = new StoredSetBasedResultSet<Integer>(emptySet()) {
            @Override
            public void close() {
                super.close();
                closeCalled.set(true);
            }
        };

        Stream<Integer> stream = resultSet.stream();

        assertFalse(closeCalled.get());
        stream.close();
        assertTrue(closeCalled.get());
    }
}