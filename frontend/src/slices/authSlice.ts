import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface Role {
    id: number;
    role: string;
}

interface User {
    id: string;
    username: string;
    email: string;
    phoneNumber: string;
    registrationDate: string;
    status: string;
    latitude: number;
    longitude: number;
    isNotify: boolean;
    role: Role;
}

interface UserState {
    user: User | null;
    isAuth: boolean;
}
const getUserFromLocalStorage = (): any | null => {
    const userString = localStorage.getItem('user');
    if (userString) {
        return JSON.parse(userString);
    }
    return null;
};

const initialState: UserState = {
    user: getUserFromLocalStorage(),
    isAuth: false,
};

const authSlice = createSlice({
    name: 'user',
    initialState,
    reducers: {
        set: (state, action: PayloadAction<any>) => {
            state.user = action.payload;
        },
        setAuth: (state, action: PayloadAction<any>) => {
            state.isAuth = action.payload;
        },
        loginUser: (state, action: PayloadAction<any>) => {
            state.isAuth = true;

            if (action.payload.access_token && action.payload.refresh_token) {
                sessionStorage.setItem('user', JSON.stringify(action.payload));
            }
        },
        setUserData: (state, action: PayloadAction<any>) => {
            const date = new Date(action.payload.registrationDate);
  
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            const seconds = String(date.getSeconds()).padStart(2, '0');
            
            action.payload.registrationDate  = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

            state.user = action.payload;
            if (action.payload.id) {
                localStorage.setItem('user', JSON.stringify(action.payload));
            }
        },
        logoutUser: (state) => {
            state.isAuth = false;
            state.user = null;

            localStorage.removeItem('user');
            sessionStorage.removeItem('user');
        }
    },
});

export const { set, setAuth, loginUser, logoutUser, setUserData } = authSlice.actions;

export default authSlice.reducer;