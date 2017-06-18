package dk.alexandra.fresco.framework;

import dk.alexandra.fresco.framework.value.SInt;

public class LegacyTransfer implements ProtocolProducer {

  private final Computation<SInt> computation;
  private final SInt result;

  public LegacyTransfer(Computation<SInt> computation, SInt result) {
    this.computation = computation;
    this.result = result;
  }

  @Override
  public void getNextProtocols(ProtocolCollection protocolCollection) {
    result.setSerializableContent(computation.out().getSerializableContent());
  }

  @Override
  public boolean hasNextProtocols() {
    return false;
  }
}
