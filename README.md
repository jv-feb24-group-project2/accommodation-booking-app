## 🏡 Welcome to Accommodation Booking App 🏠
*****
![img_house.jpg](img%2Fimg_house.jpg)
*****
This app is a booking accommodation application where people can advertise the places they want to rent out and where people looking for accommodations can search for them.  
Our goal? Streamline property management, empower customers, and redefine how you experience housing rentals.
Accommodation Booking App addresses key challenges:

For Customers: Simplifies finding accommodations as per their needs, provides Telegram Notifications Service for real-time updates and alerts, and ensures secure and cashless transactions through Stripe.

For Administrators: Provides comprehensive tools for inventory management, order booking, and real-time updates, making it easy to manage rental properties.

The Telegram Notifications Service is an integral part of our platform. It provides users with instant notifications about booking confirmations, payment statuses, new property listings, and important updates directly through Telegram. This service enhances user experience by keeping them informed and engaged without the need  to check the app constantly.  

Built with Spring Boot, Spring Security, Spring Data JPA, and Springdoc OpenAPI, the Accommodation Booking App offers a secure and robust backend that supports rental property operations.
### 🛠️ Technologies and Tools Used
- Spring Boot: For building the application framework.
- Spring Security: To handle authentication and authorization.
- Spring Data JPA: For database interactions using JPA.
- Spring Web: For building web-based applications with Spring MVC.
- Swagger: For API documentation and testing.
- Hibernate: As the ORM tool.
- MySQL: For the relational database.
- Liquibase: For database schema versioning and management.
- Docker: For containerizing the application.
- Maven: For project management and dependency management.  

### 🚀 Current Functionalities
- ##### Authentication

| HTTP method              | Endpoint             | Role  | Description                      |
|--------------------------|----------------------|-------|----------------------------------|
| POST                     | /api/auth/register   | ---   | Register a new user              |
| POST                     | /api/auth/login      | ---   | Authenticate a user              |
| GET                      | /api/users/me        | USER  | Get user profile information     |
| PUT                      | /api/users/me        | USER  | Update user profile information  |
| PUT                      | /api/users/{id}/role | ADMIN | Update user roles                |


- ##### Accommodations

| HTTP method | Endpoint                 | Role    | Description                  |
|-------------|--------------------------|---------|------------------------------|
| GET         | /api/accommodations      | All     | Get page of accommodations   |
| GET         | /api/accommodations/{id} | All     | Get accommodation by ID      |
| POST        | /api/accommodations      | MANAGER | Add a new accommodation      |
| PUT         | /api/accommodations/{id} | MANAGER | Update accommodation by ID   |
| DELETE      | /api/accommodations/{id} | MANAGER | Delete accommodation by ID   |


- ##### Amenity 

| HTTP method | Endpoint            | Role  | Description                       |
|-------------|---------------------|-------|-----------------------------------|
| GET         | /api/amenities      | ---   | Get all accommodations' amenities |
| GET         | /api/amenities/{id} | ---   | Get an amenity by id              |
| POST        | /api/amenities      | ADMIN | Add a new amenity                 |
| PUT         | /api/amenities/{id} | ADMIN | Update category by ID             |
| DELETE      | /api/amenities/{id} | ADMIN | Delete category by ID             |

- ##### Payment 
| HTTP method | Endpoint                                 | Role | Description                                            |
|-------------|------------------------------------------|------|--------------------------------------------------------|
| GET         | /api/payments                            | USER | Get payments history of certain customer               |
| GET         | /api/payments/{id}                       | USER | Get the payment by ID                                  |
| GET         | /api/payments/create-session/{bookingId} | ---  | Init payment sessions for booking transactions         |
| GET         | /api/payments/success/                   | ---  | Redirection after successfully processing of payment   |
| GET         | /api/payments/cancel/                    | ---  | Redirection after unsuccessfully processing of payment |

- ##### Booking
| HTTP method | Endpoint           | Role    | Description                                                           |
|-------------|--------------------|---------|-----------------------------------------------------------------------|
| POST        | /api/bookings      | USER    | Creat new accommodation bookings                                      |
| GET         | /api/bookings/my   | USER    | Get booking history of current customer                               |
| GET         | /api/bookings/{id} | MANAGER | Get booking by ID                                                     |
| GET         | /api/bookings      | MANAGER | Get page of bookings with filtration by user ID and/or booking status |
| PUT         | /api/bookings/{id} | MANAGER | Update booking by ID                                               |
| DELETE      | /api/bookings/{id} | MANAGER | Delete booking by ID                                               |

  
- #####  Swagger Documentation
    - the API documentation at http://localhost:8080/swagger-ui.html.
    - You can explore all available endpoints, their descriptions, request parameters, and response schemas.  
      **[Use Postman collection](https://rent-masters.postman.co/workspace/Accommodation-Booking-APP~2719fff1-5c95-44a8-8913-0a604ac879f4/overview)**
      

### 🌟 Getting Started
##### Prerequisites
- Java 21+
- Maven 4+
- MySQL 8+
- Docker
##### Installation
To run the application locally, follow these steps:

1. Clone the Repository
```
git clone https://github.com/jv-feb24-group-project2/accommodation-booking-app.git 
cd accommodation-booking-app
```
2. Configure environment variables:
   Create a .env file in the project root directory and populate it with the following environment variables:
```env
- DB_USERNAME=your_db_username  
- DB_PASSWORD=your_db_password  
- DB_LOCAL_PORT=3306
- DB_NAME=easy_stay
- STRIPE_API_KEY=sk_test_00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000

- BOT_USERNAME=your-bot-username
- BOT_TOKEN=your-bot-token
- BASE_URL=your-base-url
- JWT_SECRET=topsecret2024topsecret2024topsecret2024topsecret2024
```
4. Install dependencies and build the project:
```
mvn clean install
```
5. Run the application:
```
mvn spring-boot:run
```
The server will start on http://localhost:8080.

##### Using Docker
1. Build the Docker image:
```
docker build -t accommodation-booking-app .
```
2. Build and Run the Docker Containers:
```
docker-compose up
```
3. Stop and Remove Containers:
```
docker-compose down
```

##### 📄 Challenges and Solutions
Challenge 1: Securing the API
Solution: Implemented Spring Security to manage authentication and authorization, ensuring that sensitive endpoints are protected.

Challenge 2: Database Management
Solution: Utilized Spring Data JPA and Hibernate for efficient database interactions and ORM capabilities.

Challenge 3: Telegram Notifications Service
Solution: Integrated Telegram Notifications Service to provide users instant notifications about booking confirmations, payment statuses, new property listings, and important updates directly through Telegram.

Challenge 4: Stripe
Solution: Understanding the intricacies of payment session creation proved crucial to ensuring a seamless payment process.

### 👥 Contributors:
- Artem Akymenko **[LinkedIn](https://www.linkedin.com/in/artem-akymenko-4b143030a/)** , **[GitHub](https://github.com/k0sm0naft)** 
- Taras Fedak **[LinkedIn](https://www.linkedin.com/in/taras-fedak-a4535b147/)** , **[GitHub](https://github.com/fedaktaras)** 
- Serhii Gainovskyi **[LinkedIn](https://www.linkedin.com/in/serj-gainovskyi-58b631215/)** , **[GitHub](https://github.com/JohnGrey17)** 
- Pavlo Serikov **[LinkedIn](https://www.linkedin.com/in/pavlo-serikov/)** , **[GitHub](https://github.com/rel1c-hub)** 
- Hanna Ratushniak **[LinkedIn](https://www.linkedin.com/in/hanna-ratushnyak/)** , **[GitHub](https://github.com/AnyaRatusnyak)** 