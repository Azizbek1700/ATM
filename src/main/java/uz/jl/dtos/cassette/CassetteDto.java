package uz.jl.dtos.cassette;

import lombok.Getter;
import lombok.Setter;
import uz.jl.dtos.BaseDto;
import uz.jl.enums.Status;
import uz.jl.enums.money.MoneyType;

/**
 * @author Doston Bokhodirov, Fri 10:36 PM. 12/10/2021
 */

@Getter
@Setter
public class CassetteDto implements BaseDto {
    private MoneyType moneyType;
    private int currencyCount;
    private Status status;
}
