package com.maxleap.las.sdk;

import com.maxleap.las.sdk.types.MLPointer;
import com.maxleap.las.sdk.types.TypesUtils;

import java.util.*;

/**
 * object update for ML
 *
 * @author sneaky
 * @since 3.0.0
 */
public class MLUpdate {

  public enum Position {
    MLT, FIRST
  }
  private Map<String, Object> modifierOps = new LinkedHashMap<String, Object>();
  public MLUpdate() {

  }

  public MLUpdate(Map map) {
    this.modifierOps = TypesUtils.toMap(map);
  }

  public static MLUpdate getUpdate() {
    return new MLUpdate();
  }
  public static MLUpdate getUpdate(Map map) {
    return new MLUpdate(map);
  }

  public MLUpdate set(String key, Object value) {
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
  public MLUpdate setBright(String key, Object value) {
    if (value instanceof Map) {
      for (Object childKey : ((Map) value).keySet()) {
        setBright(key + "." + childKey, ((Map) value).get(childKey));
      }
    } else {
      addMultiFieldOperation(key, value);
    }
    return this;
  }

  public MLUpdate setMany(Map<String, Object> map) {
    if (map != null) {
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        set(entry.getKey(), entry.getValue());
      }
    }
    return this;
  }

  public MLUpdate unset(String key) {
    Map del = new HashMap();
    del.put("__op", "Delete");
    addMultiFieldOperation(key, del);
    return this;
  }

  public MLUpdate unsetMany(String... keys) {
    for (String key : keys) {
      unset(key);
    }
    return this;
  }

  public MLUpdate unsetMany(List<String> keys) {
    for (String key : keys) {
      unset(key);
    }
    return this;
  }

  public MLUpdate removeRelation(String relationKey, MLPointer... pointers) {
    return removeRelation(relationKey, Arrays.asList(pointers));
  }

  public MLUpdate removeRelation(String relationKey, List<MLPointer> pointers) {
    Map relation = new HashMap();
    relation.put("__op", "RemoveRelation");
    relation.put("objects", pointers);
    addMultiFieldOperation(relationKey, relation);
    return this;
  }

  public MLUpdate addRelation(String relationKey, MLPointer... pointers) {
    return addRelation(relationKey, Arrays.asList(pointers));
  }

  public MLUpdate addRelation(String relationKey, List<MLPointer> pointers) {
    Map relation = new HashMap();
    relation.put("__op", "AddRelation");
    relation.put("objects", pointers);
    addMultiFieldOperation(relationKey, relation);
    return this;
  }

  public <T> MLUpdate arrayAdd(String arrayKey, T... items) {
    return arrayAdd(arrayKey, Arrays.asList(items));
  }

  public <T> MLUpdate arrayAdd(String arrayKey, List items) {
    Map addUnique = new HashMap();
    addUnique.put("__op", "Add");
    addUnique.put("objects", items);
    addMultiFieldOperation(arrayKey, addUnique);
    return this;
  }

  public <T> MLUpdate arrayAddUnique(String arrayKey, T... items) {
    return arrayAddUnique(arrayKey, Arrays.asList(items));
  }

  public <T> MLUpdate arrayAddUnique(String arrayKey, List items) {
    Map addUnique = new HashMap();
    addUnique.put("__op", "AddUnique");
    addUnique.put("objects", items);
    addMultiFieldOperation(arrayKey, addUnique);
    return this;
  }

  public <T> MLUpdate arrayRemove(String arrayKey, T... items) {
    return arrayRemove(arrayKey, Arrays.asList(items));
  }

  public <T> MLUpdate arrayRemove(String arrayKey, List items) {
    Map addUnique = new HashMap();
    addUnique.put("__op", "Remove");
    addUnique.put("objects", items);
    addMultiFieldOperation(arrayKey, addUnique);
    return this;
  }

  public MLUpdate inc(String key, Number inc) {
    Map $inc = new HashMap();
    $inc.put("__op", "Increment");
    $inc.put("amount", inc);
    addMultiFieldOperation(key, $inc);
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