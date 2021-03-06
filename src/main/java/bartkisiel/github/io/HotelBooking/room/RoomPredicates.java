package bartkisiel.github.io.HotelBooking.room;
import bartkisiel.github.io.HotelBooking.room.QRoom;
import com.querydsl.core.types.Predicate;

public final class RoomPredicates {

    private static final QRoom room = QRoom.room;

    private RoomPredicates() {

    }

    public static Predicate availableRoom(long hotelId) {
        return room.hotel.id.eq(hotelId).and(room.reservation.isNull());
    }
}
