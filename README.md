# Spring Boot E-Commerce API

A modular and extendable backend API for an e-commerce application built with **Java** and **Spring Boot**. The system supports product browsing, user authentication, shopping cart management, order processing, and sandbox payment integration via **Zarinpal**.

---

## Features

-  **Authentication & Authorization**
  - User registration and login
  - JWT-based stateless authentication
  - Role-based access (User/Admin-ready)

-  **Product Management**
  - CRUD operations for products and categories
  - Category-product relation
  - Admin-only endpoints ready for extension

-  **Shopping Cart**
  - Add, view, and remove items in the cart
  - Cart linked to authenticated user

-  **Order System**
  - Convert cart to orders
  - Order history per user
  - Order item details stored at time of purchase

-  **Payment Integration (Zarinpal Sandbox)**
  - Initiate payments via Zarinpal sandbox
  - Verify transactions and update order status

---

##  Tech Stack

- Java 17+
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- PostgreSQL / H2
- Lombok
- Zarinpal REST API (sandbox)
- Maven
- Postman (for API testing)

---

##  Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/springboot-ecommerce.git
cd springboot-ecommerce
