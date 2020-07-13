package bartkisiel.github.io.HotelBooking.features;

public enum DietPreference {
    Vegan("Vegan"),
    Vegetarian("Vegetarian"),
    GlutenIntolerant("Gluten Intolerant"),
    LactoseIntolerant("Lactose Intolerant"),
    Diabetics("Diabetics"),
    Raw("Raw"),
    Ketogenic("Ketogenic");

    private String description;

    DietPreference(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
