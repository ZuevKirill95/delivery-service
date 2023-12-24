import { createSlice, PayloadAction } from "@reduxjs/toolkit";

export interface Order {
  id: number;
  courierId: number | null;
  clientName: string | null;
  description: string | null;
  clientPhone: number | null;
  status: string | null;
  orderTime: string;
  address: string | null;
  branchAddress: string | null;
  flat: number | null;
  frontDoor: number | null;
  floor: number | null;
  weight: number | null;
  endCookingTime: string ;
  dishesOrders: any[];
}

export interface OrderSliceState {
  currentOrder: Order | null;
  allOrders: Order[];
  countOrder:number;
}

const initialState: OrderSliceState = {
  currentOrder: null,
  allOrders: [],
  countOrder: 0,
};

const orderSlice = createSlice({
  name: "orders",
  initialState,
  reducers: {
    setCurrentOrder: (state, action: PayloadAction<Order | null>) => {
      state.currentOrder = action.payload;
    },
    concatenateAllOrders: (state, action: PayloadAction<Order[]>) => {
      state.allOrders = [ ...state.allOrders, ...action.payload];
    },
    setAllOrders: (state, action: PayloadAction<Order[]>) => {
      state.allOrders = action.payload;
    },
    setCountOrder: (state, action: PayloadAction<number>) => {
      state.countOrder = action.payload;
    },
  },
});

export const { setCurrentOrder, 
              setAllOrders, 
              concatenateAllOrders,
              setCountOrder} = orderSlice.actions;

export default orderSlice.reducer;
