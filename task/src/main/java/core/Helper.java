package core;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static core.JsonHelper.getCollectionOfFeeds;

public class Helper {

    static final Logger LOGGER = LoggerFactory.getLogger(Helper.class);
//    private static Integer testStepNr = 0;
    private static AtomicInteger testStepNr;

    public void initTest() {
        testStepNr = new AtomicInteger();
    }

    public void setTestStep(String description, String...values) {
        for (Object var : values) {
            description = description.replaceFirst("\\{}", var.toString());
        }
        String message = ">>> SCENARIO TEST STEP NR: " + testStepNr.incrementAndGet() + " | " + description;
        LOGGER.info(message);
    }


    public static HashMap<String, Integer> getMostLiquidInstruments(ExtractableResponse<Response> response, int countTop)
    {
        assert countTop > 0 : "count should be positive value!";
        LinkedList<String> symbolsInFeeds = new LinkedList<>();
        getCollectionOfFeeds(response).forEach(
                (feedRecord) -> {
                    symbolsInFeeds.add(feedRecord.get("symbol").toString());
                }
        );

        HashMap<String, Integer> freqMap = new HashMap<>();

        for (String el : symbolsInFeeds) {
            if (freqMap.containsKey(el)) {
                // If number is present in freqMap,
                // incrementing its count by 1
                freqMap.put(el, freqMap.get(el) + 1);
            }
            else {
                // If integer is not present in freqMap,
                // putting this integer to freqMap with 1 as it's value
                freqMap.put(el, 1);
            }
        }

        HashMap<String, Integer> topX =
                freqMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(countTop)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return topX;
    }

}
