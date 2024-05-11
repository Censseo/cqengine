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
package network.rhizome.cqengine.examples.wildcard;

import static network.rhizome.cqengine.query.QueryFactory.*;

import java.util.Arrays;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.attribute.Attribute;
import network.rhizome.cqengine.attribute.SelfAttribute;
import network.rhizome.cqengine.index.radix.RadixTreeIndex;
import network.rhizome.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import network.rhizome.cqengine.query.Query;
import network.rhizome.cqengine.query.option.QueryOptions;
import network.rhizome.cqengine.resultset.ResultSet;
import network.rhizome.cqengine.resultset.filter.FilteringResultSet;

/**
 * Demonstrates how to perform wildcard queries in CQEngine as of CQEngine ~1.0.3.
 * This will be further simplified in later versions.
 * <p/>
 * These queries can be accelerated by adding both {@link RadixTreeIndex} and {@link ReversedRadixTreeIndex}.
 *
 * @author Niall Gallagher
 */
public class Wildcard {

    public static void main(String[] args) {
        IndexedCollection<String> indexedCollection = new ConcurrentIndexedCollection<String>();
        indexedCollection.addAll(Arrays.asList("TEAM", "TEST", "TOAST", "T", "TT"));
        IndexedCollection<String> collection = indexedCollection;
        collection.addIndex(RadixTreeIndex.onAttribute(SELF));
        collection.addIndex(ReversedRadixTreeIndex.onAttribute(SELF));

        for (String match : retrieveWildcardMatches(collection, "T", "T")) {
            System.out.println(match); // TOAST, TEST, TT
        }
    }

    public static ResultSet<String> retrieveWildcardMatches(IndexedCollection<String> collection, final String prefix, final String suffix) {
        Query<String> query = and(startsWith(SELF, prefix), endsWith(SELF, suffix));
        ResultSet<String> candidates = collection.retrieve(query);

        return new FilteringResultSet<String>(candidates, query, noQueryOptions()) {
            @Override
            public boolean isValid(String candidate, QueryOptions queryOptions) {
                return candidate.length() >= prefix.length() + suffix.length();
            }
        };
    }

    static final Attribute<String, String> SELF = new SelfAttribute<String>(String.class);

}
