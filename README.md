# Event Registration API

Backend REST API for event registration with capacity limits, waitlist handling, schedule conflict detection, and user-specific event availability.

## Features

- User and event management
- Event registration with capacity control
- Automatic waitlist assignment when an event is full
- Automatic promotion from waitlist after cancellation
- Prevention of duplicate active registrations
- Prevention of registrations for overlapping events
- Event summary with confirmed and waitlisted counts
- User-specific available events based on current registrations
- Structured error responses

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Docker Compose
- Maven

## Business Logic

### Registration status

When a user registers for an event:

- if the event has free seats, registration becomes `CONFIRMED`
- if the event is full, registration becomes `WAITLISTED`

Only `CONFIRMED` registrations count toward event capacity.

### Waitlist promotion

When a confirmed registration is cancelled, the earliest waitlisted user is automatically promoted to `CONFIRMED`.

### Schedule conflict detection

A user cannot register for an event if it overlaps with another active registration.

### Concurrency handling

Registration operations use transactions and pessimistic locking for event rows to prevent race conditions around capacity limits.

## API Endpoints

### Users

```http
POST /api/users
GET  /api/users
GET  /api/users/{id}
```

### Events

```http
POST  /api/events
GET   /api/events
GET   /api/events?status=OPEN
GET   /api/events/{id}
GET   /api/events/with-seats
PATCH /api/events/{id}/status
```

### Registrations

```http
POST   /api/events/{eventId}/registrations
GET    /api/events/{eventId}/registrations
DELETE /api/events/{eventId}/registrations/{userId}
GET    /api/events/{eventId}/summary
```

### User-specific availability

```http
GET /api/users/{userId}/events/available
GET /api/users/{userId}/events/available/with-seats
```

`/available` returns open events that do not overlap with the user's active registrations.

`/available/with-seats` returns only those available events that still have confirmed seats.

## Running Locally

### 1. Start PostgreSQL

```bash
docker compose up -d
```

### 2. Run the application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Application will be available at:

```text
http://localhost:8080
```

## Project Structure

```text
controller - REST controllers
service - business logic
repository - access to database
entity - JPA entities
dto - request/response objects
exception - custom exceptions and global error handling
model - enums
```