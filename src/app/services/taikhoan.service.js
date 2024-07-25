import axios from "axios";

function getMe() {
  return axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/user/account");
};

function changePassword(nhanVien) {
  return axios.post(process.env.REACT_APP_URL_SERVER + "/be-spr/user/change-pas", nhanVien);
};
const dangNhap = ({ username, password }) => {
  return axios.post(process.env.REACT_APP_URL_SERVER + "/ql-lich/user/dang-nhap", {
    "username": username,
    "password": password
  })
};
const dangXuat = async () => {
  try {
    await axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/user/dang-xuat");
    localStorage.removeItem('tkv');
    axios.defaults.headers.common["Authorization"] = "";
    window.location.href = "/dang-nhap";
  } catch (error) {
    console.error("Lỗi khi đăng xuất:", error);
   
  }
};
const refreshToken = async (token) => {
  return axios.post(process.env.REACT_APP_URL_SERVER + "/be-spr/user/refresh-token", token)
};
const taiKhoanService = {
  dangNhap,
  dangXuat,
  getMe,
  changePassword,
  refreshToken
};
export default taiKhoanService;