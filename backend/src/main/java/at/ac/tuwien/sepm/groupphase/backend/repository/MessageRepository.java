package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Find all message entries ordered by published at date (descending).
     *
     * @return ordered list of all message entries
     */
    List<Message> findAllByOrderByPublishedAtDesc();
}
