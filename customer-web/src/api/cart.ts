import { api } from "./client";
import type { ShoppingCartItem } from "./types";

export interface CartItemInput {
  dishId?: number;
  setmealId?: number;
  dishFlavor?: string;
}

export function getCart() {
  return api.get<ShoppingCartItem[]>("/user/shoppingCart/list");
}

export function addToCart(item: CartItemInput) {
  return api.post<void>("/user/shoppingCart/add", item);
}

export function subFromCart(item: CartItemInput) {
  return api.post<void>("/user/shoppingCart/sub", item);
}

export function clearCart() {
  return api.delete<void>("/user/shoppingCart/clean");
}
