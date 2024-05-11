package network.rhizome.cqengine.persistence.support.serialization;

import org.junit.Test;

import network.rhizome.cqengine.persistence.support.serialization.FurySerializer;
import network.rhizome.cqengine.persistence.support.serialization.PersistenceConfig;
import network.rhizome.cqengine.persistence.support.serialization.PojoSerializer;

import java.lang.annotation.Annotation;

/**
 * Unit tests for {@link FurySerializer}.
 *
 * @author npgall
 */
public class FurySerializerTest {

    @Test
    public void testPositiveSerializability() {
        FurySerializer.validateObjectIsRoundTripSerializable(new KryoSerializablePojo(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeSerializability() {
        FurySerializer.validateObjectIsRoundTripSerializable(new NonKryoSerializablePojo(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateObjectEquality() {
        FurySerializer.validateObjectEquality(1, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateHashCodeEquality() {
        FurySerializer.validateHashCodeEquality(1, 2);
    }

    @Test
    public void testPolymorphicSerialization_WithPolymorphicConfig() {
        FurySerializer.validateObjectIsRoundTripSerializable(1, Number.class);
    }

    @SuppressWarnings("unused")
    static class KryoSerializablePojo {
        int i;
        KryoSerializablePojo() {
        }
        KryoSerializablePojo(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof KryoSerializablePojo)) return false;

            KryoSerializablePojo that = (KryoSerializablePojo) o;

            return i == that.i;
        }

        @Override
        public int hashCode() {
            return i;
        }
    }

    @SuppressWarnings("unused")
    static class NonKryoSerializablePojo {
        int i;
        NonKryoSerializablePojo() {
            throw new IllegalStateException("Intentional exception");
        }
        NonKryoSerializablePojo(int i) {
            this.i = i;
        }
    }

    PersistenceConfig POLYMORPHIC_CONFIG = new PersistenceConfig() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return PersistenceConfig.class;
        }

        @Override
        public Class<? extends PojoSerializer> serializer() {
            return FurySerializer.class;
        }

        @Override
        public boolean polymorphic() {
            return true;
        }
    };
}