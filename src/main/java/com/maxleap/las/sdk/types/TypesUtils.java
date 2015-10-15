package com.maxleap.las.sdk.types;

import com.maxleap.las.sdk.ObjectId;

import java.util.List;
import java.util.Map;

/**
 *
 * @author sneaky
 * @since 2.0.0
 */
public class TypesUtils {
  public static Object toMap(Object obj) {
    if (obj == null) {
      return null;
    }
    if ((obj instanceof String) || (obj instanceof Number) || (obj instanceof Boolean)) {
      return obj;
    } else if (obj instanceof ObjectId) {
      return ((ObjectId) obj).toHexString();
    } else if (obj instanceof MLBytes) {
      return ((MLBytes) obj).toMap();
    } else if (obj instanceof MLDate) {
      return ((MLDate) obj).toMap();
    } else if (obj instanceof MLFile) {
      return ((MLFile) obj).toMap();
    } else if (obj instanceof MLGeoPoint) {
      return ((MLGeoPoint) obj).toMap();
    } else if (obj instanceof MLPointer) {
      return ((MLPointer) obj).toMap();
    } else if (obj instanceof MLRelation) {
      return ((MLRelation) obj).toMap();
    } else if (obj instanceof Map) {
      toMap((Map) obj);
    } else if (obj instanceof List) {
      toMap((List) obj);
    } else {
      throw new IllegalArgumentException("Type mismatch");
    }
    return obj;
  }

  public static Map toMap(Map map) {
    for (Object key : map.keySet()) {
      Object obj = map.get(key);
      Object o = toMap(obj);
      if (o instanceof Map) {
        map.put(key, o);
      }
    }
    return map;
  }

  public static List toMap(List list) {
    int size = list.size();
    int j = 0;
    for (int i = 0; i < size; i++) {
      Object obj = list.get(j++);
      Object o = toMap(obj);
      if (o instanceof Map) {
        list.remove(obj);
        list.add(o);
        j--;
      }
    }
    return list;
  }
}
