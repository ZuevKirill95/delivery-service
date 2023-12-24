import axios from "axios";
import authHeader from "./authHeader";
import { Dispatch } from "redux";
import {setCurrentOrder, setAllOrders, concatenateAllOrders, setCountOrder} from "../slices/orderSlice";


const API_URL_ORDER = "/orders";

const updateOrder = async (orderData: { id: number; status: string }, dispatch: Dispatch) => {
  const headers = authHeader();

  try {
    const response = await axios.put(API_URL_ORDER+"/"+orderData.id, orderData, { headers });
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

const getAwaitingDeliveryOrders = async (page: number, dispatch: Dispatch) => {
  const headers = authHeader();

  try {
    const response = await axios.get(`${API_URL_ORDER}/awaiting-delivery?page=${page}`, { headers });
    const awaitingDeliveryOrders = response.data.content;
    console.log(response.data.content)

    dispatch(concatenateAllOrders(awaitingDeliveryOrders));

    return awaitingDeliveryOrders;
  } catch (error) {
    console.error("Ошибка при получении ожидающих заказов:", error);
    throw error;
  }
};

const getActiveDeliveryOrders = async (dispatch: Dispatch) => {
  const headers = authHeader();

  try {
    const response = await axios.get(`${API_URL_ORDER}/delivering`, { headers });
    const activeDeliveryOrders = response.data;
    console.log(response.data)

    dispatch(setAllOrders(activeDeliveryOrders));

    return activeDeliveryOrders;
  } catch (error) {
    console.error("Ошибка при получении доставляющихся заказов:", error);
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

const getOrdersForCourier = async (page: number, dispatch: Dispatch) => {
  const headers = authHeader();

  try {
    const response = await axios.get(`${API_URL_ORDER}?page=${page}`, { headers });
    const courierOrders = response.data.content;
    console.log(response.data.content)

    dispatch(concatenateAllOrders(courierOrders));

    return courierOrders;
  } catch (error) {
    console.error("Ошибка при получении заказов для курьера:", error);
    throw error;
  }
};

const getCountOrder = async (dispatch: Dispatch) => {
  const headers = authHeader();

  try {
    const response = await axios.get(`${API_URL_ORDER}/analytic/count`, { headers });
    const courierOrders = response.data;

    dispatch(setCountOrder(courierOrders));

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
  getActiveDeliveryOrders,
  getOrderById,
  getOrdersForCourier,
  getCountOrder
};

export default orderService;
