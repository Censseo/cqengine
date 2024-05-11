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
package network.rhizome.cqengine.examples.codegen;

import network.rhizome.cqengine.testutil.Car;
import network.rhizome.cqengine.testutil.CarFactory;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.attribute.Attribute;
import network.rhizome.cqengine.attribute.SimpleAttribute;
import network.rhizome.cqengine.codegen.AttributeBytecodeGenerator;
import network.rhizome.cqengine.resultset.ResultSet;

import static network.rhizome.cqengine.query.QueryFactory.equal;

import java.util.Map;

/**
 * Demonstrates how to auto-generate bytecode for CQEngine attributes which access fields in a given class, which
 * can then be used directly at runtime.
 *
 * @author Niall Gallagher
 */
public class GenerateAttributeByteCode {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        // Generate an attribute from bytecode to read the Car.model field...
        Map<String, ? extends Attribute<Car, ?>> attributes = AttributeBytecodeGenerator.createAttributes(Car.class);
        Attribute<Car, String> MODEL = (Attribute<Car, String>) attributes.get("model");

        // Create a collection of 10 Car objects (Ford Focus, Honda Civic etc.)...
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        cars.addAll(CarFactory.createCollectionOfCars(10));

        // Retrieve the cars whose Car.model field is "Civic" (i.e. the Honda Civic)...
        ResultSet<Car> results = cars.retrieve(equal(MODEL, "Civic"));
        for (Car car : results) {
            System.out.println(car);
        }
        // ..prints:
        // Car{carId=3, manufacturer='Honda', model='Civic', color=WHITE, doors=5, price=4000.0, features=[grade b]}
    }
}
