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

import static network.rhizome.cqengine.query.QueryFactory.*;
import static network.rhizome.cqengine.query.option.EngineThresholds.*;

import network.rhizome.cqengine.testutil.Car;
import network.rhizome.cqengine.testutil.CarFactory;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.index.navigable.NavigableIndex;
import network.rhizome.cqengine.persistence.disk.DiskPersistence;
import network.rhizome.cqengine.query.option.EngineThresholds;
import network.rhizome.cqengine.query.option.QueryLog;
import network.rhizome.cqengine.resultset.ResultSet;
import network.rhizome.cqengine.resultset.iterator.IteratorUtil;

/**
 * TODO - remove this temporary test (functionality is tested in IndexedCollectionFunctionalTest).
 *
 * Created by npgall on 27/07/2015.
 */
public class IndexOrderingTest {

    public static void main(String[] args) {
        final int NUM_ITERATIONS = 1000;
        final int[] numObjects = {10000, 10000, 100000};
        final double[] selectivityThreshold = {0.0, 0.5, 1.0};

        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.addAll(CarFactory.createCollectionOfCars(1000000));

        cars.addIndex(NavigableIndex.onAttribute(Car.CAR_ID));
        cars.addIndex(NavigableIndex.onAttribute(Car.COLOR));

        for (int n : numObjects) {
            for (double s : selectivityThreshold) {
                long start = System.currentTimeMillis();
                long count = 0;
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    count = countRetrievedResults(cars, n, s);
                }
                long timeTaken = System.currentTimeMillis() - start;
                System.out.println("Number: " + n + ", selectivity threshold: " + s + ", time taken per iteration: " + (timeTaken / (double)NUM_ITERATIONS) + " (count=" + count + ")");
            }
        }
    }

    static long countRetrievedResults(IndexedCollection<Car> cars, int numObjects, double indexOrderingSelectivityThreshold) {
        ResultSet<Car> resultSet = cars.retrieve(and(lessThan(Car.CAR_ID, numObjects), equal(Car.COLOR, Car.Color.BLUE)), queryOptions(
                orderBy(descending(Car.CAR_ID)),
                applyThresholds(threshold(INDEX_ORDERING_SELECTIVITY, indexOrderingSelectivityThreshold))
        ));
        int count = 0;
        for (Car c : resultSet) {
            count++;
            if (count>= 10) {
                break;
            }
        }
        return count;
    }
}
