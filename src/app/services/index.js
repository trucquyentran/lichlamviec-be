
import TaiKhoanService from "./taikhoan.service"

import donViService from "./donvi.service"
import nguoiDungService from "./nguoidung.service"
import LichService from "./lichlamviec.service"
import QuyenService from "./quyen.service"
function getDonViService() { return donViService }
function getLichService() {return LichService}
function getTaiKhoanService() { return TaiKhoanService }
function getNguoiDungService() { return nguoiDungService}
function getQuyenService() { return QuyenService} 
const Services = {
    getTaiKhoanService,
    getDonViService,
    getNguoiDungService,
    getLichService,
    getQuyenService
}

export default Services