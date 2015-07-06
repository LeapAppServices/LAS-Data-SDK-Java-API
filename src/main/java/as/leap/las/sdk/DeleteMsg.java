package as.leap.las.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author sneaky
 * @since 2.0.0
 */
public class DeleteMsg implements Serializable {

  /**
   * The affected rows
   */
  private int number;

  public DeleteMsg() {
  }

  public DeleteMsg(int number) {
    this.number = number;
  }

  /**
   *
   * @return The actual number of records to delete
   */
  @JsonProperty
  public int number() {
    return number;
  }

  @Override
  public String toString() {
    return "DeleteMsg{" +
      "number=" + number +
      '}';
  }
}
