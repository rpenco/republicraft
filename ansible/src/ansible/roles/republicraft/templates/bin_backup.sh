#!/bin/bash


cur=$(pwd)
date=$(date --iso-8601=seconds)

echo "${date}: Backup starting..."

mkdir -p /data/backup
chmod -R o+rw /data/backup

# TODO ajouter backup:
 - server.properties
 - ops.json
 - paper.yml
 - permission.yml

# build1
docker stop world_build1
cd /data/opt/paper/build/1/
zip -r "backup-${date}-build-1-world.zip" world/**/*
docker start world_build1
mv "backup-${date}-build-1-world.zip" /data/backup/
echo "${date}: Backup Build 1 - World. Done"

# waiting world starting before stop lobby
sleep 30

# lobby1
docker stop world_lobby
cd /data/opt/paper/lobby/1/
zip -r "backup-${date}-lobby-1-world.zip" world/**/*
docker start world_lobby
mv "backup-${date}-lobby-1-world.zip" /data/backup/
echo "${date}: Backup Lobby 1 - World. Done"

# backup db
docker exec mariadb /usr/bin/mysqldump -u root --password=9F00DC68980C382F49431CD22AB5FE2B2CD87C00 republicraft > "backup-${date}-republicraft.sql"
mv "backup-${date}-republicraft.sql" /data/backup
echo "${date}: Backup Republicraft DB. Done"

# backup wordpress
docker exec mariadb /usr/bin/mysqldump -u root --password=9F00DC68980C382F49431CD22AB5FE2B2CD87C00 wordpress > "backup-${date}-wordpress.sql"
zip -r "backup-${date}-worldpress.zip" /data/opt/wordpress/wp-content/**/*
mv "backup-${date}-wordpress.sql" /data/backup
mv "backup-${date}-worldpress.zip" /data/backup
echo "${date}: Backup Wordpress. Done"

echo "${date}: Backup end."
