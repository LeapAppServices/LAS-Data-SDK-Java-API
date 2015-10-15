package com.maxleap.las.sdk.types;

import com.maxleap.las.sdk.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: qinpeng
 * Date: 14-5-19
 * Time: 10:07
 */
public class MLDate {

  private static final String __type = MLKeyType.DATE.v();
  private String iso;

  public MLDate() {
  }

  public MLDate(Date date) {
    this.iso = DateUtils.encodeDate(date);
  }

  public String get__type() {
    return __type;
  }

  public String getIso() {
    return iso;
  }

  public void setIso(String iso) {
    this.iso = iso;
  }

  public MLDate from(Date date) throws ParseException {
    MLDate zCloudDate = new MLDate();
    zCloudDate.setIso(DateUtils.encodeDate(date));
    return zCloudDate;
  }

  public Long getTime() {
    return DateUtils.parseDate(this.iso).getTime();
  }

  public Map toMap() {
    Map map = new HashMap();
    map.put("__type", "Date");
    map.put("iso", iso);
    return map;
  }

}
