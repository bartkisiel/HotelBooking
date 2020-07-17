package bartkisiel.github.io.HotelBooking.util;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class YearMonthTypeDescriptor extends AbstractTypeDescriptor<YearMonth> {
    public static final YearMonthTypeDescriptor INSTANCE = new YearMonthTypeDescriptor();

    public YearMonthTypeDescriptor() {
        super(YearMonth.class);
    }

    @Override
    public boolean areEqual(
            YearMonth one,
            YearMonth another) {
        return Objects.equals(one, another);
    }

    @Override
    public String toString(YearMonth value) {
        return value.toString();
    }

    @Override
    public YearMonth fromString(String str) {
        return YearMonth.parse(str);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <X> X unwrap(YearMonth yearMonth, Class<X> aClass, WrapperOptions wrapperOptions) {
        if(yearMonth == null) {
            return null;
        }
        if(String.class.isAssignableFrom(aClass)) {
            Integer numericValue = (yearMonth.getYear() * 100) + yearMonth.getMonth().getValue();
            return (X) (numericValue);
        }
        if(Date.class.isAssignableFrom(aClass)) {
            return(X) java.sql.Date.valueOf(yearMonth.atDay(1));
        }
        throw unknownUnwrap(aClass);
    }

    @Override
    public <X> YearMonth wrap(X x, WrapperOptions wrapperOptions) {
        if(x == null) {
            return null;
        }
        if(x instanceof String) {
            return fromString((String) x);
        }
        if(x instanceof Number) {
            int numericValue = ((Number)(x)).intValue();
            int year = numericValue / 100;
            int month = numericValue % 100;
            return YearMonth.of(year, month);
        }
        if (x instanceof Date) {
            Date date = (Date) x;
            return YearMonth
                    .from(Instant.ofEpochMilli(date.getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
        }
        throw unknownUnwrap(x.getClass());
    }


}
