package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ApplicationUser,Long> {
   ApplicationUser findUserByEmail(String eMail);
   ApplicationUser save(ApplicationUser user);
}