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

  public static LASUpdate getUpdate() {
    return new LASUpdate();
  }
  public static LASUpdate getUpdate(Map map) {
    return new LASUpdate(map);
  }

  public LASUpdate set(String key, Object value) {
    addMultiFieldOperation(key, value);
    return this;
  }

  /**
   * Update using the {@literal $set} update modifier
   * Just update a given field in sub documents
   * @param key The key
   * @param value The value
   * @return this
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

  public LASUpdate unsetMany(List<String> keys) {
    for (String key : keys) {
      unset(key);
    }
    return this;
  }

  public LASUpdate removeRelation(String relationKey, LASPointer... pointers) {
    return removeRelation(relationKey, Arrays.asList(pointers));
  }

  public LASUpdate removeRelation(String relationKey, List<LASPointer> pointers) {
    Map relation = new HashMap();
    relation.put("__op", "RemoveRelation");
    relation.put("objects", pointers);
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

  public <T> LASUpdate arrayAdd(String arrayKey, T... items) {
    return arrayAdd(arrayKey, Arrays.asList(items));
  }

  public <T> LASUpdate arrayAdd(String arrayKey, List items) {
    Map addUnique = new HashMap();
    addUnique.put("__op", "Add");
    addUnique.put("objects", items);
    addMultiFieldOperation(arrayKey, addUnique);
    return this;
  }

  public <T> LASUpdate arrayAddUnique(String arrayKey, T... items) {
    return arrayAddUnique(arrayKey, Arrays.asList(items));
  }

  public <T> LASUpdate arrayAddUnique(String arrayKey, List items) {
    Map addUnique = new HashMap();
    addUnique.put("__op", "AddUnique");
    addUnique.put("objects", items);
    addMultiFieldOperation(arrayKey, addUnique);
    return this;
  }

  public <T> LASUpdate arrayRemove(String arrayKey, T... items) {
    return arrayRemove(arrayKey, Arrays.asList(items));
  }

  public <T> LASUpdate arrayRemove(String arrayKey, List items) {
    Map addUnique = new HashMap();
    addUnique.put("__op", "Remove");
    addUnique.put("objects", items);
    addMultiFieldOperation(arrayKey, addUnique);
    return this;
  }

  public LASUpdate inc(String key, Number inc) {
    Map $inc = new HashMap();
    $inc.put("__op", "Increment");
    $inc.put("amount", inc);
    addMultiFieldOperation(key, $inc);
    return this;
  }

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