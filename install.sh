#!/bin/bash

#Change OS time zone
mv -f /etc/localtime /etc/localtime.bak
ln -s /usr/share/zoneinfo/Africa/Lagos /etc/localtime

cd /opt/tima-auth-service/

export $(cat /root/tima-auth-service/.env | xargs)

java -jar -Dspring.profiles.active=docker tima-auth-service-*.jar