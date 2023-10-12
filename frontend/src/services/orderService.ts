import axios from "axios";
import authHeader from "./authHeader";
import { Dispatch } from "redux";
import { setCurrentOrder, setAllOrders } from "../slices/orderSlice";

interface Dishes {
  orderId:number;
  dishId:number;
  dishName:string;
}

interface Order {
  id: number;
  courierId: number | null;
  clientName: string | null;
  description: string | null;
  clientPhone: number | null;
  estatusOrders: string | null;
  orderTime: string;
  address: string | null;
  branchAddress: string | null;
  flat: number | null;
  frontDoor: number | null;
  floor: number | null;
  weight: number | null;
  endCookingTime: string;
  dishesOrders: Dishes[];
}

const API_URL_ORDER = "/orders";

const updateOrder = async (orderData: Order, dispatch: Dispatch) => {
  const headers = authHeader();

  try {
    const response = await axios.put(API_URL_ORDER, orderData, { headers });
    const updatedOrder = response.data;

    dispatch(setCurrentOrder(updatedOrder));

    return updatedOrder;
  } catch (error) {
    console.error("Ошибка при обновлении заказа:", error);
    throw error;
  }
};

const assignOrderToCourier = async (order: { courierId: any; id: number }, dispatch: Dispatch) => {
  const headers = authHeader();

  try {
    const response = await axios.put(`${API_URL_ORDER}/courier`, order, { headers });
    const assignedOrder = response.data;

    dispatch(setCurrentOrder(assignedOrder));

    return assignedOrder;
  } catch (error) {
    console.error("Ошибка при присвоении заказа курьеру:", error);
    throw error;
  }
};

const getAwaitingDeliveryOrders = async (dispatch: Dispatch) => {
  const headers = authHeader();

  try {
    const response = await axios.get(`${API_URL_ORDER}/awaiting-delivery`, { headers });
    const awaitingDeliveryOrders = response.data;
    console.log(response.data)

    dispatch(setAllOrders(awaitingDeliveryOrders));

    return awaitingDeliveryOrders;
  } catch (error) {
    console.error("Ошибка при получении ожидающих заказов:", error);
    throw error;
  }
};

const getOrderById = async (orderId: number, dispatch: Dispatch) => {
  const headers = authHeader();

  try {
    const response = await axios.get(`${API_URL_ORDER}/${orderId}`, { headers });
    const order = response.data;

    dispatch(setCurrentOrder(order));

    return order;
  } catch (error) {
    console.error("Ошибка при получении заказа по Id:", error);
    throw error;
  }
};

const getOrdersForCourier = async (
  courierId: number,
  dispatch: Dispatch
) => {
  const headers = authHeader();

  try {
    const response = await axios.get(`${API_URL_ORDER}/courier/${courierId}`, { headers });
    const courierOrders = response.data;

    dispatch(setAllOrders(courierOrders));

    return courierOrders;
  } catch (error) {
    console.error("Ошибка при получении заказов для курьера:", error);
    throw error;
  }
};

const orderService = {
  updateOrder,
  assignOrderToCourier,
  getAwaitingDeliveryOrders,
  getOrderById,
  getOrdersForCourier,
};

export default orderService;