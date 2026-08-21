import { createContext, useContext, useEffect, useState, type ReactNode } from "react";
import { getShopStatus } from "./api/shop";

const POLL_INTERVAL_MS = 30_000;

interface ShopStatusContextValue {
  isShopOpen: boolean;
}

const ShopStatusContext = createContext<ShopStatusContextValue | undefined>(undefined);

export function ShopStatusProvider({ children }: { children: ReactNode }) {
  const [isShopOpen, setIsShopOpen] = useState(true);

  useEffect(() => {
    function refresh() {
      getShopStatus()
        .then(setIsShopOpen)
        .catch(() => {});
    }

    refresh();
    const interval = setInterval(refresh, POLL_INTERVAL_MS);
    return () => clearInterval(interval);
  }, []);

  return <ShopStatusContext.Provider value={{ isShopOpen }}>{children}</ShopStatusContext.Provider>;
}

export function useShopStatus() {
  const context = useContext(ShopStatusContext);
  if (!context) {
    throw new Error("useShopStatus must be used within a ShopStatusProvider");
  }
  return context;
}
