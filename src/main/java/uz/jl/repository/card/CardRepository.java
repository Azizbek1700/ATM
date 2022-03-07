package uz.jl.repository.card;

import uz.jl.enums.Status;
import uz.jl.enums.card.CardType;
import uz.jl.models.auth.AuthUser;
import uz.jl.models.card.Card;
import uz.jl.services.filesystems.CardsDB;
import uz.jl.services.filesystems.UsersDB;

import java.util.Calendar;
import java.util.Objects;
import java.util.Objects;

public class CardRepository {

    private  static CardRepository cardRepository;

    public static CardRepository getInstance(){
        if (cardRepository==null){
            cardRepository=new CardRepository();
        }
        return cardRepository;
    }


    public Card findCardByPan(String pan){
        for (Card card : CardsDB.getCards()) {
            if(card.getPan().equals( pan ) ){
                return card;
            }
        }
        return null;
    }

    public Card findCardByPan(String pan, Status status){
        for (Card card : CardsDB.getCards()) {
            if(card.getPan().equals( pan ) && !card.getCardStatus().equals( status )){
                return card;
            }
        }
        return null;
    }

    public AuthUser findByPassportSerial(String passportSerial) {
        for (AuthUser user : UsersDB.getUsers()) {
            if (Objects.nonNull(user) && user.getPassportNumber() != null && user.getPassportNumber().equals( passportSerial )) return user;
        }
        return null;
    }


    public Card finCardByPanAndExpiry(String pan, String expiry) {
        for (Card card : CardsDB.getCards()) {
            if(card.getPan().equals( pan ) && card.getExpiry().equals(expiry)){
                return card;
            }
        }
        return null;
    }


    public boolean isBlocked(Card card) {
        return ( card.getCardStatus() != null && card.getCardStatus().equals(Status.BLOCKED) );
    }

    public String generateCardPan() {
        return (System.currentTimeMillis() + "").substring( 1 );

    }

    public String Cardexpiry() {
        Calendar rightNow = Calendar.getInstance();
        String month=rightNow.get( Calendar.MONTH )+"";
        if (month.length()==1){
            month="0"+month;
        }
        int year=rightNow.get( Calendar.YEAR );
        return month+"/"+(year+4);
    }

    public String showCardcView(CardType cardType, String pan,String fullName, String ex) {
        return String.format("""
                 _______________________________________
                |   DENA BANK               %s 
                |                             ⬜⬜⬜      |
                |  %s                                     
                |                                       |
                |  %s                          
                |              Expiry Date %s      |
                |_______________________________________|
                """, cardType+"", pan,fullName, ex);

    }

}
