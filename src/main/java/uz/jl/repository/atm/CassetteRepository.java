package uz.jl.repository.atm;

import lombok.Getter;
import lombok.Setter;
import uz.jl.enums.Status;
import uz.jl.enums.money.MoneyType;
import uz.jl.models.atm.Atm;
import uz.jl.models.atm.Cassette;
import uz.jl.services.filesystems.AtmsDB;
import uz.jl.services.filesystems.CassettesDB;

/**
 * @author Botirov Najmiddin, Fri 12:16. 10/12/2021
 */
@Getter
@Setter

public class CassetteRepository {
    private static CassetteRepository cassetteRepository;

    public static CassetteRepository getInstance() {
        if (cassetteRepository == null) {
            cassetteRepository = new CassetteRepository();
        }
        return cassetteRepository;
    }



    public Cassette findCassetteFromAtmId(String id, MoneyType moneyType) {
        for ( Cassette cassette : CassettesDB.getCassettes() ) {
            if ( cassette.getMoneyType().equals(moneyType) && cassette.getAtmId().equals(id) && cassette.getStatus().equals(Status.ACTIVE) ) {
                return cassette;
            }
        }
        return null;
    }
}
