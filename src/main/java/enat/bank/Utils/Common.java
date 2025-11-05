package enat.bank.Utils;

import enat.bank.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface  Common<E,D,O,Dto> {
     @PostMapping
     ResponseEntity<E> create(@RequestBody  E e) ;
     @PutMapping("/{id}")
     ResponseEntity<E> update(@PathVariable("id") Long id ,@RequestBody E e);
     @DeleteMapping("/{id}")
     void delete(@PathVariable("id") Long id);
     @GetMapping("/{id}")
     Optional<Dto> getById(@PathVariable("id") Long id);
     @GetMapping
     Page<Dto> getAllPageable(@PageableDefault Pageable pageable,@RequestParam(name="name" ,required = false)String name);
}
