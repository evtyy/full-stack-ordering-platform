import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getCategories, getDishesByCategory, getSetmealsByCategory } from "../api/menu";
import { addToCart, getCart } from "../api/cart";
import type { Category, DishVO, Setmeal, ShoppingCartItem } from "../api/types";
import CartBar from "../components/CartBar";
import ItemDetailModal from "../components/ItemDetailModal";
import Logo from "../components/Logo";

const DISH_CATEGORY_TYPE = 1;

type MenuItem = (DishVO | Setmeal) & { isSetmeal: boolean };

export default function Menu() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [activeCategoryId, setActiveCategoryId] = useState<number | null>(null);
  const [items, setItems] = useState<MenuItem[]>([]);
  const [cart, setCart] = useState<ShoppingCartItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedItem, setSelectedItem] = useState<MenuItem | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    getCategories().then((list) => {
      setCategories(list);
      if (list.length > 0) setActiveCategoryId(list[0].id);
    });
    refreshCart();
  }, []);

  useEffect(() => {
    if (activeCategoryId === null) return;
    const category = categories.find((c) => c.id === activeCategoryId);
    if (!category) return;

    setLoading(true);
    const request =
      category.type === DISH_CATEGORY_TYPE
        ? getDishesByCategory(activeCategoryId)
        : getSetmealsByCategory(activeCategoryId);

    request
      .then((list) => setItems(list.map((item) => ({ ...item, isSetmeal: category.type !== DISH_CATEGORY_TYPE }))))
      .finally(() => setLoading(false));
  }, [activeCategoryId, categories]);

  function refreshCart() {
    getCart().then(setCart);
  }

  async function handleAdd(item: MenuItem) {
    await addToCart(item.isSetmeal ? { setmealId: item.id } : { dishId: item.id });
    refreshCart();
  }

  function needsCustomization(item: MenuItem) {
    return item.isSetmeal || (item as DishVO).flavors?.length > 0;
  }

  function handleCardAction(item: MenuItem) {
    if (needsCustomization(item)) {
      setSelectedItem(item);
    } else {
      handleAdd(item);
    }
  }

  return (
    <div className="menu-page">
      <header className="menu-header">
        <Logo size={34} />
        <button className="link-button" onClick={() => navigate("/orders")}>
          My orders
        </button>
      </header>

      <div className="menu-body">
        <nav className="category-tabs">
          {categories.map((category) => (
            <button
              key={category.id}
              className={category.id === activeCategoryId ? "active" : ""}
              onClick={() => setActiveCategoryId(category.id)}
            >
              {category.name}
            </button>
          ))}
        </nav>

        <div className="item-grid">
          {loading && <p>Loading menu...</p>}
          {!loading && items.length === 0 && <p>No items in this category.</p>}
          {items.map((item) => (
            <div
              className="item-card"
              key={`${item.isSetmeal ? "setmeal" : "dish"}-${item.id}`}
              onClick={() => setSelectedItem(item)}
            >
              {item.image && <img src={item.image} alt={item.name} />}
              <div className="item-card-body">
                <h3>{item.name}</h3>
                <p className="item-description">{item.description}</p>
                <div className="item-card-footer">
                  <span className="price">${item.price.toFixed(2)}</span>
                  <button
                    onClick={(event) => {
                      event.stopPropagation();
                      handleCardAction(item);
                    }}
                  >
                    {needsCustomization(item) ? "Customize" : "Add"}
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      <CartBar cart={cart} onCheckout={() => navigate("/checkout")} />

      {selectedItem && (
        <ItemDetailModal
          item={selectedItem}
          onClose={() => setSelectedItem(null)}
          onAdded={() => {
            refreshCart();
            setSelectedItem(null);
          }}
        />
      )}
    </div>
  );
}
