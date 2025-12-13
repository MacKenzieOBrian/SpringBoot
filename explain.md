The application is structured into layers, each with a single responsibility:

Controller → Service → Repository → Database

- Controllers handle HTTP and input/output
- Services contain business logic and rules
- Repositories handle persistence
- The database stores state

Security (e.g. JWT) sits in front of the controllers and decides whether a request is allowed to proceed.

---

## Step 1: Define the Data Model

Start by identifying the core entities the system needs to store.

For each entity, determine:
- Required fields
- Generated fields (e.g. IDs)
- Relationships to other entities

This establishes the foundation of the system. APIs and services are built around the data, not the other way around.

---

## Step 2: Create Entities (Database Shape)

Entities map directly to database tables.

They define:
- Table structure
- Primary keys
- Relationships (one-to-one, one-to-many)

At this stage, the focus is on persistence, not HTTP or validation.

---

## Step 3: Create Repositories (Data Access)

Repositories provide a clean interface to the database.

Their responsibilities are limited to:
- Saving data
- Loading data
- Updating data
- Deleting data

They do not contain business rules or application logic.

---

## Step 4: Implement Services (Business Logic)

Services coordinate application behaviour.

This is where to place:
- Validation beyond basic annotations
- Rules such as limits or permissions
- Multi-step operations
- Transaction boundaries

If logic determines whether something is allowed, it belongs in the service layer.

---

## Step 5: Implement Controllers (HTTP Layer)

Controllers expose the application through HTTP endpoints.

Their responsibilities:
- Accept input from requests
- Map input to request models
- Call service methods
- Return appropriate responses

Controllers should remain thin and free of business logic.

---

## Step 6: Use Request and Response Models

Request and response models define the external API contract.

Benefits:
- Prevent clients from setting internal fields
- Make validation explicit
- Decouple API design from database structure

Common patterns:
- Create request with required fields
- Update request with optional fields
- Response model including generated fields

---

## Step 7: Use Transactions Where Necessary

When a single operation involves multiple database changes, wrap it in a transaction.

Purpose:
- Ensure atomicity
- Prevent partial updates
- Maintain data consistency

Typical use cases include financial operations or linked record creation.

---

## Step 8: Add Security After Core Functionality

Authentication and authorisation are added once the core CRUD functionality is stable.

Security components:
- Login endpoint
- Token generation
- Request filtering and access rules

Security should protect endpoints without mixing with business logic.

---

## End-to-End Request Flow

1. HTTP request reaches the controller
2. Input is validated and passed to the service
3. Service applies rules and calls repositories
4. Repository interacts with the database
5. Result flows back to the client as a response

---

## Key Design Principle

Controllers handle requests  
Services enforce rules  
Repositories manage data  
Databases store state