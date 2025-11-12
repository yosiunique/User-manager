package enat.bank.role.roleTypes;

import enat.bank.role.RoleTypes;
import enat.bank.utils.Common;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/role_types")
@RequiredArgsConstructor
public class RoleTypessController implements Common<RoleTypes,RoleTypes,RoleTypes,RoleTypes> {
    private final RoleTypesServices roleService;
    @Override
    public ResponseEntity<RoleTypes> create(@RequestBody  RoleTypes roleTypes) {
        return roleService.create(roleTypes);
    }

    @Override
    public ResponseEntity<RoleTypes> update(@PathVariable("id") Long id,@RequestBody RoleTypes roleTypes) {
        return roleService.update(roleTypes,id);
    }

    @Override
    public void delete(@PathVariable("id") Long id) {
 roleService.delete(id);
    }

    @Override
    public Optional<RoleTypes> getById(@PathVariable("id") Long id) {
        return roleService.getById(id);
    }

    @Override
    public Page<RoleTypes> getAllPageable(Pageable pageable, String name) {
        return roleService.getAllPageable(pageable);
    }
}
