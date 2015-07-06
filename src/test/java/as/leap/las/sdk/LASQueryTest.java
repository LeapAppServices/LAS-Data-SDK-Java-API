package as.leap.las.sdk;

import as.leap.las.sdk.types.LASGeoPoint;
import as.leap.las.sdk.types.LASPointer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;

/**
 * @author sneaky
 * @since 3.0.0
 */
public class LASQueryTest {

  private static final ObjectMapper mapper = new ObjectMapper();
  private static final ObjectMapper objectIgnoreNullMapper;
  private static final TypeFactory typeFactory = TypeFactory.defaultInstance();

  static {
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    SimpleModule module = new SimpleModule();
    module.addSerializer(ObjectId.class, new ObjectIdSerializer());
    module.addDeserializer(ObjectId.class, new ObjectIdDeserialier());
    mapper.registerModule(module);
    objectIgnoreNullMapper = new ObjectMapper();
    objectIgnoreNullMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  public static <T> T asObject(String source, Class<T> clazz) {
    try {
      return mapper.readValue(source, typeFactory.uncheckedSimpleType(clazz));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static <T> String asJson(T obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }


  public static void main(String[] args) {
    String className = "Test0011";
    ObjectId objectId = new ObjectId();

    LASQuery query = LASQuery.instance();
    LASPointer pointer = new LASPointer(objectId, className);
    query.relatedTo("relation", pointer);
    String queryString = asJson(query.query());
    String s = "{\"$relatedTo\":{\"object\":{\"__type\":\"Pointer\",\"className\":\"" + className + "\",\"objectId\":\"" + objectId + "\"},\"key\":\"relation\"}}";
    System.out.println(s);
    System.out.println(queryString);

    query.addKey("zhao");

    System.out.println(query.keys());
    String[] arrays = new String[] {"zhaojing", "zhoajing"};
    query.in("key", arrays);

    System.out.println(asJson(query.query()));

    LASQuery query1 = new LASQuery();
    LASGeoPoint geoPoint = new LASGeoPoint(31.11339, 121.10013);
    LASQuery query2 = query1.nearSpherePoint("geoPoint", geoPoint, 500);
    System.out.println(asJson(query2.query()));
    String nearSphere = "\"geoPoint\": {\"$nearSphere\":{\"__type\": \"GeoPoint\", \"latitude\": 31.11339, \"longitude\": 121.10013},\"$maxDistance\": 500}";
    System.out.println(nearSphere);

    LASQuery query3 = new LASQuery();
    LASQuery.SelectOperator selectOperator = new LASQuery.SelectOperator(className, "city");
    selectOperator.$gt("winPct", 0.5);
    query3.notSelect("hometown", selectOperator);
    System.out.println(asJson(query3.query()));
    String select = "\"hometown\":{\"$dontSelect\":{\"query\":{\"className\":\"" + className + "\",\"where\":{\"winPct\":{\"$gt\":0.5}}},\"key\":\"city\"}}";
    System.out.println(select);

    LASQuery query4 = new LASQuery();
    LASQuery.InQueryOperator queryOperator = new LASQuery.InQueryOperator(className);
    queryOperator.$exists("image", false);
    query4.inQuery("pointer", queryOperator);

    System.out.println(asJson(query4.query()));
    String inQuery = "\"pointer\":{\"$inQuery\":{\"where\":{\"image\":{\"$exists\": false}},\"className\":\"" + className + "\"}}}";
    System.out.println(inQuery);

    LASQuery query5 = LASQuery.instance();
    query5.sort(LASQuery.SORT_ASC, "createdAt", "updatedAt");
    query5.sort(LASQuery.SORT_DESC, "test");
    System.out.println(query5.sort());

    System.out.println(0.0083 * (3 / 256F));
  }
}
