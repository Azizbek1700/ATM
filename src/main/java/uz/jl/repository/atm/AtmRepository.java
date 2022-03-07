package uz.jl.repository.atm;

import uz.jl.enums.Status;
import uz.jl.models.atm.Atm;
import uz.jl.repository.auth.AuthUserRepository;
import uz.jl.services.filesystems.AtmsDB;

/**
 * @author Botirov Najmiddin, Fri 11:32. 10/12/2021
 */
public class AtmRepository {
    private static AtmRepository authUserRepository;

    public static AtmRepository getInstance() {
        if (authUserRepository == null) {
            authUserRepository = new AtmRepository();
        }
        return authUserRepository;
    }

    public Atm findATMByName(String name) {
        for ( Atm atm : AtmsDB.getAtms() ) {
            if ( atm.getName().equals(name) ) {
                return atm;
            }
        }
        return null;
    }

    public Atm findATMByName(String name, Status status){
        for (Atm atm : AtmsDB.getAtms()) {
            if (atm.getName().equals(name) && !atm.getStatus().equals(status)) return atm;
        }
        return null;
    }
}
