#!/bin/sh
java -cp "$1" org.hsqldb.Server -database.0 file:"$2" -dbname.0 "$2" > /home/haulmont/info.log 2 > /home/haulmont/errors.log
