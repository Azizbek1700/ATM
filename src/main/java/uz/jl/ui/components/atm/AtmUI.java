package uz.jl.ui.components.atm;

import uz.jl.dtos.atm.AtmDto;
import uz.jl.enums.Status;
import uz.jl.enums.money.MoneyType;
import uz.jl.models.atm.Atm;
import uz.jl.models.atm.Cassette;
import uz.jl.response.ResponseEntity;
import uz.jl.services.atm.AtmService;
import uz.jl.services.atm.CassetteService;
import uz.jl.services.filesystems.CassettesDB;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.List;
import java.util.Objects;


/**
 * @author Elmurodov Javohir, Wed 4:51 PM. 12/8/2021
 */
public class AtmUI extends BaseAbstractUI implements BaseUI {

    private static AtmUI atmUI;

    public static AtmUI getInstance() {
        if (Objects.isNull(atmUI)) {
            return atmUI = new AtmUI();
        }
        return atmUI;
    }

    private AtmUI() {
    }

    public void services() {

    }

    @Override
    public void create() {
        String atmName = Input.getStr("Enter ATM name: ");
        ResponseEntity<String> response = AtmService.getInstance().create(atmName);
        show(response);
    }

    @Override
    public void block() {
        if (showByStatus(Status.ACTIVE) == 1) {
            Print.println(Color.RED, "There is no active atm");
            return;
        }
        String name = Input.getStr("Enter ATM name: ");
        ResponseEntity<String> response = AtmService.getInstance().block(name);
        show(response);
    }

    @Override
    public void unblock() {
        if (showByStatus(Status.BLOCKED) == 1) {
            Print.println(Color.RED, "There is no blocked atm");
            return;
        }
        String name = Input.getStr("Enter ATM name: ");
        ResponseEntity<String> response = AtmService.getInstance().unblock(name);
        show(response);
    }

    @Override
    public void delete() {
        if (showIgnoreStatus(Status.DELETED) == 1) {
            Print.println(Color.RED, "There is no atm");
            return;
        }
        String name = Input.getStr("Enter ATM name: ");
        ResponseEntity<String> response = AtmService.getInstance().delete(name);
        show(response);
    }

    @Override
    public void update() {
        // TODO: 12/11/2021 show atms Doston
        String name = Input.getStr("Choice atm name: ");
        ResponseEntity<Atm> atmResponse = AtmService.getInstance().findAtm(name);
        if (Objects.isNull(atmResponse.getData())) {
            Print.println("Atm not found");
            return;
        }

        boolean process = true;
        while (process) {
            // TODO: 12/11/2021 show cassettes in atm Doston
            String choice = Input.getStr("Do you want to stop the process? (y/n) : ");
            if (choice.toLowerCase().startsWith("n")) {
                CassettesDB.writeCassettes(CassettesDB.getCassettes());
                process = false;
                continue;
            }
            String type = Input.getStr("Enter Cassette type: ");
            MoneyType moneyType = MoneyType.findByCassette((type));
            if (Objects.isNull(moneyType)) {
                Print.println(Color.RED, "Wrong cassette type");
                continue;
            }
            Cassette cassette = CassetteService.getInstance().findCassetteFromType(atmResponse.getData(), moneyType);
            if (Objects.isNull(cassette)) {
                Print.println(Color.RED, "Something went wrong");
                continue;
            }
            if (cassette.getCurrencyCount() == 10000) {
                Print.println(Color.RED, "This cassette is already full");
                continue;
            }
            Print.println(Color.BLUE, "Current amount of money: " + cassette.getCurrencyCount());
            String str = Input.getStr("Enter the amount of money to add: ");
//            if (!Utils.getInstance().isOnlyDigits(str)) {
//                Print.println(Color.RED, "Invalid amount of money");
//                continue;
//            }
            int amount = Integer.parseInt(str);
            if (amount + cassette.getCurrencyCount() > 10000) {
                Print.println(Color.RED, "The cassette capacity accepts 10,000 coins");
                continue;
            }
            cassette.setCurrencyCount(cassette.getCurrencyCount() + amount);
            Print.println(Color.BLUE, "This cassette is successfully replenished");
        }
    }

    @Override
    public void list() {
        int index = 1;
        ResponseEntity<List<AtmDto>> response = AtmService.getInstance().showIgnoreStatus(Status.DELETED);
        if (response.getData().isEmpty()) {
            Print.println(Color.RED, "There is no Atm");
            return;
        }
        for (AtmDto atmDto : response.getData()) {
            if (atmDto.getStatus().equals(Status.ACTIVE)) {
                Print.println(Color.BLUE, index++ + ". " + atmDto.getName());
            } else if (atmDto.getStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". " + atmDto.getName());
            }
        }
    }

    public int showByStatus(Status status) {
        int index = 1;
        ResponseEntity<List<AtmDto>> response = AtmService.getInstance().showByStatus(status);
        if (response.getData().isEmpty()) return index;
        for (AtmDto atmDto : response.getData()) {
            Print.println(index++ + ". " + Color.BLUE, atmDto.getName());
        }
        return index;
    }

    public int showIgnoreStatus(Status status) {
        int index = 1;
        ResponseEntity<List<AtmDto>> response = AtmService.getInstance().showIgnoreStatus(status);
        if (response.getData().isEmpty()) return index;
        for (AtmDto atmDto : response.getData()) {
            if (atmDto.getStatus().equals(Status.ACTIVE)) {
                Print.println(Color.GREEN, index++ + ". " + atmDto.getName());
            } else if (atmDto.getStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". " + atmDto.getName());
            }
        }
        return index;
    }

    public void loginForAtm() {
        String atmName = Input.getStr("Enter atm name: ");
        ResponseEntity<String> response = AtmService.getInstance().loginForATM(atmName);
        show(response);
    }
}
