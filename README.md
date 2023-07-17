# Odds Based App
Odds Based App is an application that allows users to register, place bets with their available credits based on random numbers, view wallet information, and see details of played bets.

### Technologies Used

#### Spring WebFlux: Reactive web framework for building the API endpoints.
#### Kotlin: Programming language used for the implementation.
#### Java 17: The version of Java used in the project.
#### PostgreSQL: Database management system for storing player and transaction data.
#### OpenAPI: Specification for designing and documenting the API.
#### Spring Data: Framework for interacting with the database.
#### MapStruct: Library for mapping objects between layers.
#### Lombok: Library for reducing boilerplate code.

### Functionality
The Odds Based App provides the following functionality:

#### User Registration: Users can register by providing their username, name, and surname.
#### Placing Bets: Users can place bets by specifying the bet amount and number. The system will deduct the bet amount from the user's wallet balance.
#### Wallet Information: Users can view their current wallet balance and transaction history.
#### Bet History: Users can view the details of their played bets, including the bet amount, bet number, generated number, transaction type, winning amount, and updated wallet balance.
#### Top Winners: The app displays a list of the top winners based on their total winnings.

### Setup and Configuration
To set up and configure the Odds Based App, follow these steps:

#### Install Java 17 and PostgreSQL on your system.
#### Clone the project repository from GitHub.
#### Run docker-compose.yml for build Postgresql in Docker container.
#### Configure the PostgreSQL database connection in the application.yml file.
#### Build the project using the Gradle.
#### Run the application and access the API endpoints through a web browser or API testing tool.

### API Documentation
The API endpoints and their details are documented using OpenAPI. You can access the API documentation by navigating to the /api-docs endpoint when the application is running.
ex: [Swagger UI](http://localhost:9999/swagger-ui.html)

# Important information

I couldn't use the H2 database as suggested because I couldn't see the tables and it made my development process very difficult. That's why I used PostgreSQL.