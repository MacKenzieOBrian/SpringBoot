# Spring Boot JWT Demo

This project is a simple demonstration of a Spring Boot application with JWT (JSON Web Token) based authentication and authorization. It provides a basic user management API with secured endpoints.

## Features

*   User Registration
*   User Login (generates JWT)
*   Secured API endpoint (`/api/users`) that requires a valid JWT
*   Basic frontend to interact with the authentication and user API

## Technologies Used

*   Spring Boot
*   Spring Security
*   JWT (jjwt)
*   Spring Data JPA
*   H2 Database (in-memory)
*   Maven
*   HTML, CSS, JavaScript

## Setup and Running

1.  **Clone the repository:**
    ```bash
    git clone <repository_url>
    cd demo
    ```
    (Replace `<repository_url>` with the actual URL of your repository)

2.  **Build the project:**
    ```bash
    ./mvnw clean install
    ```

3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```
    The application will start on `http://localhost:8080`.

## Using the Application

1.  **Access the Frontend:** Open your web browser and go to `http://localhost:8080/`.

2.  **Register a User:**
    *   Use the "Register" form to create a new user. Enter a username and password and click "Register".
    *   You should see a "Registration successful!" message.

3.  **Login:**
    *   Use the "Login" form with the username and password of the user you just registered (or any existing user). Click "Login".
    *   If successful, you will see a "Login successful! Token obtained." message, and the JWT will be printed in your browser's developer console.

4.  **Access Secured Endpoint:**
    *   Click the "Get All Users (Requires Login)" button.
    *   If you are logged in (have a valid JWT), the application will fetch and display the list of registered users.
    *   If you are not logged in, or your token is invalid/expired, you will receive an error message indicating unauthorized access.

## API Endpoints

*   `POST /api/users`: Register a new user. Requires a JSON body with `username` and `password`.
*   `POST /auth/login`: Authenticate a user and get a JWT. Requires a JSON body with `username` and `password`.
*   `GET /api/users`: Get all registered users. **Requires a valid JWT in the `Authorization: Bearer <token>` header.**
*   `GET /api/users/{id}`: Get a user by ID. **Requires a valid JWT.**
*   `PUT /api/users/{id}`: Update a user by ID. **Requires a valid JWT.**
*   `DELETE /api/users/{id}`: Delete a user by ID. **Requires a valid JWT.**

## Project Structure

```
.
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── demo
    │   │               ├── DemoApplication.java
    │   │               ├── config
    │   │               │   └── SecurityConfig.java
    │   │               ├── controller
    │   │               │   ├── AuthenticationController.java
    │   │               │   ├── SimpleController.java
    │   │               │   └── UserController.java
    │   │               ├── model
    │   │               │   └── User.java
    │   │               ├── repository
    │   │               │   └── UserRepository.java
    │   │               └── service
    │   │                   ├── JwtService.java
    │   │                   └── UserService.java
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       │   ├── index.html
    │       │   ├── script.js
    │       │   └── style.css
    │       └── templates
    └── test
        └── java
            └── com
                └── example
                    └── demo
                        └── DemoApplicationTests.java
```

This project provides a solid foundation for demonstrating your skills in building secure web applications with Spring Boot and JWT.

### 2. Dependencies 

The `pom.xml` file manages the project's dependencies. Key dependencies include:

*   `spring-boot-starter-data-jpa`: Provides everything needed to use Spring Data JPA with Hibernate.
*   `spring-boot-starter-security`: Provides Spring Security for authentication and authorization.
*   `spring-boot-starter-validation`: Provides support for Java Bean Validation.
*   `spring-boot-starter-web`: Provides everything needed to build web applications, including RESTful APIs.
*   `h2`: An in-memory database used for development and testing.
*   `mysql-connector-j`: MySQL database driver (included but not used by default with H2).
*   `lombok`: A library that reduces boilerplate code (e.g., getters, setters, constructors).
*   `jjwt-api`, `jjwt-impl`, `jjwt-jackson`: Libraries for working with JSON Web Tokens (JWT).

### 3. Security Configuration (SecurityConfig.java)

The `SecurityConfig.java` file configures Spring Security for the application. Key aspects include:

*   **`UserDetailsService`**: Defines how to load user details (in this case, from the `UserRepository`).
*   **`AuthenticationManager`**: Manages the authentication process.
*   **`DaoAuthenticationProvider`**: An authentication provider that uses the `UserDetailsService` and `PasswordEncoder`.
*   **`PasswordEncoder`**: Configured to use `BCryptPasswordEncoder` for secure password hashing.
*   **`SecurityFilterChain`**: Configures the security filter chain, defining which requests are permitted and which require authentication. It also adds the `JwtAuthFilter` before the standard `UsernamePasswordAuthenticationFilter`.
*   **Request Matchers**:
    *   `/auth/**`: Permitted for authentication endpoints (login, register).
    *   `POST /api/users`: Permitted for user registration.
    *   `/api/users/**`: Requires authentication for other user-related endpoints.
    *   `anyRequest().permitAll()`: Permits all other requests not explicitly matched (including static resources like HTML, CSS, JS).
*   **CSRF and HTTP Basic**: CSRF protection and HTTP Basic authentication are disabled for simplicity in this example.

### 4. JWT Authentication Filter (JwtAuthFilter.java)

The `JwtAuthFilter.java` is a filter that intercepts incoming requests and validates JWTs. Key aspects include:

*   **`OncePerRequestFilter`**: Ensures the filter is executed only once per request.
*   **`JwtService`**: Used to extract the username from the JWT and validate the token.
*   **`UserDetailsService`**: Used to load user details by username.
*   **`doFilterInternal`**:
    *   Extracts the JWT from the `Authorization` header.
    *   Extracts the username from the JWT.
    *   Loads user details using `UserDetailsService`.
    *   Validates the JWT using `JwtService`.
    *   If the token is valid, it authenticates the user and sets the authentication in the `SecurityContextHolder`.

### 5. JWT Service (JwtService.java)

The `JwtService.java` class handles the generation and validation of JWTs. Key aspects include:

*   **`@Value("${jwt.secret}")`**: Injects the secret key from the `application.properties` file.
*   **`extractUsername`**: Extracts the username from a JWT.
*   **`extractClaim`**: Extracts a specific claim from a JWT.
*   **`generateToken`**: Generates a JWT for a given `UserDetails` object.  Includes methods to add extra claims.
*   **`isTokenValid`**: Validates a JWT against a `UserDetails` object.
*   **`isTokenExpired`**: Checks if a JWT has expired.
*   **`extractExpiration`**: Extracts the expiration date from a JWT.
*   **`extractAllClaims`**: Extracts all claims from a JWT.
*   **`getSignInKey`**: Generates a signing key from the secret key.


### 6. User Service (UserService.java)

The `UserService.java` class provides methods for managing users. Key aspects include:

*   **`UserRepository`**: Used to interact with the database.
*   **`PasswordEncoder`**: Used to encode passwords securely.
*   **`findAllUsers`**: Retrieves all users.
*   **`findUserById`**: Retrieves a user by ID.
*   **`findUserByUsername`**: Retrieves a user by username.
*   **`saveUser`**: Saves a new user, encoding the password before saving.
*   **`updateUser`**: Updates an existing user, encoding the password if a new one is provided.
*   **`deleteUser`**: Deletes a user by ID.

