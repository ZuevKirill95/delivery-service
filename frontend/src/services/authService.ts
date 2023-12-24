import axios from "axios";
import { setUserData, loginUser, logoutUser } from "../slices/authSlice";
import { Dispatch } from "redux";
import authHeader from "./authHeader";

interface Registration {
    username: string;
    email: string;
    password: string;
  }

interface Login {
    username: string;
    password: string;
}

interface Role {
    id: number;
    role: string;
}

interface User {
    id: number;
    username: string;
    email: string;
    phoneNumber: string;
    dateRegistration: string;
    status: string;
    latitude: number;
    longitude: number;
    isNotify: boolean;
    role: Role;
    accessToken: string;
}

const API_URL = "/api/auth/"

const register = (registration: Registration) => {
    const { username, email, password } = registration;
    return axios.post(API_URL + "signup", {
      username,
      email,
      password,
    });
  };

const login = async (login: Login, dispatch: Dispatch): Promise<User> => {
    const {username, password} = login;

    let response = await axios
        .post<User>(API_URL + "signin", {
            username,
            password,
        });
    console.log(response);
    dispatch(loginUser(response.data));

    const headers = authHeader();

    let detailsResponse = await axios
        .get<User>("/api/auth", { headers });
    console.log(detailsResponse);
    dispatch(setUserData(detailsResponse.data));

    return detailsResponse.data;
};

const refresh = async (refresh_token: String, dispatch: Dispatch): Promise<User> => {
    let response = await axios
        .post<User>(API_URL + "refresh", {
            refresh_token
        });
    console.log(response);
    dispatch(loginUser(response.data));

    const headers = authHeader();

    let detailsResponse = await axios
        .get<User>("/api/auth", { headers });
    console.log(detailsResponse);
    dispatch(setUserData(detailsResponse.data));

    return detailsResponse.data;
};

const logout = (dispatch: Dispatch): void => {
    console.log("logout");
    dispatch(logoutUser());
};

const authService = {
    register,
    login,
    logout,
    refresh,
};

export default authService;