package fr.republicraft.common.api.helper;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleLocation {
    float x;
    float y;
    float z;

    public static SimpleLocation fromString(String location) {
        String[] parsers = location.split(",");
        if (parsers.length == 3) {
            return new SimpleLocation(Float.parseFloat(parsers[0]), Float.parseFloat(parsers[1]), Float.parseFloat(parsers[2]));
        } else {
            throw new RuntimeException("invalid location format, expected \"x,y,z\" obtains " + location + ".");
        }
    }

    public static String toString(float x, float y, float z) {
        return x + "," + y + "," + z;
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z;
    }
}
