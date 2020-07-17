package bartkisiel.github.io.HotelBooking.room;

import bartkisiel.github.io.HotelBooking.hotel.Hotel;
import bartkisiel.github.io.HotelBooking.reservation.Reservation;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Hotel hotel;
    @NaturalId //DO NOT provide setter method on field with @NaturalId.
    @Column(nullable = false, unique = true)
    private String roomNumber;
    @Column(nullable = false, name = "room_type")
    @Enumerated(EnumType.STRING)
    private RoomType roomtype;
    @Column(nullable = false)
    private int beds;
    @Column(nullable = false)
    private BigDecimal pricePerNight;
    @OneToOne(cascade = CascadeType.ALL)
    private Reservation reservation;

    public Room(String roomNumber, RoomType roomtype, int beds, BigDecimal pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomtype = roomtype;
        this.beds = beds;
        this.pricePerNight = pricePerNight;
    }

    /*
     * empty constructor for Hibernate.
     */
    public Room() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) { this.hotel = hotel; }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(RoomType roomtype) {
        this.roomtype = roomtype;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        if(reservation != null){
            this.reservation = reservation;
            reservation.setRoom(this);
        }
    }

    public boolean isAvailable() {
        return reservation != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id &&
                beds == room.beds &&
                Objects.equals(hotel, room.hotel) &&
                Objects.equals(roomNumber, room.roomNumber) &&
                roomtype == room.roomtype &&
                Objects.equals(pricePerNight, room.pricePerNight) &&
                Objects.equals(reservation, room.reservation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                '}';
    }

}
