package enat.bank.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
