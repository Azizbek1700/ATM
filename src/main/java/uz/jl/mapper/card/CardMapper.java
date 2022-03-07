package uz.jl.mapper.card;

public class CardMapper {

    private static CardMapper cardMapper;

    public static CardMapper getInstance() {
        if (cardMapper == null)
            cardMapper = new CardMapper();
        return cardMapper;
    }

}
