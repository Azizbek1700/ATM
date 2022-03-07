package uz.jl.services.card;

import uz.jl.configs.AtmSession;
import uz.jl.configs.CardSession;
import uz.jl.configs.Session;
import uz.jl.dtos.card.CardDto;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.enums.card.CardType;
import uz.jl.enums.money.MoneyType;
import uz.jl.mapper.card.CardMapper;
import uz.jl.models.atm.Atm;
import uz.jl.models.atm.Cassette;
import uz.jl.models.auth.AuthUser;
import uz.jl.models.card.Card;
import uz.jl.repository.atm.CassetteRepository;
import uz.jl.repository.auth.AuthUserRepository;
import uz.jl.repository.card.CardRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.response.ResponseStatus;
import uz.jl.services.BaseAbstractService;
import uz.jl.services.UtilsForService;
import uz.jl.services.auth.client.ClientService;
import uz.jl.services.filesystems.CardsDB;
import uz.jl.services.filesystems.CassettesDB;
import uz.jl.services.filesystems.UsersDB;
import uz.jl.ui.AuthUI;
import uz.jl.ui.ShowMessageUI;
import uz.jl.ui.components.card.CardUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.*;
import java.util.regex.Pattern;


public class CardService extends BaseAbstractService<Card, CardRepository, CardMapper> {


    protected CardService(CardRepository repository, CardMapper mapper) {
        super(repository, mapper);
    }

    private static CardService cardService;

    public static CardService getInstance() {
        if ( cardService == null ) {
            cardService = new CardService(CardRepository.getInstance(), CardMapper.getInstance());
        }
        return cardService;
    }


    public ResponseEntity<String> create(String fullName, String phoneNumber, String passportSerial, CardType cardType, String code) {
        ClientService.getInstance().create(fullName, phoneNumber, passportSerial);

        return create(passportSerial, cardType, code);
    }

    public ResponseEntity<String> create(String passportSerial, CardType cardType, String cardPassword) {
        AuthUser user = repository.findByPassportSerial(passportSerial);
        Card newCard = new Card();
        if ( UtilsForService.getInstance().isInteger(cardPassword) ) {
            return new ResponseEntity<>("Wrong date!!!", ResponseStatus.HTTP_FORBIDDEN);
        }

        String pan = cardType.getCode() + repository.generateCardPan();
        String expiry = repository.Cardexpiry();

        newCard.setPassword((cardPassword));
        newCard.setCardType(cardType);
        newCard.setOwnerId(user.getId());
        newCard.setPan(pan);
        newCard.setExpiry(expiry);
        Print.println(repository.showCardcView(cardType , pan, user.getFullName(),expiry));
        Print.println(pan);
        Print.println(expiry);
        CardsDB.getCards().add(newCard);
        CardsDB.writeCards(CardsDB.getCards());
        return new ResponseEntity<>("SUCCESS!!!");
    }

    public ResponseEntity<String> block(String pan) {
        Card card = repository.findCardByPan(pan);
        if ( Objects.isNull(card) ) return new ResponseEntity<>("Card not fount", ResponseStatus.HTTP_NOT_FOUND);
        if ( card.getCardStatus().equals(Status.BLOCKED) )
            return new ResponseEntity<>("CARD is already blocked", ResponseStatus.HTTP_FORBIDDEN);
        card.setCardStatus(Status.BLOCKED);
        card.setUpdatedAt(new Date());
        CardsDB.writeCards(CardsDB.getCards());
        return new ResponseEntity<>("Card is successfully blocked ");
    }

    public ResponseEntity<String> unblock(String pan) {
        Card card = repository.findCardByPan(pan);
        if ( Objects.isNull(card) ) return new ResponseEntity<>("Atm not fount", ResponseStatus.HTTP_NOT_FOUND);
        if ( card.getCardStatus().equals(Status.ACTIVE) )
            return new ResponseEntity<>("Card is already unblocked", ResponseStatus.HTTP_FORBIDDEN);

        card.setCardStatus(Status.ACTIVE);
        card.setUpdatedAt(new Date());
        CardsDB.writeCards(CardsDB.getCards());
        return new ResponseEntity<>("Card is successfully unblocked");
    }

    public ResponseEntity<String> delete(String pan) {
        Card card = repository.findCardByPan(pan, Status.DELETED);
        if ( Objects.isNull(card) ) return new ResponseEntity<>("Card not fount ", ResponseStatus.HTTP_NOT_FOUND);
        if ( card.getCardStatus().equals(Status.DELETED) )
            return new ResponseEntity<>("Card is already deleted", ResponseStatus.HTTP_FORBIDDEN);

        card.setCardStatus(Status.DELETED);
        card.setUpdatedAt(new Date());
        CardsDB.writeCards(CardsDB.getCards());
        return new ResponseEntity<>("Card successfully deleted");
    }


    public void withDrawMoneyForAtm() {

        Cassette cassette1 = CassetteRepository.getInstance().findCassetteFromAtmId(AtmSession.getInstance().getSessionAtm().getId(), MoneyType.TEN_DOLLAR);
        Cassette cassette2 = CassetteRepository.getInstance().findCassetteFromAtmId(AtmSession.getInstance().getSessionAtm().getId(), MoneyType.ONE_HUNDRED_DOLLAR);
        Cassette cassette3 = CassetteRepository.getInstance().findCassetteFromAtmId(AtmSession.getInstance().getSessionAtm().getId(), MoneyType.FIVE_THOUSAND);
        Cassette cassette4 = CassetteRepository.getInstance().findCassetteFromAtmId(AtmSession.getInstance().getSessionAtm().getId(), MoneyType.TEN_THOUSAND);
        Cassette cassette5 = CassetteRepository.getInstance().findCassetteFromAtmId(AtmSession.getInstance().getSessionAtm().getId(), MoneyType.FIFTY_THOUSAND);
        Cassette cassette6 = CassetteRepository.getInstance().findCassetteFromAtmId(AtmSession.getInstance().getSessionAtm().getId(), MoneyType.ONE_HUNDRED_THOUSAND);

        if ( CardSession.getInstance().getSessionCard().getCardType().equals(CardType.VISA) ) {
            withdrawMoneyForVisaCard(cassette1, cassette2);
        } else {
            withdrawMoneyForSumCard(cassette3, cassette4, cassette5, cassette6);
        }
        CassettesDB.writeCassettes(CassettesDB.getCassettes());
        CardsDB.writeCards(CardsDB.getCards());
    }

    private void withdrawMoneyForVisaCard(Cassette cassette1, Cassette cassette2) {
        String moneyVisaString = CardUI.getInstance().getValueString();
        if ( !UtilsForService.getInstance().isInteger(moneyVisaString) ) {
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("Wrong data!!!"));
        }
        long moneyVisa = Long.parseLong("" + moneyVisaString);
        boolean checkForCard = checkCardMoney(moneyVisa);
        if ( !checkForCard ) {
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("Not enough money on your card", ResponseStatus.HTTP_FORBIDDEN));
            return;
        }
        boolean checkForCassette = checkForVisaCassette(cassette1, cassette2, moneyVisa);
        if ( checkForCassette ) {
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("SUCCESS!!!"));
        } else {
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("There is not enough money in the ATM", ResponseStatus.HTTP_FORBIDDEN));
        }
    }

    private boolean checkForVisaCassette(Cassette cassette1, Cassette cassette2, long moneyVisa) {
        int countForCas1 = 0;
        int countForCas2 = 0;
        long sum = moneyVisa;

        if ( Objects.isNull(cassette2) ) {
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("2 - cassette blocked!", ResponseStatus.HTTP_FORBIDDEN));
        } else {
            if ( moneyVisa / 100 <= cassette2.getCurrencyCount() ) {
                while ( moneyVisa / 100 != 0 ) {
                    moneyVisa -= 100;
                    countForCas2++;
                }
            }
        }

        if ( Objects.isNull(cassette1) ) {
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("1 - cassette blocked!", ResponseStatus.HTTP_FORBIDDEN));
        } else {
            if ( moneyVisa <= cassette1.getCurrencyCount() ) {
                while ( moneyVisa != 0 ) {
                    moneyVisa -= 1;
                    countForCas1++;
                }
            }
        }

        if ( moneyVisa == 0 ) {
//            println(GREEN, "Summa: " + sum);
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("USD: " + sum));
            if ( Objects.nonNull(cassette1) ) {
                countForCas1 = cassette1.getCurrencyCount() - countForCas1;
                cassette1.setCurrencyCount(countForCas1);
            }

            if ( Objects.nonNull(cassette2) ) {
                countForCas2 = cassette2.getCurrencyCount() - countForCas2;
                cassette2.setCurrencyCount(countForCas2);
            }


            CardSession.getInstance().getSessionCard().setBalance(CardSession.getInstance().getSessionCard().getBalance() - sum);
//            println(CYAN, "Card balance: " + CardDao.getInstance().getSessionCard().getBalance());
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("Card balance: " + CardSession.getInstance().getSessionCard().getBalance()));
            CardsDB.writeCards(CardsDB.getCards());
            return true;
        }
        return false;
    }

    private void withdrawMoneyForSumCard(Cassette cassette3, Cassette cassette4, Cassette cassette5, Cassette cassette6) {
//        println(BLUE, "Your balance: " + CardDao.getInstance().getSessionCard().getBalance() + " sum");
        String moneySUMString = CardUI.getInstance().getValueString();
        if ( UtilsForService.getInstance().isInteger(moneySUMString) ) {
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("Wrong data!!!"));
        }
        long moneySUM = Long.parseLong(moneySUMString);
        boolean checkForCard = checkCardMoney(moneySUM);
        if ( !checkForCard ) {
//            println(RED, "Not enough money on your card");
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("Not enough money on your card", ResponseStatus.HTTP_FORBIDDEN));
            return;
        }

        if ( moneySUM % 5000 != 0 ) {
//            println("Wrong data!");
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("Wrong data", ResponseStatus.HTTP_FORBIDDEN));
            return;
        }

        boolean checkForCassette = checkForSumCassette(cassette3, cassette4, cassette5, cassette6, moneySUM);

        if ( checkForCassette ) {
//            println("SUCCESS!!!");
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("SUCCESS!!!"));
        } else {
//            println(RED, "There is not enough money in the ATM");
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("There is not enough money in the ATM", ResponseStatus.HTTP_FORBIDDEN));
        }

    }

    private boolean checkCardMoney(double money) {
        return money <= CardSession.getInstance().getSessionCard().getBalance();
    }

    private boolean checkCassetteMoney(int amountMoney, double moneyUSD) {
        return amountMoney >= moneyUSD;
    }


    private boolean checkForSumCassette(Cassette cassette3, Cassette cassette4, Cassette cassette5, Cassette cassette6, long moneySUM) {
        int countForCas3 = 0;
        int countForCas4 = 0;
        int countForCas5 = 0;
        int countForCas6 = 0;
        long sum = moneySUM;

        if ( Objects.isNull(cassette6) ) {
//            println("Cassette 4 blocked!!!");
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("6 - cassette blocked!", ResponseStatus.HTTP_FORBIDDEN));
        } else {

            if ( moneySUM / 100000 <= cassette6.getCurrencyCount() ) {
                while ( moneySUM / 100000 != 0 ) {
                    moneySUM -= 100000;
                    countForCas6++;
                }
            }
        }

        if ( Objects.isNull(cassette5) ) {
//            println("Cassette 5 blocked!!!");
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("5 - cassette blocked!", ResponseStatus.HTTP_FORBIDDEN));
        } else {
            if ( moneySUM / 50000 <= cassette5.getCurrencyCount() ) {
                while ( moneySUM / 50000 != 0 ) {
                    moneySUM -= 50000;
                    countForCas5++;
                }
            }
        }

        if ( Objects.isNull(cassette4) ) {
//            println("Cassette 4 blocked!!!");
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("4 - cassette blocked!"));
        } else {
            if ( moneySUM / 10000 <= cassette4.getCurrencyCount() ) {
                while ( moneySUM / 10000 != 0 ) {
                    moneySUM -= 10000;
                    countForCas4++;
                }
            }
        }
        if ( Objects.isNull(cassette3) ) {
//            println("Cassette 3 blocked!!!");
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("3 - cassette blocked!"));
        } else {
            if ( moneySUM / 5000 <= cassette4.getCurrencyCount() ) {
                while ( moneySUM / 5000 != 0 ) {
                    moneySUM -= 5000;
                    countForCas3++;
                }
            }
        }

        if ( moneySUM == 0 ) {
//            println(GREEN, "Summa: " + sum);
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("SUMMA: " + sum));
            if ( Objects.nonNull(cassette6) ) {
                countForCas6 = cassette6.getCurrencyCount() - countForCas6;
                cassette6.setCurrencyCount(countForCas6);
            }

            if ( Objects.nonNull(cassette5) ) {
                countForCas5 = cassette5.getCurrencyCount() - countForCas5;
                cassette5.setCurrencyCount(countForCas5);
            }

            if ( Objects.nonNull(cassette4) ) {
                countForCas4 = cassette4.getCurrencyCount() - countForCas4;
                cassette4.setCurrencyCount(countForCas4);
            }

            if ( Objects.nonNull(cassette3) ) {
                countForCas3 = cassette3.getCurrencyCount() - countForCas3;
                cassette3.setCurrencyCount(countForCas3);
            }

            CardSession.getInstance().getSessionCard().setBalance(CardSession.getInstance().getSessionCard().getBalance() - sum);
//            println(CYAN, "Card balance: " + CardDao.getInstance().getSessionCard().getBalance());
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("Card balance: " + CardSession.getInstance().getSessionCard().getBalance()));
            CardsDB.writeCards(CardsDB.getCards());
            return true;
        }

        return false;
    }

    @Override
    public ResponseEntity<List<Card>> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<Card> get(String id) {
        return null;
    }

    public ResponseEntity<String> login(String pan, String expiry) {
        Card card = CardRepository.getInstance().finCardByPanAndExpiry(pan, expiry);
        if ( Objects.isNull(card) ) {
            return new ResponseEntity<>("Card not found", ResponseStatus.HTTP_FORBIDDEN);
        }
        boolean checkBlocked = CardRepository.getInstance().isBlocked(card);

        if ( checkBlocked ) {
            return new ResponseEntity<>("Card blocked!!!", ResponseStatus.HTTP_FORBIDDEN);
        }
        int count = 0;
        String code = Input.getStr("Enter the code: ");

        while ( !code.equals(String.valueOf(card.getPassword())) ) {
            count++;
            if ( count == 3 ) {
                card.setCardStatus(Status.BLOCKED);
                CardsDB.writeCards(CardsDB.getCards());
                AuthUI.getInstance().logout();
                return new ResponseEntity<>("This card blocked!!!");
            }
            code = Input.getStr("Enter the code: ");
        }
        CardSession.getInstance().setSessionCard(card);
        return new ResponseEntity<>("WELCOME ATM");
    }

    public ResponseEntity<String> createCardAnonymous(String firstName, String lastName, String cardtype, String password) {
        Card card = new Card();


        return new ResponseEntity<>("Card is successfully created");
    }

    public void replenishCard() {
        Print.println(Color.BLUE, "Your balance: " + CardSession.getInstance().getSessionCard().getBalance());
        String str = Input.getStr("Enter balance to replenish card: ");
        if ( UtilsForService.getInstance().isInteger(str) ) {
            Print.println(Color.RED, "Invalid balance");
            return;
        }
        long balance = Integer.parseInt(str);
        CardSession.getInstance().getSessionCard().setBalance(CardSession.getInstance().getSessionCard().getBalance() + balance);
        Print.println(Color.BLUE, "Card balance is successfully replenished");
        CardsDB.writeCards(CardsDB.getCards());
    }

    public ResponseEntity<List<CardDto>> showByStatus(Status status) {
        List<CardDto> cardDtoList = new ArrayList<>();
        for ( Card card : CardsDB.getCards() ) {
            if ( card.getCardStatus().equals(status) ) {
                CardDto cardDto = new CardDto();
                cardDto.setPan(card.getPan());
                cardDto.setExpiry(card.getExpiry());
                cardDto.setCardType(card.getCardType());
                cardDto.setBalance(card.getBalance());
                cardDtoList.add(cardDto);
            }
        }
        return new ResponseEntity<>(cardDtoList);
    }

    public ResponseEntity<List<CardDto>> showIgnoreStatus(Status status) {
        List<CardDto> cardDtoList = new ArrayList<>();
        for ( Card card : CardsDB.getCards() ) {
            if ( !card.getCardStatus().equals(status) ) {
                CardDto cardDto = new CardDto();
                cardDto.setPan(card.getPan());
                cardDto.setExpiry(card.getExpiry());
                cardDto.setCardType(card.getCardType());
                cardDto.setCardStatus(card.getCardStatus());
                cardDto.setBalance(card.getBalance());
                cardDtoList.add(cardDto);
            }
        }
        return new ResponseEntity<>(cardDtoList);
    }
}
