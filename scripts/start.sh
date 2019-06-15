#!/bin/sh

cd "$( dirname "$0" )"
java -Xms2G -Xmx4G -XX:+UseConcMarkSweepGC -jar spigot-1.14.2.jar