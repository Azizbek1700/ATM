package uz.jl.models.atm;

import lombok.Getter;
import lombok.Setter;
import uz.jl.enums.Status;
import uz.jl.models.Auditable;

/**
 * @author Botirov Najmiddin, Fri 11:27. 10/12/2021
 */

@Getter
@Setter
public class Atm extends Auditable {
    private String name;
    private Status status = Status.ACTIVE;

}
