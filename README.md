# Republicraft Minecraft server plugins

> All plugins used by the (offline now) Minecraft Server: **Républicraft**.


![Republicraft Island Hub](/docs/wallpaper/wallpaper-iles-2-2048x1024.jpg)

## Ansible

Ansible directory contains roles to quickly initialise your server with minimal configuration (docker) and security (ssh, fail2ban).

## Docker

Docker directory contains Dockerfile to create [PaperMC](https://papermc.io/) and [Velocity](https://velocitypowered.com/) images used to create Minecraft cluster.


## Docs

Docs directory contains many visual resources like images and psd.

## Plugins

Plugins contains modular libraries and plugins used for `velocity` and/or `papermc`, like a shopping system, a voting system, many text tools, a jdbc connection to Postgres, a discord bot, and an metric tool used to report usage to Kibana.


## WP-plugins

WP-plugins contains 2 usefull Wordpress plugins. `minestatus` used to known how many players are online; and `votes` to list players with the most supported the server through votes on referenced servers.