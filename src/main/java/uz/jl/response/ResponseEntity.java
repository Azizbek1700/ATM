package uz.jl.response;

/**
 * @author Elmurodov Javohir, Mon 6:29 PM. 12/6/2021
 */

import lombok.Getter;
import lombok.Setter;

/**
 * @param <RD> -> Response Data
 */

@Getter
@Setter
public class ResponseEntity<RD> {
    private RD data;
    private Integer status;

    public ResponseEntity() {
        this.status = ResponseStatus.HTTP_OK.getCode();
    }

    public ResponseEntity(RD data, Integer code) {
        validateCode(code);
        this.data = data;
        this.status = code;
    }

    public ResponseEntity(RD data, ResponseStatus status) {
        this.data = data;
        this.status = status.getCode();
    }


    public ResponseEntity(RD data) {
        this.data = data;
        this.status = ResponseStatus.HTTP_OK.getCode();
    }

    private void validateCode(Integer code) {
        ResponseStatus status = ResponseStatus.getByCode(code);
        if (status.equals(ResponseStatus.UNDEFINED)) {
            throw new RuntimeException("Code is invalid");
        }
    }

}
