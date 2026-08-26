import { api } from "./client";
import type { OrderSubmitVO, OrderVO, PageResult, StripeCheckoutVO } from "./types";

export interface SubmitOrderInput {
  addressBookId: number;
  payMethod: number;
  remark?: string;
  deliveryStatus: number;
  tablewareNumber: number;
  tablewareStatus: number;
  packAmount: number;
  amount: number;
}

export function submitOrder(order: SubmitOrderInput) {
  return api.post<OrderSubmitVO>("/user/order/submit", order);
}

export function createCheckoutSession(orderNumber: string) {
  return api.post<StripeCheckoutVO>("/user/order/checkout-session", { orderNumber });
}

export function confirmCheckoutSession(sessionId: string) {
  return api.get<boolean>("/user/order/checkout-session/confirm", { sessionId });
}

export function getOrderDetail(id: number) {
  return api.get<OrderVO>(`/user/order/orderDetail/${id}`);
}

export function getHistoryOrders(page: number, pageSize: number, status?: number) {
  return api.get<PageResult<OrderVO>>("/user/order/historyOrders", { page, pageSize, status });
}

export function cancelOrder(id: number) {
  return api.put<string>(`/user/order/cancel/${id}`);
}
