package uz.jl.configs;

import lombok.Getter;
import lombok.Setter;
import uz.jl.models.atm.Atm;
import uz.jl.models.atm.Cassette;
import uz.jl.models.auth.AuthUser;

/**
 * @author Botirov Najmiddin, Fri 18:23. 10/12/2021
 */
public class AtmSession {

    @Getter
    @Setter
    private Atm sessionAtm;

    private static AtmSession atmSession;

    private AtmSession(){}

    public static AtmSession getInstance() {
        if ( atmSession == null )
            atmSession = new AtmSession();
        return atmSession;
    }


}
