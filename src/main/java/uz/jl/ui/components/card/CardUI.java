package uz.jl.ui.components.card;


import uz.jl.configs.CardSession;
import uz.jl.dtos.card.CardDto;
import uz.jl.enums.Status;
import uz.jl.enums.card.CardType;
import uz.jl.models.auth.AuthUser;
import uz.jl.response.ResponseEntity;
import uz.jl.response.ResponseStatus;
import uz.jl.services.auth.AuthUserService;
import uz.jl.services.auth.admin.AdminService;
import uz.jl.services.card.CardService;
import uz.jl.services.filesystems.CardsDB;
import uz.jl.ui.AuthUI;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.io.ObjectStreamClass;
import java.util.List;
import java.util.Objects;

import static uz.jl.enums.card.CardType.findByCardType;
import static uz.jl.enums.card.CardType.getAll;

public class CardUI extends BaseAbstractUI implements BaseUI {

    private static CardUI cardUI;

    public static CardUI getInstance() {
        if (Objects.isNull(cardUI)) {
            return cardUI = new CardUI();
        }
        return cardUI;
    }


    @Override
    public void create() {
        ResponseEntity<List<CardType>> cardTypeListResponse = CardType.getAll();
        if (cardTypeListResponse.getData().isEmpty()) {
            Print.println(Color.RED, "There is no Card type");
            return;
        }
        String passportNumber = Input.getStr("Enter passport serial: ");
        boolean check = AdminService.getInstance().checkPassportSerial(passportNumber);
        if (check) {
            String fullName = Input.getStr("Enter fullName: ");
            String phoneNumber = Input.getStr("Enter phone number: ");
            show(cardTypeListResponse);
            String cardType = Input.getStr("Enter cardType: ");
            CardType cardType1 = findByCardType(cardType);
            if (Objects.isNull(cardType1)) {
                show(new ResponseEntity<>("Wrong date", ResponseStatus.HTTP_FORBIDDEN));
                return;
            }
            String cardPassword = Input.getStr("Enter card password: ");
            ResponseEntity<String> response = CardService.getInstance().create(fullName, phoneNumber, passportNumber, cardType1, cardPassword);
            show(response);
        }
        else {
            show(cardTypeListResponse);
            String cardType = Input.getStr("Enter cardType: ");
            CardType cardType1 = findByCardType(cardType);
            if (Objects.isNull(cardType1)) {
                show(new ResponseEntity<>("Wrong date", ResponseStatus.HTTP_FORBIDDEN));
                return;
            }
            String cardPassword = Input.getStr("Enter card password: ");
            ResponseEntity<String> response = CardService.getInstance().create(passportNumber, cardType1, cardPassword);
            show(response);

        }
    }

    public String getValueString() {
        return Input.getStr("Enter: ");
    }

    @Override
    public void block() {

        if ( Objects.isNull(CardSession.getInstance().getSessionCard()) ) {
            if (showByStatus(Status.ACTIVE) == 1) {
                Print.println(Color.RED, "There is no active card");
                return;
            }

            String pan = Input.getStr("Enter card pan: ");
            ResponseEntity<String> response = CardService.getInstance().block(pan);
            show(response);
        } else {
            String choice = Input.getStr("Is your card blocked? yes/no: ");
            if ( choice.startsWith("y") ) {
                CardSession.getInstance().getSessionCard().setCardStatus(Status.BLOCKED);
                show(new ResponseEntity<>("Your card successfully blocked!!!"));
                CardsDB.writeCards(CardsDB.getCards());
                AuthUI.getInstance().logout();
            } else {
                show(new ResponseEntity<>("The order was not confirmed!!!", ResponseStatus.HTTP_FORBIDDEN));
            }
        }


    }

    @Override
    public void unblock() {
        if (showByStatus(Status.BLOCKED) == 1) {
            Print.println(Color.RED, "There is no blocked card");
            return;
        }
        String pan = Input.getStr("Enter card pan: ");
        ResponseEntity<String> response = CardService.getInstance().unblock(pan);
        show(response);
    }

    @Override
    public void delete() {
        if (showIgnoreStatus(Status.DELETED) == 1) {
            Print.println(Color.RED, "There is no card");
            return;
        }
        String pan = Input.getStr("Enter card pan: ");
        ResponseEntity<String> response = CardService.getInstance().delete(pan);
        show(response);
    }

    @Override
    public void update() {

    }

    @Override
    public void list() {
        int index = 1;
        ResponseEntity<List<CardDto>> response = CardService.getInstance().showIgnoreStatus(Status.DELETED);
        if (response.getData().isEmpty()) return;
        for (CardDto cardDto : response.getData()) {
            if (cardDto.getCardStatus().equals(Status.ACTIVE)) {
                Print.println(Color.GREEN, index++ + ". Card Pan: " + cardDto.getPan());
                Print.println(Color.GREEN, ". Card Expiry: " + cardDto.getExpiry());
                Print.println(Color.GREEN, "    Card Type: " + cardDto.getCardType());
                Print.println(Color.GREEN, "    Card Balance: " + cardDto.getBalance());
            } else if (cardDto.getCardStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". Card Pan: " + cardDto.getPan());
                Print.println(Color.PURPLE, ". Card Expiry: " + cardDto.getExpiry());
                Print.println(Color.PURPLE, "    Card Type: " + cardDto.getCardType());
                Print.println(Color.PURPLE, "    Card Balance: " + cardDto.getBalance());
            }
        }
    }

    public ResponseEntity<String> login() {
        String pan = Input.getStr("Enter pan: ");
        String expiry = Input.getStr("Enter expiry: ");
        return CardService.getInstance().login(pan, expiry);
    }

    public int showByStatus(Status status) {
        int index = 1;
        ResponseEntity<List<CardDto>> response = CardService.getInstance().showByStatus(status);
        if (response.getData().isEmpty()) return index;
        for (CardDto cardDto : response.getData()) {
            Print.println(Color.GREEN, index++ + ". Card Pan: " + cardDto.getPan());
            Print.println(Color.GREEN, ". Card Expiry: " + cardDto.getExpiry());
            Print.println(Color.GREEN, "    Card Type: " + cardDto.getCardType());
            Print.println(Color.GREEN, "    Card Balance: " + cardDto.getBalance());
        }
        return index;
    }

    public int showIgnoreStatus(Status status) {
        int index = 1;
        ResponseEntity<List<CardDto>> response = CardService.getInstance().showIgnoreStatus(status);
        if (response.getData().isEmpty()) return index;
        for (CardDto cardDto : response.getData()) {
            if (cardDto.getCardStatus().equals(Status.ACTIVE)) {
                Print.println(Color.GREEN, index++ + ". Card Pan: " + cardDto.getPan());
                Print.println(Color.GREEN, ". Card Expiry: " + cardDto.getExpiry());
                Print.println(Color.GREEN, "    Card Type: " + cardDto.getCardType());
                Print.println(Color.GREEN, "    Card Balance: " + cardDto.getBalance());
            } else if (cardDto.getCardStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". Card Pan: " + cardDto.getPan());
                Print.println(Color.PURPLE, ". Card Expiry: " + cardDto.getExpiry());
                Print.println(Color.PURPLE, "    Card Type: " + cardDto.getCardType());
                Print.println(Color.PURPLE, "    Card Balance: " + cardDto.getBalance());
            }
        }
        return index;
    }
}
