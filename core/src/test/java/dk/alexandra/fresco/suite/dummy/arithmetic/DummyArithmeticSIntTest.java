package dk.alexandra.fresco.suite.dummy.arithmetic;

import dk.alexandra.fresco.framework.builder.numeric.FieldElementBigInteger;
import dk.alexandra.fresco.framework.builder.numeric.ModulusBigInteger;
import dk.alexandra.fresco.framework.util.ModulusFinder;
import java.math.BigInteger;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Test;

public class DummyArithmeticSIntTest {

  private ModulusBigInteger modulus = new ModulusBigInteger(ModulusFinder.findSuitableModulus(128));

  @Test
  public void testToString() {
    DummyArithmeticSInt value = new DummyArithmeticSInt(
        create(BigInteger.valueOf(42)));
    String toString = value.toString();
    Assert.assertThat(toString, StringContains.containsString("42"));
    Assert.assertThat(value.toString(), Is.is(toString));
  }

  private FieldElementBigInteger create(BigInteger value) {
    return new FieldElementBigInteger(value, new ModulusBigInteger(500));
  }

  @Test
  public void testEquals() {
    DummyArithmeticSInt value1 = new DummyArithmeticSInt(create(BigInteger.valueOf(42)));
    DummyArithmeticSInt value2 = new DummyArithmeticSInt(create(BigInteger.valueOf(42)));
    DummyArithmeticSInt value3 = new DummyArithmeticSInt(
        new FieldElementBigInteger(41, modulus));
    Assert.assertThat(value1, Is.is(value2));
    Assert.assertThat(value1, IsNot.not(value3));
    Assert.assertThat(value1, Is.is(value1));

    Assert.assertThat(value1, IsNot.not((DummyArithmeticSInt) null));
    Assert.assertThat(value1, IsNot.not("42"));
  }

  @Test
  public void testHashCode() {
    DummyArithmeticSInt value1 = new DummyArithmeticSInt(
        new FieldElementBigInteger(42, modulus));
    DummyArithmeticSInt value2 = new DummyArithmeticSInt(
        new FieldElementBigInteger(42, modulus));
    DummyArithmeticSInt value3 = new DummyArithmeticSInt(
        new FieldElementBigInteger(41, modulus));
    Assert.assertThat(value1.hashCode(), Is.is(value2.hashCode()));
    Assert.assertThat(value1.hashCode(), IsNot.not(value3.hashCode()));
    Assert.assertThat(value1.hashCode(), Is.is(value1.hashCode()));
  }
}