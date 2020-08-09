package bartkisiel.github.io.HotelBooking.reservation;

public enum Step {
    Dates(0),
    Guests(1),
    Extras(2),
    Meals(3),
    Review(4),
    Payment(5);

    int flowStep;

    Step(int flowStep) {
        this.flowStep = flowStep;
    }

    public static Step from(int flowStep) {
        switch (flowStep) {
            default:
                return Dates;
            case 0:
                return Dates;
            case 1:
                return Guests;
            case 2:
                return Extras;
            case 3:
                return Meals;
            case 4:
                return Review;
            case 5:
                return Payment;
        }
    }
}
