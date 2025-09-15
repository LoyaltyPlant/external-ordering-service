# External Ordering Service

## Overview
The External Ordering Service is a template microservice designed to act as an integration layer between external Point of Sale (POS) systems and the LoyaltyPlant platform. It provides a standardized API for order management, menu retrieval, and order status updates.

Protocol description with examples can be found [here](https://loyaltyplant.atlassian.net/wiki/spaces/INTG/pages/1212450/Loyalty+program+public+API+Digital+ordering).

## Key Features

### 1. Order Management
- Create new orders in the system
- Retrieve order details and status
- Handle order status updates via webhooks

### 2. Menu Integration
- Fetch current menu with all available items and categories

### 3. System Health
- Health check endpoints for monitoring
- Service status reporting
- Integration with monitoring systems

### 4. Security
- Request authentication using tokens
- Sales outlet identification
- Secure communication channels

### 5. Database (Optional)
The service includes optional database support. If your implementation doesn't require database functionality, you can safely remove:

- Database configuration classes
- Liquibase migration dependencies
- Other database-related configurations

## API Endpoints

### Health Check
- `GET /api/v2/healthcheck` - Verify service status

### Order Operations
- `POST /api/v2/orders` - Retrieve list of orders
- `POST /api/v2/order` - Create a new order

### Menu Operations
- `GET /api/v2/menu` - Get current menu with items and categories

### Webhook
- `POST /api/v2/webhook/orders` - Receive order status updates

## Authentication
All API endpoints require authentication using the following headers:
- `SalesOutletId`: Unique identifier for the sales outlet
- `AuthorizationToken`: Authentication token for the request

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- (Optional) PostgreSQL if database functionality is enabled

### Configuration
Service configuration can be managed through:
- Application properties
- Environment variables
- Consul configuration (when deployed in a Consul-enabled environment)

### Running the Service
```bash
mvn spring-boot:run