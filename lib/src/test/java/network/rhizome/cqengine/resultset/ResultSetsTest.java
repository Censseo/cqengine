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
package network.rhizome.cqengine.resultset;

import org.junit.Test;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.resultset.ResultSet;
import network.rhizome.cqengine.resultset.common.ResultSets;
import network.rhizome.cqengine.resultset.iterator.IteratorUtil;

import java.util.Collection;

import static java.util.Arrays.asList;
import static network.rhizome.cqengine.query.QueryFactory.all;
import static org.junit.Assert.*;

public class ResultSetsTest {

    @Test
    public void testAsCollection() throws Exception {
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        indexedCollection.addAll(asList("how", "now", "brown", "cow"));

        ResultSet<String> resultSet = indexedCollection.retrieve(all(String.class));
        Collection<String> collection = ResultSets.asCollection(resultSet);

        assertEquals(resultSet.size(), collection.size());
        assertEquals(resultSet.size(), IteratorUtil.countElements(collection));
        assertEquals(resultSet.isEmpty(), collection.isEmpty());
        assertTrue(collection.contains("now"));
        assertFalse(collection.contains("baz"));
    }
}