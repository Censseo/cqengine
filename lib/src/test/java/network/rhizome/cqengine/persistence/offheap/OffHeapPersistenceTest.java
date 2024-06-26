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
package network.rhizome.cqengine.persistence.offheap;

import network.rhizome.cqengine.testutil.Car;
import network.rhizome.cqengine.testutil.CarFactory;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.index.Index;
import network.rhizome.cqengine.index.disk.DiskIndex;
import network.rhizome.cqengine.index.navigable.NavigableIndex;
import network.rhizome.cqengine.index.offheap.OffHeapIndex;
import network.rhizome.cqengine.persistence.offheap.OffHeapPersistence;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Assert;
import org.junit.Test;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

/**
 * @author niall.gallagher
 */
public class OffHeapPersistenceTest {

    @Test
    public void testGetBytesUsed() {
        OffHeapPersistence<Car, Integer> persistence = OffHeapPersistence.onPrimaryKey(Car.CAR_ID);
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(persistence);
        cars.addAll(CarFactory.createCollectionOfCars(50));
        long bytesUsed = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used should be greater than zero: " + bytesUsed, bytesUsed > 0);
    }

    @Test
    public void testCompact() {
        OffHeapPersistence<Car, Integer> persistence = OffHeapPersistence.onPrimaryKey(Car.CAR_ID);
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(persistence);
        cars.addAll(CarFactory.createCollectionOfCars(100));
        long bytesUsedWhenFullyPopulated = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used when fully populated should be greater than zero: " + bytesUsedWhenFullyPopulated, bytesUsedWhenFullyPopulated > 0);
        cars.removeAll(CarFactory.createCollectionOfCars(100));
        long bytesUsedWhenObjectsRemoved = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used when objects removed (" + bytesUsedWhenObjectsRemoved + ") should remain the same as when fully populated (" + bytesUsedWhenFullyPopulated + ")", bytesUsedWhenObjectsRemoved == bytesUsedWhenFullyPopulated);
        persistence.compact(); // Truncates size of the database, but not to zero as the tables which were created remain (although empty)
        long bytesUsedAfterCompaction = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used after compaction (" + bytesUsedAfterCompaction + ") should be less than when fully populated (" + bytesUsedWhenFullyPopulated + ")", bytesUsedAfterCompaction < bytesUsedWhenFullyPopulated);
    }

    @Test
    public void testExpand() {
        final long bytesToExpand = 102400;  // Expand by 100KB;
        OffHeapPersistence<Car, Integer> persistence = OffHeapPersistence.onPrimaryKey(Car.CAR_ID);
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>(persistence);
        cars.addAll(CarFactory.createCollectionOfCars(50));
        persistence.compact();
        long initialBytesUsed = persistence.getBytesUsed();
        Assert.assertTrue("Initial bytes used should be greater than zero: " + initialBytesUsed, initialBytesUsed > 0);
        persistence.expand(bytesToExpand);
        long bytesUsedAfterExpanding = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used after expanding (" + bytesUsedAfterExpanding + ") should have been increased by at least bytes to expand (" + bytesToExpand + ") above initial bytes used (" + initialBytesUsed + ")", bytesUsedAfterExpanding >= (initialBytesUsed + bytesToExpand));
        persistence.compact();
        long bytesUsedAfterCompaction = persistence.getBytesUsed();
        Assert.assertTrue("Bytes used after compaction (" + bytesUsedAfterCompaction + ") should be equal to initial bytes used (" + initialBytesUsed + ")", bytesUsedAfterCompaction == initialBytesUsed);
    }

    @Test
    public void testSupportsIndex() {
        OffHeapPersistence<Car, Integer> persistence = OffHeapPersistence.onPrimaryKey(Car.CAR_ID);

        Index<Car> offHeapIndex = OffHeapIndex.onAttribute(Car.MANUFACTURER);
        Index<Car> diskIndex = DiskIndex.onAttribute(Car.MANUFACTURER);
        Index<Car> navigableIndex = NavigableIndex.onAttribute(Car.MANUFACTURER);

        Assert.assertTrue(persistence.supportsIndex(offHeapIndex));
        Assert.assertFalse(persistence.supportsIndex(diskIndex));
        Assert.assertFalse(persistence.supportsIndex(navigableIndex));
    }

    @Test
    public void testEqualsAndHashCode() {
        SQLiteDataSource ds1 = new SQLiteDataSource(new SQLiteConfig());
        ds1.setUrl("foo");
        SQLiteDataSource ds2 = new SQLiteDataSource(new SQLiteConfig());
        ds2.setUrl("bar");
        EqualsVerifier.forClass(OffHeapPersistence.class)
                .withIgnoredFields("sqLiteDataSource", "persistentConnection", "closed", "readWriteLock")
                .suppress(Warning.NULL_FIELDS, Warning.STRICT_INHERITANCE)
                .withPrefabValues(SQLiteDataSource.class, ds1, ds2)
                .verify();
    }
}