package com.maxleap.las.sdk;

import com.maxleap.las.sdk.types.MLGeoPoint;
import com.maxleap.las.sdk.types.MLPointer;
import com.maxleap.las.sdk.types.TypesUtils;

import java.util.*;

/**
 * object query for Sun
 *
 * @author sneaky
 * @since 1.0
 */
public class MLQuery {

  /**
   * @see <b>http://docs.mongodb.org/manual/reference/operator/meta/hint/</b>
   */
  protected String hint;

  protected Map queryDBObject = new HashMap();

  /**
   * ascending order of direction
   */
  public static final int SORT_DESC = -1;

  /**
   * descending order of direction
   */
  public static final int SORT_ASC = 1;

  /**
   * @see <b>http://docs.mongodb.org/manual/reference/method/cursor.limit/#cursor.limit</b>
   */
  private Map<String, Integer> sort;

  private int skip = -1;

  private int limit = -1;

  private String includes;

  private List<String> keys;
  private boolean keysFlag;

  public MLQuery() {
  }

  public MLQuery(Map query) {
    this.queryDBObject = TypesUtils.toMap(query);
  }

  public MLQuery(Map query, List keys) {
    this.queryDBObject = TypesUtils.toMap(query);
    this.keys = keys;
  }

  public static MLQuery instance() {
    return new MLQuery();
  }

  public static MLQuery instance(Map query) {
    return new MLQuery(query);
  }

  public MLQuery not(String key, SingleElemMatcher elemMatcher) {
    addOperand(key, SunQueryType.NOT, elemMatcher.getElemMatherFiler());
    return this;
  }

  public <T> MLQuery equalTo(String key, T value) {
    addOperand(key, null, value);
    return this;
  }

  public <T> MLQuery notEqualTo(String key, T value) {
    addOperand(key, SunQueryType.NE, value);
    return this;
  }

  public MLQuery exists(String key) {
    addOperand(key, SunQueryType.EXIST, true);
    return this;
  }

  public MLQuery notExist(String key) {
    addOperand(key, SunQueryType.EXIST, false);
    return this;
  }

  public <T> MLQuery greaterThan(String key, T value) {
    addOperand(key, SunQueryType.GT, value);
    return this;
  }

  public <T> MLQuery greaterThanOrEqualTo(String key, T value) {
    addOperand(key, SunQueryType.GTE, value);
    return this;
  }

  public <T> MLQuery lessThan(String key, T value) {
    addOperand(key, SunQueryType.LT, value);
    return this;
  }

  public <T> MLQuery lessThanOrEqualTo(String key, T value) {
    addOperand(key, SunQueryType.LTE, value);
    return this;
  }

  public MLQuery matches(String key, String regex) {
    throw new UnsupportedOperationException("Unsupported. Please use regex(String key, String regex)");
  }

  public MLQuery regex(String key, String regex) {
    addOperand(key, SunQueryType.REGULAR, regex);
    return this;
  }

  public <T> MLQuery in(String key, T... value) {
    if (value.length == 0) {
      throw new IllegalArgumentException("value must be empty.");
    }
    addOperand(key, SunQueryType.IN, translator(value));
    return this;
  }

  public <T> MLQuery in(String key, List<T> values) {
    if (values != null && values.size() == 0) {
      throw new IllegalArgumentException("value must be empty.");
    }
    addOperand(key, SunQueryType.IN, translator(values));
    return this;
  }

  public <T> MLQuery notIn(String key, T... value) {
    if (value.length == 0) {
      throw new IllegalArgumentException("value must be empty.");
    }
    addOperand(key, SunQueryType.NIN, translator(value));
    return this;
  }

  public <T> MLQuery notIn(String key, List<T> values) {
    if (values != null && values.size() == 0) {
      throw new IllegalArgumentException("value must be empty.");
    }
    addOperand(key, SunQueryType.NIN, translator(values));
    return this;
  }

  /**
   * @deprecated Instead of arrayAll.remove soon
   *
   * @param key The key
   * @param values The values
   * @param <T> T
   * @return this
   */
  public <T> MLQuery all(String key, T... values) {
    arrayAll(key, values);
    return this;
  }

  /**
   * Selects the documents where the value of a field is an array that contains all the specified elements
   *
   * @param key The key
   * @param values The values
   * @param <T> T
   * @return this
   *
   */
  public <T> MLQuery arrayAll(String key, T... values) {
    if (values.length == 0) {
      throw new IllegalArgumentException("value must be empty.");
    }
    addOperand(key, SunQueryType.ALL, translator(values));
    return this;
  }

  public <T> MLQuery arrayAll(String key, List<T> values) {
    if (values != null && values.size() == 0) {
      throw new IllegalArgumentException("value must be empty.");
    }
    addOperand(key, SunQueryType.ALL, translator(values));
    return this;
  }

  /**
   * Equivalent of the $nearSphere operand
   *
   * @param key The key
   * @param geoPoint The GeoPoint
   * @param maxDistance max spherical distance
   * @return this
   */
  public MLQuery nearSpherePoint(String key, MLGeoPoint geoPoint, double maxDistance) {
    Map geoMap = new HashMap();
    geoMap.put("$nearSphere", geoPoint.toMap());
    geoMap.put("$maxDistance", maxDistance);
    addOperand(key, null, geoMap);
    return this;
  }

  /**
   * Equivalent to a $within operand, based on a bounding polygon represented by an array of points
   * @param key The key
   * @param points An array of Double[] defining the vertices of the search area
   * @return this
   */
  public MLQuery withinPolygon(String key, List<MLGeoPoint> points) {
    throw new UnsupportedOperationException("");
  }

  public <T> MLQuery inQuery(String key, InQueryOperator queryOperator) {
    addOperand(key, SunQueryType.INQUERY, queryOperator.toMap());
    return this;
  }

  public <T> MLQuery notInQuery(String key, InQueryOperator queryOperator) {
    addOperand(key, SunQueryType.NINQUERY, queryOperator);
    return this;
  }

  public <T> MLQuery select(String key, SelectOperator selectOperator) {
    addOperand(key, SunQueryType.SELECT, selectOperator.toMap());
    return this;
  }

  public <T> MLQuery notSelect(String key, SelectOperator selectOperator) {
    addOperand(key, SunQueryType.DONTSELECT, selectOperator.toMap());
    return this;
  }

  public MLQuery or(MLQuery... queries) {
    Map $or = new HashMap();
    List list = new ArrayList();
    list.add(this.query());
    for (MLQuery query : queries) {
      if (query == null) continue;
      list.add(query.query());
    }
    $or.put(SunQueryType.OR.getOperator(), list);
    this.queryDBObject = $or;

    return this;
  }

  public MLQuery and(MLQuery... queries) {
    Map $and = new HashMap();
    List list = new ArrayList();
    list.add(this.query());
    for (MLQuery query : queries) {
      if (query == null) continue;
      list.add(query.query());
    }
    $and.put(SunQueryType.AND.getOperator(), list);
    this.queryDBObject = $and;

    return this;
  }

  public MLQuery addKey(String key) {
    if (keys == null) {
      keys = new ArrayList();
      keysFlag = true;
    }
    if (!keysFlag) {
      throw new IllegalArgumentException("BadValue Projection cannot have a mix of inclusion and exclusion.");
    }
    this.keys.add(key);
    return this;
  }

  public MLQuery addKeys(List keys) {
    if (this.keys == null) {
      this.keys = new ArrayList();
      keysFlag = true;
    }
    this.keys.addAll(keys);
    if (!keysFlag) {
      throw new IllegalArgumentException("BadValue Projection cannot have a mix of inclusion and exclusion.");
    }
    return this;
  }


  public MLQuery excludeKey(String key) {
    throw new UnsupportedOperationException("UnsupportedOperation.");
  }

  public MLQuery excludeKeys(String[] keys) {
    throw new UnsupportedOperationException("UnsupportedOperation.");
  }

  public void setKeys(List keys) {
    this.keys = keys;
  }

  public String keys() {
    if (keys != null) {
      StringBuilder sb = new StringBuilder();
      int size = keys.size();
      for (int i = 0; i < size; i++) {
        if (size - 1 == i) {
          sb.append(keys.get(i));
        } else {
          sb.append(keys.get(i) +",");
        }
      }
      return sb.toString();
    }
    return null;
  }

  public MLQuery setSort(Map<String, Integer> sort) {
    if (this.sort == null) {
      this.sort = new LinkedHashMap();
    }

    for (String key : sort.keySet()) {
      this.sort.put(key, sort.get(key));
    }

    return this;
  }

  /**
   * @see MLQuery#SORT_ASC
   * @see MLQuery#SORT_DESC
   * @param direction direction of sort key
   * @param keys Sort keys
   * @return this
   */
  public MLQuery sort(int direction, String... keys) {
    if (sort == null) {
      sort = new LinkedHashMap();
    }
    for (String key : keys) {
      String trim = key.trim();
      sort.put(trim, direction);
    }
    return this;
  }

  public MLQuery relatedTo(String key, MLPointer pointer) {
    Map $relatedTo = new HashMap();
    $relatedTo.put("object", pointer.toMap());
    $relatedTo.put("key", key);
    equalTo("$relatedTo", $relatedTo);
    return this;
  }

  public String sort() {
    if (sort != null) {
      StringBuilder sb = new StringBuilder();
      int size = sort.size();
      int i = 0;
      for (Map.Entry<String, Integer> entry : sort.entrySet()) {
        i++;
        String key = entry.getKey();
        if (entry.getValue() == SORT_DESC) {
          sb.append("-");
        }
        sb.append(key);
        if( i < size) {
          sb.append(",");
        }
      }
      return sb.toString();
    }
    return null;
  }

  public int skip() {
    return skip;
  }

  public MLQuery setSkip(int skip) {
    this.skip = skip;
    return this;
  }

  public int limit() {
    return limit;
  }

  /**
   * eg : pointer.pointerParent,pointer2
   * @param includes The includes
   * @return this
   */
  public MLQuery setIncludes(String includes) {
    this.includes = includes;
    return this;
  }

  public String includes() {
    return includes;
  }

  public MLQuery setLimit(int limit) {
    this.limit = limit;
    return this;
  }

  public Map query() {
    return queryDBObject;
  }

  public MLQuery from(Map map) {
    queryDBObject.putAll(map);
    return this;
  }

  private <T> List translator(List<T> value){
    ArrayList ts = new ArrayList();
    for (T t : value) {
      ts.add(TypesUtils.toMap(t));
    }
    return ts;
  }

  private <T> List translator(T... value) {
    ArrayList ts = new ArrayList();
    for (T t : value) {
      ts.add(TypesUtils.toMap(t));
    }
    return ts;
  }

  protected void addOperand(String key, SunQueryType queryType, Object value) {
    value = TypesUtils.toMap(value);
    if (queryType == null) {
      queryDBObject.put(key, value);
      return;
    }
    Object storedValue = queryDBObject.get(key);
    Map operand;
    if (storedValue == null || !(storedValue instanceof Map)) {
      operand = new HashMap();
      queryDBObject.put(key, operand);
    } else {
      operand = (Map) queryDBObject.get(key);
    }
    operand.put(queryType.operator, value);
  }

  public enum SunQueryType {

    OR("$or", "or"),

    AND("$and", "and"),

    NE("$ne", "!="),

    LT("$lt", "<"),

    LTE("$lte", "<="),

    GT("$gt", ">"),

    GTE("$gte", ">="),

    IN("$in", "similar sql in"),

    NIN("$nin", "similar sql not in"),

    NOT("$not", ""),

    ALL("$all", ""),

    WITHIN("$geoWithin", "queries for a defined point, line or shape that exists entirely within another defined shape"),

    NEAR_SPHERE("$nearSphere", "Specifies a point for which a geospatial query returns the closest documents first"),

    MAX_DISTANCE("$maxDistance", " constrains the results of a geospatial $near or $nearSphere query to the specified distance"),

    REGULAR("$regex", "Matches documents that have the specified field in perl regular expression"),

    EXIST("$exists", "Matches documents that have the specified field"),

    SELECT("$select", "Matching the return value of another query"),

    DONTSELECT("$dontSelect", " Don't matching the return value of another query"),

    INQUERY("$inQuery", "In the return value of another query"),

    NINQUERY("$ninQuery", "Not in the return value of another query"),

    RELATETO("$relatedTo", "Relation query in zcloud");

    private String operator;
    private String desc;

    SunQueryType(String operator, String desc) {
      this.operator = operator;
      this.desc = desc;
    }

    public String getOperator() {
      return operator;
    }

    public String getDesc() {
      return desc;
    }

    public static SunQueryType from(String operator) {
      if (operator == null) return null;

      if (operator.equals(OR.operator)) {
        return OR;
      } else if (operator.equals(AND.operator)) {
        return AND;
      } else if (operator.equals(NE.operator)) {
        return NE;
      } else if (operator.equals(LT.operator)) {
        return LT;
      } else if (operator.equals(LTE.operator)) {
        return LTE;
      } else if (operator.equals(GT.operator)) {
        return GT;
      } else if (operator.equals(GTE.operator)) {
        return GTE;
      } else if (operator.equals(IN.operator)) {
        return IN;
      } else if (operator.equals(NIN.operator)) {
        return NIN;
      } else if (operator.equals(ALL.operator)) {
        return ALL;
      } else if (operator.equals(EXIST.operator)) {
        return EXIST;
      } else if (operator.equals(REGULAR.operator)) {
        return REGULAR;
      } else if (operator.equals(SELECT.operator)) {
        return SELECT;
      } else if (operator.equals(DONTSELECT.operator)) {
        return DONTSELECT;
      } else if (operator.equals(INQUERY.operator)) {
        return INQUERY;
      } else if (operator.equals(NINQUERY.operator)) {
        return NINQUERY;
      } else if (operator.equals(RELATETO.operator)) {
        return RELATETO;
      } else if (operator.equals(NEAR_SPHERE.operator)) {
        return NEAR_SPHERE;
      } else if ((operator.equals(WITHIN.operator))) {
        return WITHIN;
      }

      return null;
    }
  }

  public static class SelectOperator {
    String className;
    String key;
    ElemMatcher elemMatcher = new ElemMatcher();

    public SelectOperator(String className, String key) {
      this.className = className;
      this.key = key;
    }

    public SelectOperator $eq(String key, Object filter) {
      elemMatcher.$eq(key, filter);
      return this;
    }

    public SelectOperator $gt(String key, Object filter) {
      elemMatcher.$gt(key, filter);
      return this;
    }

    public SelectOperator $gte(String key, Object filter) {
      elemMatcher.$gte(key, filter);
      return this;
    }

    public SelectOperator $lt(String key, Object filter) {
      elemMatcher.$lt(key, filter);
      return this;
    }

    public SelectOperator $exists(String key, Object filter) {
      elemMatcher.$exists(key, filter);
      return this;
    }

    public SelectOperator $lte(String key, Object filter) {
      elemMatcher.$lte(key, filter);
      return this;
    }

    public SelectOperator $ne(String key, Object filter) {
      elemMatcher.$ne(key, filter);
      return this;
    }

    public SelectOperator $in(String key, Object... filters) {
      elemMatcher.$in(key, filters);
      return this;
    }

    public SelectOperator $nin(String key, Object... filters) {
      elemMatcher.$nin(key, filters);
      return this;
    }

    Map toMap() {
      Map map = new HashMap();
      Map query = new HashMap();
      query.put("className", className);
      query.put("where", elemMatcher.getElemMatherFiler());
      map.put("query", query);
      map.put("key", key);
      return map;
    }
  }

  public static class InQueryOperator {
    String className;
    ElemMatcher elemMatcher = new ElemMatcher();

    public InQueryOperator(String className) {
      this.className = className;
    }

    public InQueryOperator $eq(String key, Object filter) {
      elemMatcher.$eq(key, filter);
      return this;
    }

    public InQueryOperator $gt(String key, Object filter) {
      elemMatcher.$gt(key, filter);
      return this;
    }

    public InQueryOperator $gte(String key, Object filter) {
      elemMatcher.$gte(key, filter);
      return this;
    }

    public InQueryOperator $lt(String key, Object filter) {
      elemMatcher.$lt(key, filter);
      return this;
    }

    public InQueryOperator $exists(String key, Object filter) {
      elemMatcher.$exists(key, filter);
      return this;
    }

    public InQueryOperator $lte(String key, Object filter) {
      elemMatcher.$lte(key, filter);
      return this;
    }

    public InQueryOperator $ne(String key, Object filter) {
      elemMatcher.$ne(key, filter);
      return this;
    }

    public InQueryOperator $in(String key, Object... filters) {
      elemMatcher.$in(key, filters);
      return this;
    }

    public InQueryOperator $nin(String key, Object... filters) {
      elemMatcher.$nin(key, filters);
      return this;
    }

    Map toMap() {
      Map query = new HashMap();
      query.put("className", className);
      query.put("where", elemMatcher.getElemMatherFiler());
      return query;
    }
  }

  public static class ElemMatcher {
    Map elemMatherFiler = new HashMap();

    public static ElemMatcher instance() {
      return new ElemMatcher();
    }

    public ElemMatcher $eq(String key, Object filter) {
      elemMatherFiler.put(key, toMap("$eq", filter));
      return this;
    }

    public ElemMatcher $gt(String key, Object filter) {
      Map $eq = new HashMap();
      $eq.put(key, filter);
      elemMatherFiler.put(key, toMap("$gt", filter));
      return this;
    }

    public ElemMatcher $gte(String key, Object filter) {
      elemMatherFiler.put(key, toMap("$gte", filter));
      return this;
    }

    public ElemMatcher $lt(String key, Object filter) {
      elemMatherFiler.put(key, toMap("$lt", filter));
      return this;
    }

    public ElemMatcher $lte(String key, Object filter) {
      elemMatherFiler.put(key, toMap("$lte", filter));
      return this;
    }

    public ElemMatcher $exists(String key, Object filter) {
      elemMatherFiler.put(key, toMap("$exists", filter));
      return this;
    }

    public ElemMatcher $ne(String key, Object filter) {
      elemMatherFiler.put(key, toMap("$ne", filter));
      return this;
    }

    public ElemMatcher $in(String key, Object... filters) {
      elemMatherFiler.put(key, toMap("$in", filters));
      return this;
    }

    public ElemMatcher $nin(String key, Object... filters) {
      elemMatherFiler.put(key, toMap("$nin", filters));
      return this;
    }

    Map toMap(String op, Object filter) {
      Map opMap = new HashMap();
      opMap.put(op, filter);
      return opMap;
    }

    Map getElemMatherFiler() {
      return elemMatherFiler;
    }
  }

  public static class SingleElemMatcher {
    Map elemMatherFiler = new LinkedHashMap();

    public static SingleElemMatcher instance() {
      return new SingleElemMatcher();
    }

    public SingleElemMatcher $eq(Object filter) {
      elemMatherFiler.put("$eq", filter);
      return this;
    }

    public SingleElemMatcher $gt(Object filter) {
      elemMatherFiler.put("$gt", filter);
      return this;
    }

    public SingleElemMatcher $gte(Object filter) {
      elemMatherFiler.put("$gte", filter);
      return this;
    }

    public SingleElemMatcher $lt(Object filter) {
      elemMatherFiler.put("$lt", filter);
      return this;
    }

    public SingleElemMatcher $lte(Object filter) {
      elemMatherFiler.put("$lte", filter);
      return this;
    }

    public SingleElemMatcher $size(int size) {
      elemMatherFiler.put("$size", size);
      return this;
    }

    public SingleElemMatcher $ne(Object filter) {
      elemMatherFiler.put("$ne", filter);
      return this;
    }

    public SingleElemMatcher $in(Object... filters) {
      elemMatherFiler.put("$in", filters);
      return this;
    }

    public SingleElemMatcher $nin(Object... filters) {
      elemMatherFiler.put("$nin", filters);
      return this;
    }

    public Map getElemMatherFiler() {
      return elemMatherFiler;
    }
  }

}

