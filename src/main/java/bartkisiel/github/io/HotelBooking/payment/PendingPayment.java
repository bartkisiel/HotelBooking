package bartkisiel.github.io.HotelBooking.payment;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PendingPayment {
    @NotBlank(message = "Credit card type has to be chosen.")
    private CreditCardType creditCardType;
    @Pattern(regexp = "[0-9]{10}", message = "Credit card number has to be valid length.")
    @NotBlank
    private String creditCardNumber;
    @Pattern(regexp = "[0-9]{10}", message = "CCV number has to be valid length.")
    private String cvvNumber;
    @NotBlank
    private String cardHolderName;
    @NotBlank(message = "Card expiry date must not be empty.")
    private Year cardExpirationYear;
    @NotBlank(message = "Card expiry date must not be empty.")
    private Month cardExpirationMonth;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    public enum CreditCardType {
        MasterCard("Mastercard"),
        Visa("Visa");

        private String description;

        CreditCardType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public PendingPayment(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCcvNumber() {
        return cvvNumber;
    }

    public void setCcvNumber(String ccvNumber) {
        this.cvvNumber = ccvNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public YearMonth getCardExpiration() {
        return YearMonth.of(cardExpirationYear.getValue(), cardExpirationMonth.getValue());
    }

    public Year getCardExpirationYear() {
        return cardExpirationYear;
    }

    public void setCardExpirationYear(Year cardExpirationYear) {
        this.cardExpirationYear = cardExpirationYear;
    }

    public Month getCardExpirationMonth() {
        return cardExpirationMonth;
    }

    public void setCardExpirationMonth(Month cardExpirationMonth) {
        this.cardExpirationMonth = cardExpirationMonth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Year> possibleExpirationYears() {
        Year currentYear = Year.of(createdAt.getYear());

        return IntStream.rangeClosed(0,10)
                .mapToObj(currentYear::plusYears)
                .collect(Collectors.toList());
    }

    public List<Month> possibleExpirationMonth() {
        Month currentMonth = createdAt.getMonth();

        return Arrays.stream(Month.values())
                .filter(m -> m.getValue() >= currentMonth.getValue())
                .collect(Collectors.toList());
    }

    public String getLastFourNumbersOfCreditCard() {
        return creditCardNumber.substring(creditCardNumber.length() - 4);
    }

    public SuccessfulPayment markAsSuccessful() {
        return new SuccessfulPayment(creditCardType, getLastFourNumbersOfCreditCard(), cvvNumber, getCardExpiration());
    }

    @Override
    public String toString() {
        return "PendingPayment{" +
                "creditCardType=" + creditCardType +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                ", cvvNumber='" + cvvNumber + '\'' +
                ", cardHolderName='" + cardHolderName + '\'' +
                ", cardExpirationYear=" + cardExpirationYear +
                ", cardExpirationMonth=" + cardExpirationMonth +
                ", createdAt=" + createdAt +
                '}';
    }
}
