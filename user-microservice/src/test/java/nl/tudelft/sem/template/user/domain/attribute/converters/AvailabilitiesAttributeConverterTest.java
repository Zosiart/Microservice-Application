package nl.tudelft.sem.template.user.domain.attribute.converters;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.*;
import nl.tudelft.sem.template.user.domain.entity.Availability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AvailabilitiesAttributeConverterTest {

    private AvailabilitiesAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new AvailabilitiesAttributeConverter();
    }

    @Test
    void convertToDatabaseColumnNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToDatabaseColumnOneEntry() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 26, 12, 16, 0, 0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date start = calendar.getTime();
        calendar.set(2022, 26, 12, 18, 0, 0);
        Date end = calendar.getTime();
        String expected = format.format(start) + ";" + format.format(end);
        assertEquals(expected, converter.convertToDatabaseColumn(List.of(new Availability(start, end))));
    }

    @Test
    public void convertToDatabaseColumnMultipleEntries() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 1, 28, 8, 30, 0);
        Date start1 = calendar.getTime();
        calendar.set(2023, 1, 29, 8, 30, 0);
        Date start2 = calendar.getTime();
        calendar.set(2023, 2, 1, 8, 45, 0);
        Date start3 = calendar.getTime();

        calendar.set(2023, 1, 28, 10, 30, 0);
        Date end1 = calendar.getTime();
        calendar.set(2023, 1, 29, 10, 30, 0);
        Date end2 = calendar.getTime();
        calendar.set(2023, 2, 1, 10, 45, 0);
        Date end3 = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String expected = sdf.format(start1) + ";" + sdf.format(end1) + ","
                + sdf.format(start2) + ";" + sdf.format(end2) + ","
                + sdf.format(start3) + ";" + sdf.format(end3);
        assertEquals(expected, converter.convertToDatabaseColumn(List.of(
                new Availability(start1, end1),
                new Availability(start2, end2),
                new Availability(start3, end3)
        )));
    }

    @Test
    void convertToEntityAttributeNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }

}