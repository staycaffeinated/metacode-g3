#!/bin/sh
#

filename=$(ls -l ../*.jar | awk '{print $9}')

#echo 'Filename: '  $filename

JAR=$filename

java -jar $JAR create project spring-webmvc \
    -n mvc-psq-schema \
    -p acme.anvils \
    --base-path /anvils \
    --schema acme \
    --add openapi postgres testcontainers

java -jar $JAR create endpoint --resource Anvil --route /anvil

