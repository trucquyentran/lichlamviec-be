import axios from "axios";


function getDsQuyen() {
  return axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/quyen/getall");
};

  const QuyenService = {

    getDsQuyen
  
};
export default QuyenService;