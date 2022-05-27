package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShowRepository extends JpaRepository<Show, Long> {

    Show getByShowId(long showId);

    /**
     * Searches for shows according to the parameters. The search is case-insensitive. The params
     * are connected with the AND operator.
     *
     * @param dateTime    check if the date (date + hours + minutes) is equal to the ones stored
     * @param hour        hours of the date to check for
     * @param minutes     minutes of the date to check for
     * @param eventName   name of the event to search for if the stored entries contain it
     * @param price       searches for shows with a price <= stored price
     * @param seatingPlan id of the seating plan to search for
     * @param pageable    contains information about the page
     * @return page of shows
     */
    @Query(
        "select distinct s from Show s join SectorPrice sp on sp.show.showId = s.showId join Sector"
            + " sec on sec.sectorId = sp.sector.sectorId join SeatingPlan seatP on seatP.seatingPlanId = sec.seatingPlan.seatingPlanId "
            + "where "
            + "((:name is null) or (upper(s.event.name) like upper(concat('%', :name, '%')))) and "
            + "((:date is null and :hour <> 0 and :minute <> 0) or "
            + "((function('HOUR', s.date) = :hour) and function('MINUTE', s.date) = :minute and "
            + "year(s.date) = year(:date) and month(s.date) = month (:date) and day(s.date) = day(:date))) and"
            + "((:date is null and :hour = 0 and :minute = 0) or (year(s.date) = year(:date) and month(s.date) = month (:date) and "
            + "day(s.date) = day(:date))) and"
            + "((:price is null) or (sp.price <= :price)) and "
            + "((:seating is null) or (seatP.seatingPlanId = :seating))")
    Page<Show> search(@Param("date") OffsetDateTime dateTime, @Param("hour") Integer hour,
        @Param("minute") Integer minutes, @Param("name") String eventName,
        @Param("price") BigDecimal price, @Param("seating") Long seatingPlan, Pageable pageable);


}
