package spring.rest.crud.repository;

import spring.rest.crud.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailsRepository extends JpaRepository<Email, Integer> {
  Optional<Email> findByEmail(String email);

  Optional<Email> findById(UUID id);

  void deleteByEmail(String email);
}
