package bartkisiel.github.io.HotelBooking;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.function.Function;

public class MappedAssertion {
    public static <U,T> FeatureMatcher<U,T> mappedAssertion (Function<U,T> mapper, Matcher<T> subMatcher) {

        return new FeatureMatcher<U, T>(subMatcher, "mappedAssertion", "mappedAssertion") {
            protected T featureValueOf(U value) {
                return mapper.apply(value);
            }
        };
    }
}
