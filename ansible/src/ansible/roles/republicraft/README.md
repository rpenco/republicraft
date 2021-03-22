# Configuration des plugins


> Set max connection luckperm 300000 (5min) < wait_connection mariadb 


## PaperMC

### Update plugins

**LuckPerms**
https://ci.lucko.me/job/LuckPerms/1060/artifact/bukkit/build/libs/LuckPerms-Bukkit-5.0.146.jar

**PlugMan**
https://github.com/r-clancy/PlugMan

**HolographicDisplays**
https://github.com/filoghost/HolographicDisplays
https://ci.codemc.io/job/filoghost/job/HolographicDisplays/

## Velocity

### Update plugins

https://ci.lucko.me/job/LuckPerms/1060/artifact/velocity/build/libs/LuckPerms-Velocity-5.0.146.jar



## Regenerate Resources World

```shell script
/worldborder set 5000
/wb set 5000
/wb fill
/wb fill confirm
```



## Regenerate Build World

```shell script
/worldborder set 4000
/wb set 4000
/wb dynmap on
/wb fill
/wb fill confirm
/wb trim
/wb trim confirm
# restart server
/dynamp fullrender
```
