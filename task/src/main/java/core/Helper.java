package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Helper {

    static final Logger LOGGER = LoggerFactory.getLogger(Helper.class);
    private static Integer testStepNr = 0;

    public static void initTest() {
        testStepNr = 0;
    }

    public static void setTestStep(String description, String...values) {
        for (Object var : values) {
            description = description.replaceFirst("\\{}", var.toString());
        }
        String message = ">>> SCENARIO TEST STEP NR: " + (++testStepNr) + " | " + description;
        LOGGER.info(message);
    }


    public static HashMap<String, Integer> getMostLiquidInstruments(List<String> symbolsInFeeds)
    {
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

        HashMap<String, Integer> topTen =
                freqMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(10)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return topTen;
    }

}
