import axios from "axios";

function searchDV(search) {
  return axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/don-vi/tim-kiem?keyword=" + search);
};
function getAll(page,limit) {
  return axios.get(process.env.REACT_APP_URL_SERVER + "/ql-lich/don-vi/select/toan-bo-donvi?page="+page+"&size="+limit);
};
function save(donVi) {
  return axios.post(process.env.REACT_APP_URL_SERVER + "/ql-lich/don-vi", donVi);
};
function update(donVi) {
  return axios.put(process.env.REACT_APP_URL_SERVER + "/ql-lich/don-vi", donVi);
};
function deleteByID(donVi) {
  return axios.delete(process.env.REACT_APP_URL_SERVER + "/ql-lich/don-vi/"+ donVi);
};
function getSelectDonViThuocTrucTiep() {
  return axios.get(process.env.REACT_APP_URL_SERVER + `/be-spr/don-vi/select/truc-tiep`);
};
function getSelectToanBoDonViDuoi() {
  return axios.get(process.env.REACT_APP_URL_SERVER + `/be-spr/don-vi/select/gian-tiep`);
};
const donViService = {
  getSelectDonViThuocTrucTiep,
  getSelectToanBoDonViDuoi,
  getAll,
  save,
  update,
  deleteByID,
  searchDV
};
export default donViService;