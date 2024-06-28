import axios from 'axios';
const USER_API_BASE_URL = "http://localhost:8090/api/v1/user";
function UserService ()
{
    
    return {

        getUsers () 
        {
            return axios.get( USER_API_BASE_URL+'/getall' );
            // return axios.get(USER_API_BASE_URL);

        },

        createUser ( user )
        {
            return axios.post(USER_API_BASE_URL+'/save', user );
        },

        getUserById ( userId )
        {
            return axios.get( `${ USER_API_BASE_URL }/search/${ userId }` );
        },

        updateUser ( user, userId ) 
        {
            return axios.put( `${ USER_API_BASE_URL }/edit/${ userId }`, user );
        },

        deleteUser ( userId )
        {
            return axios.delete( `${ USER_API_BASE_URL }/delete/${ userId }` );
        },
    };
}

export default UserService();
