package fr.republicraft.papermc.resources.managers;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockSaved {
    long key;
    String material;
    int x;
    int y;
    int z;
    boolean removed;
}
