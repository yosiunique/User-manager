package enat.bank.role;

import enat.bank.utils.Common;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
@Tag(
        name = "Role Management",
        description = "APIs for managing user roles, including creating, updating, deleting, and listing roles."
)
public class RoleController implements Common<Roles, Roles, Roles, Roles> {

    private final RoleService roleService;

    @Override
    @Operation(
            summary = "Create new role",
            description = "Creates a new role in the system."
    )
    public ResponseEntity<Roles> create(@RequestBody Roles roles) {
        return roleService.create(roles);
    }

    @Override
    @Operation(
            summary = "Update existing role",
            description = "Updates role details using its ID."
    )
    public ResponseEntity<Roles> update(
            @PathVariable Long id,
            @RequestBody Roles roles) {
        return roleService.update(roles, id);
    }

    @Override
    @Operation(
            summary = "Delete role",
            description = "Deletes a role by its ID."
    )
    public void delete(@PathVariable Long id) {
        roleService.delete(id);
    }

    @Override
    @Operation(
            summary = "Get role by ID",
            description = "Fetches a specific role by its unique identifier."
    )
    public Optional<Roles> getById(@PathVariable Long id) {
        return roleService.getById(id);
    }

    @Override
    @Operation(
            summary = "List all roles",
            description = "Retrieves a paginated list of all roles in the system."
    )
    public Page<Roles> getAllPageable(Pageable pageable, @RequestParam(required = false) String name) {
        return roleService.getAllPageable(pageable);
    }
}
