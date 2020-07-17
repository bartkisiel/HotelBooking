package bartkisiel.github.io.HotelBooking.util;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {
    public static <T> String toCsv(Collection<T> coll, Function<T, CharSequence> keyMapper) {
        return coll.stream()
                .map(keyMapper)
                .collect(Collectors.joining(", "));
    }

    public static String capitalize(String str) {
        if(str.isEmpty()) return str;
        return str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String capitalizeWords(String str) {
        return Arrays.stream(str.split("\\s"))
                .map(Utils::capitalize)
                .collect(Collectors.joining(" "));
    }

    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            flyway.repair();
            flyway.migrate();
        };
    }
}
