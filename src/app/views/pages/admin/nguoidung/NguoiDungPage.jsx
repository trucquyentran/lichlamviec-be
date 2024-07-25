import React, { useCallback, useEffect, useState } from 'react';
import { Breadcrumb, Button, Input, Modal, Pagination, Table, Tag, Tooltip, message } from 'antd';
import NguoiDungModal from './NguoiDungModal';
import Services from 'app/services';
import { debounce } from 'lodash';
import HomeIcon from '@mui/icons-material/Home';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import { EditOutlined, MinusCircleOutlined } from '@ant-design/icons';
const { Search } = Input;

const NguoiDungPage = () => {
    const [modal, contextHolder] = Modal.useModal();
    const [listNguoiDung, setListNguoiDung] = useState([]);
    const [listNDMD, setListNDMD] = useState([]);
    const [nguoiDungUp, setNDUp] = useState();
    const [openNDModal, setOpenNDModal] = useState(false);
    const [windowScreen, setWindowScreen] = useState(window.screen.width > 1000);
    const [limit, setLimit] = useState(5);
    const [page, setPage] = useState(1);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        reLoadList();
    }, [limit, page]);

    useEffect(() => {
        if (listNDMD?.length > 0) {
            let data = listNDMD.filter((v, i) => {
                const start = limit * (page - 1);
                const end = start + limit;
                return i >= start && i < end;
            });
            setListNguoiDung(data);
        }
    }, [limit, page, listNDMD]);

    async function reLoadList(params) {
        setLoading(true);
        let dataRSListND = await Services.getNguoiDungService().getAll(page, limit);
        if (dataRSListND.data) {
            setListNDMD(formatData(dataRSListND?.data));
            setPage(1);
            console.log(dataRSListND.data);
        }
        setLoading(false);
    }

    function formatData(list) {
        let rs = [];
        list?.forEach(element => {
            if (element?.children?.length === 0) {
                rs.push({ ...element, children: null });
            } else {
                rs.push({ ...element, children: formatData(element?.children) });
            }
        });
        return rs;
    }

    const onShowSizeChange = (current, pageSize) => {
        setPage(1); // Reset to the first page when pageSize changes
        setLimit(pageSize);
    };

    const onClick = async (key, data) => {
        console.log("id", data)
        switch (key?.key) {
            case "1":
                setOpenNDModal(true);
                setNDUp({ ...data, nguoiDung: data?.nguoiDung ? { _id: data?.nguoiDung } : null });
                break;
            case "2":
                const confirmed = await modal.confirm({
                    title: 'Bạn có chắc muốn xóa người dùng này?',
                    content: "",
                });
                if (confirmed) {
                    setLoading(true);

                    Services.getNguoiDungService().deleteByID(data?._id)?.then(
                        (res) => {
                            if (res?.data?.error) {
                                alert(res?.data?.message);
                            } else {
                                message.success(`Đã xoá người dùng: ${data?.hoTen}`);
                                reLoadList();
                            }
                        }
                    );
                }
                break;
        }
    };

    const handleSearch = useCallback(
        debounce(async (e) => {
            setLoading(false);
            let dataRSListND = await Services.getNguoiDungService().searchND(e?.target?.value);
            if (dataRSListND?.data?.length === 0) {
                message.error("Không tìm thấy người dùng");
            } else {
                setListNDMD(dataRSListND?.data);
                setPage(1);
            }
        }, 500),
        [],
    );

    return (
        <>
            <div className='pb-2'>
                <Breadcrumb
                    items={[
                        {
                            title: <p className='bold f-16 c-575762'>Trang chủ</p>,
                        },
                        {
                            title: <p className='bold f-16 c-blue2'><HomeIcon className='mb-1' /> Quản lý người dùng</p>,
                            href: "/"
                        }
                    ]}
                />
            </div>

            <div className="page-new">
                <NguoiDungModal open={openNDModal} setOpen={setOpenNDModal} nguoiDungUp={nguoiDungUp} reLoadList={reLoadList} />
                <div className='flex ieoqwpesad'>
                    <div>
                        <Button onClick={() => { setOpenNDModal(true); setNDUp(); }} type="primary" className='btn-add bold'><AddIcon className='icon-btn' />Thêm mới</Button>
                    </div>
                    <div>
                        <Search placeholder="Tìm kiếm" style={{ width: 300, marginRight: "5px" }} onChange={handleSearch} />
                    </div>
                </div>
                {contextHolder}
                <Table
                    rowKey="_id"
                    loading={loading}
                    columns={[
                        {
                            title: "Trạng thái",
                            dataIndex: "trangThai",
                            key: "trangThai",
                            width: 120,
                            render: (trangThai) => {
                                if (trangThai === 1) {
                                    return <Tag color="green">Hoạt động</Tag>;
                                } else {
                                    return <Tag color="red">Bị cấm</Tag>;
                                }
                            },
                        },
                        {
                            title: "Họ và tên",
                            dataIndex: "hoTen",
                            key: "hoTen",
                            width: 250,
                        },
                        {
                            title: "Username",
                            dataIndex: "username",
                            key: "username",
                            width: 150,
                        },
                        {
                            title: "Role",
                            dataIndex: "listQuyen",
                            key: "listQuyen",
                            width: 180,
                            render: (listQuyen) => (
                                <>
                                    {listQuyen?.map((item, index) => {
                                        let color = item.quyen?.tenQuyen?.length > 8 ? 'cyan' : 'green';
                                        if (item.quyen?.tenQuyen === 'Admin') {
                                            color = 'red';
                                        }
                                        return (
                                            <Tag color={color} key={index}>
                                                {item.quyen?.tenQuyen?.toUpperCase()}
                                            </Tag>
                                        );
                                    })}
                                </>
                            ),
                        },
                        {
                            title: "Email",
                            dataIndex: "email",
                            key: "email",
                            width: 250,
                        },
                        {
                            title: "SDT",
                            dataIndex: "sdt",
                            key: "sdt",
                            width: 120,
                        },
                        {
                            title: "Giới tính",
                            dataIndex: "gioiTinh",
                            key: "gioiTinh",
                            width: 100,
                            render: (gioiTinh) => {
                                if (gioiTinh === true) {
                                    return 'Nữ';
                                } else {
                                    return 'Nam';
                                }
                            },
                        },
                        {
                            title: "Ngày sinh",
                            dataIndex: "ngaySinh",
                            key: "ngaySinh",
                            width: 120,
                        },
                        {
                            title: "Đơn vị",
                            dataIndex: "donVi",
                            key: "donVi",
                            width: 150,
                            render: (donVi) => donVi.tenDonVi,
                        },
                        {
                            title: " ",
                            render: (data, record) => (
                                <div className='flex'>
                                    <Button style={{ backgroundColor: '#efb540' }} onClick={() => { onClick({ key: "1" }, record) }} type="primary" shape="circle" icon={<EditOutlined />} />
                                    <Button style={{ backgroundColor: '#fa5944' }} className='mx-2' onClick={() => { onClick({ key: "2" }, data) }} type="primary" shape="circle" icon={<MinusCircleOutlined />} />
                                </div>
                            ),
                            fixed: windowScreen ? 'right' : false,
                            width: "80px"
                        }
                    ]}
                    scroll={{ x: '100%', y: 415 }}
                    locale={{ emptyText: 'Không có dữ liệu' }}
                    style={{ minHeight: 415 }}
                    dataSource={listNguoiDung}
                    pagination={false}
                    size='small'
                    className='pointer mt-1 table-cus-antd'
                />
                <div className='div-flex justify-between'>
                    <div></div>
                    <Pagination
                        showSizeChanger
                        onShowSizeChange={onShowSizeChange}
                        current={page}
                        pageSize={limit}
                        total={listNDMD.length}
                        showTotal={(total, range) => `${range[0]}-${range[1]} của ${total} mục`}
                    />
                </div>
            </div>
        </>
    );
};

export default NguoiDungPage;
