package com.maxleap.las.sdk;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sneaky
 * @since 2.0.0
 */
public class SaveMsg implements Serializable {

  private ObjectId objectId;
  private long createdAt;

  private Map result = new HashMap();

  public SaveMsg() {
  }

  public SaveMsg(long createdAt, ObjectId objectId) {
    this.createdAt = createdAt;
    this.objectId = objectId;
  }

  /**
   * @return The created time.
   */
  public long createdAt() {
    return createdAt;
  }

  @JsonProperty("createdAt")
  public void setCreatedAt(String createdAt) {
    if (createdAt != null) this.createdAt = DateUtils.parseDate(createdAt).getTime();
  }

  @JsonProperty("createdAt")
  public String createdAtString() {
    return DateUtils.encodeDate(new Date(this.createdAt));
  }


  public ObjectId objectId() {
    return objectId;
  }

  @JsonProperty("objectId")
  public String objectIdString(){
    return objectId == null ? null : objectId.toString();
  }

  @JsonProperty("objectId")
  public void setObjectId(String objectId) {
    this.objectId = new ObjectId(objectId);
  }

  public void setObjectId(ObjectId objectId) {
    this.objectId = objectId;
  }

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
    return "SaveMsg{" +
      "objectId=" + objectId +
      ", createdAt=" + createdAt +
      ", result=" + result +
      '}';
  }
}
