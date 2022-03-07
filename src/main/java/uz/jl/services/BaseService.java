package uz.jl.services;

import uz.jl.response.ResponseEntity;

public interface BaseService {
    ResponseEntity<String> create();
    ResponseEntity<String> block();
    ResponseEntity<String> unblock();
    ResponseEntity<String> delete();
    ResponseEntity<String> showList();
}
