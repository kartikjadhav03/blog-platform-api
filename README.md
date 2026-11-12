# Blog Platform REST API

A RESTful Blog Platform API built using Java and Spring Boot. This project provides APIs for user authentication, blog post management, categories, tags, and comments.

## Features

- User registration and login
- JWT-based authentication
- Password encryption using BCrypt
- Post ownership authorization with ADMIN override
- Create, read, update, and delete blog posts
- Category creation and retrieval
- Tag creation and retrieval
- Comment creation and retrieval by post
- Pagination and sorting
- Search and filtering by title, category, and tag
- Input validation
- Global exception handling
- API documentation using Swagger/OpenAPI

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.1.1
- **Security:** Spring Security, JWT
- **Database:** MySQL
- **ORM:** Spring Data JPA, Hibernate
- **Build Tool:** Maven
- **Password Encryption:** BCrypt
- **API Documentation:** Swagger/OpenAPI
- **Testing:** JUnit, Mockito, H2 Database

## Project Structure

```text
src/main/java/com/kartik/blog_platform_api
│
├── BlogPlatformApiApplication.java
│
├── config
│   ├── OpenApiConfig.java
│   ├── PasswordConfig.java
│   └── SecurityConfig.java
│
├── controller
│   ├── CategoryController.java
│   ├── CommentController.java
│   ├── HelloController.java
│   ├── PostController.java
│   ├── TagController.java
│   └── UserController.java
│
├── dto
│   ├── CategoryRequestDto.java
│   ├── CategoryResponseDto.java
│   ├── CommentRequestDto.java
│   ├── CommentResponseDto.java
│   ├── LoginRequestDto.java
│   ├── LoginResponseDto.java
│   ├── PostRequestDto.java
│   ├── PostResponseDto.java
│   ├── TagRequestDto.java
│   ├── TagResponseDto.java
│   ├── UserRequestDto.java
│   └── UserResponseDto.java
│
├── entity
│   ├── Category.java
│   ├── Comment.java
│   ├── Post.java
│   ├── PostStatus.java
│   ├── Role.java
│   ├── Tag.java
│   └── User.java
│
├── exception
│   ├── ApiErrorResponse.java
│   ├── EmailAlreadyExistsException.java
│   ├── GlobalExceptionHandler.java
│   └── InvalidCredentialsException.java
│
├── repository
│   ├── CategoryRepository.java
│   ├── CommentRepository.java
│   ├── PostRepository.java
│   ├── TagRepository.java
│   └── UserRepository.java
│
├── security
│   └── JwtAuthenticationFilter.java
│
└── service
    ├── CategoryService.java
    ├── CommentService.java
    ├── JwtService.java
    ├── PostService.java
    ├── TagService.java
    ├── UserService.java
    └── WelcomeService.java
```

## Prerequisites

Make sure you have the following installed:

- Java 21 or later
- MySQL
- Maven (optional, because the project includes Maven Wrapper)
- IntelliJ IDEA or another Java IDE

## Database Setup

1. Open MySQL.
2. Create a database:

```sql
CREATE DATABASE blog_platform;
```

3. Configure your MySQL database username and password in:

`src/main/resources/application.properties`

4. Set the `JWT_SECRET` environment variable before running the application.

**Important:** Never store your real JWT secret in the source code or commit it to GitHub.

## Run the Application

Clone the repository:

```bash
git clone https://github.com/kartikjadhav03/blog-platform-api.git
```

Navigate to the project directory:

```bash
cd blog-platform-api
```

Set your JWT secret environment variable:

```bash
export JWT_SECRET="replace-with-a-strong-secret"
```

Run the application:

```bash
./mvnw spring-boot:run
```

The application will start at:

`http://localhost:8080`

## API Documentation

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

OpenAPI JSON:

`http://localhost:8080/v3/api-docs`

## Authentication

The application uses JWT-based authentication.

1. Register a new user.
2. Log in using registered credentials.
3. Receive a JWT access token.
4. Include the token in the Authorization header when accessing protected endpoints.

Example:

```http
Authorization: Bearer <your-jwt-token>
```

Public endpoints include user registration, login, and public post retrieval.

Creating, updating, and deleting posts require authentication. Users can modify or delete their own posts, while ADMIN users can modify or delete any post.

## Testing

Run all tests using Maven Wrapper:

```bash
./mvnw test
```

The project uses:

- JUnit for testing
- Mockito for mocking dependencies
- H2 in-memory database for testing

## Author

**Kartik Jadhav**

[GitHub](https://github.com/kartikjadhav03) | [LinkedIn](https://www.linkedin.com/in/kartik-jadhav-22a0b2246/)

Java Backend Developer | Java | Spring Boot | REST APIs | MySQL