package bartkisiel.github.io.HotelBooking.util;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.DateTypeDescriptor;

import java.time.YearMonth;

public class YearMonthDateType extends AbstractSingleColumnStandardBasicType<YearMonth> {
    public static final YearMonthDateType INSTANCE = new YearMonthDateType();

    public YearMonthDateType() {
        super(DateTypeDescriptor.INSTANCE, YearMonthTypeDescriptor.INSTANCE);
    }


    @Override
    public String getName() {
        return "yearmonth-date";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }
}
