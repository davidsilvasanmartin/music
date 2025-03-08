# Music Library Management System

## Overview
A comprehensive music library management system built with modern technologies, providing secure access to music collections with advanced features for managing albums, songs, and user permissions.

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.4.0-M2
- **Language**: Java 22
- **Database**: SQLite with JPA/Hibernate
- **Security**: Spring Security with Role-Based Access Control (RBAC)
- **Build Tool**: Maven

### Frontend
- **Framework**: Angular 18.2.0
- **Language**: TypeScript 5.5.2
- **State Management**: NgRx/Store
- **Styling**: TailwindCSS
- **Testing**: Karma/Jasmine
- **Development Tools**:
  - ESLint for code quality
  - Prettier for formatting
  - Angular CLI for development workflow

### DevOps
- **Containerization**: Docker with docker-compose
- **Infrastructure**:
  - Redis for session management (planned)
  - Custom network configuration
  - Persistent volume management
- **Environment Management**:
  - Development environment configuration
  - Service isolation
  - Port mapping and networking

## Core Features

### Music Management
1. **Album Management**
   - Album metadata (artist, title, genre, year)
   - Album artwork support
   - One-to-many relationship with songs

2. **Song Management**
   - Song metadata (title, lyrics)
   - File path management
   - Multiple audio format support (MP3, FLAC, M4A, OGG)
   - Streaming capabilities

### Security System
1. **User Management**
   - User registration and authentication
   - Email and username uniqueness
   - Account enable/disable functionality
   - Password encryption

2. **Role-Based Access Control (RBAC)**
   - Hierarchical permission system
   - Role management
   - Granular permissions
   - User-Role assignments

3. **Permission System**
   - Fine-grained access control
   - Custom permission definitions
   - Role-Permission mappings

### Audit System
- Comprehensive audit logging
- Track all system actions
- Record changes (old and new values)
- User action tracking
- Timestamp-based audit trail

## API Endpoints

### Album Endpoints
- GET /albums - List albums (with pagination and sorting)
- GET /albums/{id} - Get specific album
- GET /albums/{id}/albumArt - Get album artwork

### Song Endpoints
- GET /songs/{id} - Get song details
- GET /songs/{id}/album - Get album for song
- GET /songs/{id}/albumArt - Get album art for song
- GET /songs/{id}/play - Stream song audio

### User Management Endpoints
- GET /users - List users (with pagination and sorting)
- GET /users/{username} - Get user details
- POST /users - Create new user
- DELETE /users/{username} - Delete user
- PUT /users/{username}/roles/{roleName} - Assign role to user

### Authentication Endpoints
- POST /auth/login - User login
- GET /auth/user - Get current user info

## Security Features
1. **Session Management**
   - Session-based authentication
   - Maximum sessions per user
   - Session invalidation on logout

2. **CORS Configuration**
   - Configurable CORS policies
   - Secure cross-origin requests

3. **Access Control**
   - URL-based security rules
   - Method-level security
   - Role-based endpoint access

## Data Model

### Music Entities
1. **Album**
   - ID (Primary Key)
   - Album Artist
   - Album Title
   - Genre
   - Year
   - Art Path
   - Songs (One-to-Many)

2. **Song**
   - ID (Primary Key)
   - Title
   - File Path
   - Lyrics
   - Album (Many-to-One)

### Security Entities
1. **User**
   - ID (Primary Key)
   - Username (Unique)
   - Email (Unique)
   - Password
   - Enabled Status
   - Created/Updated Timestamps
   - Roles (Many-to-Many)

2. **Role**
   - ID (Primary Key)
   - Name (Unique)
   - Description
   - Created/Updated Timestamps
   - Permissions (Many-to-Many)

3. **Permission**
   - ID (Primary Key)
   - Name (Unique)
   - Description
   - Created/Updated Timestamps

### Audit Entity
- ID (Primary Key)
- Action
- Entity Type
- Entity ID
- User ID
- Old Value
- New Value
- Created Timestamp
- Description

## Development Features
1. **Testing Support**
   - TestNG framework for test organization
   - REST-assured for API testing
   - H2 database for testing
   - Comprehensive test coverage:
     - Authentication and authorization
     - Album and song management
     - User operations
     - Search functionality
   - Integration tests for:
     - Error handling
     - Session management
     - Resource access control
     - Input validation

2. **Monitoring**
   - Spring Actuator integration
   - System health monitoring
   - Performance metrics

3. **Documentation**
   - API documentation
   - Code documentation
   - Development guidelines

## Future Enhancements (TODOs)
1. **Security Improvements**
   - CSRF protection implementation
   - Enhanced session management
   - Request validation
   - Standard security filter usage

2. **Feature Additions**
   - Playlist management
   - User configurations
   - Enhanced audit logging
   - Metrics with Micrometer

3. **Technical Improvements**
   - Redis session management consideration
   - Enhanced error handling
   - Performance optimizations
   - Additional audio format support

## Project Structure
```
music/
├── back-end/               # Main backend application
├── front-end/              # Frontend application
├── tests/                  # Test suite
├── databases/              # Database files
├── config/                 # Configuration files
└── docker-compose.yml      # Docker configuration
```

## Getting Started
(To be added: Setup instructions, configuration details, and deployment guidelines)
