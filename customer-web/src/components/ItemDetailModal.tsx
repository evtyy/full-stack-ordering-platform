import { useEffect, useMemo, useState } from "react";
import { getSetmealDishes } from "../api/menu";
import { addToCart } from "../api/cart";
import type { DishFlavor, DishItemVO, DishVO, Setmeal } from "../api/types";

type Item = (DishVO | Setmeal) & { isSetmeal: boolean };

function parseFlavorOptions(flavor: DishFlavor): string[] {
  try {
    const parsed = JSON.parse(flavor.value);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
}

function FlavorPicker({
  flavors,
  selected,
  onSelect,
}: {
  flavors: DishFlavor[];
  selected: Record<string, string>;
  onSelect: (groupName: string, value: string) => void;
}) {
  return (
    <div className="flavor-groups">
      {flavors.map((flavor) => (
        <div className="flavor-group" key={flavor.id}>
          <div className="flavor-group-name">{flavor.name}</div>
          <div className="flavor-options">
            {parseFlavorOptions(flavor).map((option) => (
              <button
                type="button"
                key={option}
                className={`flavor-chip${selected[flavor.name] === option ? " selected" : ""}`}
                onClick={() => onSelect(flavor.name, option)}
              >
                {option}
              </button>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}

export default function ItemDetailModal({
  item,
  isShopOpen,
  onClose,
  onAdded,
}: {
  item: Item;
  isShopOpen: boolean;
  onClose: () => void;
  onAdded: () => void;
}) {
  const [dishSelections, setDishSelections] = useState<Record<string, string>>({});
  const [setmealItems, setSetmealItems] = useState<DishItemVO[]>([]);
  const [setmealSelections, setSetmealSelections] = useState<Record<number, Record<string, string>>>({});
  const [loadingItems, setLoadingItems] = useState(item.isSetmeal);
  const [submitting, setSubmitting] = useState(false);

  const dishFlavors = useMemo(() => (!item.isSetmeal ? (item as DishVO).flavors ?? [] : []), [item]);

  useEffect(() => {
    if (!item.isSetmeal) return;
    setLoadingItems(true);
    getSetmealDishes(item.id)
      .then(setSetmealItems)
      .finally(() => setLoadingItems(false));
  }, [item]);

  const dishComplete = useMemo(
    () => dishFlavors.every((flavor) => dishSelections[flavor.name]),
    [dishFlavors, dishSelections]
  );

  const setmealComplete = useMemo(
    () =>
      setmealItems.every((dishItem) =>
        (dishItem.flavors ?? []).every((flavor) => setmealSelections[dishItem.dishId]?.[flavor.name])
      ),
    [setmealItems, setmealSelections]
  );

  const canAdd = isShopOpen && (item.isSetmeal ? !loadingItems && setmealComplete : dishComplete);

  function buildDishFlavorText(): string | undefined {
    if (!item.isSetmeal) {
      if (dishFlavors.length === 0) return undefined;
      return dishFlavors.map((flavor) => `${flavor.name}: ${dishSelections[flavor.name]}`).join(", ");
    }

    const parts = setmealItems
      .filter((dishItem) => (dishItem.flavors ?? []).length > 0)
      .map((dishItem) => {
        const groups = (dishItem.flavors ?? [])
          .map((flavor) => `${flavor.name}: ${setmealSelections[dishItem.dishId]?.[flavor.name]}`)
          .join(", ");
        return `${dishItem.name} (${groups})`;
      });
    return parts.length > 0 ? parts.join("; ") : undefined;
  }

  async function handleAdd() {
    setSubmitting(true);
    try {
      await addToCart(
        item.isSetmeal
          ? { setmealId: item.id, dishFlavor: buildDishFlavorText() }
          : { dishId: item.id, dishFlavor: buildDishFlavorText() }
      );
      onAdded();
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-panel" onClick={(event) => event.stopPropagation()}>
        <button type="button" className="modal-close" onClick={onClose}>
          &times;
        </button>

        {item.image && <img className="modal-image" src={item.image} alt={item.name} />}

        <div className="modal-body">
          <h2>{item.name}</h2>
          <p className="item-description">{item.description}</p>
          <div className="price">${item.price.toFixed(2)}</div>

          {!item.isSetmeal && dishFlavors.length > 0 && (
            <FlavorPicker
              flavors={dishFlavors}
              selected={dishSelections}
              onSelect={(groupName, value) =>
                setDishSelections((current) => ({ ...current, [groupName]: value }))
              }
            />
          )}

          {item.isSetmeal && (
            <div className="setmeal-items">
              <h3>What's included</h3>
              {loadingItems && <p>Loading...</p>}
              {!loadingItems &&
                setmealItems.map((dishItem) => (
                  <div className="setmeal-item" key={dishItem.dishId}>
                    <div className="setmeal-item-header">
                      {dishItem.image && <img src={dishItem.image} alt={dishItem.name} />}
                      <div>
                        <div className="setmeal-item-name">
                          {dishItem.name} &times;{dishItem.copies}
                        </div>
                        <p className="item-description">{dishItem.description}</p>
                      </div>
                    </div>
                    {(dishItem.flavors ?? []).length > 0 && (
                      <FlavorPicker
                        flavors={dishItem.flavors}
                        selected={setmealSelections[dishItem.dishId] ?? {}}
                        onSelect={(groupName, value) =>
                          setSetmealSelections((current) => ({
                            ...current,
                            [dishItem.dishId]: { ...current[dishItem.dishId], [groupName]: value },
                          }))
                        }
                      />
                    )}
                  </div>
                ))}
            </div>
          )}

          <button className="modal-add-button" disabled={!canAdd || submitting} onClick={handleAdd}>
            {!isShopOpen ? "Shop closed" : submitting ? "Adding..." : "Add to cart"}
          </button>
        </div>
      </div>
    </div>
  );
}
