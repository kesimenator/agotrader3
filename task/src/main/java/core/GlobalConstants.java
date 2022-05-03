package core;

import java.math.BigDecimal;

public class GlobalConstants {

    public static final long RESPONSE_TIMEOUT = 60;
    public static final BigDecimal INFINITY = BigDecimal.valueOf(Double.MAX_VALUE);

    public static final String BASE_URL = "https://www.bitmex.com/api/v1";

    // Individual & Bucketed Trades
    public static final String ENDPOINT_TRADE = BASE_URL + "/trade";
    public static final String ENDPOINT_TRADE_BUCKETED = BASE_URL + "/trade/bucketed";

    public static final int REST_SUCCESS_CODE = 200;
    static final String FAILED_RESPONSE_MESSAGE = "Failed to obtain response, reason: ";


    public static String [][] topicsWithoutAuthentication =
    {
            {"announcement"},        // Site announcements
            {"chat"},                // Trollbox chat
            {"connected"},           // Statistics of connected users/bots
            {"funding"},             // Updates of swap funding rates. Sent every funding interval (usually 8hrs)
            {"instrument"},          // Instrument updates including turnover and bid/ask
            {"insurance"},           // Daily Insurance Fund updates
            {"liquidation"},         // Liquidation orders as they're entered into the book
            {"orderBookL2_25"},      // Top 25 levels of level 2 order book
            {"orderBookL2"},         // Full level 2 order book
            {"orderBook10"},         // Top 10 levels using traditional full book push
            {"publicNotifications"}, // System-wide notifications (used for short-lived messages)
            {"quote"},               // Top level of the book
            {"quoteBin1m"},          // 1-minute quote bins
            {"quoteBin5m"},          // 5-minute quote bins
            {"quoteBin1h"},          // 1-hour quote bins
            {"quoteBin1d"},          // 1-day quote bins
            {"settlement"},          // Settlements
            {"trade"},               // Live trades
            {"tradeBin1m"},          // 1-minute trade bins
            {"tradeBin5m"},          // 5-minute trade bins
            {"tradeBin1h"},          // 1-hour trade bins
            {"tradeBin1d"},          // 1-day trade bins
    };

}
