import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { cancelOrder, getHistoryOrders } from "../api/order";
import type { OrderVO } from "../api/types";

const STATUS_LABELS: Record<number, string> = {
  1: "Pending payment",
  2: "Waiting for restaurant to confirm",
  3: "Confirmed",
  4: "Out for delivery",
  5: "Completed",
  6: "Cancelled",
};

// Matches the order-status check in OrderServiceImpl.userCancelById (statuses > 2 can't be cancelled)
const CANCELLABLE_STATUSES = new Set([1, 2]);

export default function OrderHistory() {
  const [orders, setOrders] = useState<OrderVO[]>([]);
  const [loading, setLoading] = useState(true);
  const [confirmingId, setConfirmingId] = useState<number | null>(null);
  const [cancellingId, setCancellingId] = useState<number | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    getHistoryOrders(1, 20)
      .then((page) => setOrders(page.records))
      .finally(() => setLoading(false));
  }, []);

  async function handleCancel(id: number) {
    setCancellingId(id);
    try {
      await cancelOrder(id);
      setOrders((prev) => prev.map((o) => (o.id === id ? { ...o, status: 6 } : o)));
    } catch (err) {
      alert(err instanceof Error ? err.message : "Failed to cancel order");
    } finally {
      setCancellingId(null);
      setConfirmingId(null);
    }
  }

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

          {confirmingId === order.id ? (
            <span>
              Cancel this order?{" "}
              <button
                className="link-button"
                disabled={cancellingId === order.id}
                onClick={() => handleCancel(order.id)}
              >
                {cancellingId === order.id ? "Cancelling..." : "Yes"}
              </button>{" "}
              <button className="link-button" onClick={() => setConfirmingId(null)}>
                No
              </button>
            </span>
          ) : (
            <>
              <span>${order.amount.toFixed(2)}</span>
              {CANCELLABLE_STATUSES.has(order.status) && (
                <button className="link-button" onClick={() => setConfirmingId(order.id)}>
                  Cancel order
                </button>
              )}
            </>
          )}
        </div>
      ))}
    </div>
  );
}
