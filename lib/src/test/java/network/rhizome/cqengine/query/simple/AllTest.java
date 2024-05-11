/**
 * Copyright 2012-2015 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package network.rhizome.cqengine.query.simple;

import org.junit.Test;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.query.Query;
import network.rhizome.cqengine.query.logical.And;
import network.rhizome.cqengine.query.logical.Or;
import network.rhizome.cqengine.resultset.ResultSet;

import static java.util.Arrays.asList;
import static network.rhizome.cqengine.query.QueryFactory.*;
import static network.rhizome.cqengine.query.option.DeduplicationStrategy.LOGICAL_ELIMINATION;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class AllTest {

    @Test
    public void testAll() {
        IndexedCollection<Integer> indexedCollection = new ConcurrentIndexedCollection<Integer>();
        indexedCollection.addAll(asList(1, 2, 3, 4, 5));
        IndexedCollection<Integer> collection = indexedCollection;
        Query<Integer> query = all(Integer.class);
        ResultSet<Integer> results = collection.retrieve(query);
        assertEquals(5, results.size());
        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testAllAnd() {
        IndexedCollection<Integer> indexedCollection = new ConcurrentIndexedCollection<Integer>();
        indexedCollection.addAll(asList(1, 2, 3, 4, 5));
        IndexedCollection<Integer> collection = indexedCollection;
        final And<Integer> query = and(
                all(Integer.class),
                lessThan(selfAttribute(Integer.class), 3)
        );
        ResultSet<Integer> results = collection.retrieve(query);
        assertEquals(2, results.size());
        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testAllOr() {
        IndexedCollection<Integer> indexedCollection = new ConcurrentIndexedCollection<Integer>();
        indexedCollection.addAll(asList(1, 2, 3, 4, 5));
        IndexedCollection<Integer> collection = indexedCollection;
        final Or<Integer> query = or(
                all(Integer.class),
                lessThan(selfAttribute(Integer.class), 3)
        );
        ResultSet<Integer> results = collection.retrieve(query, queryOptions(deduplicate(LOGICAL_ELIMINATION)));
        assertEquals(5, results.size());
        assertTrue(results.iterator().hasNext());
    }
}