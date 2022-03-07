package uz.jl.dtos.card;

import lombok.Getter;
import lombok.Setter;
import uz.jl.dtos.BaseDto;
import uz.jl.enums.Status;
import uz.jl.enums.card.CardType;

/**
 * @author Doston Bokhodirov, Mon 12:25 AM. 12/13/2021
 */

@Getter
@Setter
public class CardDto implements BaseDto {
    private String pan;
    private String expiry;
    private CardType cardType;
    private Status cardStatus;
    private long balance;
}
