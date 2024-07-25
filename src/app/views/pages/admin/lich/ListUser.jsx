import React, { useCallback, useEffect, useState } from 'react';
import { Input,message } from 'antd';
import Services from 'app/services';
import { debounce } from 'lodash';
import { Checkbox } from "flowbite-react";
const ListUser = ({ onCheckedUsersChange, user, searchQuery }) => {
  const [loading, setLoading] = useState(false);
  const [listUser, setListUser] = useState([]);
  const [checkedUsers, setCheckedUsers] = useState([]);
  const [userColors, setUserColors] = useState({});
  useEffect(() => {
    getListUser();
  }, []);
console.log(checkedUsers);
  async function getListUser() {
    setLoading(true);
    try {
      let dataListUser = await Services.getNguoiDungService().getUserOfUnit();
      if (dataListUser.data) {
  
        const currentUser = user._id;
      
        const filteredUsers = dataListUser.data.filter(user => user._id !== currentUser);

        setListUser(filteredUsers);

        const initialCheckedUsers = filteredUsers.map(user => user._id);

        const initialColors = {};
        filteredUsers.forEach((user, index) => {
          initialColors[user._id] = colors[index % colors.length].borderColor;
        });
  
        // setCheckedUsers(initialCheckedUsers);
        setUserColors(initialColors);
        onCheckedUsersChange(initialCheckedUsers, initialColors);
  
     
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  }
  

  const colors = [
    { label: "Red", textColor: "text-red-600", borderColor: "#f87171", focusRing: "focus:ring-red-500 dark:focus:ring-red-600" },
    { label: "Green", textColor: "text-green-600", borderColor: "#34d399", focusRing: "focus:ring-green-500 dark:focus:ring-green-600" },
    { label: "Purple", textColor: "text-purple-600", borderColor: "#a78bfa", focusRing: "focus:ring-purple-500 dark:focus:ring-purple-600" },
    { label: "Teal", textColor: "text-teal-600", borderColor: "#5eead4", focusRing: "focus:ring-teal-500 dark:focus:ring-teal-600" },
    { label: "Yellow", textColor: "text-yellow-400", borderColor: "#facc15", focusRing: "focus:ring-yellow-500 dark:focus:ring-yellow-600" },
    { label: "Orange", textColor: "text-orange-500", borderColor: "#fb923c", focusRing: "focus:ring-orange-500 dark:focus:ring-orange-600" },
    { label: "Blue", textColor: "text-blue-600", borderColor: "#60a5fa", focusRing: "focus:ring-blue-500 dark:focus:ring-blue-600" },
    { label: "Pink", textColor: "text-pink-600", borderColor: "#f472b6", focusRing: "focus:ring-pink-500 dark:focus:ring-pink-600" },
    { label: "Indigo", textColor: "text-indigo-600", borderColor: "#818cf8", focusRing: "focus:ring-indigo-500 dark:focus:ring-indigo-600" },
    { label: "Cyan", textColor: "text-cyan-600", borderColor: "#22d3ee", focusRing: "focus:ring-cyan-500 dark:focus:ring-cyan-600" },
    { label: "Neutral", textColor: "text-neutral-600", borderColor: "#9ca3af", focusRing: "focus:ring-neutral-500 dark:focus:ring-neutral-600" },
    { label: "Amber", textColor: "text-amber-600", borderColor: "#fbbf24", focusRing: "focus:ring-amber-500 dark:focus:ring-amber-600" },
  ];

  const handleCheckboxChange = (userId) => {
    let updatedCheckedUsers;
    if (checkedUsers.includes(userId)) {
      updatedCheckedUsers = checkedUsers.filter((id) => id !== userId);
    } else {
      updatedCheckedUsers = [...checkedUsers, userId];
    }
    setCheckedUsers(updatedCheckedUsers);
    onCheckedUsersChange(updatedCheckedUsers, userColors);
  };
  const filteredList = listUser.filter(user => user.hoTen.toLowerCase().includes(searchQuery.toLowerCase()));
  return (
    <div className="flex flex-col space-y-2">
      {loading ? (
        <div>Loading...</div>
      ) : (
        filteredList.map((user, index) => {
          const color = colors[index % colors.length];

          return (
            <>

            <div key={user._id} className="flex items-center">

              <Checkbox
                id={`user-checkbox-${user._id}`}
                value={user._id}
                // checked={checkedUsers.includes(user._id)}
                onChange={() => handleCheckboxChange(user._id)}
                className={`w-5 h-5 ${color.textColor} bg-gray-100 ${color.borderColor} border-2 rounded ${color.focusRing} dark:ring-offset-gray-800 dark:bg-gray-700 dark:border-gray-600`}
              />
              <label
                htmlFor={`user-checkbox-${user._id}`}
                className="ms-2 text-base font-bold text-gray-900 dark:text-gray-300"
              >
                {user.hoTen}
              </label>
            </div>
            </>
          );
        })
      )}
    </div>
   
  );
};

export default ListUser;
