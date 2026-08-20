import { api } from "./client";
import type { AddressBook } from "./types";

export function listAddresses() {
  return api.get<AddressBook[]>("/user/addressBook/list");
}

export function addAddress(address: AddressBook) {
  return api.post<void>("/user/addressBook", address);
}
