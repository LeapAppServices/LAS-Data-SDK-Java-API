package as.leap.las.sdk;

import as.leap.las.sdk.types.LASPointer;
import as.leap.las.sdk.types.TypesUtils;

import java.util.*;

/**
 * object update for LAS
 *
 * @author sneaky
 * @since 3.0.0
 */
public class LASUpdate {

  public enum Position {
    LAST, FIRST
  }
  private Map<String, Object> modifierOps = new LinkedHashMap<String, Object>();
  public LASUpdate() {

  }

  public LASUpdate(Map map) {
    this.modifierOps = TypesUtils.toMap(map);
  }

  /**
   * Static factory method to create an  empty Update
   *
   * @return
   */
  public static LASUpdate getUpdate() {
    return new LASUpdate();
  }

  /**
   * Static factory method to create an  empty Update
   *
   * @return
   */
  public static LASUpdate getUpdate(Map map) {
    return new LASUpdate(map);
  }

  /**
   * Update using the {@literal $set} update modifier
   *
   * @param key
   * @param value
   * @return
   */
  public LASUpdate set(String key, Object value) {
    addMultiFieldOperation(key, value);
    return this;
  }

  /**
   * Update using the {@literal $set} update modifier
   * <p/>
   * Just update a given field in sub documents
   *
   * @see <b>http://docs.mongodb.org/manual/reference/operator/update/set/</b>
   */
  public LASUpdate setBright(String key, Object value) {
    if (value instanceof Map) {
      for (Object childKey : ((Map) value).keySet()) {
        setBright(key + "." + childKey, ((Map) value).get(childKey));
      }
    } else {
      addMultiFieldOperation(key, value);
    }
    return this;
  }

  public LASUpdate setMany(Map<String, Object> map) {
    if (map != null) {
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        set(entry.getKey(), entry.getValue());
      }
    }
    return this;
  }

  /**
   * Update using the {@literal $unset} update modifier
   *
   * @param key
   * @return
   */
  public LASUpdate unset(String key) {
    Map del = new HashMap();
    del.put("__op", "Delete");
    addMultiFieldOperation(key, del);
    return this;
  }

  public LASUpdate unsetMany(String... keys) {
    for (String key : keys) {
      unset(key);
    }
    return this;
  }

  public LASUpdate removeRelation(String relationKey, LASPointer... pointers) {
    Map relation = new HashMap();
    relation.put("__op", "RemoveRelation");
    List<LASPointer> lasPointers = Arrays.asList(pointers);
    relation.put("objects", lasPointers);
    addMultiFieldOperation(relationKey, relation);
    return this;
  }

  public LASUpdate addRelation(String relationKey, LASPointer... pointers) {
    return addRelation(relationKey, Arrays.asList(pointers));
  }

  public LASUpdate addRelation(String relationKey, List<LASPointer> pointers) {
    Map relation = new HashMap();
    relation.put("__op", "AddRelation");
    relation.put("objects", pointers);
    addMultiFieldOperation(relationKey, relation);
    return this;
  }

  /**
   * For array
   *
   * @param arrayKey
   * @param items
   * @param <T>
   * @return
   */
  public <T> LASUpdate arrayAdd(String arrayKey, T... items) {
    Map addUnique = new HashMap();
    addUnique.put("__op", "Add");
    addUnique.put("objects", Arrays.asList(items));
    addMultiFieldOperation(arrayKey, addUnique);
    return this;
  }

  /**
   * For array
   *
   * @param arrayKey
   * @param items
   * @param <T>
   * @return
   */
  public <T> LASUpdate arrayAddUnique(String arrayKey, T... items) {
    Map addUnique = new HashMap();
    addUnique.put("__op", "AddUnique");
    addUnique.put("objects", Arrays.asList(items));
    addMultiFieldOperation(arrayKey, addUnique);
    return this;
  }

  /**
   * For array
   *
   * @param arrayKey
   * @param items
   * @param <T>
   * @return
   */
  public <T> LASUpdate arrayRemove(String arrayKey, T... items) {
    Map addUnique = new HashMap();
    addUnique.put("__op", "Remove");
    addUnique.put("objects", Arrays.asList(items));
    addMultiFieldOperation(arrayKey, addUnique);
    return this;
  }


  /**
   * Update using the {@literal $inc} update modifier
   *
   * @param key
   * @param inc
   * @return
   */
  public LASUpdate inc(String key, Number inc) {
    Map $inc = new HashMap();
    $inc.put("__op", "Increment");
    $inc.put("amount", inc);
    addMultiFieldOperation(key, $inc);
    return this;
  }

  /**
   * Update using the {@literal $addToSet} update modifier
   *
   * @param key
   * @param value
   * @return
   * @see <b>http://docs.mongodb.org/manual/reference/operator/update/addToSet/</b>
   */
  public LASUpdate addToSet(String key, Object value) {
    addMultiFieldOperation(key, value);
    return this;
  }

  public Map update() {
    return modifierOps;
  }

  protected void addMultiFieldOperation(String key, Object value) {
    if (key == null || key.trim().equals("")) {
      throw new IllegalArgumentException("Key for update must not be null or blank.");
    }
    modifierOps.put(key, TypesUtils.toMap(value));
  }

}