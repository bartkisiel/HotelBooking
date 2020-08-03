package bartkisiel.github.io.HotelBooking.booking;

import bartkisiel.github.io.HotelBooking.hotel.Hotel;
import bartkisiel.github.io.HotelBooking.hotel.HotelRepository;
import bartkisiel.github.io.HotelBooking.locations.Address;
import bartkisiel.github.io.HotelBooking.locations.Country;
import bartkisiel.github.io.HotelBooking.locations.Postcode;
import bartkisiel.github.io.HotelBooking.room.Room;
import bartkisiel.github.io.HotelBooking.room.RoomPredicates;
import bartkisiel.github.io.HotelBooking.room.RoomRepository;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;

//static imports
import static bartkisiel.github.io.HotelBooking.MappedAssertion.mappedAssertion;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(HotelSearchingController.class)
@ActiveProfiles("test")
class HotelSearchingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HotelRepository hotelRepository;
    @MockBean
    private RoomRepository roomRepository;

    @Test
    @DisplayName("should not return any hotels with no location provided.")
    public void getHotels_with_noQueryParameters_returnEmptyPage() throws Exception {
        FeatureMatcher<Page<Hotel>, Long> result = mappedAssertion(Page::getTotalElements, Matchers.is(0L));

        mockMvc.perform(get("/booking/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("/booking/hotels"))
                .andExpect(model().attribute("hotels", result));

        //final verification
        verify(hotelRepository, times(1))
                .findAllByLocation(isNull(), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    @DisplayName("should return no hotels found for specific parameters.")
    public void getHotels_with_noHotelsFound() throws Exception {
        //given
        List<Hotel> hotelList = List.of();
        PageImpl<Hotel> result = new PageImpl<>(hotelList, PageRequest.of(0, 20), hotelList.size());

        //when
        when(hotelRepository.findAllByLocation(eq("Belgium"), isNull(), eq("33333"), any(Pageable.class)))
                //then
        .thenReturn(result);

        //check if page with right hotels appears
        FeatureMatcher<Page<Hotel>, List<Hotel>> hasExpectedHotelsPaged = mappedAssertion(Slice::getContent, Matchers.is(hotelList));

        mockMvc.perform(get("/booking/search?country=Belgium&postcode=33333"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("/booking/hotels"))
                .andExpect(model().attribute("hotels", hasExpectedHotelsPaged));

        //final verification
        verify(hotelRepository, times(1))
                .findAllByLocation(eq("Belgium"), isNull(), eq("33333"), any(Pageable.class));
    }

    @Test
    @DisplayName("should return matching hotels when valid query parameter provided.")
    public void getHotels_when_HotelsFound() throws Exception {
        //given
        Hotel hotel = new Hotel("Testini", new Address("Testini", "Valid 33/1", null, Country.Denmark, "Aarhus", new Postcode("33333")),
                3, "testini@gmail.com");
        //and
        List<Hotel> hotelList = List.of(hotel);
        PageImpl<Hotel> result = new PageImpl<>(hotelList, PageRequest.of(0,20), hotelList.size());

        //when
        when(hotelRepository.findAllByLocation(eq("Denmark"), isNull(), isNull(), any(Pageable.class)))
                //then
                .thenReturn(result);

        //check if page with right hotels appears
        FeatureMatcher<Page<Hotel>, List<Hotel>> hasExpectedHotelsPaged = mappedAssertion(Slice::getContent, Matchers.is(hotelList));

        mockMvc.perform(get("/booking/search?country=Denmark"))
                .andExpect(status().isOk())
                .andExpect(view().name("/booking/hotels"))
                .andExpect(model().attribute("hotels", hasExpectedHotelsPaged));

        //final verification
        verify(hotelRepository, times(1))
                .findAllByLocation(eq("Denmark"), isNull(), isNull(), any(Pageable.class));
    }

    @Test
    @DisplayName("should return 400 error for invalid hotel id format.")
    public void getHotels_when_invalidHotelIdProvided() throws Exception {
        mockMvc.perform(get("/booking/hotel/rooms"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 404 for rooms in non-existent hotel.")
    public void getAvailableRooms_for_hotelIdNotFound_Throws404() throws Exception {
        //given
        long hotelId = 4;
        //when + then
        mockMvc.perform(get(String.format("/hotel/%d/rooms", hotelId)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return available rooms for the hotel with correct id given.")
    public void getAvailableHotelRooms_in_hotelWithAvailableRooms() throws Exception {
        //given
        FeatureMatcher<Page<Hotel>, Long> hasExpectedPageResult = mappedAssertion(Page::getTotalElements, Matchers.is(1L));

        Address address = new Address("Xavier Hotel", "100 smith road", "",
                Country.Croatia, "Brisbane", new Postcode("33333"));
        Hotel hotel = new Hotel("Xavier Hotel", address, 4, "xavier@hotel.com");
        hotel.setId(3L);

        PageImpl<Room> page = new PageImpl<>(List.of(), PageRequest.of(0, 20), 1);

        //when
        when(roomRepository.findAll(RoomPredicates.availableRoom(hotel.getId()), PageRequest.of(0, 20))).thenReturn(page);
        when(hotelRepository.findById(hotel.getId())).thenReturn(Optional.of(hotel));

        //then
        mockMvc.perform(get(String.format("/hotel/%d/rooms", hotel.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("/hotel/rooms"))
                .andExpect(model().attribute("hotel", Matchers.isA(Hotel.class)))
                .andExpect(model().attribute("rooms", hasExpectedPageResult));

        verify(roomRepository, times(1))
                .findAll(eq(RoomPredicates.availableRoom(hotel.getId())), any(Pageable.class));

        verify(hotelRepository, times(1)).findById(eq(hotel.getId()));
    }

}