import React, { useCallback, useEffect, useState,useRef } from 'react';
import { Input,message } from 'antd';
import Services from 'app/services';
import { debounce } from 'lodash';import { Button, Divider, Checkbox } from "antd";
import AddRoundedIcon from '@mui/icons-material/AddRounded';
import '../../../../assets/css/style.css';
import { Draggable } from '@fullcalendar/interaction';
import ListUser from './ListUser';

const LichSide = ({onCheckedUsersChange, open, setOpenLichModalAdd, setShowPersonal, setShowUnit, user }) => {
  const { Search } = Input;
  const [searchQuery, setSearchQuery] = useState('');
  const [checkedUsers, setCheckedUsers] = useState([]);

  const handleCheckedUsersChange = (updatedCheckedUsers) => {
    setCheckedUsers(updatedCheckedUsers);
    onCheckedUsersChange(updatedCheckedUsers); 
  };
  const containerEl = useRef(null);
  const handleCheckboxChange = (e) => {
    if (e.target.name === 'personal') {
      setShowPersonal(e.target.checked);
    } else if (e.target.name === 'unit') {
      setShowUnit(e.target.checked);
    }
  };

  useEffect(() => {
    const draggable = new Draggable(containerEl.current, {
      itemSelector: '.fc-event',
      eventData: function (eventEl) {
        return {
          title: eventEl.innerText.trim(),
          color: getButtonColor(eventEl)
        };
      }
    });

    return () => {
      draggable.destroy();
    };
  }, []);
  const handleSearch = useCallback(
    debounce(async (e) => {
      setSearchQuery(e.target.value); // Update search query state
    }, 500),
    []
  );
//   const handleSearch = useCallback(
//     debounce(async (e) => {
//         let dataRSListND = await Services.getNguoiDungService().searchND(e?.target?.value);
//         if (dataRSListND?.data?.length === 0) {
//             message.error("Không tìm thấy người dùng");
//         } else {
//           setListUser(dataRSListND?.data);
           
//         }
//     }, 500),
//     [],
// );


  const getButtonColor = (eventEl) => {
    const button = eventEl;
    return window.getComputedStyle(button).backgroundColor;
  };

  return (
    <div id="external-events" ref={containerEl} className="px-4 hide-on-md">
      <Button className="w-full h-10 flex justify-center items-center " type="primary" onClick={() => setOpenLichModalAdd(true)}>
        <AddRoundedIcon />
        <div className=' hidden lg:block'> <span className="text-base font-semibold">Thêm lịch</span></div>
      </Button>
      <p >Kéo và thả sự kiện hoặc nhấn vào lịch</p>

      <Button className="w-full h-9 mt-2 btn-add-event cursor-move fc-event" style={{ backgroundColor: "#277DA1" }} draggable>
        Công việc
      </Button>
      <Button className="w-full h-9 mt-2 btn-add-event cursor-move fc-event" style={{ backgroundColor: "#20b799" }} draggable>
        Công tác
      </Button>
      <Button className="w-full h-9 mt-2 btn-add-event cursor-move fc-event" style={{ backgroundColor: "#F8961E" }} draggable>
        Họp
      </Button>
      <Button className="w-full h-9 mt-2 btn-add-event cursor-move fc-event" style={{ backgroundColor: "#F94144" }} draggable>
        Gấp
      </Button>

      <Divider />
      <div className='mb-2'><p>DANH SÁCH LỊCH</p></div>
      <Checkbox
        name='personal' defaultChecked onChange={handleCheckboxChange}
        value="personal"
    
        className='font-bold text-base mb-2'
      >
        Lịch cá nhân
      </Checkbox>
      <br />
      <Checkbox
        name='unit' defaultChecked onChange={handleCheckboxChange}
        value="unit"
        className='font-bold text-base'
      >
        {user.donVi.tenDonVi}
      </Checkbox>

      {user.listQuyen == 'Admin' && (
        <>
      <Divider />
      <div className='mb-2'><p>DANH SÁCH NHÂN VIÊN</p></div>
      <Search placeholder="Tìm kiếm" style={{ width: 280, marginRight: "15px" }} onChange={handleSearch} />

      <ListUser searchQuery={searchQuery} user={user} onCheckedUsersChange={handleCheckedUsersChange} />
      </>
)}
    </div>
  );
};

export default LichSide;
