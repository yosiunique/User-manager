package enat.bank.Utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public abstract class CommonService<E, ID, D> {

    protected final CommonJpaRepo<E, ID> repository;

    protected CommonService(CommonJpaRepo<E, ID> repository) {
        this.repository = repository;
    }

    protected CommonService() {
    }

    public Page<E> getAllPageable(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public ResponseEntity<E> create(E entity) {
        E saved = repository.save(entity);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    public ResponseEntity<E> update(E entity, ID id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(entity);
        }
        repository.save(entity);
        return ResponseEntity.ok(entity);
    }

    public void delete(ID id) {
        repository.deleteById(id);
    }

    public Optional<E> getById(ID id) {
        return repository.findById(id);
    }
}
