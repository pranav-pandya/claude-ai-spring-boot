# User Management REST API

A Spring Boot application providing REST API endpoints for user management operations.

## Version
1.0.1

## Features

- **CRUD Operations**: Create, Read, Update, Delete users
- **Validation**: Input validation with Bean Validation
- **Error Handling**: Global exception handling with meaningful error messages
- **Database**: H2 in-memory database for development
- **REST API**: JSON-based REST endpoints

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users` | Get all users |
| GET | `/api/v1/users/{id}` | Get user by ID |
| POST | `/api/v1/users` | Create a new user |
| PUT | `/api/v1/users/{id}` | Update an existing user |
| DELETE | `/api/v1/users/{id}` | Delete a user |

## User Model

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

## Running the Application

### Prerequisites
- Java 17
- Maven 3.6+

### Local Development
```bash
mvn spring-boot:run
```

The application will start on http://localhost:8080

### Docker
```bash
docker-compose up --build
```

### H2 Console
When running locally, access H2 console at: http://localhost:8080/h2-console

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

## Testing
```bash
mvn test
```

## Building
```bash
mvn clean package
```

## CI/CD
This project uses CircleCI for continuous integration. The pipeline runs tests and builds the application.
│           └── SKILL.md
├── CLAUDE.md
├── README.md
└── pom.xml
```

You can find the detailed explanation and description of that that template in my post [Claude Code Template for Spring Boot](https://piotrminkowski.com/2026/03/24/claude-code-template-for-spring-boot/).