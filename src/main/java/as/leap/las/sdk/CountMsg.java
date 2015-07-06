package as.leap.las.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * desc this class
 *
 * @author jing zhao
 * @date 9/1/14
 * @see
 * @since 2.0
 */
public class CountMsg implements Serializable {

  private int count;

  public CountMsg(int count) {
    this.count = count;
  }

  /**
   *
   * @return The counts.
   */
  @JsonProperty
  public long count() {
    return count;
  }

}
