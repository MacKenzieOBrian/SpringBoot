
# Spring Boot Fintech Demo

This project demonstrates a secure fintech web application using Spring Boot, Java, and web technologies. It covers user registration, authentication, account management, transactions, and post creation, all integrated with a simple frontend. The project is designed to be educational and approachable for students.


## Features

* User Registration
* User Login (generates JWT)
* Account creation and management (auto-created for each user)
* Deposit and withdraw funds
* Transaction history
* Create posts (with payment logic)
* Secured API endpoints (JWT required)



## Technologies Used

* Spring Boot
* Spring Security & JWT
* Spring Data JPA & Hibernate ORM
* H2 Database (in-memory)
* Maven
* HTML, CSS, JavaScript


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

