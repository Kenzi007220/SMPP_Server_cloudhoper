package mock_smpp_server.model;

/**
 * Created by sasha on 01/02/17.
 */
public class ObjectDeliverSm {

    private String sourceAddress;
    private String destAddress;
    private String esmClass;
    private String registeredDelivered;
    private String shortMessage;

    // Getters,setters ect //

  public String getSourceAddress() {
    return sourceAddress;
  }

  public void setSourceAddress(String sourceAddress) {
    this.sourceAddress = sourceAddress;
  }

  public ObjectDeliverSm(String sourceAddress, String destAddress, String esmClass,
      String registeredDelivered, String shortMessage) {
    this.sourceAddress = sourceAddress;
    this.destAddress = destAddress;
    this.esmClass = esmClass;
    this.registeredDelivered = registeredDelivered;
    this.shortMessage = shortMessage;
  }

  public String getDestAddress() {
    return destAddress;
  }

  public void setDestAddress(String destAddress) {
    this.destAddress = destAddress;
  }

  public String getEsmClass() {
    return esmClass;
  }

  public void setEsmClass(String esmClass) {
    this.esmClass = esmClass;
  }

  public String getRegisteredDelivered() {
    return registeredDelivered;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ObjectDeliverSm{");
    sb.append("sourceAddress='").append(sourceAddress).append('\'');
    sb.append(", destAddress='").append(destAddress).append('\'');
    sb.append(", esmClass='").append(esmClass).append('\'');
    sb.append(", registeredDelivered='").append(registeredDelivered).append('\'');
    sb.append(", shortMessage='").append(shortMessage).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public void setRegisteredDelivered(String registeredDelivered) {
    this.registeredDelivered = registeredDelivered;
  }

  public String getShortMessage() {
    return shortMessage;
  }

  public void setShortMessage(String shortMessage) {
    this.shortMessage = shortMessage;
  }


}
