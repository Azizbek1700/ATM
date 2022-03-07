package uz.jl.configs;

import lombok.Getter;
import lombok.Setter;
import uz.jl.models.atm.Atm;
import uz.jl.models.card.Card;

/**
 * @author Botirov Najmiddin, Fri 20:52. 10/12/2021
 */
public class CardSession {
    @Getter
    @Setter
    private Card sessionCard;

    private static CardSession cardSession;

    private CardSession(){}

    public static CardSession getInstance() {
        if ( cardSession == null )
            cardSession = new CardSession();
        return cardSession;
    }

}
