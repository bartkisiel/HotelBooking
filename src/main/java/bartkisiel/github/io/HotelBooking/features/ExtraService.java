package bartkisiel.github.io.HotelBooking.features;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtraService {
    private ExtraRepository extraRepository;

    public ExtraService(ExtraRepository extraRepository) {
        this.extraRepository = extraRepository;
    }

    public List<Extra> getGeneralExtras(Extra.Type type) {
        return extraRepository.findAllByTypeAndCategory(type, Extra.Category.General);
    }

    public List<Extra> getFoodExtras(Extra.Type type) {
        return extraRepository.findAllByTypeAndCategory(type, Extra.Category.Food);
    }

    public List<Extra> getExtrasById(List<Long> ids) {
        List<Extra> result = new ArrayList<>();
        extraRepository.findAllById(ids).forEach(result::add);
        return result;
    }
}
