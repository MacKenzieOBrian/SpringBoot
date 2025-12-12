
# Spring Boot Fintech Demo

This project demonstrates a secure fintech web application using Spring Boot, Java, and web technologies. It covers user registration, authentication, account management, transactions  and post creation, all integrated with a simple frontend. 

The project is organized so that models define the data objects used throughout the system, while repositories handle persistence—reading and writing to the database without the need for explicit SQL. Services are where the business rules live, controllers act as the bridge between frontend requests and backend logic, exposing endpoints for interaction. Then a static folder holds frontend assets, and a central configuration file (application.properties) defines database connections, ports and other application settings.

The first step is to define a User entity which is a password, name, and email. Other entities extend the functionality. An Account tracks each user’s balance and status, while Transaction logs deposits, withdrawals, or fees. A Post entity demonstrates how users can create and manage content within the system.
Spring Data JPA allows you to declare repositories that automatically provide CRUD functionality. Extending JpaRepository I can instantly query entities without boilerplate code. For example UserRepository handles users, while AccountRepository manages accounts.

The service layer is where the rules of the application are enforced. Controllers should handle HTTP requests and services take care of the actual work. A UserService might handle registering and authenticating users. An AccountService could manage deposits and withdrawals, applying business logic like checking for sufficient balance

In this project Spring Security is configured to use JWT for stateless authentication. Instead of storing session data on the server, each successful login generates a signed token that the client must include in future requests.

Controllers provide the interface through which external clients interact with the application. Using @RestController and @RequestMapping, endpoints are defined for user registration, login, account actions, transaction history, and post creation.

## Setup and Running

1. **Clone the repository:**
    ```bash
    git clone <repository_url>
    cd SpringBootProject
    ```
    (Replace `<repository_url>` with the actual URL of your repository)

2. **Build the project:**
    ```bash
    ./mvnw clean install
    ```

3. **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```
    The application will start on `http://localhost:8080`.

