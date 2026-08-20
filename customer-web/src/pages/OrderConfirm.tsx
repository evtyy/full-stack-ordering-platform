import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { confirmCheckoutSession } from "../api/order";

type Status = "checking" | "paid" | "not-paid" | "error";

export default function OrderConfirm() {
  const [searchParams] = useSearchParams();
  const [status, setStatus] = useState<Status>("checking");
  const navigate = useNavigate();
  const sessionId = searchParams.get("session_id");
  const orderNumber = searchParams.get("orderNumber");

  useEffect(() => {
    if (!sessionId) {
      setStatus("error");
      return;
    }
    confirmCheckoutSession(sessionId)
      .then((paid) => setStatus(paid ? "paid" : "not-paid"))
      .catch(() => setStatus("error"));
  }, [sessionId]);

  return (
    <div className="auth-screen">
      <div className="auth-card">
        {status === "checking" && <p>Confirming your payment...</p>}
        {status === "paid" && (
          <>
            <h1>Order confirmed!</h1>
            {orderNumber && <p>Order #{orderNumber}</p>}
            <p>The kitchen has been notified and is preparing your order.</p>
          </>
        )}
        {status === "not-paid" && <p>Payment was not completed. You can try again from your order history.</p>}
        {status === "error" && <p className="auth-error">We couldn't confirm this payment.</p>}
        <button onClick={() => navigate("/orders")}>View my orders</button>
      </div>
    </div>
  );
}
