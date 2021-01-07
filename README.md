
# Whale Observation Application
This repo includes the backend and frontend of a whale observation application using Java Play framework. The application includes a page that create whales, a page that creates observations of one or more whales and a stats page that visualizes the data in the database.</br> </br>
[Visit web application](https://gentle-savannah-14222.herokuapp.com/Whales)

## Features/Functionality
1. The app **displays** Observations and Whales.
   - I. Whales have species, estimated weight, gender, and id fields.
   - II. Observations have a collection of whales observed, location, date/time (to the nearest hour).
2. The app enables a user to **create** a new Whale record. 
3. The app enables a user to **create** a new Observation record. 
4. The app supports the ability to **search** on Whale species (e.g., "show all Orcas") and Observation date (e.g.,"show all Whales seen on 2012-01-01")
5. Has a REST API with the endpoint: `GET http://localhost:9000/whales`.
6. Stats page that visualizes the data in the database.
7. Unit tests that test every operation on the model.
8. The app's internal architecture follows principles with respect to code quality and OO principles. 
9. This includes following best practices in MVC using Play. 
