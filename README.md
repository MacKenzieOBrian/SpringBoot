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


