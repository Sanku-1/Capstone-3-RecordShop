package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao
{
    private ProductDao productDao;

    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) { super(dataSource); }


    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        String query = "SELECT * FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int productId = resultSet.getInt("product_id");
                    int quantity = resultSet.getInt("quantity");

                    Product product = productDao.getById(productId);

                    if (product != null) {
                        ShoppingCartItem item = new ShoppingCartItem();
                        item.setProduct(product);
                        item.setQuantity(quantity);

                        cart.add(item);
                        }
                    }
                }
            } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the SQL exception.
        }
        return cart;
    }

    @Override
    public void addItem(int userId, int productId) {
        if (itemExists(userId, productId))
        {
            String query = "UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id = ? AND product_id = ?";

            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.setInt(1, userId);
                statement.setInt(2, productId);
                statement.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace(); // Log or handle the SQL exception.
            }
        }
        else
        {
            String query = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, 1)";

            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.setInt(1, userId);
                statement.setInt(2, productId);
                statement.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace(); // Log or handle the SQL exception.
            }
        }
    }

    @Override
    public void updateItemQuantity(int userId, int productId, int quantity) {
        if (!itemExists(userId, productId)) {
            return;
        }

        if (quantity < 1) {
            removeItem(userId, productId);
        } else {
            String query = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, quantity);
                statement.setInt(2, userId);
                statement.setInt(3, productId);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace(); // Log or handle the SQL exception.            }
            }
        }
    }

    @Override
    public void removeItem(int userId, int productId) {
        String query = "DELETE FROM shopping_cart WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace(); // Log or handle the SQL exception.
        }
    }

    @Override
    public void clearCart(int userId) {

    }

    @Override
    public boolean itemExists(int userId, int productId) {
        String query = "SELECT COUNT(*) FROM shopping_cart WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, userId);
            statement.setInt(2, productId);

            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace(); // Log or handle the SQL exception.
        }

        return false;
    }
}
