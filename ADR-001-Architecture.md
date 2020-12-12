# ADR 002 - Architecture

## Status

Accepted

## Context
To implement the whale tracking system in a web application, we need to construct a solid foundation architecture.
Furthermore, this design should compile all the topics discussed in SENG330 to produce an effective product.
In doing so, the design should conform to general standards and practices. 

## Decision

To achieve the desired design for our web application’s architecture, we made two main decision: to closely align with the Play Framework and the 
MVC pattern, and pursue proper database normalization for the relationships between our models.

From this, we developed 3 models: Whale, Observation, and Sighting. Whale and Observation both contain a single primary key,
ID and Sightings is the model that references and relates both. Lastly, to maintain our commitment to DBMS design, the decision was made to only
allow Observations to be made with Whales already present in the system.

The first MVC of the input form on the Observation page used the Observation and ObservationData classes. These classes contain a list of whales, which Chloe was able to input from the Form. The difficult part was to get an interactive UI that would allow users to choose whales for the observation by entering or clicking whales from the Whales table. 

The MVC with Observation and ObservationData had the user entering new whale info on the observation page, so the observation page and whale page did not interact with each other. By using Sighting and SightingData as the MVC to input Observations both whale and observation pages were utilized. The new SightingForm MVC makes the user use the Whale table to look up Whale IDs to input into the Observation form, so both pages work together with the new MVC.

We have three tables in our database WHALES, OBSERVATIONS, and SIGHTINGS.       <br />
WHALES(id int primary key, species varchar(255), weight integer, gender varchar(255))    <br />
OBSERVATIONS(id int primary key, location varchar(255), numWhales integer, date varchar(255), time varchar(255))    <br />
SIGHTINGS( whale_id int, observation_id int, PRIMARY KEY(whale_id, observation_id) )    <br />

The relationship between WHALES and OBSERVATIONS would be a many to many relationship. 
The database code uses PreparedStatements to avoid SQL Injection.
We decided to use no triggers, constraints, or foreign keys because this course is not about databases or database design.



## Consequences

Using the Play Framework was the logical next step for this project. Having already studied the MVC and Observer patterns, moving to Play was a relatively smooth transition.  With Play, we continued to
use the MVC pattern. In our application ParentController monitors the inputs on the webpage via HTML Forms and when triggered, creates a model object from the input data.
Once created, ParentController then notifies observers, in this case calling the appropriate view to update the webpage. Using this pattern follows convention and allows any reader of our code familiar with Play
and MVC to quickly, and intuitively understand our architecture.

Secondly, choosing to closely follow proper database normalization with our underlying models has two main advantages: data integrity, and fully utilizing object-oriented methods.
Both Whale and Observation have a primary key, ID. Then Sighting stores the pair of Whale ID with the Observation ID for its primary key. Using this, we can better ensure the integrity of the data entered into our system.
Also, only allowing Observations to be made from Whales already in the system reinforces this integrity by reducing the chance for bad data to enter the system.
Also, designing our data models this way better makes use of object-oriented principles. With the Sighting model, in particular, the system can easily access the Whale objects associated with an Observation, 
or the Observations associated with a Whale, by cross-referencing their IDs. This allows each object to contain only the necessary information about itself.

Finally, the largest drawbacks to our architecture design are the current Observation creation process and the potential for future expansion of the application.
The current strategy to create Observations requires Whales to already be in the system and to know the Whale ID for the whale the user wants to add. 
This would not be ideal in a full-scale implementation of this product, especially if there were hundreds or thousands of whales in the system.
Better optimizing this process would be a key focus moving forward with the application.
Furthermore, following such a strict data model between our model's limits possibilities for future development as designers must adhere to the relationships between the models we have laid out.


