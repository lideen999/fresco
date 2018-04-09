package dk.alexandra.fresco.suite.spdz.datatypes;

import dk.alexandra.fresco.framework.DRes;
import dk.alexandra.fresco.framework.value.SInt;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

public class SpdzSInt implements SInt, Serializable {

  private static final long serialVersionUID = 8828769687281856043L;
  private final BigInteger share;
  private final BigInteger mac;
  private final BigInteger mod;

  /**
   * Create a SpdzSInt containing a share, mac and modulus.
   *
   * @param share The share
   * @param mac The mac
   * @param modulus The modulus
   */
  public SpdzSInt(BigInteger share, BigInteger mac, BigInteger modulus) {
    this.share = share;
    this.mac = mac;
    this.mod = modulus;
  }

  /**
   * Creates a {@link SpdzSInt} from a public value. <p>All parties compute the mac share of the
   * value but only party one (by convention) stores the public value as the share, the others store
   * 0.</p>
   */
  public SpdzSInt(BigInteger share, BigInteger macKeyShare, BigInteger modulus,
      boolean isPartyOne) {
    this(isPartyOne ? share : BigInteger.ZERO, share.multiply(macKeyShare).mod(modulus), modulus);
  }

  //Communication methods

  /**
   * Create a SpdzSInt containing a share, mac and modulus. This constructor handles serialized
   * data.
   *
   * @param data Array of bytes containing the share and mac. First half contains the share, second
   * contains the mac
   * @param modulus The modulus
   * @param modulusSize The size of the share and mac
   */
  public SpdzSInt(byte[] data, BigInteger modulus, int modulusSize) {
    byte[] shareBytes = new byte[modulusSize];
    byte[] macBytes = new byte[modulusSize];
    for (int i = 0; i < data.length / 2; i++) {
      shareBytes[i] = data[i];
      macBytes[i] = data[modulusSize + i];
    }
    this.share = new BigInteger(shareBytes);
    this.mac = new BigInteger(macBytes);
    mod = modulus;
  }

  //get operations
  public BigInteger getShare() {
    return share;
  }

  public BigInteger getMac() {
    return mac;
  }

  /**
   * Adds two SpdzSInt.
   *
   * @param e The element to add
   * @return The sum
   */
  public SpdzSInt add(SpdzSInt e) {
    BigInteger share = this.share.add(e.getShare()).mod(mod);
    BigInteger mac = this.mac.add(e.getMac()).mod(mod);
    return new SpdzSInt(share, mac, this.mod);
  }

  /**
   * Compute sum of this and constant (open) value. <p>All parties compute their mac share of the
   * public value and add it to the mac share of the authenticated value, however only party 1 adds
   * the public value to is value share.</p>
   *
   * @param other constant, open value
   * @param macKeyShare mac key share for maccing open value
   * @param isPartyOne used to ensure that only one party adds value to share
   * @return result of sum
   */
  public SpdzSInt addConstant(
      BigInteger other, BigInteger macKeyShare, BigInteger modulus, boolean isPartyOne) {
    SpdzSInt wrapped = new SpdzSInt(other, macKeyShare, modulus, isPartyOne);
    return add(wrapped);
  }

  /**
   * Add a public value to this element.
   *
   * @param e The element to add
   * @param id The id
   * @return The sum
   */
  public SpdzSInt add(SpdzSInt e, int id) {
    BigInteger share = this.share;
    BigInteger mac = this.mac;
    mac = mac.add(e.getMac()).mod(mod);
    if (id == 1) {
      share = share.add(e.getShare()).mod(mod);
    }
    return new SpdzSInt(share, mac, this.mod);
  }

  /**
   * Subtract a SpdzSInt from this element.
   *
   * @param e The element to subtract
   * @return The difference
   */
  public SpdzSInt subtract(SpdzSInt e) {
    BigInteger share = e.getShare();
    BigInteger diffShare = this.share.subtract(share).mod(mod);
    BigInteger mac = e.getMac();
    BigInteger diffMac = this.mac.subtract(mac).mod(mod);
    return new SpdzSInt(diffShare, diffMac, this.mod);
  }

  /**
   * Multiply this element with a constant.
   *
   * @param c The constant to multiply
   * @return The multiple
   */
  public SpdzSInt multiply(BigInteger c) {
    BigInteger share = this.share.multiply(c).mod(mod);
    BigInteger mac = this.mac.multiply(c).mod(mod);
    return new SpdzSInt(share, mac, this.mod);
  }

  //Utility methods

  @Override
  public String toString() {
    return "spdz(" + share + ", " + mac + ")";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((mac == null) ? 0 : mac.hashCode());
    result = prime * result + ((mod == null) ? 0 : mod.hashCode());
    result = prime * result + ((share == null) ? 0 : share.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    SpdzSInt other = (SpdzSInt) obj;
    if (mac == null) {
      if (other.mac != null) {
        return false;
      }
    } else if (!mac.equals(other.mac)) {
      return false;
    }
    if (mod == null) {
      if (other.mod != null) {
        return false;
      }
    } else if (!mod.equals(other.mod)) {
      return false;
    }
    if (share == null) {
      if (other.share != null) {
        return false;
      }
    } else if (!share.equals(other.share)) {
      return false;
    }
    return true;
  }

  @Override
  public SInt out() {
    return this;
  }

  /**
   * Get result from deferred and downcast result to {@link SpdzSInt}.
   */
  public static SpdzSInt toSpdzSInt(DRes<SInt> value) {
    return Objects.requireNonNull((SpdzSInt) value.out());
  }

}
