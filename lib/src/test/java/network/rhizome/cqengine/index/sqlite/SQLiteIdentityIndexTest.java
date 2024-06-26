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
package network.rhizome.cqengine.index.sqlite;

import network.rhizome.cqengine.index.sqlite.TemporaryDatabase.TemporaryInMemoryDatabase;
import network.rhizome.cqengine.testutil.Car;
import network.rhizome.cqengine.testutil.CarFactory;

import network.rhizome.cqengine.attribute.SimpleAttribute;
import network.rhizome.cqengine.index.sqlite.SQLiteIdentityIndex;

import static network.rhizome.cqengine.query.QueryFactory.*;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class SQLiteIdentityIndexTest {

    @Rule
    public TemporaryInMemoryDatabase temporaryDatabase = new TemporaryInMemoryDatabase();

    @Test
    public void testSerialization() {
        SQLiteIdentityIndex<Integer, Car> index = new SQLiteIdentityIndex<Integer, Car>(
                Car.CAR_ID
        );

        SimpleAttribute<Car, byte[]> serializingAttribute = index.new SerializingAttribute(Car.class, byte[].class);
        SimpleAttribute<byte[], Car> deserializingAttribute = index.new DeserializingAttribute(byte[].class, Car.class);

        Car c1 = CarFactory.createCar(1);
        byte[] s1 = serializingAttribute.getValue(c1, noQueryOptions());
        Car c2 = deserializingAttribute.getValue(s1, noQueryOptions());
        byte[] s2 = serializingAttribute.getValue(c2, noQueryOptions());
        Assert.assertEquals(c1, c2);
        Assert.assertArrayEquals(s1, s2);
    }

}