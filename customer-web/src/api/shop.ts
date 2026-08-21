import { api } from "./client";

const SHOP_OPEN = 1;

export function getShopStatus() {
  return api.get<number>("/user/shop/status").then((status) => status === SHOP_OPEN);
}
