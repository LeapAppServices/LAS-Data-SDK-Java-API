package com.maxleap.las.sdk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 *
 * @author sneaky
 * @since 3.0.0
 */
public class DateUtils {
  private static final DateFormat dateFormat;

  static {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    format.setTimeZone(new SimpleTimeZone(0, "GMT"));
    dateFormat = format;
  }

  public static String encodeDate(Date date) {
    return dateFormat.format(date);
  }

  public static Date parseDate(String dateString) {
    try {
      return dateFormat.parse(dateString);
    } catch (ParseException e) {
      return null;
    }
  }
}

