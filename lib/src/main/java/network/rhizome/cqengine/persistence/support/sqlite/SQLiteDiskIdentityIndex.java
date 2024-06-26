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
package network.rhizome.cqengine.persistence.support.sqlite;

import network.rhizome.cqengine.attribute.SimpleAttribute;
import network.rhizome.cqengine.index.Index;
import network.rhizome.cqengine.index.sqlite.SQLiteIdentityIndex;
import network.rhizome.cqengine.index.support.indextype.DiskTypeIndex;

/**
 * A subclass of {@link SQLiteIdentityIndex} intended for use with disk persistence.
 * This subclass does not override any behaviour, and exists only so that CQEngine can distinguish between
 * disk-based and off-heap configurations of the superclass index.
 *
 * @author niall.gallagher
 */
public class SQLiteDiskIdentityIndex<A extends Comparable<A>, O> extends SQLiteIdentityIndex<A, O> implements DiskTypeIndex{

    public SQLiteDiskIdentityIndex(SimpleAttribute<O, A> primaryKeyAttribute) {
        super(primaryKeyAttribute);
    }

    @Override
    public Index<O> getEffectiveIndex() {
        return this;
    }

    /**
     * Creates a new {@link SQLiteDiskIdentityIndex} for the given primary key attribute.
     *
     * @param primaryKeyAttribute The {@link SimpleAttribute} representing a primary key on which the index will be built.
     * @param <A> The type of the attribute.
     * @param <O> The type of the object containing the attributes.
     * @return a new instance of {@link SQLiteDiskIdentityIndex}
     */
    public static <A extends Comparable<A>, O> SQLiteDiskIdentityIndex<A, O> onAttribute(final SimpleAttribute<O, A> primaryKeyAttribute) {
        return new SQLiteDiskIdentityIndex<A, O>(primaryKeyAttribute);
    }
}
