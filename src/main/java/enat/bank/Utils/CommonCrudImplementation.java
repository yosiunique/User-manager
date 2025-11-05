package enat.bank.Utils;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


public class CommonCrudImplementation <E,D,O,Dto> implements Common<E,D,O,Dto>  {
    @Override
    public ResponseEntity<E> create(E e) {
        return null;
    }

    @Override
    public ResponseEntity<E> update(@PathVariable("id") Long id , @RequestBody E e) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<Dto> getById(Long id) {
        return null;
    }

    @Override
    public Page<Dto> getAllPageable(@PageableDefault  Pageable pageable, @RequestParam(name="name" ,required = false) String name) {
        return null;
    }
}
