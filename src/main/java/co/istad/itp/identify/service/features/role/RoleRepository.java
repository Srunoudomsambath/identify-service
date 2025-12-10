package co.istad.itp.identify.service.features.role;

import co.istad.itp.identify.service.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long>{
    Role findByName(String name);
}
