import { createContext, useContext, useMemo, useState, type ReactNode } from "react";
import { TOKEN_STORAGE_KEY } from "./api/client";

interface AuthContextValue {
  isLoggedIn: boolean;
  login: (token: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem(TOKEN_STORAGE_KEY));

  const value = useMemo<AuthContextValue>(
    () => ({
      isLoggedIn: token !== null,
      login: (newToken: string) => {
        localStorage.setItem(TOKEN_STORAGE_KEY, newToken);
        setToken(newToken);
      },
      logout: () => {
        localStorage.removeItem(TOKEN_STORAGE_KEY);
        setToken(null);
      },
    }),
    [token]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
