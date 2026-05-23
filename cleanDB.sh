#!/bin/bash
export $(grep -v '^#' .env | xargs)
mvn flyway:clean
mvn flyway:migrate