# Herbal Calendar API

A RESTful backend application built with **Java 21** and **Spring Boot** for managing medicinal herbs, users, and personal herb collections.

The application provides secure authentication using JWT, allows users to browse information about herbs, manage their personal collections, and exposes a fully documented REST API with Swagger/OpenAPI.

## Features

- User registration and authentication
- JWT-based authorization
- User management (CRUD operations)
- Herb management (CRUD operations)
- Personal herb collection management
- RESTful API design
- Request validation
- Global exception handling
- API documentation with Swagger/OpenAPI
- Unit tests for REST controllers

## Project Architecture

The application follows a layered architecture:

- **Controllers** – expose REST endpoints and handle HTTP requests.
- **Services** – implement business logic.
- **Repositories** – manage database access using Spring Data JPA.
- **Models** – represent the application's domain entities.
- **DTOs** – transfer data between the client and the application.
- **Security** – JWT authentication and authorization.
- **Exception Handling** – centralized exception management.

## Main Entities

- User
- Herb
- UserHerb
- Location

## Security

The application uses **Spring Security** with **JWT (JSON Web Token)** authentication.

Features include:

- User login
- Token generation
- Token validation
- Authorization using Bearer Tokens
- Protected API endpoints

## API Documentation

The project includes **Swagger/OpenAPI** integration for interactive API documentation and endpoint testing.

## Technologies

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Security
- JWT Authentication
- Hibernate
- Spring Validation
- MySQL
- H2 Database
- Swagger / OpenAPI
- Lombok
- JUnit 5
- Mockito
- MockMvc
- Maven

## Project Goal

The goal of this project was to develop a secure RESTful backend application for managing medicinal herbs and personal herb collections. The application demonstrates modern backend development practices, including layered architecture, JWT authentication, REST API design, request validation, exception handling, API documentation, database persistence, and controller testing.
