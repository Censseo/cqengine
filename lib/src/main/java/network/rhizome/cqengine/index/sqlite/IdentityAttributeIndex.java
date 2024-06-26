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

import network.rhizome.cqengine.attribute.SimpleAttribute;
import network.rhizome.cqengine.index.AttributeIndex;

/**
 * Implemented by indexes which persist serialized objects directly in the index instead of persisting foreign keys.
 *
 * @author niall.gallagher
 */
public interface IdentityAttributeIndex<A, O> extends AttributeIndex<A, O> {

    /**
     * Returns an attribute which given a primary key of a stored object can read (deserialize) the corresponding
     * object from the identity index. This is called a foreign key attribute, because typically those keys will
     * be stored in other indexes, referring to the primary keys of this index.
     */
    SimpleAttribute<A, O> getForeignKeyAttribute();
}
