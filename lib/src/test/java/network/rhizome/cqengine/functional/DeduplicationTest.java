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
package network.rhizome.cqengine.functional;

import network.rhizome.cqengine.testutil.Car;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.index.hash.HashIndex;
import network.rhizome.cqengine.query.Query;
import network.rhizome.cqengine.query.option.DeduplicationOption;
import network.rhizome.cqengine.query.option.DeduplicationStrategy;
import network.rhizome.cqengine.resultset.ResultSet;

import org.junit.Test;

import java.util.Collections;

import static network.rhizome.cqengine.testutil.Car.COLOR;
import static network.rhizome.cqengine.testutil.Car.Color.BLUE;
import static network.rhizome.cqengine.query.QueryFactory.*;
import static network.rhizome.cqengine.testutil.Car.MANUFACTURER;
import static org.junit.Assert.assertEquals;
/**
 * @author Niall Gallagher
 */
public class DeduplicationTest {

    @Test
    public void testDeduplication_Materialize() {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.add(new Car(1, "Ford", "Focus", BLUE, 5, 1000.0, Collections.<String>emptyList(), Collections.emptyList()));
        cars.addIndex(HashIndex.onAttribute(Car.COLOR));
        cars.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));

        Query<Car> query = or(
                equal(COLOR, BLUE),
                equal(MANUFACTURER, "Ford")
        );
        ResultSet<Car> results;
        results = cars.retrieve(query);
        assertEquals(2, results.size());

        DeduplicationOption deduplicate = deduplicate(DeduplicationStrategy.MATERIALIZE);
        results = cars.retrieve(query, queryOptions(deduplicate));
        assertEquals(1, results.size());
    }

    @Test
    public void testDeduplication_Logical() {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.add(new Car(1, "Ford", "Focus", BLUE, 5, 1000.0, Collections.<String>emptyList(), Collections.emptyList()));
        cars.addIndex(HashIndex.onAttribute(Car.COLOR));
        cars.addIndex(HashIndex.onAttribute(Car.MANUFACTURER));

        Query<Car> query = or(
                equal(COLOR, BLUE),
                equal(MANUFACTURER, "Ford")
        );
        ResultSet<Car> results;
        results = cars.retrieve(query);
        assertEquals(2, results.size());

        DeduplicationOption deduplicate = deduplicate(DeduplicationStrategy.LOGICAL_ELIMINATION);
        results = cars.retrieve(query, queryOptions(deduplicate));
        assertEquals(1, results.size());
    }
}
