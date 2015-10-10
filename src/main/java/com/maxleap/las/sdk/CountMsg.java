package com.maxleap.las.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 *
 * @author sneaky
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
