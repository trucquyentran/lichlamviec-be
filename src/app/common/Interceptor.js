// import getCookie from './getCookie';
import { Modal } from "antd";
import Services from "app/services";
import axios from "axios";
import dayjs from "dayjs";
import { useLocation } from "react-router-dom";
// import { toast } from 'react-toastify';
// import 'react-toastify/dist/ReactToastify.css';
function initInterceptor() {
    // Đặt token
    axios.defaults.headers.common["Authorization"] = `Bearer ${localStorage.getItem("tkv")}`;
    // Lỗi 401 và 403
    axios.interceptors.request.use(async function (config) {

        return config;
    }, function (error) {
        console.log(error);

        return Promise.reject(error);
    });

    axios.interceptors.response.use(
        (res) => {
            if (201 === res?.status) {


            }
            return res;
        },
        async (error) => {
            // console.log(error);
            if (400 === error?.response?.status) {
                Modal.error({
                    title : 'Đã có lỗi xảy xa',
                    content: error.response?.data?.message ? error.response?.data?.message : error.response?.data ? error.response?.data : 'Đã có lỗi xảy ra',
                });
                if ((axios.defaults.headers.common['Authorization']) != undefined) {

                    Modal.error({
                        title : 'Đã có lỗi xảy xa',
                        content: error.response?.data?.message ? error.response?.data?.message : error.response?.data ? error.response?.data : 'Đã có lỗi xảy ra',
                    });
                }


            }

            if (500 === error?.response?.status) {
                Modal.error({
                    title : 'Đã có lỗi xảy ra!!!',
                    content: error.response?.data?.message ? error.response?.data?.message : error.response?.data ? error.response?.data : 'Đã có lỗi xảy ra',
                             onOk: () => {
                         window.location.reload();
                    }
                });
            }
            if (401 === error?.response?.status) {



                localStorage.removeItem('tkv')
                localStorage.removeItem('tkfv')
                axios.defaults.headers.common["Authorization"] = ""
                window.location.href = "/dang-nhap" + "?url=" + window.location.pathname;



            } if (403 === error?.response?.status) {
                Modal.error({
                    title: 'Bạn không có quyền truy cập',
                    content: 'Vui lòng liên hệ quản trị viên để được cấp quyền',
                   
                    okText: 'Trang chủ',
                    onOk() {
                        window.location.href = "/";
                    },
                 
                });
            
                return Promise.reject(error);
            }
            else if (413 === error?.response?.status) {
                Modal.error({
                    title: 'Tổng file cần lưu không được quá 25MB',
                    content: 'Vui lòng liên hệ quản trị viên để được hỗ trợ',
                    onOk() {
                        window.location.href = "/";
                    }
                });

            } else {
                // alert("Không thành công");
                return Promise.reject(error);
            }
        }
    );
}

const checkvalidToken = (token) => {
    try {
        const [, payloadBase64] = token.split('.');
        const decodedPayload = atob(payloadBase64);
        const decodedPayloadObj = JSON.parse(decodedPayload);
        console.log(dayjs.unix(decodedPayloadObj?.exp));
        return dayjs.unix(decodedPayloadObj?.exp).isAfter(dayjs());
    } catch (error) {
        console.error('Error decoding JWT:', error.message);
        return null;
    }
};
export default {
    initInterceptor

};