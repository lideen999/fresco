package dk.alexandra.fresco.suite.marlin.resource;

import dk.alexandra.fresco.commitment.HashBasedCommitment;
import dk.alexandra.fresco.commitment.HashBasedCommitmentSerializer;
import dk.alexandra.fresco.framework.builder.numeric.NumericResourcePool;
import dk.alexandra.fresco.framework.network.Network;
import dk.alexandra.fresco.framework.network.serializers.ByteSerializer;
import dk.alexandra.fresco.framework.util.Drbg;
import dk.alexandra.fresco.suite.marlin.datatypes.CompUInt;
import dk.alexandra.fresco.suite.marlin.datatypes.CompUIntFactory;
import dk.alexandra.fresco.suite.marlin.resource.storage.MarlinDataSupplier;
import dk.alexandra.fresco.suite.marlin.resource.storage.MarlinOpenedValueStore;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Common resources for {@link dk.alexandra.fresco.suite.marlin.MarlinProtocolSuite}.
 *
 * @param <PlainT> the type of the core arithmetic data type, in this case an instance of {@link
 * CompUInt}.
 */
public interface MarlinResourcePool<PlainT extends CompUInt<?, ?, PlainT>>
    extends NumericResourcePool {

  /**
   * Returns instance of {@link MarlinOpenedValueStore} which tracks all opened, unchecked values.
   */
  MarlinOpenedValueStore<PlainT> getOpenedValueStore();

  /**
   * Returns instance of {@link MarlinDataSupplier} which provides pre-processed material such as
   * multiplication triples.
   */
  MarlinDataSupplier<PlainT> getDataSupplier();

  /**
   * Returns factory for constructing concrete instances of {@link PlainT}, i.e., the class
   * representing the raw element data type.
   */
  CompUIntFactory<PlainT> getFactory();

  /**
   * Returns serializer for instances of {@link PlainT}.
   */
  ByteSerializer<PlainT> getRawSerializer();

  /**
   * Initializes deterministic joint randomness source. <p>Must be called before any protocols
   * relying on joint randomness are used. Requires a network since a coin tossing protocol is
   * executed to establish a joint random seed. It is guaranteed that the supplied network will be
   * closed upon completion of this method.</p>
   *
   * @param networkSupplier supplier for network to be used in coin-tossing
   * @param drbgGenerator creates drbg given the seed generated via coin-tossing
   * @param seedLength expected length for drbg seed
   */
  void initializeJointRandomness(Supplier<Network> networkSupplier,
      Function<byte[], Drbg> drbgGenerator, int seedLength);

  /**
   * The DRBG is useful for protocols which needs a form of shared randomness where the random bytes
   * are not easily guessed by an adversary. This generator will provide exactly that. For explicit
   * security guarantees, we refer to implementations of {@link dk.alexandra.fresco.framework.util.Drbg}.
   *
   * @return An instance of a DRBG.
   */
  Drbg getRandomGenerator();

  /**
   * Returns bit length of maximum representable element.
   */
  int getMaxBitLength();

  /**
   * Converts opened value of underlying arithmetic type to a BigInteger. <p>This may convert the
   * value to a negative value depending on the semantics of the plain text type.</p>
   */
  default BigInteger convertRepresentation(PlainT value) {
    return value.getLeastSignificant().toBigInteger();
  }

  /**
   * Returns serializer for {@link HashBasedCommitment}.
   */
  default ByteSerializer<HashBasedCommitment> getCommitmentSerializer() {
    return new HashBasedCommitmentSerializer();
  }

  @Override
  default BigInteger convertRepresentation(BigInteger value) {
    return convertRepresentation(getFactory().createFromBigInteger(value));
  }

}
