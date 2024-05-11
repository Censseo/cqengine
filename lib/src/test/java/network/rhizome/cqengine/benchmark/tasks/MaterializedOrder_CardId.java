package network.rhizome.cqengine.benchmark.tasks;

import network.rhizome.cqengine.benchmark.BenchmarkTask;
import network.rhizome.cqengine.testutil.Car;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.index.hash.HashIndex;
import network.rhizome.cqengine.query.Query;
import network.rhizome.cqengine.query.option.QueryOptions;
import network.rhizome.cqengine.resultset.ResultSet;

import static network.rhizome.cqengine.query.QueryFactory.ascending;
import static network.rhizome.cqengine.query.QueryFactory.equal;
import static network.rhizome.cqengine.query.QueryFactory.orderBy;
import static network.rhizome.cqengine.query.QueryFactory.queryOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class MaterializedOrder_CardId  implements BenchmarkTask {
    private Collection<Car> collection;
    private IndexedCollection<Car> indexedCollection;

    private final Comparator<Car> carIdComparator = Comparator.comparingInt(Car::getCarId);

    private final Query<Car> query = equal(Car.MODEL, "Focus");

    private final QueryOptions queryOptions = queryOptions(orderBy(ascending(Car.CAR_ID)));
    @Override
    public void init(final Collection<Car> collection) {
        this.collection = collection;
        IndexedCollection<Car> indexedCollection = new ConcurrentIndexedCollection<Car>();
        indexedCollection.addAll(collection);
        this.indexedCollection = indexedCollection;
        this.indexedCollection.addIndex(HashIndex.onAttribute(Car.MODEL));
    }

    /**
     * Uses iteration with insertion sort.
     */
    @Override
    public int runQueryCountResults_IterationNaive() {
        final TreeSet<Car> result = new TreeSet<>(carIdComparator);
        for (final Car car : collection) {
            if (car.getModel().equals("Focus")) {
                result.add(car);
            }
        }
        return result.size();
    }

    /**
     * Uses iteration with merge sort.
     */
    @Override
    public int runQueryCountResults_IterationOptimized() {
        final List<Car> result = new ArrayList<>();
        for (final Car car : collection) {
            if (car.getModel().equals("Focus")) {
                result.add(car);
            }
        }
        result.sort(carIdComparator);
        return result.size();
    }

    @Override
    public int runQueryCountResults_CQEngine() {
        final ResultSet<Car> sortedResult = indexedCollection.retrieve(query, queryOptions);
        return BenchmarkTaskUtil.countResultsViaIteration(sortedResult);
    }

    @Override
    public int runQueryCountResults_CQEngineStatistics() {
        final ResultSet<Car> result = indexedCollection.retrieve(query, queryOptions);
        return result.size();
    }
}
