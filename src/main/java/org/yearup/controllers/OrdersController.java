package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;

@RestController
@RequestMapping("/orders")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrdersController {
    private OrderDao orderDao;
    private UserDao userDao;
    private ProfileDao profileDao;
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    public OrdersController(OrderDao orderDao, UserDao userDao, ProfileDao profileDao, ShoppingCartDao shoppingCartDao)
    {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
        this.shoppingCartDao = shoppingCartDao;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order checkout(Principal principal)
    {
        try {

            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);
            Profile profile = profileDao.getByUserId(userId);

            Order order = new Order();
            order.setUserId(userId);
            order.setDate(Date.valueOf(LocalDate.now()));
            order.setAddress(profile.getAddress());
            order.setCity(profile.getCity());
            order.setState(profile.getState());
            order.setZip(profile.getZip());
            order.setShippingAmount(BigDecimal.valueOf(10.0));

            Order createdOrder = orderDao.create(order);

            for (ShoppingCartItem shoppingCartItem : shoppingCart.getItems().values()) {
                OrderLineItem lineItem = new OrderLineItem();
                lineItem.setOrderId(createdOrder.getOrderId());
                lineItem.setProductId(shoppingCartItem.getProductId());
                lineItem.setSalesPrice(shoppingCartItem.getProduct().getPrice());
                lineItem.setQuantity(shoppingCartItem.getQuantity());
                lineItem.setDiscount(shoppingCartItem.getDiscountPercent());

                orderDao.addOrderLineItem(lineItem);
            }

            shoppingCartDao.clearCart(userId);

            return createdOrder;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad: ");
        }
    }
}
