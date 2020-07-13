package bartkisiel.github.io.HotelBooking.reservation;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Embeddable
public class ReservationDates {
    @Column(nullable = false)
    @NotBlank(message = "Check in date must not be empty!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkInDate;
    @Column(nullable = false)
    @NotBlank(message = "Check out date must not be empty!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkOutDate;
    @Column(nullable = false)
    @NotBlank(message = "Possible check in time must not be empty!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime possibleCheckInTime;
    @Column(nullable = false)
    private boolean delayedCheckOutTime = false;
    @Column(nullable = false)
    @AssertTrue(message = "Please check the acknowledgement of the policy.")
    private boolean policy = false;

    /*
     * empty constructor for Hibernate.
     */
    public ReservationDates() {

    }

    public ReservationDates(LocalDate checkInDate, LocalDate checkOutDate,
                            LocalTime possibleCheckInTime, boolean delayedCheckInTime, boolean policy) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.possibleCheckInTime = possibleCheckInTime;
        this.delayedCheckOutTime = delayedCheckOutTime;
        this.policy = policy;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public LocalTime getPossibleCheckInTime() {
        return possibleCheckInTime;
    }

    public void setPossibleCheckInTime(LocalTime possibleCheckInTime) {
        this.possibleCheckInTime = possibleCheckInTime;
    }

    public boolean isDelayedCheckOutTime() {
        return delayedCheckOutTime;
    }

    public void setDelayedCheckOutTime(boolean delayedCheckOutTime) {
        this.delayedCheckOutTime = delayedCheckOutTime;
    }

    public boolean isDelayed(boolean delayedCheckOutTime) {
        return delayedCheckOutTime;
    }

    public boolean isPolicy() {
        return policy;
    }

    public void setPolicy(boolean policy) {
        this.policy = policy;
    }

    public int totalDays() {
        if(checkInDate == null || checkOutDate == null) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public Optional<ReservationValidationError> validation(LocalDate now) {
        if(checkInDate == null) {
            return Optional.of(new ReservationValidationError("checkInDate.missing", "Check in date not found."));
        } else if (checkOutDate == null) {
            return Optional.of(new ReservationValidationError("checkOutDate.missing", "Check out date not found."));
        } else if (checkInDate.isBefore(now)) {
            return Optional.of(new ReservationValidationError("checkInDate.past", "Check in date is in the past."));
        } else if (checkOutDate.isBefore(checkInDate)) {
            return Optional.of(new ReservationValidationError("checkOutDate.beforeCheckIn", "Check out date is before" +
                    " the date of check in."));
        } else if(totalDays() < 1 ) {
            return Optional.of(new ReservationValidationError("checkOutDate.minNights", "Reservation must be made for at" +
                    " least one night."));
        }
        return Optional.empty();
    }

    public static class ReservationValidationError {
        private String code;
        private String reason;

        public ReservationValidationError(String code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        public String getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }
    }

    @Override
    public String toString() {
        return "ReservationDates{" +
                "checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", possibleCheckInTime=" + possibleCheckInTime +
                ", delayedCheckOutTime=" + delayedCheckOutTime +
                ", policy=" + policy +
                '}';
    }

}
