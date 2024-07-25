
import { Breadcrumb, Button, Dropdown, Input, Modal, Pagination, Space, Table, message } from 'antd';
import FormatDate from 'app/common/FormatDate';
import React, { useCallback, useEffect, useState } from 'react'
import DonViModal from './DonViModal';
import Services from 'app/services';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import AddIcon from '@mui/icons-material/Add';
import { debounce } from "lodash";
import SapXep from 'app/common/SapXep';
import HomeIcon from '@mui/icons-material/Home';

import { EditOutlined, MinusCircleOutlined } from '@ant-design/icons';
const { Search } = Input;
const items = [
    {
        key: '1',
        label: "Cập nhật",
    },
    {
        key: '2',
        danger: true,
        label: 'Xóa',
    },
];
const DonViPage = () => {
    const [modal, contextHolder] = Modal.useModal();
    const [listDonVi, setListDonVi] = useState([]);
    const [listDonViMD, setListDonViMD] = useState([]);
    const [donViUp, setDonViUp] = useState();
    const [openDonViModal, setOpenDonViModal] = useState(false);

    const [windowScreen, setWindowScreen] = useState(window.screen.width > 1000);
    const [limit, setLimit] = useState(5);
    const [page, setPage] = useState(1);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        reLoadList()
    }, []);
    useEffect(() => {
        if (listDonViMD?.length > 0) {
            let data = listDonViMD.filter((v, i) => {
                const start = limit * (page - 1);
                const end = start + limit;
                return i >= start && i < end;
            });
            setListDonVi(data);
        }

    }, [limit, page, listDonViMD]);

console.log(page);
    async function reLoadList(params) {
        setLoading(true)

        let dataRSLisstDv = await Services.getDonViService().getAll(page,limit)
        if (dataRSLisstDv.data) {
            setListDonViMD(formatData(dataRSLisstDv?.data))
            setPage(1)
        }
        setLoading(false)
    }
    function formatData(list) {
        let rs = []
        list?.forEach(element => {

            if (element?.children?.length == 0) {
                rs.push({ ...element, children: null })
            } else {
                rs.push({ ...element, children: formatData(element?.children) })
            }
        });
        return rs
    }
    const onShowSizeChange = (current, pageSize) => {
        setPage(current)
        setLimit(pageSize)
    };
    const onClick = async (key, data) => {
        console.log(key?.key);
        switch (key?.key) {
            case "1":
                setOpenDonViModal(true)
                setDonViUp({ ...data, donViTrucThuoc: data?.donViTrucThuoc ? { _id: data?.donViTrucThuoc } : null })
                break;      
            case "2":
                const confirmed = await modal.confirm({
                    title: 'Bạn có chắc muốn xóa đơn vị này',
                    content: "",
                });
                console.log(confirmed);
                if (confirmed) {
                    setLoading(true);
                    Services.getDonViService().deleteByID(data?._id)?.then(
                        (res) => {
                            if (res?.data?.error) {
                                alert(res?.data?.message)
                            } else {
                                message.success("Xoá thành công")
                                reLoadList()
                            }
                        });
                }

                break;

        }
    };
    const handleSearch = useCallback(
        debounce(async (e) => {
            setLoading(false)
            console.log(e);
            let dataRSLisstDv = await Services.getDonViService().searchDV(e?.target?.value)

            if (dataRSLisstDv?.data?.length == 0) {
                message.error("Không tìm thấy đơn vị")
            } else {
                setListDonViMD(SapXep.sapXepTheoObjectAtrVaAtr(dataRSLisstDv?.data, "donViTrucThuoc", "stt", -1, 1))
                setPage(1)
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
                            title: <p className='bold f-16 c-575762'>Trang chủ </p>,
                        },
                        {
                            title: <p className='bold f-16 c-blue2'><HomeIcon className='mb-1' /> Đơn vị quản lý</p>,
                            href: "/"
                        }

                    ]}
                /></div>

            <div className="page-new">
                <DonViModal open={openDonViModal} setOpen={setOpenDonViModal} donViUp={donViUp} reLoadList={reLoadList} />
                <div className='flex  ieoqwpesad'>
                    <div>
                        <Button onClick={() => { setOpenDonViModal(true); setDonViUp() }} type="primary" className='btn-add  bold'><AddIcon className='icon-btn' />Thêm mới</Button>
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
                            title: "Tên đơn vị",
                            dataIndex: "tenDonVi",
                            key: "tenDonVi",
                            width: 200,
                        },

                        {
                            title: " ",
                            render: (data, record) => (
                                <div className='flex'>
                                    {/* <Dropdown
                                        menu={{
                                            items,
                                            onClick: (e) => onClick(e, data)
                                        }}
                                    // onClick={(e) => }
                                    >
                                        <a onClick={(e) => e.preventDefault()}>
                                            <Space>
                                                <MoreVertIcon className='f-13' />
                                            </Space>
                                        </a>
                                    </Dropdown> */}

<Button style={{backgroundColor:'#efb540'}} onClick={() => { onClick({ key: "1" }, record) }} type="primary" shape="circle" icon={<EditOutlined />} />

<Button style={{backgroundColor:'#fa5944'}} className='mx-2' onClick={() => { onClick({ key: "2" }, data) }} type="primary" shape="circle" icon={<MinusCircleOutlined />} />

                                </div>
                            ),
                            fixed: windowScreen ? 'right' : false,
                            width: "15px"
                        }

                    ]}
                    scroll={{ x: '100%', y: 415 }}
                    locale={{ emptyText: 'Không có dữ liệu' }}
                    style={{ minHeight: 415 }}
                    dataSource={listDonVi}
                    pagination={false}
                    size='small'
                    className='pointer mt-1 table-cus-antd'

                />

                <div className='div-flex justify-between'>
                    <div></div>
                    <Pagination
                        showSizeChanger
                        onShowSizeChange={onShowSizeChange}

                        total={listDonVi?.length || 0}
                        defaultPageSize={30}
                    />
                </div>
            </div >
        </>
    );
};

export default DonViPage;
