package fr.republicraft.papermc.resources.config;

import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

/**
 * Configuration du plugin de vote
 */
@Data
public class ResourcesConfig {
    List<Material> blockIds;
}
