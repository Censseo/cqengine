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
package network.rhizome.cqengine.resultset.filter;

import network.rhizome.cqengine.query.Query;
import network.rhizome.cqengine.query.option.QueryOptions;
import network.rhizome.cqengine.resultset.ResultSet;
import network.rhizome.cqengine.resultset.iterator.IteratorUtil;

import java.util.Iterator;

/**
 * A {@link ResultSet} which wraps an {@link Iterable} and which calls an abstract {@link #isValid(Object, network.rhizome.cqengine.query.option.QueryOptions)} method
 * for each object. Returns the object if the implementation of the method returns true, or skips to the
 * next object if the method returns false.
 *
 * @author Niall Gallagher
 */
public abstract class FilteringResultSet<O> extends ResultSet<O> {

    final ResultSet<O> wrappedResultSet;
    final Query<O> query;
    final QueryOptions queryOptions;

    public FilteringResultSet(ResultSet<O> wrappedResultSet, Query<O> query, QueryOptions queryOptions) {
        this.wrappedResultSet = wrappedResultSet;
        this.query = query;
        this.queryOptions = queryOptions;
    }

    @Override
    public Iterator<O> iterator() {
        return new FilteringIterator<O>(wrappedResultSet.iterator(), queryOptions) {
            @Override
            public boolean isValid(O object, QueryOptions queryOptions) {
                return FilteringResultSet.this.isValid(object, queryOptions);
            }
        };
    }

    @Override
    public boolean contains(O object) {
        // Check if this ResultSet contains the given object by iterating the ResultSet...
        return IteratorUtil.iterableContains(this, object);
    }

    @Override
    public boolean matches(O object) {
        return query.matches(object, queryOptions);
    }

    public abstract boolean isValid(O object, QueryOptions queryOptions);

    @Override
    public int size() {
        return IteratorUtil.countElements(this);
    }

    @Override
    public int getRetrievalCost() {
        return wrappedResultSet.getRetrievalCost();
    }

    @Override
    public int getMergeCost() {
        return wrappedResultSet.getMergeCost();
    }

    @Override
    public void close() {
        wrappedResultSet.close();
    }

    @Override
    public Query<O> getQuery() {
        return query;
    }

    @Override
    public QueryOptions getQueryOptions() {
        return queryOptions;
    }
}
