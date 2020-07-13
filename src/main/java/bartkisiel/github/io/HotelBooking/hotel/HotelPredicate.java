package bartkisiel.github.io.HotelBooking.hotel;

import bartkisiel.github.io.HotelBooking.WhereCauseBuilder;
import bartkisiel.github.io.HotelBooking.locations.QAdress;
import com.querydsl.core.types.Predicate;

public final class HotelPredicate {
    private static final QHotel hotel = QHotel.hotel;

    /*
     * empty constructor for Hibernate.
     */
    private HotelPredicate() {
    }

    public static Predicate byLocation(String country, String city, String postcode) {
        QAdress adress = hotel.adress;

        return new WhereCauseBuilder()
                .andNullable(country, () -> adress.country.stringValue().equalsIgnoreCase(country))
                .andNullable(city, () -> adress.city.equalsIgnoreCase(city))
                .andNullable(postcode, () -> adress.postcode.code.eq(postcode));
    }
}
