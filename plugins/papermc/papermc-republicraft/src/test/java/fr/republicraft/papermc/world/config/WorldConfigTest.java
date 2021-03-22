package fr.republicraft.papermc.world.config;

import fr.republicraft.common.api.helper.ConfigHelper;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorldConfigTest {

    @Test
    public void testWorldConfig() {
        WorldConfig config = ConfigHelper.loadConfig(Paths.get(getClass().getClassLoader()
                .getResource("config.yml").getPath()).getParent(), WorldConfig.class);

        assertEquals("world", config.getWorld());
    }
}
