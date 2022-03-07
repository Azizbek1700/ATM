package uz.jl.enums.money;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import static uz.jl.utils.Color.BLUE;
import static uz.jl.utils.Print.println;

/**
 * @author Botirov Najmiddin, Fri 14:51. 10/12/2021
 */
@Getter
@AllArgsConstructor
@ToString
public enum MoneyType {
    FIVE_THOUSAND("5000"),
    TEN_THOUSAND("10000"),
    FIFTY_THOUSAND("50000"),
    ONE_HUNDRED_THOUSAND("100000"),
    TEN_DOLLAR("10"),
    ONE_HUNDRED_DOLLAR("100$");
    private final String key;

    public static MoneyType findByCassette(String key) {
        for ( MoneyType value : values() ) {
            if ( value.key.equalsIgnoreCase(key) ) {
                return value;
            }
        }
        return null;
    }

    public static void showMoneyType() {
        int index = 1;
        for ( MoneyType value : values() ) {
            println(BLUE, (index++) + ". " + value);
        }
    }

    @Override
    public String toString() {
        return name();
    }
}
