package bartkisiel.github.io.HotelBooking.booking;

import bartkisiel.github.io.HotelBooking.hotel.Hotel;
import bartkisiel.github.io.HotelBooking.hotel.HotelRepository;
import bartkisiel.github.io.HotelBooking.room.Room;
import bartkisiel.github.io.HotelBooking.room.RoomPredicates;
import bartkisiel.github.io.HotelBooking.room.RoomRepository;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;


@Controller
public class HotelSearchingController {
    private static final Logger logger = LoggerFactory.getLogger(HotelSearchingController.class);
    private HotelRepository hotelRepository;
    private RoomRepository roomRepository;

    public HotelSearchingController(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping(value = "/booking/search")
    public String getHotels(@RequestParam(value = "country", required = false) String country,
                            @RequestParam(value = "city", required = false) String city,
                            @RequestParam(value = "postcode", required = false) String postcode,
                            Pageable pageable, Model model) {
        Page<Hotel> hotels = hotelRepository.findAllByLocation(country, city, postcode, pageable);
        model.addAttribute("hotels", hotels == null ? Page.empty() : hotels);
        logger.info("Exposing hotels for specific params: country, city, postcode.");
        return "/booking/hotels";
    }

    @GetMapping(value = "/booking/{hotel_id}/rooms")
    public String getRooms(@PathVariable("hotel_id") Long id, Pageable pageable, Model model) throws NotFoundException {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Page<Room> rooms = roomRepository.findAll(RoomPredicates.availableRoom(id), pageable);
        model.addAttribute("rooms", rooms);
        model.addAttribute("hotel", hotel);
        return "/booking/rooms";
    }


}
