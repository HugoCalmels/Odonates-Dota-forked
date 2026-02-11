# Launch Database

### init

``docker run -d -p 54322:5432 --name dota-postgres -e POSTGRES_PASSWORD=naga -e POSTGRES_USER=ikeaz -e POSTGRES_DB=dota postgres:latest``

### run

``docker start dota-postgres``
