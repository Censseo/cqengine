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

import org.junit.Test;

import network.rhizome.cqengine.attribute.Attribute;
import network.rhizome.cqengine.attribute.SelfAttribute;

import static network.rhizome.cqengine.query.QueryFactory.noQueryOptions;
import static network.rhizome.cqengine.query.QueryFactory.startsWith;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Niall Gallagher
 */
public class StringStartsWithTest {

    @Test
    public void testStringStartsWith() {

        Attribute<String, String> stringIdentity = new SelfAttribute<String>(String.class, "identity");
        assertTrue(startsWith(stringIdentity, "THIS").matches("THIS IS A TEST", noQueryOptions()));
        assertFalse(startsWith(stringIdentity, "TEST").matches("THIS IS A TEST", noQueryOptions()));
        assertFalse(startsWith(stringIdentity, "HIS").matches("THIS IS A TEST", noQueryOptions()));
        assertTrue(startsWith(stringIdentity, "").matches("THIS IS A TEST", noQueryOptions()));
        assertTrue(startsWith(stringIdentity, "").matches("", noQueryOptions()));
        assertFalse(startsWith(stringIdentity, "TEST").matches("", noQueryOptions()));
    }
}
