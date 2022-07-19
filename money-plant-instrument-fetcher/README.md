# money-plant-instrument-fetcher

### Objective: 

* To Maintain the list of instruments on which we want to perform trades(e.g NSE:INFY)
* To Maintain the list of parameters on which we will do trades(e.g. MACD, Candle Data, etc)

### Build Steps: 

* Navigate to money-plant-instrument-fetcher-directory

* Perform the following command to generate jar file
```
mvn clean install
``` 
* Build the docker image
```
docker build -t <TAG_NAME> .
```
* Push image to a repository
```
docker push <TAG_NAME>
```

### Deploy:

* Spawn a mongoDB Container
```
docker run --name mongodb -e MONGODB_ROOT_PASSWORD=password123 -d -p 27017:27017 --restart=always -v $HOME/docker/volumes/mongodb:/bitnami/mongodb bitnami/mongodb:5.0
```
* Run the service image

```
docker run --name money-plant-instrument-fetcher -d -p 8080:8080 --restart=always -e spring.data.mongodb.uri=<MONGO_DB_URI> -e spring.data.mongodb.database=money_plant
```