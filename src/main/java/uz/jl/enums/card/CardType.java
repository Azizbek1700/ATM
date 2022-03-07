package uz.jl.enums.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uz.jl.response.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Elmurodov Javohir, Mon 5:37 PM. 12/6/2021
 */


@Getter
@AllArgsConstructor
public enum CardType {
    UZCARD("uzcard", "8600"),
    HUMO("humo", "9860"),
    COBAGE("cobage", "6320"),
    VISA("visa", "4790"),
    MASTER_CARD("master", "5471"),
    UNION_PAY("union", "4567");
    private final String type;
    private final String code;

    public static CardType findByCardType(String type) {
        for (CardType cardType : values()) {
            if (cardType.type.equalsIgnoreCase(type))
                return cardType;
        }
        return null;
    }

    public static ResponseEntity<List<CardType>> getAll() {
        List<CardType> cardTypeList = new ArrayList<>();
        Collections.addAll(cardTypeList, values());
        return new ResponseEntity<>(cardTypeList);
    }
}
