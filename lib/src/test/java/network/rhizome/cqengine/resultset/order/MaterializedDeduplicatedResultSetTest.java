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
package network.rhizome.cqengine.resultset.order;

import org.junit.Test;

import network.rhizome.cqengine.resultset.order.MaterializedDeduplicatedResultSet;
import network.rhizome.cqengine.resultset.stored.StoredSetBasedResultSet;

import org.junit.Assert;

import java.util.Collections;
import java.util.Iterator;

/**
 * @author dsmith
 */
public class MaterializedDeduplicatedResultSetTest {
    @Test
    public void testMaterializingResultSetIterator() throws Exception {
        final MaterializedDeduplicatedResultSet<Object> set = new MaterializedDeduplicatedResultSet<Object>(new StoredSetBasedResultSet<Object>(Collections.<Object>singleton(this)));
        final Iterator<Object> it = set.iterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertTrue(it.hasNext());
    }
}
