package dk.alexandra.fresco.framework.builder.numeric;

import dk.alexandra.fresco.framework.sce.resources.ResourcePool;
import dk.alexandra.fresco.suite.ProtocolSuiteNumeric;
import java.math.BigInteger;

/**
 * Every resource pool must have a set of properties available, primarily
 * the modulus and a BigInteger serialization.
 * <p>
 * This is paired with the {@link ProtocolSuiteNumeric}.
 */
public interface NumericResourcePool extends ResourcePool {

  /**
   * Gets the modulus.
   *
   * @return modulus
   */
  default BigInteger getModulus() {
    return getFieldDefinition().getModulus();
  }

  /**
   * Gets the modulus halved.
   *
   * @return modulus halved
   */
  default BigInteger getModulusHalved() {
    return getFieldDefinition().getModulusHalved();
  }

  /**
   * Gets the field definition.
   *
   * @return field definition
   */
  FieldDefinition getFieldDefinition();

  /**
   * Takes a unsigned BigInteger and converts it (reasonable) to a signed version.
   *
   * @param value the unsigned BigInteger
   * @return the signed BigInteger
   */
  default BigInteger convertRepresentation(FieldElement value) {
    BigInteger modulus = getModulus();
    BigInteger actual = value.convertToBigInteger().mod(modulus);
    if (actual.compareTo(getModulusHalved()) > 0) {
      return actual.subtract(modulus);
    } else {
      return actual;
    }
  }
}
