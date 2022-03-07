package uz.jl;

import uz.jl.configs.CardSession;
import uz.jl.configs.Session;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.response.ResponseEntity;
import uz.jl.response.ResponseStatus;
import uz.jl.services.card.CardService;
import uz.jl.ui.AuthUI;
import uz.jl.ui.MenuUI;
import uz.jl.ui.components.admin.AdminUI;
import uz.jl.ui.components.atm.AtmUI;
import uz.jl.ui.components.atm.CassetteUI;
import uz.jl.ui.components.card.CardUI;
import uz.jl.ui.components.client.ClientUI;
import uz.jl.ui.components.employee.EmployeeUI;
import uz.jl.ui.components.hr.HrUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.Objects;

/**
 * @author Elmurodov Javohir, Mon 6->14 PM. 11/29/2021
 */
public class App {
    static AdminUI adminUI;
    static AtmUI atmUI;
    static AuthUI authUI;
    static ClientUI clientUI;
    static EmployeeUI employeeUI;
    static HrUI hrUI;

    static {
        adminUI = AdminUI.getInstance();
        atmUI = AtmUI.getInstance();
        authUI = AuthUI.getInstance();
        clientUI = ClientUI.getInstance();
        employeeUI = EmployeeUI.getInstance();
        hrUI = HrUI.getInstance();
    }

    public static void main(String[] args) {
        run(args);

    }

    private static void run(String[] args) {
        MenuUI.show();

        String choice = Input.getStr("?->");
        if (Session.getInstance().getUser().getRole().equals(Role.ANONYMOUS) && Objects.isNull(CardSession.getInstance().getSessionCard())) {
            if (choice.equals("1")) {
                AuthUI.getInstance().login();
            } else if (choice.equals("2") && Objects.isNull(CardSession.getInstance().getSessionCard())) {
                if (AtmUI.getInstance().showIgnoreStatus(Status.DELETED) == 1) {
                    Print.println(Color.RED, "There is no atm");
                    return;
                }
                AtmUI.getInstance().loginForAtm();
            }
        } else if (Session.getInstance().getUser().getRole().equals(Role.ANONYMOUS) && Objects.nonNull(CardSession.getInstance().getSessionCard())) {
            switch (choice) {
                case "1" -> CardService.getInstance().withDrawMoneyForAtm();
                case "2" -> CardService.getInstance().replenishCard();
                case "3" -> CardUI.getInstance().block();
                case "4" -> AuthUI.getInstance().changeLanguage();
                case "5" -> AuthUI.getInstance().logout();
            }

        } else if (Session.getInstance().getUser().getRole().equals(Role.ADMIN)) {
            switch (choice) {
                case "1" -> HrUI.getInstance().create();
                case "2" -> HrUI.getInstance().delete();
                case "3" -> HrUI.getInstance().block();
                case "4" -> HrUI.getInstance().unblock();
                case "5" -> HrUI.getInstance().list();

                case "6" -> AtmUI.getInstance().create();
                case "7" -> AtmUI.getInstance().delete();
                case "8" -> AtmUI.getInstance().block();
                case "9" -> AtmUI.getInstance().unblock();
                case "10" -> AtmUI.getInstance().list();

                case "11" -> CardUI.getInstance().create();
                case "12" -> CardUI.getInstance().delete();
                case "13" -> CardUI.getInstance().block();
                case "14" -> CardUI.getInstance().unblock();
                case "15" -> CardUI.getInstance().list();

                case "16" -> AuthUI.getInstance().changeLanguage();
                case "17" -> AuthUI.getInstance().logout();
                default -> {
                    ResponseEntity<String> response = new ResponseEntity<>("Bad credentials!", ResponseStatus.HTTP_FORBIDDEN);
                    Print.println(Color.RED, response.getData());
                }
            }
        } else if (Session.getInstance().getUser().getRole().equals(Role.SUPER_ADMIN)) {
            switch (choice) {
                case "1" -> AtmUI.getInstance().create();
                case "2" -> AtmUI.getInstance().delete();
                case "3" -> AtmUI.getInstance().block();
                case "4" -> AtmUI.getInstance().unblock();
                case "5" -> AtmUI.getInstance().list();

                case "6" -> AdminUI.getInstance().create();
                case "7" -> AdminUI.getInstance().delete();
                case "8" -> AdminUI.getInstance().block();
                case "9" -> AdminUI.getInstance().unblock();
                case "10" -> AdminUI.getInstance().list();

                case "11" -> AuthUI.getInstance().changeLanguage();
                case "12" -> AuthUI.getInstance().logout();
                default -> {
                    ResponseEntity<String> response = new ResponseEntity<>("Bad credentials!", ResponseStatus.HTTP_FORBIDDEN);
                    Print.println(Color.RED, response.getData());
                }
            }
        } else if (Session.getInstance().getUser().getRole().equals(Role.HR)) {
            switch (choice) {
                case "1" -> employeeUI.create();
                case "2" -> employeeUI.delete();
                case "3" -> employeeUI.block();
                case "4" -> employeeUI.unblock();
                case "5" -> employeeUI.list();

                case "6" -> AuthUI.getInstance().changeLanguage();
                case "7" -> AuthUI.getInstance().logout();
                default -> {
                    ResponseEntity<String> response = new ResponseEntity<>("Bad credentials!", ResponseStatus.HTTP_FORBIDDEN);
                    Print.println(Color.RED, response.getData());
                }
            }
        } else if (Session.getInstance().getUser().getRole().equals(Role.CLIENT)) {
            if ("1".equals(choice)) {
                ClientUI.getInstance().orderCard();
            } else {
                ResponseEntity<String> response = new ResponseEntity<>("Bad credentials!", ResponseStatus.HTTP_FORBIDDEN);
                Print.println(Color.RED, response.getData());
            }
        } else if (Session.getInstance().getUser().getRole().equals(Role.EMPLOYEE)) {
            switch (choice) {
                case "1" -> ClientUI.getInstance().create();
                case "2" -> ClientUI.getInstance().delete();
                case "3" -> ClientUI.getInstance().block();
                case "4" -> ClientUI.getInstance().unblock();
                case "5" -> ClientUI.getInstance().list();

                case "6" -> ClientUI.getInstance().orderCard();
                case "7" -> AtmUI.getInstance().services();
                case "8" -> CassetteUI.getInstance().block();
                case "9" -> CassetteUI.getInstance().unblock();

                case "10" -> AuthUI.getInstance().changeLanguage();
                case "11" -> AuthUI.getInstance().logout();
                case "12" -> AuthUI.getInstance().profile();
                default -> {
                    ResponseEntity<String> response = new ResponseEntity<>("Bad credentials!", ResponseStatus.HTTP_FORBIDDEN);
                    Print.println(Color.RED, response.getData());
                }
            }
        }
        if (choice.startsWith("q")) {
            AuthUI.getInstance().quit();
            return;
        }
        main(args);
    }

}
