package fr.republicraft.common.api.helper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullLocation {
    float x;
    float y;
    float z;
    float yaw;
    String server;
    String world;
    String material;

    public FullLocation() {
    }

    public FullLocation(String server, String world, float x, float y, float z, float yaw, String material) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.server = server;
        this.world = world;
        this.material = material;
    }

    public static FullLocation toFullLocation(String location) {
        return JsonHelper.fromJson(location, FullLocation.class);
    }

    public static String toJson(FullLocation fullLocation) {
        return JsonHelper.toJson(fullLocation);
    }

    public static FullLocation fromJSON(String json) {
        return toFullLocation(json);
    }

    public SimpleLocation toSimpleLocation() {
        return SimpleLocation.builder()
                .x(x)
                .y(y)
                .z(z)
                .build();
    }

    public FullLocation fromSimpleLocation(SimpleLocation location) {
        x = location.getX();
        y = location.getY();
        z = location.getZ();
        return this;
    }


    public String toJson() {
        return toJson(this);
    }

    @Override
    public String toString() {
        return "FullLocation{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", server='" + server + '\'' +
                ", world='" + world + '\'' +
                ", material=" + material +
                '}';
    }
}
