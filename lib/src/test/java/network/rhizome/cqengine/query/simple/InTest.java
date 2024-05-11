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

import network.rhizome.cqengine.examples.introduction.Car;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.attribute.Attribute;
import network.rhizome.cqengine.attribute.SimpleNullableAttribute;
import network.rhizome.cqengine.index.navigable.NavigableIndex;
import network.rhizome.cqengine.query.option.QueryOptions;

import org.junit.Assert;
import org.junit.Test;

import static network.rhizome.cqengine.query.QueryFactory.in;

import java.util.*;

/**
 * @author Kevin Minder
 * @author Niall Gallagher
 */
public class InTest {

    @Test
    public void testInMany() {
        // Create an indexed collection (note: could alternatively use CQEngine.copyFrom() existing collection)...
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();

        Attribute<Car, String> NAME = new SimpleNullableAttribute<Car, String>("name") {
            public String getValue(Car car, QueryOptions queryOptions) {
                return car.name;
            }
        };
        cars.addIndex(NavigableIndex.onAttribute(NAME));

        // Add some objects to the collection...
        cars.add(new Car(1, "ford", null, null));
        cars.add(new Car(2, "honda", null, null));
        cars.add(new Car(3, "toyota", null, null));

        Assert.assertEquals(cars.retrieve(in(NAME, "ford", "honda")).size(), 2);
        Assert.assertEquals(cars.retrieve(in(NAME, Arrays.asList("ford", "honda"))).size(), 2);
    }

    @Test
    public void testInOne() {
        // Create an indexed collection (note: could alternatively use CQEngine.copyFrom() existing collection)...
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();

        Attribute<Car, String> NAME = new SimpleNullableAttribute<Car, String>("name") {
            public String getValue(Car car, QueryOptions queryOptions) {
                return car.name;
            }
        };
        cars.addIndex(NavigableIndex.onAttribute(NAME));

        // Add some objects to the collection...
        cars.add(new Car(1, "ford", null, null));
        cars.add(new Car(2, "honda", null, null));
        cars.add(new Car(3, "toyota", null, null));

        Assert.assertEquals(cars.retrieve(in(NAME, "ford")).size(), 1);
        Assert.assertEquals(cars.retrieve(in(NAME, Collections.singletonList("ford"))).size(), 1);
    }

    @Test
    public void testInNone() {
        // Create an indexed collection (note: could alternatively use CQEngine.copyFrom() existing collection)...
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();

        Attribute<Car, String> NAME = new SimpleNullableAttribute<Car, String>("name") {
            public String getValue(Car car, QueryOptions queryOptions) {
                return car.name;
            }
        };
        cars.addIndex(NavigableIndex.onAttribute(NAME));

        // Add some objects to the collection...
        cars.add(new Car(1, "ford", null, null));
        cars.add(new Car(2, "honda", null, null));
        cars.add(new Car(3, "toyota", null, null));

        Assert.assertEquals(cars.retrieve(in(NAME)).size(), 0);
        Assert.assertEquals(cars.retrieve(in(NAME, new ArrayList<String>())).size(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void testInNull() {
        Attribute<Car, String> NAME = new SimpleNullableAttribute<Car, String>("name") {
            public String getValue(Car car, QueryOptions queryOptions) {
                return car.name;
            }
        };
        in(NAME, (Collection<String>) null);
    }
}
