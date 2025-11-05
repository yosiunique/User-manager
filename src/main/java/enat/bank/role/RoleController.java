package enat.bank.role;

import enat.bank.Utils.Common;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
public class RoleController implements Common<Roles, Roles, Roles, Roles> {
    private final RoleService roleService;
    @Override
    public ResponseEntity<Roles> create(Roles roles) {
        return roleService.create(roles);
    }

    @Override
    public ResponseEntity<Roles> update(Long id, Roles roles) {
        return roleService.update(roleService.update(id,roles),id);
    }

    @Override
    public void delete(Long id) {
        roleService.delete(id);
    }

    @Override
    public Optional<Roles> getById(Long id) {
        return roleService.getById(id);
    }

    @Override
    public Page<Roles> getAllPageable(Pageable pageable, String name) {
        return roleService.getAllPageable(pageable);
    }
}
