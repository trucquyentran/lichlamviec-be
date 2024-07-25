import axios from "axios";


function getDsLich() {
  return axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/lich-lam-viec/lich-tai-khoan");
};

function getDsLichDV() {
  return axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/lich-lam-viec/lich-don-vi");
};
function listLichOfUserUnit() {
  return axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/lich-lam-viec/lich-tai-khoan-don-vi");
};

function listLichOfOneUser(idTK) {
  return axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/lich-lam-viec/lich-user?tk="+idTK);
};
function searchLich(seacrh) {
  return axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/lich-lam-viec/tim-kiem?tuKhoa="+seacrh);
};
function getLichByID(lich) {
  return axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/lich-lam-viec?search="+lich);
};


function themLichCaNhan(lichCaNhan) {
    return axios.post(process.env.REACT_APP_URL_SERVER + "/ql-lich/lich-lam-viec/them-lich-user",lichCaNhan);
  
}


  function  suaLichCaNhan(lichCaNhan) {  
    const config = {
        headers: {
            'Content-Type': 'application/json', 
            'Accept': 'application/json' 
        }
    };

    const url = `${process.env.REACT_APP_URL_SERVER}/ql-lich/lich-lam-viec/edit-lich-user?idLich=${lichCaNhan?._id}`;
    return axios.put(url, lichCaNhan, config);
  }


  function  suaLichDonVi(lichDonVi) {  
    const config = {
        headers: {
            'Content-Type': 'application/json', 
            'Accept': 'application/json' 
        }
    };

    const url = `${process.env.REACT_APP_URL_SERVER}/ql-lich/lich-lam-viec/edit-lich-donvi?idLich=${lichDonVi?._id}`;
    return axios.put(url, lichDonVi, config);
  }


  function xoaLich(lichCaNhan) {
    return axios.delete(process.env.REACT_APP_URL_SERVER + "/ql-lich/lich-lam-viec/" + lichCaNhan);
  };
  const LichService = {
    listLichOfOneUser,
    searchLich,
    getDsLich,
    themLichCaNhan,
    suaLichCaNhan,
    xoaLich,
    getDsLichDV,
    getLichByID,
    suaLichDonVi,
    listLichOfUserUnit
};
export default LichService;