package core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;


//@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Bucket {

//        @JacksonXmlProperty(localName = "timestamp")
//        @JsonFormat(pattern = Constants.DATETIME_FORMAT)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        ZonedDateTime timestamp;
        String symbol;
        BigDecimal open;
        BigDecimal high;
        BigDecimal low;
        BigDecimal close;
        int trades;
        int volume;
        BigDecimal vwap; //null
        BigDecimal lastSize;
        BigDecimal turnover;
        BigDecimal homeNotional;
        BigDecimal foreignNotional;
}
