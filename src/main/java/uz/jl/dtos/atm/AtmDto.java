package uz.jl.dtos.atm;

import lombok.Getter;
import lombok.Setter;
import uz.jl.dtos.BaseGenericDto;
import uz.jl.enums.Status;

/**
 * @author Doston Bokhodirov, Fri 6:39 PM. 12/10/2021
 */

@Getter
@Setter
public class AtmDto extends BaseGenericDto {
    private String name;
    private Status status;
}
