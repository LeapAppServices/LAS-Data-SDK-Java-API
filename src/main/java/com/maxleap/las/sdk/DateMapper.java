package com.maxleap.las.sdk;

/**
 *
 * @author sneaky
 * @since 3.0.0
 */
public class DateMapper {

//  public static void mapperToDate(LASObject zCloudObject) {
//    if (zCloudObject == null) return;
//    Set<String> keySet = zCloudObject.getMap().keySet();
//    for (String key : keySet) {
//
//      if (key.equals("createdAt")) {
//        Object createdAt = zCloudObject.get("createdAt");
//        if (createdAt != null) {
//          zCloudObject.put("createdAt", DateUtils.encodeDate(new Date(Long.valueOf(createdAt.toString()))));
//        }
//      }
//
//      if (key.equals("updatedAt")) {
//        Object updatedAt = zCloudObject.get("updatedAt");
//        if (updatedAt != null) {
//          zCloudObject.put("updatedAt", DateUtils.encodeDate(new Date(Long.valueOf(updatedAt.toString()))));
//        }
//      }
//
//      Object val = zCloudObject.get(key);
//      if (val != null) {
//        if (val instanceof LASObject) {
//          mapperToDate((LASObject) val);
//        } else if (val instanceof List) {
//          mapperToDate((List) val);
//        }
//      }
//    }
//
//  }
//
//  public static void mapperToDate(List list) {
//    ArrayList arrayList = new ArrayList(list);
//    for (int i = 0; i < arrayList.size(); i++) {
//      Object val = list.get(i);
//      if (val instanceof LASObject) {
//        mapperToDate((LASObject) val);
//      } else if (val instanceof List) {
//        mapperToDate((List) val);
//      }
//    }
//  }

}
