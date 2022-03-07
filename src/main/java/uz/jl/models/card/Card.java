package uz.jl.models.card;

import lombok.*;
import uz.jl.enums.Status;
import uz.jl.enums.card.CardType;
import uz.jl.models.Auditable;

/**
 * @author Elmurodov Javohir, Mon 5:34 PM. 12/6/2021
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"pan"}, callSuper = false)
public class Card extends Auditable {
    private String pan;
    private String expiry;
    private String password;
    private CardType cardType;
    private Status cardStatus = Status.ACTIVE;
    private String ownerId;
    private long balance;
}
