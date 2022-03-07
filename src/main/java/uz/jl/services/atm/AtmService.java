package uz.jl.services.atm;

import lombok.Getter;
import lombok.Setter;
import uz.jl.configs.AtmSession;
import uz.jl.dtos.atm.AtmDto;
import uz.jl.enums.Status;
import uz.jl.mapper.atm.AtmMapper;
import uz.jl.models.atm.Atm;
import uz.jl.repository.atm.AtmRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.response.ResponseStatus;
import uz.jl.services.BaseAbstractService;
import uz.jl.services.filesystems.AtmsDB;
import uz.jl.ui.components.card.CardUI;
import uz.jl.utils.Input;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Botirov Najmiddin, Fri 11:46. 10/12/2021
 */
@Getter
@Setter
public class AtmService extends BaseAbstractService<Atm, AtmRepository, AtmMapper> {

    private static AtmService atmService;

    private AtmService(AtmRepository repository, AtmMapper mapper) {
        super(repository, mapper);
    }


    public static AtmService getInstance() {
        if (atmService == null)
            atmService = new AtmService(AtmRepository.getInstance(), AtmMapper.getInstance());
        return atmService;
    }

    public ResponseEntity<String> create(String name) {
        if (Objects.nonNull(repository.findATMByName(name))) {
            return new ResponseEntity<>("ATM is already taken", ResponseStatus.HTTP_FORBIDDEN);
        }
        Atm atm = new Atm();
        atm.setName(name);
        AtmsDB.getAtms().add(atm);
        AtmsDB.writeAtms(AtmsDB.getAtms());
        CassetteService.createCassette(atm);
        return new ResponseEntity<>("Successfully create ATM");

    }

    public ResponseEntity<String> block(String name) {
        Atm atm = repository.findATMByName(name);
        if (Objects.isNull(atm)) return new ResponseEntity<>("Atm not fount", ResponseStatus.HTTP_NOT_FOUND);
        if (atm.getStatus().equals(Status.BLOCKED))
            return new ResponseEntity<>("Atm is already blocked", ResponseStatus.HTTP_FORBIDDEN);

        atm.setStatus(Status.BLOCKED);
        atm.setUpdatedAt(new Date());
        return new ResponseEntity<>("Atm is successfully blocked ");
    }

    public ResponseEntity<String> unblock(String name) {
        Atm atm = repository.findATMByName(name);
        if (Objects.isNull(atm)) return new ResponseEntity<>("Atm not fount", ResponseStatus.HTTP_NOT_FOUND);
        if (atm.getStatus().equals(Status.ACTIVE))
            return new ResponseEntity<>("Atm is already unblocked", ResponseStatus.HTTP_FORBIDDEN);

        atm.setStatus(Status.ACTIVE);
        atm.setUpdatedAt(new Date());
        return new ResponseEntity<>("Atm is successfully unblocked");
    }

    public ResponseEntity<String> delete(String name) {
        Atm atm = repository.findATMByName(name, Status.DELETED);
        if (Objects.isNull(atm)) return new ResponseEntity<>("Atm not fount ", ResponseStatus.HTTP_NOT_FOUND);
        if (atm.getStatus().equals(Status.DELETED))
            return new ResponseEntity<>("Atm is already deleted", ResponseStatus.HTTP_FORBIDDEN);

        atm.setStatus(Status.DELETED);
        atm.setUpdatedAt(new Date());
        return new ResponseEntity<>("Atm successfully deleted");
    }



    public ResponseEntity<List<AtmDto>> showIgnoreStatus(Status status) {
        List<AtmDto> atmDtoList = new ArrayList<>();
        for (Atm atm : AtmsDB.getAtms()) {
            if (!atm.getStatus().equals(status)) {
                AtmDto atmDto = new AtmDto();
                atmDto.setName(atm.getName());
                atmDto.setStatus(atm.getStatus());
                atmDtoList.add(atmDto);
            }
        }
        return new ResponseEntity<>(atmDtoList);
    }

    public ResponseEntity<List<AtmDto>> showByStatus (Status status) {
        List<AtmDto> atmDtoList = new ArrayList<>();
        for (Atm atm : AtmsDB.getAtms()) {
            if (atm.getStatus().equals(status)) {
                AtmDto atmDto = new AtmDto();
                atmDto.setName(atm.getName());
                atmDtoList.add(atmDto);
            }
        }
        return new ResponseEntity<>(atmDtoList);
    }

    public ResponseEntity<String> loginForATM(String selectionAtm) {
        Atm atm = AtmRepository.getInstance().findATMByName(selectionAtm, Status.BLOCKED);
        if ( Objects.isNull(atm) ) {
            return new ResponseEntity<>("Atm not found!!!", ResponseStatus.HTTP_FORBIDDEN);
        }

        if ( atm.getStatus().equals(Status.BLOCKED) ) {
            return new ResponseEntity<>("This ATM blocked!!!", ResponseStatus.HTTP_FORBIDDEN);
        }
        AtmSession.getInstance().setSessionAtm(atm);
        return CardUI.getInstance().login();
    }

    public ResponseEntity<String> update(String name) {
        Atm atm = AtmRepository.getInstance().findATMByName(name, Status.BLOCKED);
        if ( Objects.isNull(atm) ) {
            return new ResponseEntity<>("Atm not found", ResponseStatus.HTTP_FORBIDDEN);
        }
        return new ResponseEntity<>("" );
    }

    @Override
    public ResponseEntity<List<Atm>> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<Atm> get(String id) {
        return null;
    }

    public ResponseEntity<Atm> findAtm(String name) {
        for (Atm atm : AtmsDB.getAtms()) {
            if (atm.getName().equals(name)) {
                return new ResponseEntity<>(atm);
            }
        }
        return new ResponseEntity<>();
    }
}
