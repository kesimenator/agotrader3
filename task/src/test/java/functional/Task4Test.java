package functional;

import core.Bucket;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static core.GlobalConstants.ENDPOINT_TRADE_BUCKETED;
import static io.restassured.RestAssured.given;

public class Task4Test extends TestHooks {

    static final Logger LOGGER = LoggerFactory.getLogger(Task1Test.class);
    private static ExtractableResponse<Response> response;
    private static Integer startIdx;
    private static final int COUNT = 1000;

    private Object[][] dataProvider() {
        return new Object[0][];
    }






    @Test(groups = "functional", testName = "PRECONDITION - run first")
    public void preconditionsEndpointIsAvailable() {
        startIdx = 1;
        int counter = 0;
        ZonedDateTime today = ZonedDateTime.now();
        ZonedDateTime twoWeeksPast = ZonedDateTime.now().minusWeeks(2);

//        _setTestStep("Get json Data");
//        RestServiceBase restResponseObject = new RestServiceBase();
//        response = restResponseObject.getResponseBodyWithTimeoutInSeconds(ENDPOINT_TRADE_BUCKETED, paramsMap);


        for (int i=0; i<20; i++) {
            Map<String, String> paramsMap = new HashMap<>() {{
                put("binSize", "1m");
                put("partial", "false");
                put("count", "1000");
                put("start", startIdx.toString());
                put("reverse", "true");
                put("symbol", "XBTUSD");
            }};
            LinkedList<Bucket> deserializedData =
                    given()
                            .queryParams(paramsMap)
                            .when()
                            .get(ENDPOINT_TRADE_BUCKETED)
                            .then()
                            .extract()
                            .body()
                            .as(new TypeRef<LinkedList<Bucket>>() {
                            });

            //14 dni wstecz
            for (Bucket objectMarket : deserializedData) {
                if (objectMarket.getTimestamp().isAfter(twoWeeksPast) ) {
                    LOGGER.info("Object index nr: {}, {}", ++counter, objectMarket);
                }
            }
            startIdx += COUNT;
        }
    }

}
