package functional;

import core.Bucket;
import core.RestServiceBase;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static core.GlobalConstants.ENDPOINT_TRADE_BUCKETED;
import static core.Helper.getMostLiquidInstruments;
import static org.testng.Assert.assertFalse;

public class Task4Test extends TestHooks {

    static final Logger LOGGER = LoggerFactory.getLogger(Task4Test.class);
    private static final int TOP_LIQUID_INSTRUMENT_COUNT = 10;
    private static final int DAYS_TO_TEST_HISTORY = 14; //two weeks
    private static List<String> topOnMarket;
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
            paramsMap.put("symbol", "symbol-to-replace"); // TODO: 2022-05-10 XBTUSD
        }


    @DataProvider(name = "liquid-symbols")
    public Object[][] dp() {
        String[][] returnData = new String[TOP_LIQUID_INSTRUMENT_COUNT][1];
        for (int i=0; i<TOP_LIQUID_INSTRUMENT_COUNT; i++){
            returnData[i][0] = topOnMarket.get(i);
            LOGGER.debug(returnData[i][0]);
        }
        return returnData;
    }


    @BeforeMethod(alwaysRun = true)
    public void init(Method method) {
        restResponseObject = new RestServiceBase();
    }


    /**
     * PREPARE DATA PROVIDER SOURCE
     */
    @Test(groups = "functional")
    public void preSettings() {
        HashMap<String, String> tempMap = new HashMap<>(paramsMap); // TODO: 2022-05-10 be aware of it! todo a class for a not-mutable map
        _setTestStep("Temporarily remove param for a given SYMBOL: {}", tempMap.remove("symbol"));
        ExtractableResponse<Response> response = restResponseObject.getResponseWithTimeoutInSeconds(ENDPOINT_TRADE_BUCKETED, tempMap);

        _setTestStep("Create a list for a few top liquid instruments. Such list will be used as source for the DATA PROVIDER");
        HashMap<String, Integer> topTenInstruments = getMostLiquidInstruments(response, TOP_LIQUID_INSTRUMENT_COUNT);
        topOnMarket = new ArrayList<>(topTenInstruments.keySet());
        assertFalse(topOnMarket.isEmpty());
    }


    /**
     *
     * @param symbol
     */
    @Test(groups = "functional", dependsOnMethods = "preSettings", dataProvider = "liquid-symbols")
    public void checkPreSettings(String symbol) {
        LOGGER.info("checking data provider entry - {}", symbol);
        assertFalse(paramsMap.get("symbol").isEmpty());
    }


    /**
     * Test historical data for most liquid instruments, from last 14 days
     */
    @Test(groups = "functional", dependsOnMethods = "preSettings", dataProvider = "liquid-symbols")
    public void checkSummaryForGivenHistoricalDaysAndMostLiquidInstruments(String symbol) {
        _setTestStep("Init data");
        AtomicInteger counter = new AtomicInteger();
        ZonedDateTime twoWeeksPast = ZonedDateTime.now().minusDays(DAYS_TO_TEST_HISTORY);

        _setTestStep("add a symbol {} to the params map", symbol);
        paramsMap.replace("symbol", symbol);
        assertFalse(paramsMap.get("symbol").equalsIgnoreCase("symbol-to-replace"));

        _setTestStep("Print 2-weeks summary for a symbol:");
        for (int i=0; i<PAGES_COUNT; i++) {
            restResponseObject.getResponseBodyWithTimeoutInSeconds(ENDPOINT_TRADE_BUCKETED, paramsMap) // LinkedList<Bucket> deserializedData =
                    .as(new TypeRef<LinkedList<Bucket>>() {
                    }).forEach(
                            (bucketPortion) -> {
                                if (bucketPortion.getTimestamp().isAfter(twoWeeksPast) ) {
                                    LOGGER.info("Object index nr: {}, start: {}, bucket: {}", counter.incrementAndGet(), paramsMap.get("start"), bucketPortion);
                                }
                            });

            paramsMap.replace("start", String.valueOf(Integer.parseInt(paramsMap.get("start")) + RECORDS_FOR_PAGE_LIMIT));
        }
//        TimeUnit.SECONDS.sleep(10); // TODO: 2022-05-10
    }

}
