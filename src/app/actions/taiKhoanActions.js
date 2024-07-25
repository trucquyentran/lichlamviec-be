import axios from "axios";

const setTaiKhoanNguoiDung = (taKhoan) => {
    return {
        type: "SET_USER",
        payload: taKhoan,
    };
}

const loginUser = (taKhoan) => {
    localStorage.setItem('tkv', taKhoan?.access_token)
    axios.defaults.headers.common.Authorization = `Bearer ${taKhoan?.access_token}`
    return {
        type: "SET_USER_LOGIN",
        payload: taKhoan,
    };
}

export default {
    setTaiKhoanNguoiDung,
    loginUser
};
