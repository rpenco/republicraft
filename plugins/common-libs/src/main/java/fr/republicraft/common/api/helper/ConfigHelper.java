package fr.republicraft.common.api.helper;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigHelper {

    public static <T> T loadConfig(Path path, Class<T> valueType) {
        File folder = path.toFile();
        File file = new File(folder, "config.yml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try (InputStream input = ConfigHelper.class.getResourceAsStream("/" + file.getName())) {
                if (input != null) {
                    Files.copy(input, file.toPath());
                } else {
                    file.createNewFile();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                return null;
            }
        }

        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        representer.getPropertyUtils().setBeanAccess(BeanAccess.FIELD);
        representer.getPropertyUtils().setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(new CustomClassLoaderConstructor(valueType.getClassLoader()), representer);
        try {
            return yaml.loadAs(new FileInputStream(file), valueType);
        } catch (IOException e) {
            e.printStackTrace();
            return yaml.loadAs(ConfigHelper.class.getResourceAsStream("/" + file.getName()), valueType);
        }
    }
}
