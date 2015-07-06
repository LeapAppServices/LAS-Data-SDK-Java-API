package as.leap.las.sdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sneaky
 * @since 2.0.0
 */
public class ObjectIdMapper {

    public static void mapperToMap(Map map) {
        for (Object key : map.keySet()) {
            Object val = map.get(key);
            if (val != null) {
                if (val instanceof Map) {
                    mapperToMap((Map) val);
                } else if (val instanceof List) {
                    mapperToMap((List) val);
                } else if (val instanceof ObjectId) {
                    Map $oid = new HashMap();
                    $oid.put("$oid", val.toString());
                    map.put(key, $oid);
                }
            }
        }

    }

    public static void mapperToMap(List list) {
        ArrayList arrayList = new ArrayList(list);
        for (int i = 0; i < arrayList.size(); i++) {
            Object val = list.get(i);
            if (val instanceof Map) {
                mapperToMap((Map) val);
            } else if (val instanceof List) {
                mapperToMap((List) val);
            } else if (val instanceof ObjectId) {
                Map $oid = new HashMap();
                $oid.put("$oid", val.toString());
                list.add($oid);
            }
        }
    }

    public static ObjectId mapperToObjectId(Map map) {
        if (map.size() == 1 && map.get("$oid") != null) {
            return new ObjectId(map.get("$oid").toString());
        }

        for (Object key : map.keySet()) {
            Object val = map.get(key);
            if (val != null) {
                if (val instanceof Map) {
                    Map valMap = (Map) val;
                    if (valMap.size() == 1) {
                        if (valMap.get("$oid") != null) {
                            map.put(key, new ObjectId(((Map) val).get("$oid").toString()));
                        } {
                            mapperToObjectId(valMap);
                        }
                    } else {
                        mapperToObjectId(valMap);
                    }
                } else if (val instanceof List) {
                    mapperToObjectId((List) val);
                }
            }
        }

        return null;
    }

    public static void mapperToObjectId(List list) {
        ArrayList arrayList = new ArrayList(list);
        for (int i = 0; i < arrayList.size(); i++) {
            Object val = list.get(i);
            if (val instanceof Map) {
                ObjectId objectId = mapperToObjectId((Map) val);
                if (objectId != null) {
                    list.add(objectId);
                    list.remove(val);
                }
            } else if (val instanceof List) {
                mapperToObjectId((List) val);
            }
        }
    }

}
