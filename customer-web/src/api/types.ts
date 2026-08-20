export interface Result<T> {
  code: number;
  msg?: string;
  data: T;
}

export interface Category {
  id: number;
  type: number;
  name: string;
  sort: number;
}

export interface DishFlavor {
  id: number;
  dishId: number;
  name: string;
  value: string;
}

export interface DishVO {
  id: number;
  name: string;
  categoryId: number;
  price: number;
  image: string;
  description: string;
  status: number;
  categoryName: string;
  flavors: DishFlavor[];
}

export interface Setmeal {
  id: number;
  categoryId: number;
  name: string;
  price: number;
  status: number;
  description: string;
  image: string;
}

export interface DishItemVO {
  dishId: number;
  name: string;
  copies: number;
  image: string;
  description: string;
  flavors: DishFlavor[];
}

export interface AddressBook {
  id?: number;
  userId?: number;
  consignee: string;
  phone: string;
  provinceName?: string;
  cityName?: string;
  districtName?: string;
  detail: string;
  label?: string;
  isDefault?: number;
}

export interface ShoppingCartItem {
  id: number;
  name: string;
  userId: number;
  dishId?: number;
  setmealId?: number;
  dishFlavor?: string;
  number: number;
  amount: number;
  image: string;
}

export interface OrderSubmitVO {
  id: number;
  orderNumber: string;
  orderAmount: number;
  orderTime: string;
}

export interface OrderDetail {
  id: number;
  name: string;
  image: string;
  orderId: number;
  dishId?: number;
  setmealId?: number;
  dishFlavor?: string;
  number: number;
  amount: number;
}

export interface OrderVO {
  id: number;
  number: string;
  status: number;
  userId: number;
  addressBookId: number;
  orderTime: string;
  checkoutTime?: string;
  payStatus: number;
  amount: number;
  remark?: string;
  phone: string;
  address: string;
  consignee: string;
  orderDetailList: OrderDetail[];
}

export interface PageResult<T> {
  total: number;
  records: T[];
}

export interface StripeCheckoutVO {
  sessionId: string;
  checkoutUrl: string;
}

export interface UserLoginVO {
  id: number;
  openid?: string;
  token: string;
}
