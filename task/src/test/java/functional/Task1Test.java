package functional;

import core.RestServiceBase;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static core.GlobalConstants.ENDPOINT_TRADE_BUCKETED;
import static core.GlobalConstants.REST_SUCCESS_CODE;
import static core.JsonHelper.getCollectionOfFeeds;
import static org.testng.Assert.*;


public class Task1Test extends TestHooks {

    static final Logger LOGGER = LoggerFactory.getLogger(Task1Test.class);
    private static RestServiceBase restResponseObject;
    private static ExtractableResponse<Response> response;

    public static final Map<String, String> paramsMap = new HashMap<>() {{
        put("binSize", "1m");
        put("partial", "false");
        put("count", "1000");
        put("reverse", "true");
    }};


    /**
     * Scenario: Try to reach out the data for a given endpoint
     */
    @Test(groups = "functional", testName = "PRECONDITION - run first")
    public void preconditionsEndpointIsAvailable() {
        restResponseObject = new RestServiceBase();
        response = restResponseObject.getResponseWithTimeoutInSeconds(ENDPOINT_TRADE_BUCKETED, paramsMap);
        assertNotNull(response);
    }


    /**
     * Scenario: Check status code - expected success (200)
     */
    @Test(groups = "functional", dependsOnMethods = "preconditionsEndpointIsAvailable")
    public void endpointStatusCodeShouldResponseOK() {
        LOGGER.info("Status Code for GET: {}", response.statusCode());
        assertEquals(response.statusCode(), REST_SUCCESS_CODE);
    }


    /**
     * Scenario: Check response body. Expected not to be empty.
     */
    @Test(groups = "functional", dependsOnMethods = "preconditionsEndpointIsAvailable")
    public void responseBodyByParamsShouldNotBeEmpty() {
        LOGGER.info("Response body for GET: {}", response.body().asPrettyString());
        assertFalse(response.body().asPrettyString().isEmpty(), "response body should not be empty!");
    }


    /**
     * Scenario: Check if the response data is parsable to json format.
     *  Expected: parsable at least to a JSONArray (ideally to a JSONObject)
     */
    @Test(groups = "functional", dependsOnMethods = "preconditionsEndpointIsAvailable")
    public void jsonShouldBeParsableOK() {
        String appearToBeJsonString =  response.body().asPrettyString();
        try {
            new JSONObject(appearToBeJsonString);
            LOGGER.info("looks like a json string is parsable as JSONObject");
        } catch (JSONException ex) {
            try {
                new JSONArray(appearToBeJsonString);
                LOGGER.info("looks like a json string is parsable as JSONArray");
            } catch (JSONException x) {
                fail("response string is not parsable!");
            }
        }
    }


    /**
     * Scenario: according to the model:
     * [
     *   {
     *     "timestamp": "string",
     *     "symbol": "string",
     *     "open": number,
     *     "high": number,
     *     "low": number,
     *     ...
     *   }
     * ]
     *    Expected fields validated according to their types.
     */
    @Test(groups = "functional", dependsOnMethods = "preconditionsEndpointIsAvailable", priority = 2)
    public void typesShouldBeAdequatedToFields() {

        LinkedList<JSONObject> listOfFeeds = getCollectionOfFeeds(response);
        for (JSONObject feedRecord : listOfFeeds ) {
            LOGGER.debug(feedRecord.toString());

            // looking for String
            assertTrue(feedRecord.get("timestamp") instanceof String);
            assertTrue(feedRecord.get("symbol") instanceof String);

            // and for non-Strings(double, BigDecimal, etc)
            assertFalse(feedRecord.get("open") instanceof String);
            assertFalse(feedRecord.get("high") instanceof String);
            assertFalse(feedRecord.get("low") instanceof String);
            assertFalse(feedRecord.get("close") instanceof String);
            assertFalse(feedRecord.get("trades") instanceof String);
            assertFalse(feedRecord.get("volume") instanceof String);
            assertFalse(feedRecord.get("vwap") instanceof String);
            assertFalse(feedRecord.get("lastSize") instanceof String);
            assertFalse(feedRecord.get("turnover") instanceof String);
            assertFalse(feedRecord.get("homeNotional") instanceof String);
            assertFalse(feedRecord.get("foreignNotional") instanceof String);
        }

    }


}
