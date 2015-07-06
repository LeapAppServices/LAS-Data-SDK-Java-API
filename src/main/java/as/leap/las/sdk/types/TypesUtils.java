package as.leap.las.sdk.types;

import as.leap.las.sdk.ObjectId;

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
    } else if (obj instanceof LASBytes) {
      return ((LASBytes) obj).toMap();
    } else if (obj instanceof LASDate) {
      return ((LASDate) obj).toMap();
    } else if (obj instanceof LASFile) {
      return ((LASFile) obj).toMap();
    } else if (obj instanceof LASGeoPoint) {
      return ((LASGeoPoint) obj).toMap();
    } else if (obj instanceof LASPointer) {
      return ((LASPointer) obj).toMap();
    } else if (obj instanceof LASRelation) {
      return ((LASRelation) obj).toMap();
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
