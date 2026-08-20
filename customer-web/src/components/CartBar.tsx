import type { ShoppingCartItem } from "../api/types";

export default function CartBar({ cart, onCheckout }: { cart: ShoppingCartItem[]; onCheckout: () => void }) {
  const itemCount = cart.reduce((sum, item) => sum + item.number, 0);
  const total = cart.reduce((sum, item) => sum + item.number * item.amount, 0);

  if (itemCount === 0) return null;

  return (
    <div className="cart-bar">
      <span>
        {itemCount} item{itemCount > 1 ? "s" : ""} · ${total.toFixed(2)}
      </span>
      <button onClick={onCheckout}>View cart</button>
    </div>
  );
}
