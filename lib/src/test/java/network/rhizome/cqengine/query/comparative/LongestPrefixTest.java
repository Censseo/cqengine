package network.rhizome.cqengine.query.comparative;

import static network.rhizome.cqengine.query.QueryFactory.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import network.rhizome.cqengine.testutil.MobileTerminating;
import network.rhizome.cqengine.testutil.MobileTerminatingFactory;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import network.rhizome.cqengine.ConcurrentIndexedCollection;
import network.rhizome.cqengine.IndexedCollection;
import network.rhizome.cqengine.attribute.Attribute;
import network.rhizome.cqengine.attribute.SelfAttribute;
import network.rhizome.cqengine.index.radixinverted.InvertedRadixTreeIndex;
import network.rhizome.cqengine.query.Query;
import network.rhizome.cqengine.query.QueryFactory;
import network.rhizome.cqengine.query.comparative.LongestPrefix;
import network.rhizome.cqengine.resultset.ResultSet;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Unit tests for {@link LongestPrefix} query.
 * <p>
 * This class contains regular unit tests, AND parameterized tests for the longest prefix matching.
 * <p>
 * These tests are based on a use case of finding the longest matching mobile phone number, which is a more
 * typical use case for prefix matching than the car examples used in other tests.
 * <p>
 * These tests and the support for the {@link LongestPrefix} query, were contributed by Glen Lockhart of Openet-Labs.
 */
@RunWith(DataProviderRunner.class)
public class LongestPrefixTest {

    private static IndexedCollection<MobileTerminating> mobileTerminatingCache;
    private static IndexedCollection<MobileTerminating> mobileTerminatingCacheNoIndex;

    @DataProvider
    public static Object[][] mobileTerminatingScenarios() {
        return new Object[][] {
                {"35380",          "op1",       1},
                {"35380123",       "op1",       1},
                {"3538123",        "op2",       1},
                {"35382",          "op3",       1},
                {"353822",         "op4",       1},
                {"35387",          "op5",       1},
                {"3538712345",     "op6",       1},
                {"44123",          "op7",       1},
                {"4480",           "op8,op9",   2},
                {"33380",          "op10",      1},
                {"33381",          "op11",      1},
                {"1234",           "op12",      1},
                {"111",            "op13",      1},
                {"777",            "",          0},
                {"353",            "na",        1},
                {"354",            "",          0},
        };
    }

    @BeforeClass
    public static void setupMTCache() {
        mobileTerminatingCache = new ConcurrentIndexedCollection<MobileTerminating>();
        mobileTerminatingCache.addIndex(InvertedRadixTreeIndex.onAttribute(MobileTerminating.PREFIX));
        mobileTerminatingCache.addAll(MobileTerminatingFactory.getCollectionOfMobileTerminating());

        mobileTerminatingCacheNoIndex = new ConcurrentIndexedCollection<MobileTerminating>();
        mobileTerminatingCacheNoIndex.addAll(MobileTerminatingFactory.getCollectionOfMobileTerminating());
    }

    @Test
    @UseDataProvider(value = "mobileTerminatingScenarios")
    public void testLongestPrefix(String prefix, String expectedOperator, Integer expectedCount) {
        Query<MobileTerminating> q = longestPrefix(MobileTerminating.PREFIX, prefix);
        validateLongestPrefixWithCache(q, mobileTerminatingCache, expectedOperator, expectedCount);
    }

    @Test
    @UseDataProvider(value = "mobileTerminatingScenarios")
    public void testLongestPrefixWithoutIndex(String prefix, String expectedOperator, Integer expectedCount) {
        Query<MobileTerminating> q = longestPrefix(MobileTerminating.PREFIX, prefix);
        validateLongestPrefixWithCache(q, mobileTerminatingCacheNoIndex, expectedOperator, expectedCount);
    }


    public void validateLongestPrefixWithCache(Query<MobileTerminating> q, IndexedCollection<MobileTerminating> cache, String expectedOperator, Integer expectedCount) {
        ResultSet<MobileTerminating> res = cache.retrieve(q, queryOptions(orderBy(ascending(MobileTerminating.OPERATOR_NAME))));
        assertEquals(expectedCount, (Integer)res.size());
        Iterator<String> expectedOperators = Arrays.asList(expectedOperator.split(",")).iterator();
        for (MobileTerminating mt : res) {
            assertEquals(expectedOperators.next(), mt.getOperatorName());
        }
    }

    @Test
    public void testLongestPrefix() {
        Attribute<String, String> stringIdentity = new SelfAttribute<String>(String.class, "identity");
        assertTrue(LongestPrefix.countPrefixChars( "35387123456", "35387") > 0);
        assertEquals(5, LongestPrefix.countPrefixChars( "35387123456", "35387"));

        assertTrue(LongestPrefix.countPrefixChars("35387", "35387") > 0);
        assertEquals(5, LongestPrefix.countPrefixChars("35387", "35387"));

        assertFalse(LongestPrefix.countPrefixChars("35386123456", "35387") > 0);
        assertEquals(0, LongestPrefix.countPrefixChars("35386123456", "35387"));

        assertFalse(LongestPrefix.countPrefixChars("35386123456", "35387") > 0);
        assertEquals(0, LongestPrefix.countPrefixChars("35386123456", "35387"));

        assertFalse(LongestPrefix.countPrefixChars("3538", "35387") > 0);
        assertEquals(0, LongestPrefix.countPrefixChars("3538", "35387"));
    }

    @Test
    public void testConstructor_ArgumentValidation() {
        {
            IllegalArgumentException expectedIAE = null;
            try {
                new LongestPrefix<>(null, "");
            } catch (IllegalArgumentException e) {
                expectedIAE = e;
            }
            assertNotNull(expectedIAE);
        }
        {
            NullPointerException expectedNPE = null;
            try {
                new LongestPrefix<>(QueryFactory.selfAttribute(String.class), null);
            } catch (NullPointerException e) {
                expectedNPE = e;
            }
            assertNotNull(expectedNPE);
        }
    }

    @Test
    public void testGetAttributeType() {
        Class<String> attributeType = new LongestPrefix<>(selfAttribute(String.class), "").getAttributeType();
        assertEquals(String.class, attributeType);
    }
}
