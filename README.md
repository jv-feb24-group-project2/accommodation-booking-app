## 🏡 Welcome to Accommodation Booking App 🏠
*****
![img_house.jpg](img%2Fimg_house.jpg)
*****
This app is a booking accommodation application where people can advertise the places they want to rent out and where people looking for accommodations can search for them.  
Our goal? Streamline property management, empower customers, and redefine how you experience housing rentals.
Accommodation Booking App addresses key challenges:

For Customers: Simplifies finding accommodations as per their needs, provides Telegram Notifications Service for real-time updates and alerts, and ensures secure and cashless transactions through Stripe.

For Administrators: Provides comprehensive tools for inventory management, order booking, and real-time updates, making it easy to manage rental properties.

The Telegram Notifications Service is an integral part of our platform, providing users with instant notifications about booking confirmations, payment statuses, new property listings, and important updates directly through Telegram. This service enhances user experience by keeping them informed and engaged without the need to constantly check the app.   

Built with Spring Boot, Spring Security, Spring Data JPA, and Springdoc OpenAPI, Accommodation Booking App offers a secure and robust backend, perfect for supporting the operations of rental properties.
### 🛠️ Technologies and Tools Used
- Spring Boot: For building the application framework.
- Spring Security: To handle authentication and authorization.
- Spring Data JPA: For database interactions using JPA.
- Spring Web: For building web-based applications with Spring MVC.
- Swagger: For API documentation and testing.
- Hibernate: As the ORM tool.
- H2: For the relational database.
- Liquibase: For database schema versioning and management.
- Docker: For containerizing the application.
- Maven: For project management and dependency management.  

### 🚀 Current Functionalities
- ##### Authentication
    - Register a new user: POST /api/auth/register
    - Authenticate a user: POST /api/auth/login
- ##### Accommodation
    - Get accommodations from catalog: GET /api/accommodations
    - Get accommodation for id: GET /api/accommodations/{id}
    - Create a new accommodation(only for roles MANAGER and ADMIN): POST /api/accommodations
    - Update a specific accommodation(only for role ADMIN): PUT /api/accommodations/{id}
    - Delete a specific accommodation (only for role ADMIN): DELETE /api/accommodations/{id}
- ##### Amenity (only for roles MANAGER and ADMIN)
    - Get all accommodations' amenities: GET /api/amenities
    - Get an amenity by id: GET /api/amenities/{id}
    - Add a new amenity: POST /api/amenities
    - Update a current category: PUT /api/amenities/{id}
    - Delete a current category: /api/amenities/{id}
- ##### Shopping Cart
    - Get a user's shopping cart: GET /api/cart
    - Add book to the shopping cart: POST /api/cart
    - Update quantity of a book in the shopping cart: PUT /api/cart/cart-items/{cartItemId}
    - Remove a book from the shopping cart: DELETE /api/cart/cart-items/{cartItemId}
- ##### Payment
    - Create an order: POST /api/orders
    - Get history of orders: GET /api/orders
    - Get all OrderItems for a specific order: GET /api/orders/{orderId}/items
    - Get a specific OrderItem within an order: GET /api/orders/{orderId}/items/{itemId}
    - Update order status: PATCH /api/orders/{id}
- #####  Swagger Documentation
    - the API documentation at http://localhost:8080/swagger-ui.html.
    - You can explore all available endpoints, their descriptions, request parameters, and response schemas.

How to use demo:
Use Swagger UI
Use Postman collection

If you want to use manager's endpoints, fill free to use credentials below:

{
"email":"manager@example.com",
"password":"12345678"
}
Note: The token expires after 1 hour, so you must log in again.

🛠️Technologies Used
Spring Boot: Framework for building Java-based web applications.
Spring Security: Provides authentication and authorization.
Spring Data JPA: Simplifies database interactions.
Spring Web: For building web-based applications with Spring MVC.
Springdoc OpenAPI: Generates API documentation.
MySQL: Database management system.
Hibernate: As the ORM tool.
Liquibase: Database migration tool.
MapStruct: Object mapping framework.
JUnit: Testing framework.
Mockito: Mocking framework for tests.
Maven: For project management and dependency management.
Testcontainers: Provides lightweight, throwaway instances of databases, selenium web servers, or anything else that can run in a Docker container.
Docker: Containerization platform.
🌎Features
📝Swagger Documentation
Swagger is integrated into the project to provide comprehensive API documentation and facilitate testing of endpoints. With Swagger, you can visualize and interact with the API's resources without having any of the implementation logic in place.

To access the Swagger UI and explore the API documentation:

Once the application is running, navigate to http://localhost:8080/api/swagger-ui/index.html in your web browser.

You will see the Swagger UI interface, where you can view all available endpoints, their descriptions, request parameters, and response schemas.

Use the interactive features of Swagger UI to make requests directly from the browser and observe the responses.

🔑Authentication
Authentication Endpoints: Users can register and log in.
📗Book
User's Endpoints: View and search books.
Manager's and Admin's Endpoints: Add, update, and delete books.
📑Category
User's Endpoints: View categories.
Manager's and Admin's Endpoints: Create, update, and delete book categories.
🛒Shopping Cart
User's Endpoints: Add books to the cart, view cart items, update and remove items from the cart.
🛍️Order
User's Endpoints: Complete the purchase, view past orders and their details.
Manager's and Admin's Order Endpoints: Update order statuses.
🚀Setup Instructions
🧰Required
Docker
Docker Compose
🏗️Installation
Clone the Repository:

git clone https://github.com/k0sm0naft/java-online-book-store.git
cd java-online-book-store
Set Environment Variables:

Create a .env file in the project root directory and populate it with the following environment variables:

MYSQLDB_USER=your_db_user_name
MYSQLDB_ROOT_PASSWORD=your_db_password
JWT_SECRET=yourVeryLongSecretStringForJwtSecretKey
JWT_EXPIRATION=3600000

MYSQLDB_DATABASE=your_db_name
MYSQLDB_LOCAL_PORT=3306
MYSQLDB_DOCKER_PORT=3306

SPRING_LOCAL_PORT=8080
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
Install dependencies and build the project:

mvn clean install
Build and Run the Docker Containers:

docker-compose up
Access the Application:

Open your browser and go to http://localhost:8080/api/swagger-ui.html to access the Swagger API documentation.

Stop and Remove Containers: To stop and remove the containers created by the Compose file, use the docker-compose down command:

docker-compose down
🧪Running Tests
To run tests, use the following command:

mvn test
Testing with JUnit and Mockito

The project uses JUnit for unit testing and Mockito for mocking dependencies. This ensures that the application logic is tested in isolation, making the tests more reliable and easier to maintain.

📬Postman Collection
To facilitate testing, a Postman collection has been provided. You can import it into Postman and use it to test the API endpoints.

Run in Postman

To chose URL:
After you fork collection, you can register a new user, or use credentials below in the text.
After log in, the generated token will be automatically added to all next requests that required authentication.
User
Manager
👊Challenges Faced
Security: Setting up robust authentication and authorization using Spring Security.

Database Management: Utilized Spring Data JPA and Hibernate for efficient database interactions and ORM capabilities.

Database Migrations: Implementing database migrations with Liquibase to ensure consistency across different environments.

API Documentation: Ensuring comprehensive API documentation with Springdoc OpenAPI.

👷Author
LinkedIn: Artem Akymenko
Github: @k0sm0naft