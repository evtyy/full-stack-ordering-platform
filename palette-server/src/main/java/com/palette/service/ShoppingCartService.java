package com.palette.service;

import com.palette.dto.ShoppingCartDTO;
import com.palette.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * Add to shopping cart
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * View shopping cart
     * @return
     */
    List<ShoppingCart> showShoppingCart();

    /**
     * Clear shopping cart
     */
    void cleanShoppingCart();

    /**
     * Remove one item from the shopping cart
     * @param shoppingCartDTO
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
