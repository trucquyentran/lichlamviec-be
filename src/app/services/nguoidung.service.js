import axios from "axios";

function searchND(search) {
  return axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/user/tim-kiem?tuKhoa="+search);
};

function getAll (page,size){
  return axios.get(process.env.REACT_APP_URL_SERVER+"/ql-lich/user/list-user?page="+page+"&size="+size);
}
function save(nguoiDung) {
  return axios.post(process.env.REACT_APP_URL_SERVER + "/ql-lich/user/add-user", nguoiDung);
};
function update(nguoiDung) {
  const config = {
      headers: {
          'Content-Type': 'application/json', // Set the content type for multipart form data
          'Accept': 'application/json' // Specify that you accept JSON response
      }
  };

  // Make sure to construct the URL properly based on your environment variable and endpoint
  const url = `${process.env.REACT_APP_URL_SERVER}/ql-lich/user/edit-tai-khoan?id=${nguoiDung?._id}`;

  // Perform the PUT request using Axios
  return axios.put(url, nguoiDung, config);
}
function deleteByID(nguoiDung) {
  return axios.delete(process.env.REACT_APP_URL_SERVER + "/ql-lich/user/delete-tai-khoan?id=" + nguoiDung);
};
function getSelectDonViThuocTrucTiep() {
  return axios.get(process.env.REACT_APP_URL_SERVER + `/be-spr/don-vi/select/truc-tiep`);
};
function getUserOfUnit() {
  return axios.get(process.env.REACT_APP_URL_SERVER + `/ql-lich/user/list-user-donvi`);
};

const nguoiDungService = {
  getSelectDonViThuocTrucTiep,
  getUserOfUnit,
  getAll,
  save,
  update,
  deleteByID,
  searchND
};
export default nguoiDungService;