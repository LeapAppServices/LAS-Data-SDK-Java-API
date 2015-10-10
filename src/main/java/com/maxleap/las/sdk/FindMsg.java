package com.maxleap.las.sdk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author sneaky
 * @since 2.0.0
 */
public class FindMsg<T> implements Serializable {
  /**
   * The total number of records query conditions
   */
  private int count = -1;
  private List<T> results;

  public FindMsg() {
  }

  public FindMsg(int count, List<T> results) {
    this.count = count;
    this.results = results;
  }

  /**
   * If not countable will return -1
   *
   * @return The counts.
   */
  @JsonProperty
  @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
  public long count() {
    return count;
  }

  @JsonProperty
  public List<T> results() {
    return results;
  }

  @Override
  public String toString() {
    return "FindMsg{" +
      "count=" + count +
      ", results=" + results +
      '}';
  }
}
