import { AutoComplete, Breadcrumb, Button, FloatButton, Input, Modal, message } from 'antd';
import React, { useCallback, useEffect, useState, useRef } from 'react';
import Services from 'app/services';
import { debounce } from "lodash";
import HomeIcon from '@mui/icons-material/Home';
import AddRoundedIcon from '@mui/icons-material/AddRounded';
import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid';
import dayGridPlugin from '@fullcalendar/daygrid';
import listPlugin from '@fullcalendar/list';
import Grid from '@mui/material/Grid';
import multiMonthPlugin from '@fullcalendar/multimonth';
import '../../../../assets/css/calendar.css';
import viLocale from '@fullcalendar/core/locales/vi';
import interactionPlugin from '@fullcalendar/interaction';
import dayjs from 'dayjs';
import LichModal from './LichModal';
import LichSide from './LichSide';
import rgbHex from 'rgb-hex';

const { Search } = Input;

function LichPage({ user }) {

  const calendarRef = useRef(null);
  const [listLichMD, setListLichMD] = useState([]);
  const [lichUp, setLichUp] = useState(null);
  const [openLichModal, setOpenLichModal] = useState(false);
  const [openSearch, setOpenSearch] = useState(false);
 
  const [openLichModalInfo, setOpenLichModalInfo] = useState(false);
  const [checkedUsers, setCheckedUsers] = useState([]);
  const [options, setOptions] = useState([]);
  const [windowScreen, setWindowScreen] = useState(window.screen.width > 1000);
  const [loading, setLoading] = useState(true);
  const [showPersonal, setShowPersonal] = useState(true);
  const [showUnit, setShowUnit] = useState(true);
  const [showLichOfUser, setShowLichOfUser] = useState(true);
  const [showSearchResults, setShowSearchResults] = useState(false);
  const [viewCal, setViewCal] = useState('timeGridWeek');
  console.log(viewCal);
  useEffect(() => {
    reLoadList();
  }, [showPersonal, showUnit, showLichOfUser, checkedUsers]);

  const handleCheckedUsersChange = (updatedCheckedUsers) => {
    setCheckedUsers(updatedCheckedUsers);
  };

  const reLoadList = async () => {
    setLoading(true);
    let lichLamViec = [];
    if (showPersonal) {
      try {
        const personalSchedules = await Services.getLichService().getDsLich();
        lichLamViec = personalSchedules.data || [];

      } catch (error) {
        console.error(error);
      }
    }
    if (showLichOfUser && checkedUsers.length > 0) {
      try {
        const userSchedulesPromises = checkedUsers.map((userId) =>
          Services.getLichService().listLichOfOneUser(userId)
        );
        const userSchedulesResponses = await Promise.all(userSchedulesPromises);
        userSchedulesResponses.forEach((response) => {
          lichLamViec = lichLamViec.concat(response.data.body || []);
        });
      } catch (error) {
        console.error(error);
      }
    }
    if (showUnit) {
      try {
        const unitSchedules = await Services.getLichService().getDsLichDV();
        lichLamViec = lichLamViec.concat(unitSchedules.data || []);
      } catch (error) {
        console.error(error);
      }
    }
    if (lichLamViec.length > 0) {
      setListLichMD(lichLamViec.map(formatData));
    } else {
      setListLichMD([]);
    }
    setLoading(false);
  };

  const formatData = (event) => {
    return {
      id: event._id,
      start: event.thoiGianBD,
      end: event.thoiGianKT,
      title: event.tieuDe,
      noiDung: event.noiDung,
      diadiem: event.diaDiem,
      ghiChu: event.ghiChu,
      userId: event.taiKhoan,
      color: event.bg,
      donVi: event.donVi
    };
  };

  const dayHeaderContent = (arg) => {
    const weekdayName = arg.date.toLocaleString('vi-VN', { weekday: 'narrow' });
    const dayNumber = arg.date.getDate();
    return (
      <div className="custom-day-header">
        <div className="weekday">{weekdayName}</div>
        <div style={{ fontSize: 22 }} className="day-number">{dayNumber}</div>
      </div>
    );
  };

  const handleEventClick = async (info) => {
    let lichLamViec = await Services.getLichService().getLichByID(info.event.id);
    setLichUp(lichLamViec.data);
    setOpenLichModalInfo(true);
  };

  const handleSelectMirror = async (info) => {
    const updatedEventData = {
      thoiGianBD: dayjs(info.startStr).format('YYYY-MM-DDTHH:mm:ss'),
      thoiGianKT: dayjs(info.endStr).format('YYYY-MM-DDTHH:mm:ss'),
      bg: '#277DA1'
    };
    setLichUp(updatedEventData);
    setOpenLichModal(true);
  };

  const setOpenLichModalAdd = async () => {
    setLichUp(null);
    setOpenLichModal(true);
  };

  const handleEventChange = (info) => {
    const { event } = info;
    const updatedEventData = {
      _id: event.id,
      diaDiem: event.extendedProps.diaDiem,
      ghiChu: event.extendedProps.ghiChu,
      noiDung: event.extendedProps.noiDung,
      thoiGianBD: dayjs(event.startStr).format('YYYY-MM-DDTHH:mm:ss'),
      thoiGianKT: dayjs(event.endStr).format('YYYY-MM-DDTHH:mm:ss'),
      bg: event.backgroundColor,
      tieuDe: event.title,
      donVi: event.extendedProps.donVi
    };
    const editLich = !event.extendedProps.donVi
      ? Services?.getLichService()?.suaLichCaNhan
      : Services?.getLichService()?.suaLichDonVi;

    editLich(updatedEventData).then((res) => {
      if (res?.data?.error) {
        alert(res?.data?.message);
        info.revert();
      } else {
        message.success("Sự kiện đã được cập nhật");
      }
    }).catch((error) => {
      console.error('Error updating event:', error);
      info.revert();
    });
  };

  const handleEventReceive = (info) => {
    const startTime = dayjs(info.event.start);
    let endTime;

    if (info.event.allDay) {
      endTime = startTime.add(1, 'day').startOf('day');
    } else {
      endTime = startTime.add(1, 'hours');
    }

    const formattedStartTime = startTime.format('YYYY-MM-DDTHH:mm:ss');
    const formattedEndTime = endTime.format('YYYY-MM-DDTHH:mm:ss');

    const eventData = {
      tieuDe: info.event.title,
      thoiGianBD: formattedStartTime,
      thoiGianKT: formattedEndTime,
      bg: '#' + rgbHex(info.event.backgroundColor)
    };

    Services?.getLichService()?.themLichCaNhan(eventData)?.then((res) => {
      if (res?.data?.error) {
        message.error('Thất bại');
      } else {
        message.success('Lưu thành công');
        reLoadList();
      }
    });
  };

  const eventContent = function (info) {
    const startTime = dayjs(info.event.start).format('HH:mm');
    const endTime = info.event.end ? dayjs(info.event.end).format('HH:mm') : '';
    const color = info.event.extendedProps.donVi ? 'black' : 'white';
    const content = `<div class="text-xs" style="color: ${color}; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${info.event.extendedProps.noiDung || ''}</div>`;
    const unit = info.event.extendedProps.donVi ? `<span class='font-bold'>${info.event.extendedProps.donVi.tenDonVi}</span>` : '';

    const { userId } = info.event.extendedProps;
    const { _id: currentUserId } = user;

    const name = userId?._id !== currentUserId && userId?.hoTen
      ? `<span class='font-bold text-white'>${userId.hoTen}</span>`
      : '';

    const title = `<div class="font-bold" style="color: ${color}; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${info.event.title || ''}</div>`;
    const time = `<div class="text-xs" style="color: ${color}">${startTime}${endTime ? `-${endTime}` : ''}</div>`;
    const contentElement = `<div class="text-xs" style="color: ${color}">${info.event.extendedProps.noiDung || ''}</div>`;
    return { html: `<div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;"><span class='font-bold italic'>${unit}</span> ${name} ${time} ${title} ${contentElement}</div>` };
  };

  const eventDidMount = function (info) {
    if (info.event.extendedProps.donVi) {
      const eventElement = info.el.querySelector('.fc-event-main ');
      if (eventElement) {
        const backgroundColorWithOpacity = 'rgba(255, 255, 255, 0.65)';
        eventElement.style.setProperty('background-color', backgroundColorWithOpacity, 'important');
        eventElement.style.setProperty('border-color', `${info.event.backgroundColor}`, 'important');
        eventElement.style.setProperty('color', `black`, 'important');
      }
    }
  };

  const handleSearch = useCallback(
    debounce(async (value) => {
      const searchValue = value?.trim();
      setLoading(true);
      try {
        let lichLamViec = [];
        if (!searchValue) {
          reLoadList();
        } else {
          const searchResponse = await Services.getLichService().searchLich(searchValue);
          lichLamViec = searchResponse?.data || [];
          if (lichLamViec.length === 0) {
            message.error("Không tìm thấy lịch");
          }
        }
        setOptions(lichLamViec.map(formatData));
      } catch (error) {
        console.error("Error fetching data:", error);
      } finally {
        setLoading(false);
      }
    }, 500),
    []
  );

  const handleSearchSelect = async (value) => {

    let lichLamViec = await Services.getLichService().getLichByID(value);
    setLichUp(lichLamViec.data);
    setOpenLichModalInfo(true);

  };

  const toggleSearchResults = () => {
    setShowSearchResults(!showSearchResults);
  };
  console.log(viewCal);
  return (
    <>
      <div className='pb-2'>
        <Breadcrumb
          items={[
            {
              title: <p className='bold f-16 c-575762'>Trang chủ </p>,
            },
            {
              title: <p className='bold f-16 c-blue2'><HomeIcon className='mb-1' /> Lịch làm việc</p>,
              href: "/"
            }
          ]}
        />
      </div>
      <FloatButton icon={<AddRoundedIcon />} type='primary' className='show-on-md' onClick={setOpenLichModalAdd} />
      <LichModal user={user} openInfo={openLichModalInfo} setOpenInfo={setOpenLichModalInfo} open={openLichModal} setOpen={setOpenLichModal} lichUp={lichUp} reLoadList={reLoadList} />
      <div className='flex ieoqwpesad'>
        <div></div>
        <div className='mb-4'>
          <AutoComplete
            options={options.map(event => ({ value: event.id, label: event.title }))}
            style={{ width: 400 }}
            onSelect={handleSearchSelect}
            onSearch={handleSearch}
          >
            <Search placeholder="Tìm kiếm" style={{ width: 400 }} enterButton />
          </AutoComplete>

        </div>
      </div>
      {showSearchResults && (
        <SearchResults options={options} />
      )}
      <Grid container spacing={2}>
        <Grid item xs={12} sm={3} md={3} lg={3}>
          <LichSide onCheckedUsersChange={handleCheckedUsersChange} user={user} setOpenLichModalAdd={setOpenLichModalAdd} setShowPersonal={setShowPersonal} setShowUnit={setShowUnit} />
        </Grid>
        <Grid item xs={12} sm={9} md={9} lg={9}>
          <div className="md:hidden mb-4">
            <select
              value={viewCal}
              onChange={(e) => setViewCal(e.target.value)}
              className="block w-full p-2 border rounded"
            >
              <option value="timeGridWeek">Week</option>
              <option value="timeGridDay">Day</option>
              <option value="dayGridMonth">Month</option>
              <option value="multiMonthYear">Year</option>
              <option value="listWeek">List</option>
            </select>
          </div>
          <FullCalendar
            height={'47rem'}
            businessHours={[
              {
                daysOfWeek: [1, 2, 3, 4, 5],
                startTime: '07:00',
                endTime: '11:30'
              },
              {
                daysOfWeek: [1, 2, 3, 4, 5],
                startTime: '13:30',
                endTime: '17:00'
              },
              {
                daysOfWeek: [6],
                startTime: '7:00',
                endTime: '11:30'
              }
            ]}
            ref={calendarRef}
            locale={viLocale}
            plugins={[timeGridPlugin, dayGridPlugin, interactionPlugin, listPlugin, multiMonthPlugin]}
            initialView="listWeek"
            // initialView={viewCal}
            nowIndicator={true}
            droppable={true}
            selectMirror={true}
            editable={true}
            eventChange={handleEventChange}
            selectable={true}
            events={listLichMD}
            dayHeaderContent={dayHeaderContent}
            eventClick={handleEventClick}
            select={handleSelectMirror}
            eventReceive={handleEventReceive}
            dayMaxEventRows={4}
            eventContent={eventContent}
            eventDidMount={eventDidMount}
            headerToolbar={{
              left: 'prev,today,next',
              center: 'title',
              right: 'timeGridWeek,timeGridDay,dayGridMonth,multiMonthYear,listWeek',
            }}
            slotMinTime="07:00:00"
            slotMaxTime="18:00:00"
            slotLabelFormat={{
              hour: 'numeric',
              minute: '2-digit',
            }}
          />
        </Grid>
      </Grid>
    </>
  );
};

const SearchResults = ({ options }) => (
  <div className="search-results">
    <h3>Kết quả tìm kiếm</h3>
    <ul>
      {options.map(option => (
        <li key={option.id}>{option.title}</li>
      ))}
    </ul>
  </div>
);

export default LichPage;
