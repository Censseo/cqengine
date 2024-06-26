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
package network.rhizome.cqengine.examples.dynamic;

import static network.rhizome.cqengine.query.QueryFactory.*;

import java.util.Map;

import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.attribute.Attribute;
import network.rhizome.cqengine.query.Query;
import network.rhizome.cqengine.resultset.ResultSet;

/**
 * Demonstrates generating attributes on-the-fly using reflection for fields in a POJO, building indexes on those
 * attributes on-the-fly, and then running queries against fields in the POJO.
 *
 * @author ngallagher
 * @since 2013-07-05 11:54
 */
public class DynamicExample {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // Generate attributes dynamically for fields in the given POJO...
        Map<String, Attribute<Car, Comparable>> attributes = DynamicIndexer.generateAttributesForPojo(Car.class);
        // Build indexes on the dynamically generated attributes...
        IndexedCollection<Car> cars = DynamicIndexer.newAutoIndexedCollection(attributes.values());

        // Add some objects to the collection...
        cars.add(new Car(1, "ford", "focus", 4, 9000));
        cars.add(new Car(2, "ford", "mondeo", 5, 10000));
        cars.add(new Car(2, "ford", "fiesta", 3, 2000));
        cars.add(new Car(3, "honda", "civic", 5, 11000));

        Query<Car> query = and(
                equal(attributes.get("manufacturer"), "ford"),
                lessThan(attributes.get("doors"), value(5)),
                greaterThan(attributes.get("horsepower"), value(3000))
        );
        ResultSet<Car> results = cars.retrieve(query);

        System.out.println("Ford cars with less than 5 doors and horsepower greater than 3000:- ");
        System.out.println("Using NavigableIndex: " + (results.getRetrievalCost() == 40));
        for (Car car : results) {
            System.out.println(car);
        }

        // Prints:
        //    Ford cars with less than 5 doors and horsepower greater than 3000:-
        //    Using NavigableIndex: true
        //    Car{carId=1, manufacturer='ford', model='focus', doors=4, horsepower=9000}
    }



    // This method is required for compatibility with Java 8 compiler (not required for Java 6 or 7 compiler)...
    static Comparable value(Comparable c) {
        return c;
    }
}
