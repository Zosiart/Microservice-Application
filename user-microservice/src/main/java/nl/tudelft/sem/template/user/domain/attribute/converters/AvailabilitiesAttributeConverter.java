package nl.tudelft.sem.template.user.domain.attribute.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.persistence.AttributeConverter;
import lombok.SneakyThrows;
import nl.tudelft.sem.template.user.domain.entity.Availability;

@SuppressWarnings("PMD")
public class AvailabilitiesAttributeConverter implements AttributeConverter<List<Availability>, String> {
    @Override
    public String convertToDatabaseColumn(List<Availability> attribute) {
        if (attribute == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        int size = attribute.size();
        for (Availability availability : attribute) {
            size--;
            stringBuilder.append(dateFormat.format(availability.getStartTime())).append(';')
                .append(dateFormat.format(availability.getEndTime()));
            if (size > 0) {
                stringBuilder.append(',');
            }
        }

        return stringBuilder.toString();
    }


    @SneakyThrows
    @Override
    public List<Availability> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.equals("")) {
            return null;
        }
        String[] split = dbData.split(",");
        List<Availability> availabilityList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        for (String s : split) {
            String[] dates = s.split(";");
            availabilityList.add(new Availability(dateFormat.parse(dates[0]), dateFormat.parse(dates[1])));
        }

        return availabilityList;
    }
}
