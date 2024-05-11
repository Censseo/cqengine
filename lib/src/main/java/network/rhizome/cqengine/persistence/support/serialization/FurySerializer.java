package network.rhizome.cqengine.persistence.support.serialization;
import org.apache.fury.*;
import org.apache.fury.config.*;

/**
 * Uses <a href="https://github.com/EsotericSoftware/kryo">Kryo</a> to serialize and deserialize objects;
 * for use with CQEngine's disk and off-heap indexes and persistence.
 * <p/>
 * A {@link #validateObjectIsRoundTripSerializable(Object)} method is also provided, to validate
 * the compatibility of end-user POJOs with this serializer.
 *
 * @author npgall
 */
public class FurySerializer<O> implements PojoSerializer<O> {

    protected final Class<O> objectType;
    protected final ThreadLocal<Fury> furyCache;

    /**
     * Creates a new Fury serializer which is configured to serialize objects of the given type.
     *
     * @param objectType The type of the object
     *
     */
    public FurySerializer(Class<O> objectType) {
        this.objectType = objectType;
        this.furyCache = ThreadLocal.withInitial(() -> createFury(objectType));
    }

    /**
     * Creates a new instance of Fury serializer, for use with the given object type.
     * <p/>
     * Note: this method is public to allow end-users to validate compatibility of their POJOs,
     * with the Fury serializer as used by CQEngine.
     *
     * @param objectType The type of object which the instance of Kryo will serialize
     * @return a new instance of Fury serializer
     */
    @SuppressWarnings({"ArraysAsListWithZeroOrOneArgument", "WeakerAccess"})
    protected Fury createFury(Class<?> objectType) {
        var fury = Fury.builder()
                        .withLanguage(Language.JAVA)        
                        .requireClassRegistration(false)
                        .build();

        fury.register(objectType);
        return fury;
    }

    /**
     * Serializes the given object, using the given instance of Fury serializer.
     *
     * @param object The object to serialize
     * @return The serialized form of the object as a byte array
     */
    @Override
    public byte[] serialize(O object) {
        if (object == null) {
            throw new NullPointerException("Object was null");
        }

        return furyCache.get().serialize(object);
    }

    /**
     * Deserializes the given bytes, into an object of the given type, using the given instance of Kryo serializer.
     *
     * @param bytes The serialized form of the object as a byte array
     * @return The deserialized object
     */
    @Override
    @SuppressWarnings("unchecked")
    public O deserialize(byte[] bytes) {
        try {
            return (O) furyCache.get().deserialize(bytes);
        }
        catch (ClassCastException e) {
            throw new IllegalStateException("Failed to deserialize object, object type: " + objectType + ". " +
                    "Use the FurySerializer.validateObjectIsRoundTripSerializable() method " +
                    "to test your object is compatible with CQEngine.", e);
        }
    }

    /**
     * Performs sanity tests on the given POJO object, to check if it can be serialized and deserialized with Fury
     * serialzier as used by CQEngine.
     * <p/>
     * If a POJO fails this test, then it typically means CQEngine will be unable to serialize or deserialize
     * it, and thus the POJO can't be used with CQEngine's off-heap or disk indexes or persistence.
     * <p/>
     * Failing the test typically means the data structures or data types within the POJO are too complex. Simplifying
     * the POJO will usually improve compatibility.
     * <p/>
     * This method will return normally if the POJO passes the tests, or will throw an exception if it fails.
     *
     * @param candidatePojo The POJO to test
     */
    @SuppressWarnings("unchecked")
    public static <O> void validateObjectIsRoundTripSerializable(O candidatePojo) {
        Class<O> objectType = (Class<O>) candidatePojo.getClass();
        FurySerializer.validateObjectIsRoundTripSerializable(candidatePojo, objectType);
    }

    static <O> void validateObjectIsRoundTripSerializable(O candidatePojo, Class<O> objectType) {
        try {
            var serializer = new FurySerializer<O>(objectType);
            var serialized = serializer.serialize(candidatePojo);
            O deserializedPojo = serializer.deserialize(serialized);
            serializer.furyCache.remove();  // clear cached Kryo instance
            validateObjectEquality(candidatePojo, deserializedPojo);
            validateHashCodeEquality(candidatePojo, deserializedPojo);
        }
        catch (Exception e) {
            throw new IllegalStateException("POJO object failed round trip serialization-deserialization test, object type: " + objectType + ", object: " + candidatePojo, e);
        }
    }

    static void validateObjectEquality(Object candidate, Object deserializedPojo) {
        if (!(deserializedPojo.equals(candidate))) {
            throw new IllegalStateException("The POJO after round trip serialization is not equal to the original POJO");
        }
    }

    static void validateHashCodeEquality(Object candidate, Object deserializedPojo) {
        if ((deserializedPojo.hashCode() != candidate.hashCode())) {
            throw new IllegalStateException("The POJO's hashCode after round trip serialization differs from its original hashCode");
        }
    }
}
