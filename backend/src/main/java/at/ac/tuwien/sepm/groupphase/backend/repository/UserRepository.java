package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {


    /**
     * Fetches a user with the given email.
     *
     * @param email unique email of user to be fetched
     * @return unique user if it exists
     */
    ApplicationUser findUserByEmail(String email);

    /**
     * Saves a new User and the encapsuled Address entity to the database.
     * All repository ooperations are cascaded to the Address entity.
     *
     * @param user Entity to be saved
     * @return Saved User with the generated ids for the User and the persisted Address Entity
     */
    ApplicationUser save(ApplicationUser user);

    List<ApplicationUser> findByLockedAccountEquals(boolean lockedAccount);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ApplicationUser a set a.lockedAccount = ?1 where a.userId = ?2")
    void unlockApplicationUser(boolean lockedAccount, long userId);
}
