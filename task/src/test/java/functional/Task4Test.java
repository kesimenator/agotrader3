package functional;

import core.Bucket;
import core.RestServiceBase;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static core.GlobalConstants.ENDPOINT_TRADE_BUCKETED;
import static core.Helper.getMostLiquidInstruments;

public class Task4Test extends TestHooks {

    static final Logger LOGGER = LoggerFactory.getLogger(Task1Test.class);
    private static final int TOP_LIQUID_INSTRUMENT_COUNT = 10;
    private static ExtractableResponse<Response> response;
    private static RestServiceBase restResponseObject;
    private static final int RECORDS_FOR_PAGE_LIMIT = 1000;
    private static final int PAGES_COUNT = 10;
    private static Map<String, String> paramsMap;
    static {
        paramsMap = new HashMap<>();
        paramsMap.put("binSize", "1m");
        paramsMap.put("partial", "false");
        paramsMap.put("count", "1000");
        paramsMap.put("start", "1");
        paramsMap.put("reverse", "true");
        paramsMap.put("symbol", "XBTUSD");
    }


//    private Object[][] dataProvider() {
//        LinkedList<String> symbolsAppearsInFeeds = new LinkedList<>();
//        return getMostLiquidInstruments(symbolsAppearsInFeeds);
//        //        return new Object[][]{
////
////        };
//    }


    @BeforeMethod
    public void Init(Method method) {
//        paramsMap = new HashMap<>() {{
//            put("binSize", "1m");
//            put("partial", "false");
//            put("count", "1000");
//            put("start", "1");
//            put("reverse", "true");
//            put("symbol", "XBTUSD");
//        }};
        restResponseObject = new RestServiceBase();
    }



    /**
     * Prints to the console 2-weeks summary containing (per period):
     * • Sum of volume
     * • Average highest price
     * • Minimal price - assuming that we take the "close" price as a valid one for our hitory review
     */
    @Test(groups = "functional")
    public void preconditionsEndpointIsAvailable() {
        AtomicInteger counter = new AtomicInteger();
        ZonedDateTime twoWeeksPast = ZonedDateTime.now().minusWeeks(2);

        _setTestStep("Get 10 the most liquid instruments");
        response = restResponseObject.getResponseWithTimeoutInSeconds(ENDPOINT_TRADE_BUCKETED, paramsMap);
        HashMap<String, Integer> topTenInstruments = getMostLiquidInstruments(response, TOP_LIQUID_INSTRUMENT_COUNT);

        _setTestStep("Print 2-weeks summary for a symbol:");
        for (int i=0; i<PAGES_COUNT; i++) {
//            LinkedList<Bucket> deserializedData =
                    restResponseObject.getExtractableResponseBodyWithParams(ENDPOINT_TRADE_BUCKETED, paramsMap)
                            .as(new TypeRef<LinkedList<Bucket>>() {
                            }).forEach(
                                    (bucketPortion) -> {
                                        if (bucketPortion.getTimestamp().isAfter(twoWeeksPast) ) {
                                            LOGGER.info("Object index nr: {}, start: {}, bucket: {}", counter.incrementAndGet(), paramsMap.get("start"), bucketPortion);
                                        }
                                    });

            paramsMap.replace("start", String.valueOf(Integer.parseInt(paramsMap.get("start")) + RECORDS_FOR_PAGE_LIMIT));
        }
    }

}
