package uz.jl.services.atm;

import uz.jl.dtos.cassette.CassetteDto;
import uz.jl.enums.Status;
import uz.jl.enums.money.MoneyType;
import uz.jl.mapper.atm.CassetteMapper;
import uz.jl.models.atm.Atm;
import uz.jl.models.atm.Cassette;
import uz.jl.repository.atm.CassetteRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.response.ResponseStatus;
import uz.jl.services.BaseAbstractService;
import uz.jl.services.UtilsForService;
import uz.jl.services.filesystems.AtmsDB;
import uz.jl.services.filesystems.CassettesDB;
import uz.jl.ui.ShowMessageUI;
import uz.jl.ui.components.atm.CassetteUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Botirov Najmiddin, Fri 12:14. 10/12/2021
 */
public class CassetteService extends BaseAbstractService<Cassette, CassetteRepository, CassetteMapper> {
    private static CassetteService cassetteService;

    private CassetteService(CassetteRepository repository, CassetteMapper mapper) {
        super(repository, mapper);
    }

    public static CassetteService getInstance() {
        if (cassetteService == null)
            cassetteService = new CassetteService(CassetteRepository.getInstance(), CassetteMapper.getInstance());
        return cassetteService;
    }

    public static void createCassette(Atm atm) {

        for (int i = 0; i < 6; i++) {
            boolean add = addCassette((i + 1), atm);
            if ( !add ) {
                i -= 1;
            }
            else ShowMessageUI.getInstance().showMessage(new ResponseEntity<>(Color.RED + "SUCCESS!"));
        }
        AtmsDB.writeAtms(AtmsDB.getAtms());
        CassettesDB.writeCassettes(CassettesDB.getCassettes());
        ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("SUCCESS!!!", ResponseStatus.HTTP_OK));
    }

    private static boolean addCassette(int i, Atm atm) {

        Cassette cassette = new Cassette();
        MoneyType moneyType = selectionMoneyType(i);
        Print.println(moneyType.toString());
        String amountMoney = Input.getStr("Amount money: ");
        if (UtilsForService.getInstance().isInteger(amountMoney)) {
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("Wrong data", ResponseStatus.HTTP_FORBIDDEN));
            return false;
        }
        int amount = Integer.parseInt(amountMoney);
        if ( amount > 200 ) {
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("Wrong data", ResponseStatus.HTTP_FORBIDDEN));
            return false;
        }

        if ( amount < 0 ) {
            ShowMessageUI.getInstance().showMessage(new ResponseEntity<>("Wrong data", ResponseStatus.HTTP_FORBIDDEN));
            return false;
        }

        cassette.setCurrencyCount(amount);
        cassette.setMoneyType(moneyType);
        cassette.setAtmId(atm.getId());
        cassette.setStatus(Status.ACTIVE);
        CassettesDB.getCassettes().add(cassette);
        return true;
    }

    private static MoneyType selectionMoneyType(int i) {
        switch ( i ) {
            case 1 -> { return MoneyType.FIVE_THOUSAND;}
            case 2 -> { return MoneyType.TEN_THOUSAND; }
            case 3 -> { return MoneyType.FIFTY_THOUSAND; }
            case 4 -> { return MoneyType.ONE_HUNDRED_THOUSAND; }
            case 5 -> { return MoneyType.TEN_DOLLAR; }
            case 6 -> { return MoneyType.ONE_HUNDRED_DOLLAR; }
        }
        return null;
    }

    public ResponseEntity<List<CassetteDto>> showIgnoreStatus(Status status) {
        List<CassetteDto> cassetteDtoList = new ArrayList<>();
        for (Cassette cassette : CassettesDB.getCassettes()) {
            if (!cassette.getStatus().equals(status)) {
                CassetteDto cassetteDto = new CassetteDto();
                cassetteDto.setMoneyType(cassette.getMoneyType());
                cassetteDto.setCurrencyCount(cassette.getCurrencyCount());
                cassetteDto.setStatus(cassette.getStatus());
                cassetteDtoList.add(cassetteDto);
            }
        }
        return new ResponseEntity<>(cassetteDtoList);
    }

    public ResponseEntity<List<CassetteDto>> showByStatus(Status status) {
        List<CassetteDto> cassetteDtoList = new ArrayList<>();
        for (Cassette cassette : CassettesDB.getCassettes()) {
            if (cassette.getStatus().equals(status)) {
                CassetteDto cassetteDto = new CassetteDto();
                cassetteDto.setMoneyType(cassette.getMoneyType());
                cassetteDto.setCurrencyCount(cassette.getCurrencyCount());
                cassetteDtoList.add(cassetteDto);
            }
        }
        return new ResponseEntity<>(cassetteDtoList);
    }

    public Cassette findCassetteFromType(Atm atm, MoneyType moneyType) {
        for (Cassette cassette : CassettesDB.getCassettes()) {
            if (cassette.getAtmId().equals(atm.getId()) && cassette.getMoneyType().equals(moneyType)) {
                return cassette;
            }
        }
        return null;
    }

    public ResponseEntity<String> block (String key){
        MoneyType moneyType = MoneyType.findByCassette(key);
        if (Objects.isNull(moneyType)) return  new ResponseEntity<>("Not fount cassette", ResponseStatus.HTTP_NOT_FOUND);
        for (Cassette cassette : CassettesDB.getCassettes()) {
            if (cassette.getMoneyType().equals(moneyType)){
                cassette.setStatus(Status.BLOCKED);
            }
        }
        CassettesDB.writeCassettes(CassettesDB.getCassettes());
        return new ResponseEntity<>("successfully");
    }
    public ResponseEntity<String> unblock (String key){
        MoneyType moneyType = MoneyType.findByCassette(key);
        if (Objects.isNull(moneyType)) return  new ResponseEntity<>("Not fount cassette", ResponseStatus.HTTP_NOT_FOUND);
        for (Cassette cassette : CassettesDB.getCassettes()) {
            if (cassette.getMoneyType().equals(moneyType)){
                cassette.setStatus(Status.ACTIVE);
            }
        }
        CassettesDB.writeCassettes(CassettesDB.getCassettes());
        return new ResponseEntity<>("successfully");
    }
    @Override
    public ResponseEntity<List<Cassette>> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<Cassette> get(String id) {
        return null;
    }
}
