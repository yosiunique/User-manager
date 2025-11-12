package enat.bank.utils;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonJpaRepo<Entity,id> extends JpaRepository<Entity,id> {

}
