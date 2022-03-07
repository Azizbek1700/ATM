package uz.jl.models.atm;

import lombok.Getter;
import lombok.Setter;
import uz.jl.enums.Status;
import uz.jl.enums.money.MoneyType;
import uz.jl.models.Auditable;

import javax.xml.validation.ValidatorHandler;

/**
 * @author Elmurodov Javohir, Mon 6:14 PM. 11/29/2021
 */
@Getter
@Setter

public class Cassette extends Auditable {
    private MoneyType moneyType;
    private int currencyCount;
    private Status status;
    private String atmId;
}
