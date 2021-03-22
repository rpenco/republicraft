package fr.republicraft.common.api.config;

import lombok.Getter;

@Getter
public class EntityConfig {
    String type;
    String name;
    float yaw = 0;
    String location;
}
