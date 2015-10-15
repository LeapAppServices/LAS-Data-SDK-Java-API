package com.maxleap.las.sdk;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sneaky
 * @since 2.0
 */
public class UpdateMsg implements Serializable {

  /**
   * The affected rows
   */
  private int number;
  private long updatedAt;
  private Map result = new HashMap();

  public UpdateMsg() {

  }

  public UpdateMsg(int number, long updatedAt, Map result) {
    this.number = number;
    this.updatedAt = updatedAt;
    this.result = result;
  }

  /**
   * @return The actual number of records to Updated
   */
  @JsonProperty
  public int number() {
    return number;
  }

  /**
   * @return Updated time.
   */
  public long updatedAt() {
    return updatedAt;
  }

  @JsonProperty("updatedAt")
  public String updatedAtString() {
    return DateUtils.encodeDate(new Date(this.updatedAt));
  }

  @JsonProperty("updatedAt")
  public void setUpdatedAt(String updatedAt) {
    if (updatedAt != null)  this.updatedAt = DateUtils.parseDate(updatedAt).getTime();
  }

  /**
   * @return result of Incs
   * @see MLUpdate#inc(String, Number)
   */
  @JsonAnyGetter
  public Map result() {
    return result;
  }

  @JsonAnySetter
  public void set(String name,Object value) {
    result.put(name,value);
  }

  @Override
  public String toString() {
    return "UpdateMsg{" +
      "number=" + number +
      ", updatedAt=" + updatedAt +
      ", result=" + result +
      '}';
  }
}
