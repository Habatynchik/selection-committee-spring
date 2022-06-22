package ua.epan.elearn.selection.committee.spring.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;
import ua.epan.elearn.selection.committee.spring.model.entity.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u where u.role.roleEnum = 'CLIENT'")
    List<User> findAllByRole();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
