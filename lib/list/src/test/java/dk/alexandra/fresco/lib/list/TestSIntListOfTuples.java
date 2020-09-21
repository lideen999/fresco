package dk.alexandra.fresco.lib.list;

import dk.alexandra.fresco.framework.DRes;
import dk.alexandra.fresco.framework.value.SInt;
import dk.alexandra.fresco.suite.dummy.arithmetic.AbstractDummyArithmeticTest;
import dk.alexandra.fresco.suite.dummy.arithmetic.AbstractDummyArithmeticTest.TestParameters;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

public class TestSIntListOfTuples extends AbstractDummyArithmeticTest {

  @Test (expected=RuntimeException.class)
  public void testLengthCheck() {
    SIntListofTuples list = new SIntListofTuples(2);
    list.add(new ArrayList<DRes<SInt>>(), null);
    Assert.fail("Should not be reachable.");
  }

  @Test
  public void test_findDuplicatesOne() {
    runTest(new EliminateDuplicatesTests.TestFindDuplicatesOne<>(),
        new TestParameters().numParties(2));
  }

}
