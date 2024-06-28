import axios from 'axios';

const CALENDAR_API = "http://localhost:8090/api/v1/lich";

function CaledarService() {
    return {
        getCals() {
            return axios.get(CALENDAR_API+'/');
        },
        // http://localhost:8090/api/v1/lich/them
        createCal(user) {
            return axios.post(CALENDAR_API+'/them', user);
        },

        getCalById(userId) {
            return axios.get(`${CALENDAR_API}/${userId}`);
        },

        updateCal(user, id) {
            return axios.put(`${CALENDAR_API}/sua/${id}`, user);
        },

        deleteCal(id) {
            return axios.delete(`${CALENDAR_API}/xoa/${id}`);
        },
    };
}

export default CaledarService();
