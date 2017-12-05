package dk.alexandra.fresco.tools.mascot.field;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dk.alexandra.fresco.tools.mascot.arithm.CollectionUtils;

public class FieldElementCollectionUtils {

  /**
   * Multiplies two lists of field elements, pair-wise.
   * 
   * @param leftFactors
   * @param rightFactors
   * @return
   */
  public static List<FieldElement> pairWiseMultiply(List<FieldElement> leftFactors,
      List<FieldElement> rightFactors) {
    if (leftFactors.size() != rightFactors.size()) {
      throw new IllegalArgumentException("Rows must be same size");
    }
    return IntStream.range(0, leftFactors.size())
        .mapToObj(idx -> {
          FieldElement l = leftFactors.get(idx);
          FieldElement r = rightFactors.get(idx);
          return l.multiply(r);
        })
        .collect(Collectors.toList());
  }
  
  /**
   * 
   * @param generator
   * @param elements
   * @return
   */
  public static FieldElement recombine(FieldElement generator, List<FieldElement> elements) {
    // TODO: optimize
    // TODO: use innerProduct
    BigInteger modulus = generator.modulus;
    int bitLength = generator.bitLength;
    FieldElement accumulator = new FieldElement(BigInteger.ZERO, modulus, bitLength);
    int power = 0;
    for (FieldElement element : elements) {
      // TODO: do we need/ want modular exponentiation?
      accumulator = accumulator.add(generator.pow(power)
          .multiply(element));
      power++;
    }
    return accumulator;
  }

  /**
   * 
   * @param elements
   * @param modulus
   * @param bitLength
   * @return
   */
  public static FieldElement recombine(List<FieldElement> elements, BigInteger modulus,
      int bitLength) {
    FieldElement generator = new FieldElement(BigInteger.valueOf(2), modulus, bitLength);
    return recombine(generator, elements);
  }

  /**
   * 
   * @param left
   * @param right
   * @return
   */
  public static FieldElement innerProduct(List<FieldElement> left, List<FieldElement> right) {
    // TODO: throw is unequal lengths
    return IntStream.range(0, left.size())
        .mapToObj(idx -> left.get(idx)
            .multiply(right.get(idx)))
        .reduce((l, r) -> l.add(r))
        .get();
  }
  
  /**
   * 
   * @param mat
   * @return
   */
  public static List<List<FieldElement>> transpose(List<List<FieldElement>> mat) {
    // TODO: switch to doing fast transpose
    return CollectionUtils.transpose(mat);
  }
  
  /**
   * 
   * @param elements
   * @param stretchBy
   * @return
   */
  public static List<FieldElement> stretch(List<FieldElement> elements, int stretchBy) {
    List<FieldElement> stretched = new ArrayList<>(elements.size() * stretchBy);
    for (FieldElement element : elements) {
      for (int c = 0; c < stretchBy; c++) {
        stretched.add(new FieldElement(element));
      }
    }
    return stretched;
  }
  
}
