package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    /**
     * Searches for locations according to params. The search is case-insensitive and looks if the
     * search params start with values stored in the database except for the name. Search for name
     * looks if the stored names contain the param name. The params are connected with the operator
     * AND except for one case. This case checks if name and city param are both not null. If true
     * it searches for location in the city even if the name of the location is wrong.
     *
     * @param name     searches for location which start with the name
     * @param street   searches for locations which start with the street name
     * @param city     searches for locations which start with the city
     * @param zipCode  searches for locations which start with the zip-code
     * @param country  searches for location which start with the country
     * @param pageable contains information about the page
     * @return a page of locations
     */
    @Query("""
        select distinct l from Location l
        where
        (((:name is null) or (upper(l.name) like upper(concat('%', :name, '%')))) and
        ((:city is null) or (upper(l.address.city) like upper(concat(:city, '%'))))and
        ((:country is null) or (upper(l.address.country) like upper(concat(:country, '%')))) and
        ((:street is null) or (upper(l.address.street) like upper(concat(:street, '%')))) and
        ((:zipCode is null) or (upper(l.address.zipCode) = upper(:zipCode)))) or
        (:city is not null and :name is not null and :country is null and :street is null and :zipCode is null and
        not exists (select s from Location s where (upper(s.name) like upper(concat('%',:name, '%'))) and (upper(s.address.city) like upper(concat(:city, '%')))) and (upper(l.address.city) like upper(concat(:city, '%'))))""")
    Page<Location> search(@Param("name") String name, @Param("city") String city,
        @Param("country") String country, @Param("street") String street,
        @Param("zipCode") String zipCode, Pageable pageable);

}
