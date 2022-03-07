package uz.jl.ui.components.atm;

import uz.jl.dtos.cassette.CassetteDto;
import uz.jl.enums.Status;
import uz.jl.enums.money.MoneyType;
import uz.jl.models.atm.Cassette;
import uz.jl.response.ResponseEntity;
import uz.jl.services.atm.CassetteService;
import uz.jl.services.filesystems.CassettesDB;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static uz.jl.utils.Input.getStr;
import static uz.jl.utils.Print.print;
import static uz.jl.utils.Print.println;

/**
 * @author Botirov Najmiddin, Fri 12:26. 10/12/2021
 */
public class CassetteUI extends BaseAbstractUI implements BaseUI {

    private static CassetteUI cassetteUi;

    public static CassetteUI getInstance() {
        if (Objects.isNull(cassetteUi)) {
            return cassetteUi = new CassetteUI();
        }
        return cassetteUi;
    }

    public String createCassette() {
        MoneyType.showMoneyType();
        return getStr("Enter money type: ");
    }

    public String getAmountMoney() {
        print(Color.BLUE, "Amount money: ");
        return new Scanner(System.in).nextLine();
    }


    @Override
    public void create() {

    }


    @Override
    public void block() {
//        if (showByStatus(Status.ACTIVE) == 1) {
//            Print.println(Color.RED, "There is no active cassette");
//            return;
//        }
        showUnblock(Status.ACTIVE);
        String key = Input.getStr("?-> ");
        ResponseEntity<String> response = CassetteService.getInstance().block(key);
        show(response);
    }

    @Override
    public void unblock() {
        if (showByStatus(Status.BLOCKED) == 1) {
            Print.println(Color.RED, "There is no blocked cassette");
            return;
        }
        String key = Input.getStr("?-> ");
        ResponseEntity<String> response = CassetteService.getInstance().unblock(key);
        show(response);

    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public void list() {
        int index = 1;
        ResponseEntity<List<CassetteDto>> response = CassetteService.getInstance().showIgnoreStatus(Status.DELETED);
        if (response.getData().isEmpty()) {
            Print.println(Color.RED, "There is no cassette");
            return;
        }
        for (CassetteDto cassetteDto : response.getData()) {
            if (cassetteDto.getStatus().equals(Status.ACTIVE)) {
                println(Color.BLUE, index++ + ". " + cassetteDto.getMoneyType() + " | " + cassetteDto.getCurrencyCount());
            } else if (cassetteDto.getStatus().equals(Status.BLOCKED)) {
                println(Color.PURPLE, index++ + ". " + cassetteDto.getMoneyType() + " | " + cassetteDto.getCurrencyCount());
            }
        }
    }

    public int showUnblock(Status status){

        for (MoneyType value : MoneyType.values()) {
            System.out.println(value.toString());
        }
       return 0;
    }
    public int showByStatus(Status status) {
        int index = 1;
        ResponseEntity<List<CassetteDto>> response = CassetteService.getInstance().showByStatus(status);
        if (response.getData().isEmpty()) return index;
        for (CassetteDto cassetteDto : response.getData()) {
            println(Color.BLUE, index++ + ". " + cassetteDto.getMoneyType() + " | " + cassetteDto.getCurrencyCount());
        }
        return index;
    }


    public int showIgnoreStatus(Status status) {
        int index = 1;
        ResponseEntity<List<CassetteDto>> response = CassetteService.getInstance().showIgnoreStatus(status);
        if (response.getData().isEmpty()) return index;
        for (CassetteDto cassetteDto : response.getData()) {
            println(Color.BLUE, index++ + ". " + cassetteDto.getMoneyType() + " | " + cassetteDto.getCurrencyCount());
        }
        return index;
    }
}
