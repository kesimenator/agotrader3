package core.task3;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
final public class StopOrder extends AbstractOrder implements Cloneable {

    private final BigDecimal stop;

    @Builder
    private StopOrder(int id, String description, BigDecimal fee, BigDecimal stop) {
        super(id, description, fee);
        this.stop = stop;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

