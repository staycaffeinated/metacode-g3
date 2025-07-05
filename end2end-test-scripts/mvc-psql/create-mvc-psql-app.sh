#!/bin/sh
#

filename=$(ls -l ../*.jar | awk '{print $9}')

#echo 'Filename: '  $filename

JAR=$filename

java -jar $JAR create project spring-webmvc \
    -n mvc-psql \
    -p acme.books \
    --base-path /bookstore \
    --add openapi postgres testcontainers

java -jar $JAR create endpoint --resource Book --route /book

