package dk.alexandra.fresco.suite.spdz2k.datatypes;

/**
 * Represents an authenticated, secret-share element.
 *
 * @param <PlainT> type of underlying plain value, i.e., the value type we use for arithmetic.
 */
public class Spdz2kSIntBoolean<PlainT extends CompUInt<?, ?, PlainT>> extends
    Spdz2kSInt<PlainT> {

  /**
   * Creates a {@link Spdz2kSIntBoolean}.
   */
  public Spdz2kSIntBoolean(PlainT share, PlainT macShare) {
    super(macShare, share);
  }

  /**
   * Creates a {@link Spdz2kSIntBoolean} from a public value. <p>All parties compute the mac
   * share of the value but only party one (by convention) stores the public value as the share, the
   * others store 0.</p>
   */
  public Spdz2kSIntBoolean(PlainT share, PlainT macKeyShare, PlainT zero, boolean isPartyOne) {
    this(isPartyOne ? share : zero, share.multiply(macKeyShare));
  }

  @Override
  public byte[] serializeShareLow() {
    return share.getLeastSignificant().toByteArray();
  }

  @Override
  public byte[] serializeShareWhole() {
    return share.toByteArray();
  }

  @Override
  public String toString() {
    return "Spdz2kSIntBoolean{" +
        "share=" + share +
        ", macShare=" + macShare +
        '}';
  }

}
