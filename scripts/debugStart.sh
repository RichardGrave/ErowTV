#!/bin/sh

cd "$( dirname "$0" )"
# java -Xms2G -Xmx4G -XX:+UseConcMarkSweepGC -jar spigot-1.13.2.jar
# java -Xms2G -Xmx4G -XX:+UseConcMarkSweepGC -jar spigot-1.14.2.jar

java -Xms2G -Xmx4G -XX:+UseConcMarkSweepGC -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:25565 spigot-1.14.2.jar