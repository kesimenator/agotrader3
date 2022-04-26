package functional;

import core.RestServiceBase;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static core.GlobalConstants.ENDPOINT_TRADE_BUCKETED;
import static core.GlobalConstants.INFINITY;
import static core.Helper.getMostLiquidInstruments;
import static core.JsonHelper.getCollectionOfFeeds;

public class Task2Test extends TestHooks {

    static final Logger LOGGER = LoggerFactory.getLogger(Task1Test.class);
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
        RestServiceBase restResponseObject = new RestServiceBase();
        response = restResponseObject.getResponseBodyWithTimeoutInSeconds(ENDPOINT_TRADE_BUCKETED, paramsMap);
    }


    /**
     * Prints to the console 2-weeks summary containing (per period):
     * • Sum of volume
     * • Average highest price
     * • Minimal price - assuming that we take the "close" price as a valid one for our hitory review
     */
    @Test(groups = "functional", dependsOnMethods = "preconditionsEndpointIsAvailable")
    public void printTwoWeeksSummary() {

        LinkedList<JSONObject> listOfFeeds = getCollectionOfFeeds(response);
        LinkedList<JSONObject> twoWeeksSummary = new LinkedList<>();
        LinkedList<String> symbolsAppearsInFeeds = new LinkedList<>();
        BigDecimal averageHighestPrice = new BigDecimal(0);
        BigDecimal minimalPrice = new BigDecimal(String.valueOf(INFINITY));

        LOGGER.info("For the most liquid instruments --------------- ");
        for (JSONObject feedRecord : listOfFeeds ) {
            String symbol = feedRecord.get("symbol").toString();
            symbolsAppearsInFeeds.add(symbol);
        }
        HashMap<String, Integer> freqMap = getMostLiquidInstruments(symbolsAppearsInFeeds);
        // TODO: 2022-04-25 for debug:
        for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
            LOGGER.debug("{} : {}", entry.getKey(), entry.getValue());
        }

        for (JSONObject feedRecord : listOfFeeds ) {
            String dateFromTimeStamp = feedRecord.get("timestamp").toString().split("\\.")[0];
            LocalDateTime dateTimeFromRecord = LocalDateTime.parse(dateFromTimeStamp);
            LocalDateTime today = LocalDateTime.now(ZoneId.of("Europe/London"));

            String symbol = feedRecord.get("symbol").toString();

            if (!freqMap.containsKey(symbol))
                continue;
            if ( dateTimeFromRecord.isBefore(today.minusWeeks(2)))
                continue;

            // Only 2-weeks summary containing
            twoWeeksSummary.add(feedRecord);

            // Average highest price
            BigDecimal high = new BigDecimal(feedRecord.get("high").toString());
            averageHighestPrice = new BigDecimal(String.valueOf(averageHighestPrice.add(high)));

            //Minimal value
            BigDecimal close = new BigDecimal(feedRecord.get("close").toString());
            minimalPrice = new BigDecimal(String.valueOf(minimalPrice.min(close)));
        }

        LOGGER.info("Sum of volume: {}", twoWeeksSummary.size());
        LOGGER.info("Average highest price: {}", averageHighestPrice.divide(BigDecimal.valueOf(twoWeeksSummary.size()), RoundingMode.HALF_UP));
        LOGGER.info("Minimal price(close): {}", minimalPrice);

        // TODO: 2022-04-25 Didn't understand from the task description: "Include as much historical data as possible" ???

    }


}
