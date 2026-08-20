import { api } from "./client";
import type { UserLoginVO } from "./types";

export function webLogin(name: string, phone: string) {
  return api.post<UserLoginVO>("/user/user/webLogin", { name, phone });
}
