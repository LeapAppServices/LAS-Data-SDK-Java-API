package as.leap.las.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User：poplar
 * Date：15-6-4
 */
public class MsgSerializeTest {

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final TypeFactory typeFactory = TypeFactory.defaultInstance();

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

	@Test
	public void test(){
		String response = "{\"objectId\":\"556d25e22d342904801141de\",\"createdAt\":\"2015-06-02T03:41:22.911Z\",\"name\":\"jack\"}";
		SaveMsg saveMsg = asObject(response, SaveMsg.class);
		System.out.println(saveMsg.toString());
		Assert.assertEquals(response, asJson(saveMsg));

		String response2 = "{\"number\":1,\"updatedAt\":\"2015-06-02T03:41:22.999Z\",\"name\":\"jack\"}";
		UpdateMsg updateMsg = asObject(response2, UpdateMsg.class);
		System.out.println(updateMsg);
		Assert.assertEquals(response2, asJson(updateMsg));

		String response3 = "{\"number\":1}";
		DeleteMsg deleteMsg = asObject(response3, DeleteMsg.class);
		System.out.println(deleteMsg);
		Assert.assertEquals(response3, asJson(deleteMsg));

		String response4 = "{\"results\":[{\"createdAt\":\"2015-06-02T03:41:22.911Z\",\"name\":\"鸣人_50\",\"ACL\":{\"556d25e22d342904801141de\":{\"read\":true,\"write\":true}},\"objectId\":\"556d25e22d342904801141de\",\"updatedAt\":\"2015-06-02T03:41:22.923Z\"}]}";
		FindMsg findMsg = asObject(response4, FindMsg.class);
		System.out.println(findMsg);
		Assert.assertEquals(response4, asJson(findMsg));

		String response5 = "{\"objectId\":\"556d25e22d342904801141de\",\"createdAt\":\"2015-06-02T03:41:22.911Z\",\"updatedAt\":\"2015-06-02T03:41:22.923Z\",\"ACL\":{\"556d25e22d342904801141de\":{\"read\":true,\"write\":true}}}";
		LASObject lasObject = asObject(response5, LASObject.class);
		System.out.println(lasObject);
		Assert.assertEquals(response5, asJson(lasObject));

		LASQuery lasQuery = LASQuery.instance();
		lasQuery.equalTo("name","jack");
		lasQuery.addKey("aa");
		lasQuery.addKey("bb");
		lasQuery.setIncludes("cc");
		lasQuery.sort(-1,"name","age");
		Map<String, Object> map = new HashMap<>();
		if (lasQuery.query() != null) map.put("where", asJson(lasQuery.query()));
		if (lasQuery.sort() != null) map.put("order", lasQuery.sort());
		if (lasQuery.keys() != null) map.put("keys", lasQuery.keys());
		if (lasQuery.includes() != null) map.put("include", lasQuery.includes());
		map.put("limit", lasQuery.limit());
		map.put("skip", lasQuery.skip());
//    map.put("excludeKeys", null); Unsupported.
		System.out.println(asJson(map));
	}
}
