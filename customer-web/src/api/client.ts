import axios from "axios";
import type { Result } from "./types";

// Matches sky.jwt.user-token-name in application.yml — the backend's
// JwtTokenUserInterceptor reads the JWT from this exact header name.
const AUTH_HEADER = "authentication";
export const TOKEN_STORAGE_KEY = "customer-web:token";

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
});

axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_STORAGE_KEY);
  if (token) {
    config.headers[AUTH_HEADER] = token;
  }
  return config;
});

axiosInstance.interceptors.response.use(undefined, (error) => {
  if (error.response?.status === 401) {
    localStorage.removeItem(TOKEN_STORAGE_KEY);
    window.location.href = "/login";
  }
  return Promise.reject(error);
});

// Unwraps the backend's { code, msg, data } envelope and throws on failure (code !== 1).
async function unwrap<T>(promise: Promise<{ data: Result<T> }>): Promise<T> {
  const response = await promise;
  const result = response.data;
  if (result.code !== 1) {
    throw new Error(result.msg || "Request failed");
  }
  return result.data;
}

export const api = {
  get: <T>(url: string, params?: object) => unwrap<T>(axiosInstance.get(url, { params })),
  post: <T>(url: string, body?: object) => unwrap<T>(axiosInstance.post(url, body)),
  put: <T>(url: string, body?: object) => unwrap<T>(axiosInstance.put(url, body)),
  delete: <T>(url: string) => unwrap<T>(axiosInstance.delete(url)),
};
