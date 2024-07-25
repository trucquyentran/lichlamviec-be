import { CircularProgress } from '@mui/material';
import FormatDate from 'app/common/FormatDate';
import React, { useEffect, useState } from 'react'
import { Button, Divider, Modal, DatePicker, Input, Radio, Empty, Select, message, TreeSelect } from 'antd';
import Services from 'app/services';
import Loading from 'app/components/Loading';
import dayjs from 'dayjs';
import DeleteForeverRoundedIcon from '@mui/icons-material/DeleteForeverRounded';
import CreateRoundedIcon from '@mui/icons-material/CreateRounded';
import CloseRoundedIcon from '@mui/icons-material/CloseRounded';
import Grid from '@mui/material/Unstable_Grid2';
import AccessTimeRoundedIcon from '@mui/icons-material/AccessTimeRounded';
import PersonRoundedIcon from '@mui/icons-material/PersonRounded';
import NotesRoundedIcon from '@mui/icons-material/NotesRounded';
import NotificationsNoneRoundedIcon from '@mui/icons-material/NotificationsNoneRounded';
import LocationOnRoundedIcon from '@mui/icons-material/LocationOnRounded';
import EditNoteRoundedIcon from '@mui/icons-material/EditNoteRounded';
import { CirclePicker } from 'react-color';
const { TextArea } = Input;
const LichModal = ({ open, setOpenInfo, setOpen, lichUp, reLoadList, openInfo, user }) => {

    const [lich, setLich] = useState(lichUp);
    const [selectedColor, setSelectedColor] = useState('#277DA1');
    const [listLichTT, setListLichTT] = useState([]);
    const [listUser, setListUser] = useState([]);
    const [error, setError] = useState("");
    const [sending, setSending] = useState(false);
    const [loading, setLoading] = useState(true);
    const [selectionType, setSelectionType] = useState('me');
    const [listDV, setListDV] = useState([]);


    console.log(lich);

    const handleOk = () => { }
    const range = (start, end) => {
        const result = [];
        for (let i = start; i < end; i++) {
            result.push(i);
        }
        return result;
    };


    useEffect(() => {
        if (open || openInfo) {
            setLich(lichUp?.body);
            setLoading(true);

            let newSelectionType = 'me'; // Default selection type

            if (lich?.donVi) {
                newSelectionType = 'unit';
            } else if (lich?.taiKhoan?._id && lich?.taiKhoan._id !== user._id) {
                newSelectionType = 'user';
            }

            setSelectionType(newSelectionType);

            // Only call these functions if open is true
            if (open) {
                if (newSelectionType === 'user') {
                    getUserOfUnit();
                } else if (newSelectionType === 'unit') {
                    getUnitData();
                }
            }

            console.log(selectedColor);
        }
    }, [open, openInfo, lichUp?.body, user._id]);

    // console.log(lich?.taiKhoan?._id);
    // console.log(user._id);
    // console.log(selectionType);
    const handleSelectionChange = (e) => {
        const value = e.target.value;
        setSelectionType(value);
        if (value === 'user') {
            getUserOfUnit();
        }
        else if (value === 'unit') {
            getUnitData();
        }
    };

    async function getUserOfUnit() {
        try {
            const dataUserOfUnit = await Services.getNguoiDungService().getUserOfUnit();
            if (dataUserOfUnit.data) {
                setListUser(dataUserOfUnit.data.map(obj => ({
                    value: obj._id,
                    label: obj.hoTen
                })));
            }
        } catch (error) {
            console.error("lỗi:", error);
            message.error("Có lỗi xảy ra");
        }
    }

    async function getUnitData() {

        try {
            const dataUnit = await Services.getDonViService().getAll();
            console.log(dataUnit);
            const formattedTreeData = dataUnit.data.map(unit => ({
                value: unit._id,
                label: unit.tenDonVi,

            }));
            setListDV(formattedTreeData);
            console.log(formattedTreeData);
        } catch (error) {
            console.error("lỗi:", error);
            message.error("Có lỗi xảy ra");
        }
    }


    const disabledDate = (current) => {
        return current && current < dayjs().startOf('day');
    };

    const disabledDateTime = () => ({
        disabledHours: () => [...Array(7).keys()].concat([...Array(24).keys()].slice(18, 24)),
        disabledMinutes: (selectedHour) => {
            if (selectedHour === 17) {
                return [...Array(60).keys()];
            }
            return [];
        }

    });
    const onChange = (field, value) => {
        let formattedValue = value;

        if (field === "thoiGianBD" || field === "thoiGianKT") {
            formattedValue = value ? dayjs(value).format("YYYY-MM-DDTHH:mm:ss") : null;
        } else if (field === "bg") {
            formattedValue = value || '#277DA1';
            setSelectedColor(formattedValue);
        }

        setLich({ ...lich, [field]: formattedValue });
    };


    const handleDelete = (id) => {
        setOpenInfo(false)
        Modal.confirm({

            title: 'Bạn có chắc muốn xóa lịch này?',
            content: "",
            style: {
                top: '30%',

            },
            onOk: () => {
                setLoading(true);
                Services.getLichService().xoaLich(id)?.then(
                    (res) => {
                        if (res?.value?.error) {
                            alert(res?.value?.message);
                        } else {
                            message.success(`Đã xoá lịch thành công!`);
                            reLoadList();
                            setOpen(false);
                            setOpenInfo(false);
                        }
                    }

                ).finally(() => {
                    setLoading(false);

                });
            },
            onCancel: () => {
                setOpenInfo(true)
            },

        });
    };


    const onSubmit = () => {
        setSending(true);
        const lichToSubmit = {
            ...lich,
            bg: lich.bg || '#277DA1'
        };

        if (!lichToSubmit._id) {
            Services?.getLichService()?.themLichCaNhan(lichToSubmit)?.then(
                (res) => {
                    if (res?.data?.error) {
                        setError(res?.data?.message);
                    } else {
                        setOpen(false);
                        message.success("Lưu thành công");
                        reLoadList();
                        console.log(lichToSubmit);
                    }
                    setSending(false);
                }
            );
        } else {
            const addLich = !lichToSubmit.donVi
                ? Services?.getLichService()?.suaLichCaNhan
                : Services?.getLichService()?.suaLichDonVi;


            addLich(lichToSubmit)?.then(
                (res) => {
                    if (res?.data?.error) {
                        setError(res?.data?.message);
                    } else {
                        setOpen(false);
                        message.success("Lưu thành công");
                        reLoadList();
                    }
                    setSending(false);
                }
            );
        }
        setSelectedColor('#277DA1');
    };

    const onClose = () => {

        setLich(null)
        setOpenInfo(false)
        setOpen(false)
        setSelectedColor('#277DA1')
        setSelectionType('me')
    }


    const openModalEdit = () => {
        setOpenInfo(false)
        setOpen(true)
        setSelectedColor(lichUp.bg)
    }

    console.log(lich);
    return (
        <>
            <Modal
                width={'480px'}
                open={openInfo} onOk={onSubmit} okText=""
                onCancel={onClose}
                centered
                footer={
                    <div className="flex justify-end">

                    </div>
                }
                transitionName=""
                maskTransitionName=""
                closable={false}
                title={
                    <div style={{ backgroundColor: lich?.bg || '#3cbade', margin: '-20px -24px 20px -24px' }} className=" rounded-t-lg flex justify-between items-center">
                        <div className="flex justify-start items-center p-4 ">
                            <p className='text-white text-lg font-bold'>{lich?.tieuDe}</p>
                        </div>



                        <div className="flex justify-end items-center space-x-2 mx-3 ">
                            {((lich?.donVi && user.listQuyen == 'Admin') || lich?.taiKhoan?._id == user._id) && (
                                <>
                                    <CreateRoundedIcon className="cursor-pointer text-white text-lg" onClick={openModalEdit} />
                                    <DeleteForeverRoundedIcon
                                        className="cursor-pointer text-white text-lg"
                                        onClick={() => handleDelete(lich?._id)}
                                        disabled={sending}
                                    />
                                </>
                            )}

                            <CloseRoundedIcon className='cursor-pointer text-white text-lg' onClick={onClose} />

                        </div>


                    </div>
                }
            >
                <Grid container spacing={2}>

                    {lich?.donVi ? (
                        <>
                            <Grid xs={2}>
                                <AccessTimeRoundedIcon className='text-stone-600' />
                            </Grid>

                            <Grid className="text-base font-bold" xs={10}>
                                {lich?.donVi?.tenDonVi}
                            </Grid>
                        </>
                    ) : (
                        <>
                            <Grid xs={2}>
                                <PersonRoundedIcon className='text-stone-600' />
                            </Grid>

                            <Grid className="text-base font-bold" xs={10}>
                                {lich?.taiKhoan?.hoTen}
                            </Grid>
                        </>
                    )
                    }
                    <Grid xs={2}>
                        <AccessTimeRoundedIcon className='text-stone-600' />
                    </Grid>
                    <Grid className="text-base font-bold" xs={10}>
                        {dayjs(lich?.thoiGianBD).format('HH:mm, DD/MM/YYYY')}  ―
                        {dayjs(lich?.thoiGianKT).format('  HH:mm, DD/MM/YYYY')}
                    </Grid>

                    <Grid xs={2}>
                        <NotesRoundedIcon className='text-stone-600' />
                    </Grid>
                    <Grid className="text-base text-stone-600" xs={10}>
                        {lich?.noiDung}

                    </Grid>
                    <Grid xs={2}>
                        <NotificationsNoneRoundedIcon className='text-stone-600' />
                    </Grid>
                    <Grid className="text-base text-stone-600" xs={10}>
                        10 phút
                    </Grid>
                    <Grid xs={2}>
                        <LocationOnRoundedIcon className='text-stone-600' />
                    </Grid>
                    <Grid className="text-base text-stone-600" xs={10}>
                        {lich?.diaDiem}
                    </Grid>
                    <Grid xs={2}>
                        <EditNoteRoundedIcon className='text-stone-600' />
                    </Grid>
                    <Grid className="text-base text-stone-600" xs={10}>
                        {lich?.ghiChu}
                    </Grid>
                </Grid>
            </Modal>

            <Modal centered
                width={600}
                open={open} onOk={onSubmit}
                okText=""
                onCancel={onClose}
                closable={false}
                title={
                    <div style={{ backgroundColor: lich?.bg || '#3cbade', margin: '-20px -24px 20px -24px' }} className=" rounded-t-lg flex justify-between items-center">
                        <div className="flex justify-start items-center p-4 ">
                            <p className='text-white text-lg font-bold'>{lich?.tieuDe}</p>
                        </div>
                        <div className="flex justify-end items-center space-x-2 mx-3 ">
                            <CloseRoundedIcon className='cursor-pointer text-white text-lg' onClick={onClose} />
                        </div>
                    </div>
                }
                footer={
                    <div >
                        {lich?._id && (
                            <Button className="me-1" key="delete" type="primary" danger onClick={() => handleDelete(lich?._id)} disabled={sending}>
                                {sending && <CircularProgress className="span-sender" />}
                                Xoá
                            </Button>
                        )}
                        <span key="error" className="text-red-500 me-1">
                            {error}
                        </span>
                        <Button className="me-1" key="submit" type="primary" onClick={onSubmit} disabled={sending}>
                            {sending && <CircularProgress className="span-sender" />}
                            {!lich?._id ? "Tạo mới" : "Cập nhật"}
                        </Button>
                        <Button key="back" onClick={() => setOpen(!open)}>
                            Hủy
                        </Button>
                    </div>
                }
            >
                {!loading ? <Loading />
                    :
                    <div className="div-setting-cus">
                        {user.listQuyen == 'Admin' && (
                            <div className='pb-3'>
                                <Radio.Group
                                    onChange={handleSelectionChange}
                                    value={selectionType}
                                >
                                    <Radio value='me'>Lịch cá nhân</Radio>
                                    <Radio value='user'>Lịch nhân viên</Radio>
                                    <Radio value='unit'>Lịch đơn vị</Radio>

                                </Radio.Group>
                            </div>
                        )}
                        {selectionType === 'user' && (
                            <div className='pb-3'>
                                <Grid container>
                                    <Grid item xs={3}>
                                        <p className='bold'>Nhân viên:</p>
                                    </Grid>
                                    <Grid item xs={9}>
                                        <Select
                                            showSearch
                                            style={{ width: '100%' }}
                                            value={lich?.taiKhoan?.hoTen}
                                            placeholder="Chọn nhân viên"
                                            optionFilterProp="children"
                                            onChange={(value) => onChange("nguoiDung", value)}
                                            filterOption={(input, option) =>
                                                option.label.toLowerCase().includes(input.toLowerCase())
                                            }
                                            options={listUser}
                                        />
                                    </Grid>
                                </Grid>
                            </div>
                        )}
                        {selectionType === 'unit' && (
                            <div className='pb-3'>
                                <Grid container>
                                    <Grid item xs={3}>
                                        <p className='bold'>Chọn đơn vị:</p>
                                    </Grid>
                                    <Grid item xs={9}>
                                        <TreeSelect
                                            showSearch
                                            style={{ width: '100%' }}
                                            value={lich?.donVi?.tenDonVi}
                                            dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                                            placeholder="Chọn đơn vị"
                                            allowClear
                                            treeDefaultExpandAll
                                            onChange={(value) => onChange("donVi", value)}
                                            treeData={listDV}
                                        />
                                    </Grid>
                                </Grid>
                            </div>
                        )}
                        <div className='pb-3'>
                            <Grid container >
                                <Grid xs={3}>
                                    <p className='bold'>Tiêu đề *: </p>
                                </Grid>
                                <Grid xs={9}>
                                    <Input value={lich?.tieuDe} onChange={(e) => onChange("tieuDe", e?.target?.value)} placeholder="Nhập tiêu đề" />

                                </Grid>
                            </Grid>
                        </div>
                        <div className='pb-3'>
                            <Grid container >
                                <Grid xs={3}>
                                    <p className='bold'> Ngày bắt đầu: </p>
                                </Grid>
                                <Grid xs={9}>
                                    <DatePicker
                                        showTime={{ format: 'HH:mm' }}

                                        value={lich?.thoiGianBD ? dayjs(lich?.thoiGianBD) :
                                            lich?.startStr ? dayjs(lich?.startStr) :
                                                null}
                                        onChange={(date, dateString) => onChange("thoiGianBD", dateString)}
                                        format="YYYY-MM-DD HH:mm"
                                        placeholder='Ngày bắt đầu'
                                        disabledDate={disabledDate}
                                        disabledTime={disabledDateTime}
                                    />
                                </Grid>
                            </Grid>
                        </div>
                        <div className='pb-3'>
                            <Grid container>
                                <Grid item xs={3}>
                                    <p className='bold'>Ngày kết thúc: </p>
                                </Grid>
                                <Grid item xs={9}>
                                    <DatePicker
                                        showTime={{ format: 'HH:mm' }}
                                        value={lich?.thoiGianKT ? dayjs(lich?.thoiGianKT) : lich?.endStr ? dayjs(lich?.endStr) : null}
                                        onChange={(date, dateString) => onChange('thoiGianKT', dateString)}
                                        format="YYYY-MM-DD HH:mm"
                                        placeholder='Ngày kết thúc'
                                        disabledDate={disabledDate}
                                        disabledTime={disabledDateTime}
                                        style={{ width: '100%' }}
                                    />
                                </Grid>
                            </Grid>
                        </div>

                        <div className='pb-3'>
                            <Grid container>
                                <Grid item xs={3}>
                                    <p className='bold'>Địa điểm: </p>
                                </Grid>
                                <Grid item xs={9}>
                                    <TextArea
                                        value={lich?.diaDiem}
                                        onChange={(e) => onChange('diaDiem', e?.target?.value)}
                                        placeholder="Nhập địa điểm"
                                        rows={3}
                                        style={{ width: '100%' }}
                                    />
                                </Grid>
                            </Grid>
                        </div>

                        <div className='pb-3'>
                            <Grid container>
                                <Grid item xs={3}>
                                    <p className='bold'>Nội dung: </p>
                                </Grid>
                                <Grid item xs={9}>
                                    <TextArea
                                        value={lich?.noiDung}
                                        onChange={(e) => onChange('noiDung', e?.target?.value)}
                                        placeholder="Nhập nội dung"
                                        rows={3}
                                        style={{ width: '100%' }}
                                    />
                                </Grid>
                            </Grid>
                        </div>

                        <div className='pb-3'>
                            <Grid container>
                                <Grid item xs={3}>
                                    <p className='bold'>Ghi chú: </p>
                                </Grid>
                                <Grid item xs={9}>
                                    <TextArea
                                        value={lich?.ghiChu}
                                        onChange={(e) => onChange('ghiChu', e?.target?.value)}
                                        placeholder="Nhập ghi chú"
                                        rows={3}
                                        style={{ width: '100%' }}
                                    />
                                </Grid>
                            </Grid>
                        </div>
                        <div className='pb-3'>
                            <Grid container >
                                <Grid item xs={3}>
                                    <p className='bold'>Màu sự kiện: </p>
                                </Grid>
                                <Grid item xs={9}>
                                    <CirclePicker
                                        onChange={(bg) => onChange('bg', bg.hex)}
                                        colors={['#277DA1', '#20b799', '#F8961E', '#F94144']}
                                        color={selectedColor}
                                        style={{ width: '100%' }}
                                    />
                                </Grid>
                            </Grid>
                        </div>
                    </div >
                }
            </Modal>
        </>
    );
};

export default LichModal;

