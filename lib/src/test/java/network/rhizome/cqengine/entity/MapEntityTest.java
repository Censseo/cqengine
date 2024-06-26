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
package network.rhizome.cqengine.entity;

import network.rhizome.cqengine.testutil.Car;
import org.junit.Test;

import static network.rhizome.cqengine.query.QueryFactory.mapEntity;

import java.util.List;
import java.util.Map;

/**
 * Validates general functionality using MapEntity as collection element - indexes, query engine, ordering results.
 *
 * @author Niall Gallagher
 */
public class MapEntityTest extends RegularMapTest {

    @Test
    public void testMapFunctionality() {
        super.testMapFunctionality();
    }

    protected Map buildNewCar(int carId, String manufacturer, String model, Car.Color color, int doors, double price, List<String> features) {
        return mapEntity(createMap(carId, manufacturer, model, color, doors, price, features));
    }


}
