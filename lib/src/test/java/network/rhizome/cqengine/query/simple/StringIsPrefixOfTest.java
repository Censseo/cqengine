package network.rhizome.cqengine.query.simple;

import static network.rhizome.cqengine.query.QueryFactory.isPrefixOf;
import static network.rhizome.cqengine.query.QueryFactory.noQueryOptions;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import network.rhizome.cqengine.attribute.Attribute;
import network.rhizome.cqengine.attribute.SelfAttribute;
import network.rhizome.cqengine.query.simple.StringIsPrefixOf;


public class StringIsPrefixOfTest {

    Attribute<String, String> stringIdentity = new SelfAttribute<String>(String.class, "identity");
    
    @Test
    public void testMatchesSimpleAttribute() throws Exception {
        
        assertTrue(isPrefixOf(stringIdentity, "FOO").matches("F", noQueryOptions()));
        assertTrue(isPrefixOf(stringIdentity, "FOO").matches("FO", noQueryOptions()));
        assertTrue(isPrefixOf(stringIdentity, "FOO").matches("FOO", noQueryOptions()));
        
        assertFalse(isPrefixOf(stringIdentity, "FOO").matches("OO", noQueryOptions()));
        assertFalse(isPrefixOf(stringIdentity, "FOO").matches("BOO", noQueryOptions()));
        assertFalse(isPrefixOf(stringIdentity, "FOO").matches("FOOOD", noQueryOptions()));
        
    }

    
    @Test
    public void testGetValue() throws Exception {
        StringIsPrefixOf<String, String> query = new StringIsPrefixOf<>(stringIdentity, "FOO");
        assertEquals("FOO", query.getValue());
    }

}
