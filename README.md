# Project Title
EasyShop - Record Shop API

## Description of the Project

EasyShop Record Shop is an e-commerce application that allows users to browse and purchase vinyl records, CDs, and music accessories. This project serves as the backend API built with Spring Boot and MySQL. The API handles user authentication, product browsing by category, shopping cart management, user profiles, and order checkout.

The application supports two user roles, customers and administrators. Administrators can manage product categories and inventory, while customers can browse products, manage their shopping carts, update their profiles, and complete purchases.

For this project, I assumed the role of a backend developer for an existing website. The work included fixing bugs in the existing code, implementing the CategoriesController, building out the shopping cart feature, creating user profile management, and implementing the checkout/orders functionality.

## User Stories

- As a user, I want to view all products within a specific category, so that I can find items I'm interested in purchasing.
- As an admin, I want to add new categories to the store, so that I can expand the product offerings.
- As an admin, I want to update existing categories, so that I can keep category information accurate and up-to-date.
- As an admin, I want to delete categories that are no longer needed, so that I can keep the store organized.
- As a user, I want the product search/filter to return correct results when filtering by category, price range, and subcategory, so that I can find products that match my criteria.
- As a developer, I want product updates to modify the existing product (not create duplicates), so that inventory remains accurate.
- As a user, I want to view my shopping cart, so that I can see all items I've added for purchase along with quantities and totals.
- As a logged-in user, I want to add products to my cart by product ID, so that I can collect items before checkout.
- As a user, I want to clear all items from my cart, so that I can start fresh with a new order.
- As a developer, I want user's carts to persist in the database between sessions, so that I don't lose my selections when I log out and back in.
- As a user, I want to view my profile information, so that I can verify my account details.
- As a user, I want to update my profile information, so that I can keep my shipping and contact information current for orders.
- As a user, I want to checkout and convert my shopping cart into an order, so that I can complete my purchase.
- As a user, I want my shopping cart cleared after successful checkout, so that I can start fresh for my next order.

## Setup

Instructions on how to set up and run the project using IntelliJ IDEA.

### Prerequisites

- IntelliJ IDEA: Ensure you have IntelliJ IDEA installed
- Java SDK: Make sure Java SDK 17 is installed and configured in IntelliJ.
- MySQL: Install MySQL Server and ensure it's running on your local machine.
- Insmonia: Recommended for testing API endpoints.

### Database Setup

1. Open MySQL Workbench
2. Execute the `database/create_database_recordshop.sql` script to create the `recordshop` database
3. The script will create all necessary tables and populate sample data including:
   - 3 sample users (user, admin, george) - password for all is `password`
   - 3 categories (Vinyl Records, CDs, Music Accessories)
   - 60+ products across all categories
   - Sample shopping cart items

### Running the Application in IntelliJ

Follow these steps to get your application running within IntelliJ IDEA:

1. Open IntelliJ IDEA.
2. Select "Open" and navigate to the directory where you cloned or downloaded the project.
3. After the project opens, wait for IntelliJ to index the files and set up the project.
4. Ensure Maven dependencies are downloaded (IntelliJ should do this automatically).
5. Update `src/main/resources/application.properties` with your MySQL credentials.
6. Find the main class: `src/main/java/org/yearup/EasyshopApplication.java`
7. Right-click on the file and select 'Run 'EasyshopApplication.main()'' to start the application.
8. The API will be available at `http://localhost:8080`

## Technologies Used

- Java 17
- Spring Boot 2.7.3
- Spring Security with JWT Authentication
- MySQL 8.0.33
- Maven
- Insomnia

## Demo

### Screenshots
[All Products](https://github.com/Sanku-1/Capstone-3-RecordShop/blob/master/src/main/resources/allproducts.png) \
[Category Search](https://github.com/Sanku-1/Capstone-3-RecordShop/blob/master/src/main/resources/category%20search.png) \
[Login](https://github.com/Sanku-1/Capstone-3-RecordShop/blob/master/src/main/resources/recordshoplogin.png) \
[Cleared Shopping Cart](https://github.com/Sanku-1/Capstone-3-RecordShop/blob/master/src/main/resources/emptycart.png)

## Interesting Code

### Shopping Cart DAO - Add Item Method

One of the most interesting pieces of code in this project is the `addItem` method in `MySqlShoppingCartDao`. 

```java
@Override
public void addItem(int userId, int productId) {
    if (itemExists(userId, productId)) {
        String query = "UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id = ? AND product_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } else {
        String query = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, 1)";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

This code is interesting because it:
1. Checks if the item already exists before deciding to INSERT or UPDATE
2. Uses `quantity + 1` in the SQL to increment the existing quantity
3. Keeps the cart organized by not creating duplicate entries for the same product
4. Provides a smooth user experience when adding items multiple times

## Future Work

Potential future enhancements or functionalities:

- **Order History:** Display past orders for logged-in customers with order details
- **Wishlist Feature:** Allow users to save products for future purchase
- **Product Reviews:** Enable customers to rate and review products
- **Advanced Search:** Implement search with autocomplete suggestions
- **Discount Codes:** Implement promotional codes and discount functionality

## Resources

- LTCA Java Development Workbooks
- Insomnia for API Testing
- [Spring Security Guide](https://www.baeldung.com/spring-security-method-security)
- [SQL Date Methods](https://docs.oracle.com/javase/8/docs/api/java/sql/Date.html)
- [HTTP Status Codes](https://http.cat/)


## Team Members

-  **Raymond Maroun** - Provided starter code and framework for project
-  **Stephen Anku** - Implemented all remaining functionality in project, most notable creating the checkout process.

## Thanks

- Thank you to **Raymond Maroun** for continuous support and guidance throughout my Java learning journey.
- Appreciation given to Roger Su for support with this project.
