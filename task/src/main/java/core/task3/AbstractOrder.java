package core.task3;

import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
abstract public class AbstractOrder {

    private final int id;
    private final String description;
    private final BigDecimal fee;

}
