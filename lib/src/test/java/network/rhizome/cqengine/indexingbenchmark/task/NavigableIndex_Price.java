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
package network.rhizome.cqengine.indexingbenchmark.task;

import network.rhizome.cqengine.indexingbenchmark.IndexingTask;
import network.rhizome.cqengine.testutil.Car;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.index.navigable.NavigableIndex;

import java.util.Collection;

/**
 * @author Niall Gallagher
 */
public class NavigableIndex_Price implements IndexingTask {

    private IndexedCollection<Car> indexedCollection;

    @Override
    public void init(Collection<Car> collection) {
        IndexedCollection<Car> indexedCollection1 = new ConcurrentIndexedCollection<Car>();
        indexedCollection1.addAll(collection);
        indexedCollection = indexedCollection1;
    }

    @Override
    public void buildIndex() {
        indexedCollection.addIndex(NavigableIndex.onAttribute(Car.PRICE));
    }
}
