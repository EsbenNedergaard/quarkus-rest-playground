#!/bin/bash

docker exec -i mysql-user mysql -u root --password=secret account_db < db_tables/account.sql