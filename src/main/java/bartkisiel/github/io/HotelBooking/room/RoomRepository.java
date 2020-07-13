package bartkisiel.github.io.HotelBooking.room;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends PagingAndSortingRepository<Room, Integer>, QuerydslPredicateExecutor<Room> {

}
