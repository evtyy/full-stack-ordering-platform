import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getHistoryOrders } from "../api/order";
import type { OrderVO } from "../api/types";

const STATUS_LABELS: Record<number, string> = {
  1: "Pending payment",
  2: "Waiting for restaurant to confirm",
  3: "Confirmed",
  4: "Out for delivery",
  5: "Completed",
  6: "Cancelled",
};

export default function OrderHistory() {
  const [orders, setOrders] = useState<OrderVO[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    getHistoryOrders(1, 20)
      .then((page) => setOrders(page.records))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="checkout-page">
      <header className="menu-header">
        <button className="link-button" onClick={() => navigate("/")}>
          &larr; Back to menu
        </button>
        <h1>My orders</h1>
      </header>

      {loading && <p>Loading orders...</p>}
      {!loading && orders.length === 0 && <p>You haven't placed any orders yet.</p>}

      {orders.map((order) => (
        <div className="order-row" key={order.id}>
          <div>
            <strong>#{order.number}</strong>
            <p>{STATUS_LABELS[order.status] ?? "Unknown"}</p>
          </div>
          <span>${order.amount.toFixed(2)}</span>
        </div>
      ))}
    </div>
  );
}
