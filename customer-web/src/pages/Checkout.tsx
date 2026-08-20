import { useEffect, useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { addAddress, listAddresses } from "../api/address";
import { addToCart, getCart, subFromCart } from "../api/cart";
import { createCheckoutSession, submitOrder } from "../api/order";
import type { AddressBook, ShoppingCartItem } from "../api/types";

export default function Checkout() {
  const [cart, setCart] = useState<ShoppingCartItem[]>([]);
  const [addresses, setAddresses] = useState<AddressBook[]>([]);
  const [selectedAddressId, setSelectedAddressId] = useState<number | null>(null);
  const [showNewAddress, setShowNewAddress] = useState(false);
  const [remark, setRemark] = useState("");
  const [placing, setPlacing] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    refreshCart();
    listAddresses().then((list) => {
      setAddresses(list);
      const defaultAddress = list.find((a) => a.isDefault === 1) ?? list[0];
      if (defaultAddress) setSelectedAddressId(defaultAddress.id ?? null);
      if (list.length === 0) setShowNewAddress(true);
    });
  }, []);

  function refreshCart() {
    getCart().then(setCart);
  }

  async function handleQtyChange(item: ShoppingCartItem, delta: 1 | -1) {
    const input = { dishId: item.dishId, setmealId: item.setmealId, dishFlavor: item.dishFlavor };
    await (delta === 1 ? addToCart(input) : subFromCart(input));
    refreshCart();
  }

  async function handleAddAddress(event: FormEvent) {
    event.preventDefault();
    const form = event.target as HTMLFormElement;
    const formData = new FormData(form);
    const address: AddressBook = {
      consignee: String(formData.get("consignee") ?? ""),
      phone: String(formData.get("phone") ?? ""),
      detail: String(formData.get("detail") ?? ""),
      isDefault: 1,
    };
    await addAddress(address);
    const list = await listAddresses();
    setAddresses(list);
    const newest = list[list.length - 1];
    setSelectedAddressId(newest?.id ?? null);
    setShowNewAddress(false);
  }

  const total = cart.reduce((sum, item) => sum + item.number * item.amount, 0);

  async function handlePlaceOrder() {
    if (!selectedAddressId) {
      setError("Please choose a delivery address");
      return;
    }
    setError(null);
    setPlacing(true);
    try {
      const order = await submitOrder({
        addressBookId: selectedAddressId,
        payMethod: 3, // Stripe
        remark,
        deliveryStatus: 1,
        tablewareNumber: 1,
        tablewareStatus: 1,
        packAmount: 0,
        amount: total,
      });
      const session = await createCheckoutSession(order.orderNumber);
      window.location.href = session.checkoutUrl;
    } catch (err) {
      setError(err instanceof Error ? err.message : "Could not place order");
      setPlacing(false);
    }
  }

  return (
    <div className="checkout-page">
      <header className="menu-header">
        <button className="link-button" onClick={() => navigate("/")}>
          &larr; Back to menu
        </button>
        <h1>Checkout</h1>
      </header>

      <section>
        <h2>Your order</h2>
        {cart.length === 0 && <p>Your cart is empty.</p>}
        {cart.map((item) => (
          <div className="cart-row" key={item.id}>
            <div>
              {item.name}
              {item.dishFlavor && <div className="cart-row-flavor">{item.dishFlavor}</div>}
            </div>
            <div className="qty-control">
              <button onClick={() => handleQtyChange(item, -1)}>-</button>
              <span>{item.number}</span>
              <button onClick={() => handleQtyChange(item, 1)}>+</button>
            </div>
            <span>${(item.amount * item.number).toFixed(2)}</span>
          </div>
        ))}
        <div className="cart-total">Total: ${total.toFixed(2)}</div>
      </section>

      <section>
        <h2>Delivery address</h2>
        {addresses.map((address) => (
          <label className="address-option" key={address.id}>
            <input
              type="radio"
              name="address"
              checked={selectedAddressId === address.id}
              onChange={() => setSelectedAddressId(address.id ?? null)}
            />
            {address.consignee} · {address.phone} · {address.detail}
          </label>
        ))}
        {!showNewAddress && (
          <button className="link-button" onClick={() => setShowNewAddress(true)}>
            + Add new address
          </button>
        )}
        {showNewAddress && (
          <form className="address-form" onSubmit={handleAddAddress}>
            <input name="consignee" placeholder="Name" required />
            <input name="phone" placeholder="Phone number" required />
            <input name="detail" placeholder="Delivery address" required />
            <button type="submit">Save address</button>
          </form>
        )}
      </section>

      <section>
        <h2>Order notes</h2>
        <textarea value={remark} onChange={(e) => setRemark(e.target.value)} placeholder="Optional" />
      </section>

      {error && <p className="auth-error">{error}</p>}

      <button className="place-order-button" disabled={placing || cart.length === 0} onClick={handlePlaceOrder}>
        {placing ? "Redirecting to payment..." : `Pay $${total.toFixed(2)} with card`}
      </button>
    </div>
  );
}
