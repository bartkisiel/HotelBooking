package bartkisiel.github.io.HotelBooking.hotel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long>, QuerydslPredicateExecutor <Hotel> {

    @Query("select h from Hotel h " +
            "where upper(h.address.country) = coalesce(upper(:country), upper(h.address.country)) " +
            "and upper(h.address.city) = coalesce(upper(:city), upper(h.address.city)) " +
            "and h.address.postcode.code = coalesce(:postcode, h.address.postcode.code)"
    )
    Page<Hotel> findAllByLocation(@Param("country") String country,
                                  @Param("city") String city,
                                  @Param("postcode") String postcode,
                                  Pageable pageable);

    Page<Hotel> findAll(Pageable pageable);
}
