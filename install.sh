#!/bin/bash

#Change OS time zone
mv -f /etc/localtime /etc/localtime.bak
ln -s /usr/share/zoneinfo/Africa/Lagos /etc/localtime

cd /opt/tima-service/

export $(cat /root/tima-service/.env | xargs)

java -jar -Dspring.profiles.active=docker tima-service-*.jar