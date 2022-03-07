package uz.jl.ui;

import jdk.jshell.execution.Util;
import uz.jl.response.ResponseEntity;

import static uz.jl.utils.Print.println;

/**
 * @author Botirov Najmiddin, Fri 15:35. 10/12/2021
 */
public class ShowMessageUI {
    private static ShowMessageUI showMessageUI;

    private ShowMessageUI(){}

    public static ShowMessageUI getInstance() {
        if ( showMessageUI == null )
            showMessageUI = new ShowMessageUI();

        return showMessageUI;
    }

    public void showMessage( ResponseEntity<String> obj) {
        println(obj.getData());
    }
}
