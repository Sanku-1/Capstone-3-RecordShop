package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    // Add a specific product to the cart
    void addItem(int userId, int productId);

    // Update the quantity of a specific product in the cart
    void updateItemQuantity(int userId, int productId, int quantity);

    // Remove a specific product from the cart
    void removeItem(int userId, int productId);

    // Clear all items from the user's cart
    void clearCart(int userId);

    // Check if a product exists in the user's cart
    boolean itemExists(int userId, int productId);
}
