package com.maxleap.las.sdk.types;

import java.util.HashMap;
import java.util.Map;

/**
 * User: qinpeng
 * Date: 14-5-19
 * Time: 9:21
 */
public class MLGeoPoint {

  private final String __type = MLKeyType.GEOPOINT.v();
  private double latitude = 0.0D;
  private double longitude = 0.0D;

  private static double EARTH_MEAN_RADIUS_KM = 6371.0D;
  private static double EARTH_MEAN_RADIUS_MILE = 3958.8000000000002D;

  public MLGeoPoint(double latitude, double longitude) {
    setLatitude(latitude);
    setLongitude(longitude);
  }

  public void setLatitude(double latitude) {
    if ((latitude > 90.0D) || (latitude < -90.0D)) {
      throw new IllegalArgumentException(
          "Latitude must be within the range (-90.0, 90.0).");
    }
    this.latitude = latitude;
  }

  public void setLongitude(double longitude) {
    if ((longitude > 180.0D) || (longitude < -180.0D)) {
      throw new IllegalArgumentException(
          "Longitude must be within the range (-180.0, 180.0).");
    }
    this.longitude = longitude;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public double getLongitude() {
    return this.longitude;
  }

  public double distanceInRadiansTo(MLGeoPoint point) {
    double d2r = 0.0174532925199433D;
    double lat1rad = this.latitude * d2r;
    double long1rad = this.longitude * d2r;
    double lat2rad = point.getLatitude() * d2r;
    double long2rad = point.getLongitude() * d2r;
    double deltaLat = lat1rad - lat2rad;
    double deltaLong = long1rad - long2rad;
    double sinDeltaLatDiv2 = Math.sin(deltaLat / 2.0D);
    double sinDeltaLongDiv2 = Math.sin(deltaLong / 2.0D);

    double a = sinDeltaLatDiv2 * sinDeltaLatDiv2 + Math.cos(lat1rad)
        * Math.cos(lat2rad) * sinDeltaLongDiv2 * sinDeltaLongDiv2;

    a = Math.min(1.0D, a);
    return 2.0D * Math.asin(Math.sqrt(a));
  }

  public double distanceInKilometersTo(MLGeoPoint point) {
    return distanceInRadiansTo(point) * EARTH_MEAN_RADIUS_KM;
  }

  public double distanceInMilesTo(MLGeoPoint point) {
    return distanceInRadiansTo(point) * EARTH_MEAN_RADIUS_MILE;
  }

  public String get__type() {
    return __type;
  }

  public Map toMap() {
    Map map = new HashMap();
    map.put("__type", "GeoPoint");
    map.put("latitude", latitude);
    map.put("longitude", longitude);
    return map;
  }
}
