#!/bin/sh
#

filename=$(ls -l ../*.jar | awk '{print $9}')

#echo 'Filename: '  $filename

JAR=$filename

java -jar $JAR create project spring-webmvc \
    -n mvc-h2-app \
    -p acme.bookstore \
    --base-path /bookstore \
    --add openapi

java -jar $JAR create endpoint --resource Book --route /book

