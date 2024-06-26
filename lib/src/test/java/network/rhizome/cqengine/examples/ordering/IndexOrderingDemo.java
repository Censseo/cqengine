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
package network.rhizome.cqengine.examples.ordering;

import static network.rhizome.cqengine.query.QueryFactory.*;
import static network.rhizome.cqengine.query.option.EngineThresholds.INDEX_ORDERING_SELECTIVITY;

import network.rhizome.cqengine.testutil.Car;
import network.rhizome.cqengine.testutil.CarFactory;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.index.navigable.NavigableIndex;
import network.rhizome.cqengine.resultset.ResultSet;

/**
 * An example of how to enable the <i>index</i> ordering strategy, to order results by an attribute which does not
 * provide values for every object. In this example, some Car objects have features, and some do not.
 *
 * @author Niall Gallagher
 */
public class IndexOrderingDemo {

    public static void main(String[] args) {
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.addIndex(NavigableIndex.onAttribute(Car.FEATURES));
        cars.addIndex(NavigableIndex.onAttribute(forObjectsMissing(Car.FEATURES)));
        cars.addAll(CarFactory.createCollectionOfCars(100));

        ResultSet<Car> results = cars.retrieve(
                between(Car.CAR_ID, 40, 50),
                queryOptions(
                        orderBy(ascending(missingLast(Car.FEATURES))),
                        applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, 1.0))
                )
        );
        for (Car car : results) {
            System.out.println(car); // prints cars 40 -> 50, using the index on Car.FEATURES to accelerate ordering
        }
    }
}
