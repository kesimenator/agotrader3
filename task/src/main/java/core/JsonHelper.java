package core;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

import static org.testng.Assert.fail;

public class JsonHelper {

    static final Logger LOGGER = LoggerFactory.getLogger(JsonHelper.class);

    public static JsonType whatJsonIs = JsonType.WRONG;

    public enum JsonType {
        ARRAY, OBJECT, WRONG
    }

    public static LinkedList<JSONObject> getCollectionOfFeeds(ExtractableResponse<Response> response) {
        String appearToBeJsonString =  response.body().asPrettyString();
        JSONObject singleResponse = null;
        JSONArray jsonArray = null;
        LinkedList<JSONObject> jsonObjects = new LinkedList<>();

        try {
            singleResponse = new JSONObject(appearToBeJsonString);
            jsonObjects.add(singleResponse);
//            System.out.println(singleResponse.get("symbol"));
        } catch (JSONException ex) {
            try {
                jsonArray = new JSONArray(appearToBeJsonString);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject explrObject = jsonArray.getJSONObject(i); // TODO: wydobywa mapę!
                    jsonObjects.add(explrObject);

//                    System.out.println(explrObject.get("symbol"));
                }
            } catch (JSONException x) {
                fail("response string is not parsable!");
            }
        }

        return jsonObjects;
    }

}
