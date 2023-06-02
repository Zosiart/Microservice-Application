package nl.tudelft.sem.template.activity.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.activity.domain.activity.Availability;
import nl.tudelft.sem.template.activity.domain.activity.Level;
import nl.tudelft.sem.template.activity.domain.activity.Role;
import nl.tudelft.sem.template.activity.domain.activity.RoleInfo;

public class Parser {

    /**
     * Parse the availabilities list from a map containing query parameters.
     *
     * @param requestParams - the query parameter map
     * @return a list of Availabilities
     * @throws ParseException when the availabilities were parsed incorrectly
     */
    public static List<Availability> parseAvailabilities(Map<String, String> requestParams) throws ParseException {
        var availabilityMap = new HashMap<Integer, Availability>();

        var formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        requestParams = requestParams.entrySet().stream().filter(entry -> entry.getKey().startsWith("availabilities"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            String key = entry.getKey();

            var date = formatter.parse(entry.getValue());
            int index = Integer.parseInt(key.substring(key.indexOf('[') + 1, key.indexOf(']')));
            String attribute = key.substring(key.indexOf('.') + 1);

            if (!availabilityMap.containsKey(index)) {
                availabilityMap.put(index, new Availability());
            }

            Availability availability = availabilityMap.get(index);
            availability.timeSetter(attribute, date);
        }

        return new ArrayList<>(availabilityMap.values());
    }

    /**
     * Parse the roles list from a map containing query parameters.
     *
     * @param requestParams - the query parameter map
     * @return a list of RoleInfos
     */
    public static List<RoleInfo> parseRoles(Map<String, String> requestParams) {
        var rolesMap = new HashMap<Integer, RoleInfo>();

        var mapper = new ObjectMapper();
        requestParams = requestParams.entrySet().stream().filter(entry -> entry.getKey().startsWith("roles"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            String key = entry.getKey();

            int index = Integer.parseInt(key.substring(key.indexOf('[') + 1, key.indexOf(']')));
            String attribute = key.substring(key.indexOf('.') + 1);

            if (!rolesMap.containsKey(index)) {
                rolesMap.put(index, new RoleInfo());
            }

            var value = entry.getValue();

            var role = rolesMap.get(index);
            if (attribute.equals("role")) {
                role.setRole(mapper.convertValue(value, Role.class));
            } else if (attribute.equals("level")) {
                role.setLevel(mapper.convertValue(value, Level.class));
            }
        }

        return new ArrayList<>(rolesMap.values());
    }
}
