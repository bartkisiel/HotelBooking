package bartkisiel.github.io.HotelBooking.features;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtraRepository extends CrudRepository<Extra, Long> {
    List<Extra> findAllByTypeAndCategory(Extra.Type type, Extra.Category category);
}
