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
import network.rhizome.cqengine.index.standingquery.StandingQueryIndex;
import network.rhizome.cqengine.query.Query;
import network.rhizome.cqengine.resultset.ResultSet;

import static java.util.Arrays.asList;
import static network.rhizome.cqengine.query.QueryFactory.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class StringMatchesRegexTest {

    @Test
    public void testStringMatchesRegex() {
        Query<String> query = matchesRegex(selfAttribute(String.class), "f.*");
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        indexedCollection.addAll(asList("foo1", "foo2", "bar", "baz", "car"));
        IndexedCollection<String> collection = indexedCollection;
        ResultSet<String> results = collection.retrieve(query);
        assertEquals(2, results.size());
        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testStringMatchesRegexWithIndex() {
        Query<String> query = matchesRegex(selfAttribute(String.class), "f.*");
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        indexedCollection.addAll(asList("foo1", "foo2", "bar", "baz", "car"));
        IndexedCollection<String> collection = indexedCollection;
        collection.addIndex(StandingQueryIndex.onQuery(query));
        ResultSet<String> results = collection.retrieve(query);
        assertEquals(2, results.size());
        assertTrue(results.iterator().hasNext());
    }

    @Test
    public void testStringMatchesRegexNegatedWithIndex() {
        Query<String> query = not(matchesRegex(selfAttribute(String.class), "[fb].*"));
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        indexedCollection.addAll(asList("foo1", "foo2", "bar", "baz", "car"));
        IndexedCollection<String> collection = indexedCollection;
        collection.addIndex(StandingQueryIndex.onQuery(query));
        ResultSet<String> results = collection.retrieve(query);
        assertEquals(1, results.size());
        assertTrue(results.iterator().hasNext());
    }
}