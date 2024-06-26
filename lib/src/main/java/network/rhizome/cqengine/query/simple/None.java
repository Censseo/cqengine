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
package network.rhizome.cqengine.query.simple;

import network.rhizome.cqengine.attribute.Attribute;
import network.rhizome.cqengine.attribute.SelfAttribute;
import network.rhizome.cqengine.attribute.SimpleAttribute;
import network.rhizome.cqengine.query.option.QueryOptions;

import static network.rhizome.cqengine.query.support.QueryValidation.checkObjectTypeNotNull;

/**
 * A query which matches no objects in the collection.
 * <p/>
 * This is equivalent to a literal boolean 'false'.
 *
 * @author ngallagher
 */
public class None<O> extends SimpleQuery<O, O> {

    final Class<O> objectType;

    public None(Class<O> objectType) {
        super(new SelfAttribute<O>(checkObjectTypeNotNull(objectType), "none"));
        this.objectType = objectType;
    }

    @Override
    protected boolean matchesSimpleAttribute(SimpleAttribute<O, O> attribute, O object, QueryOptions queryOptions) {
        return false;
    }

    @Override
    protected boolean matchesNonSimpleAttribute(Attribute<O, O> attribute, O object, QueryOptions queryOptions) {
        return false;
    }

    @Override
    protected int calcHashCode() {
        return 1357656699; // chosen randomly
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof None)) return false;
        None that = (None) o;
        return this.objectType.equals(that.objectType);
    }

    @Override
    public String toString() {
        return "none(" + super.getAttribute().getObjectType().getSimpleName() + ".class)";
    }
}