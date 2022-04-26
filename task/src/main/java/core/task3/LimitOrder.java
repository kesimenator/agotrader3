package core.task3;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
final public class LimitOrder extends AbstractOrder implements Cloneable {

    private final BigDecimal limit;

    @Builder
    public LimitOrder(int id, String description, BigDecimal fee, BigDecimal limit) {
        super(id, description, fee);
        this.limit = limit;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
