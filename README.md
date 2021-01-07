
# Whale Observation Application
This repo includes the backend and frontend of a whale observation application using Java Play framework. The application includes a page that create whales, a page that creates observations of one or more whales and a stats page that visualizes the data in the database.</br> </br>
[Visit web application](https://gentle-savannah-14222.herokuapp.com/Whales)

## Requirements
1. The app shall run locally only.
2. The app shall **display** Observations and Whales.
    1. Whales have species, estimated weight, gender, and id fields.
    2. Observations have a collection of whales observed, location, date/time (to the nearest hour).
3. The app shall enable a user to **create** a new Whale record. 
4. The app shall enable a user to **create** a new Observation record. 
5. The app shall support the ability to **search** on Whale species (e.g., "show all Orcas") and Observation date (e.g.,"show all Whales seen on 2012-01-01")
6. Create a REST api for your app. The REST API only has to implement ONE endpoint: `GET http://localhost:9000/whales` with mime-type set to application/txt+json should return the JSON representation of the whales currently entered. Note that this involves content negotiation since you will also have an endpoint `/whales` that will return HTML if that was requested (ie. by a web browser). See [here under "Content"](https://www.playframework.com/documentation/2.8.x/JavaContentNegotiation). Note that Play has builtin JSON parsing using Jackson at `import play.api.libs.json.Json`.
7. The source code shall have unit tests that test every operation on the model.
8. The app's internal architecture shall follow principles discussed in the course with respect to code quality and OO principles. This includes following best practices in MVC using Play. 
