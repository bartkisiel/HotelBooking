package bartkisiel.github.io.HotelBooking.hotel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HotelController {
    private final HotelRepository repository;

    public HotelController(HotelRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/hotels")
    public String showHotels(Pageable pageable, Model model) {
        Page<Hotel> result = repository.findAll(pageable);
        model.addAttribute("hotels", result);
        return "/booking/hotels";
    }

}
