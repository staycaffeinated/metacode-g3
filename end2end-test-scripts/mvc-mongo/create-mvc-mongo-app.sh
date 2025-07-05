#!/bin/sh
#

filename=$(ls -l ../*.jar | awk '{print $9}')

#echo 'Filename: '  $filename

JAR=$filename

java -jar $JAR create project spring-webmvc \
    -n widgets \
    -p acme.widgets \
    --base-path /acme \
    --add openapi mongodb testcontainers

java -jar $JAR create endpoint --resource Widget --route /widget

