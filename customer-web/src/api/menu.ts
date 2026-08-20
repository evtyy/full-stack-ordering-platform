import { api } from "./client";
import type { Category, DishItemVO, DishVO, Setmeal } from "./types";

const DISH_CATEGORY_TYPE = 1;
const SETMEAL_CATEGORY_TYPE = 2;

export function getCategories() {
  return Promise.all([
    api.get<Category[]>("/user/category/list", { type: DISH_CATEGORY_TYPE }),
    api.get<Category[]>("/user/category/list", { type: SETMEAL_CATEGORY_TYPE }),
  ]).then(([dishCategories, setmealCategories]) => [...dishCategories, ...setmealCategories]);
}

export function getDishesByCategory(categoryId: number) {
  return api.get<DishVO[]>("/user/dish/list", { categoryId });
}

export function getSetmealsByCategory(categoryId: number) {
  return api.get<Setmeal[]>("/user/setmeal/list", { categoryId });
}

export function getSetmealDishes(setmealId: number) {
  return api.get<DishItemVO[]>(`/user/setmeal/dish/${setmealId}`);
}
