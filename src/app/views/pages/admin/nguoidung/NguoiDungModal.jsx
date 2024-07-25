import { CircularProgress } from '@mui/material';
import React, { useEffect, useState } from 'react';
import { Button, Modal, DatePicker, Input, Select, message, TreeSelect, Tag, Radio, Form } from 'antd';
import Services from 'app/services';
import Loading from 'app/components/Loading';
import dayjs from 'dayjs';

const { TextArea } = Input;

const NguoiDungModal = ({ open, setOpen, nguoiDungUp, reLoadList }) => {
    const [nguoiDung, setNguoiDung] = useState({});
    const [listDv, setListDv] = useState([]);
    const [listQuyen, setListQuyen] = useState([]);
    const [error, setError] = useState("");
    const [sending, setSending] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (open) {
            setNguoiDung({
                ...nguoiDungUp,
                donVi: nguoiDungUp?.donVi?._id,
                listQuyen: nguoiDungUp?.listQuyen?.map(item => item?.quyen?._id) || []
            });
            getDsQuyen();
            reloadListSelect();
        }
    }, [open]);

    const tagRender = (props) => {
        const { label, closable, onClose } = props;
        const onPreventMouseDown = (event) => {
            event.preventDefault();
            event.stopPropagation();
        };
        const color = label === 'Admin' ? 'red' : 'cyan';
        return (
            <Tag
                color={color}
                onMouseDown={onPreventMouseDown}
                closable={closable}
                onClose={onClose}
                style={{ marginInlineEnd: 4 }}
            >
                {label}
            </Tag>
        );
    };

    async function getDsQuyen() {
        setLoading(true);
        try {
            const dataRSListQuyen = await Services.getQuyenService().getDsQuyen();
            if (dataRSListQuyen.data) {
                setListQuyen(dataRSListQuyen.data.map(obj => ({
                    value: obj._id,
                    label: obj.tenQuyen
                })));
            }
        } catch (error) {
            console.error("lỗi:", error);
            message.error("Có lỗi xảy ra");
        } finally {
            setLoading(false);
        }
    }

    async function reloadListSelect() {
        setLoading(true);
        try {
            const dataRSListDv = await Services.getDonViService().getAll();
            if (dataRSListDv.data) {
                setListDv(dataRSListDv.data.map(obj => ({
                    value: obj._id,
                    label: obj.tenDonVi
                })));
            }
        } catch (error) {
            console.error("Error fetching units:", error);
            message.error("Error fetching units");
        } finally {
            setLoading(false);
        }
    }

    const onChange = (key, value) => {
        setNguoiDung(prevState => ({
            ...prevState,
            [key]: value
        }));
    };

    const onDateChange = (key, date, dateString) => {
        onChange(key, dateString);
    };

    const onSubmit = async () => {
        setSending(true);

        const nguoiDungPayload = {
            ...nguoiDung,
            ngaySinh: dayjs(nguoiDung?.ngaySinh).format('YYYY-MM-DD')
        };

        try {
            let res;
            if (!nguoiDung?._id) {
                res = await Services.getNguoiDungService().save(nguoiDungPayload);
            } else {
                res = await Services.getNguoiDungService().update(nguoiDungPayload);
            }

            if (res?.data?.error) {
                setError(res?.data?.message);
            } else {
                setOpen(false);
                message.success("Lưu thành công");
                reLoadList();
            }
        } catch (error) {
            console.error("Error submitting user data:", error);
            message.error("Không thành công!");
        } finally {
            setSending(false);
        }
    };
console.log(nguoiDung);
    return (
        <Modal
            title="Người dùng"
            open={open}
            centered
            onOk={onSubmit}
            onCancel={() => setOpen(false)}
            footer={[
                <span key="error" className='me-1 red'>{error}</span>,
                <Button key="submit" type="primary" onClick={onSubmit} loading={sending}>
                    {!nguoiDungUp?._id ? "Tạo mới" : "Cập nhật"}
                </Button>,
                <Button key="back" onClick={() => setOpen(false)}>
                    Hủy
                </Button>
            ]}
        >
            {loading ? <Loading /> : (
                <div className="div-setting-cus">
                    <div className='pb-3'>
                        <p className='bold'> Tên người dùng *: </p>
                        <Input
                            value={nguoiDung?.hoTen}
                            onChange={(e) => onChange("hoTen", e.target.value)}
                            placeholder="Nhập tên người dùng"
                        />
                    </div>
                    {/* <Form>
                    <Form.Item
                        hasFeedback
                        label="Field B"
                        name="field_b"
                        validateDebounce={1000}
                        rules={[
                            {
                                max: 3,
                            },
                        ]}
                    >
                        <Input 
                            disabled={!!nguoiDungUp?._id}
                            value={nguoiDung?.username}
                            onChange={(e) => onChange("username", e.target.value)}
                            placeholder="Nhập tên đăng nhập" />
                    </Form.Item>
                    </Form> */}
                    <div className='pb-3'>
                        <p className='bold'> Username: </p>

                        <Input

                            disabled={!!nguoiDungUp?._id}
                            value={nguoiDung?.username}
                            onChange={(e) => onChange("username", e.target.value)}
                            placeholder="Nhập tên đăng nhập"
                        />
                    </div>
                    {!nguoiDungUp?._id && (
                        <div className='pb-3'>
                            <p className='bold'> Mật khẩu: </p>
                            <Input
                                onChange={(e) => onChange("password", e.target.value)}
                                placeholder="Nhập mật khẩu"
                            />
                        </div>
                    )}
                    <div className='pb-3'>
                        <p className='bold'> Ngày sinh: </p>
                        <DatePicker
                            value={dayjs(nguoiDung?.ngaySinh)}
                            onChange={(date, dateString) => onDateChange("ngaySinh", date, dateString)}
                            placeholder="Chọn ngày sinh"
                            format={'DD/MM/YYYY'}
                        />
                    </div>
                    <div className='pb-3'>
                        <p className='bold'> Giới tính: </p>
                        <Radio.Group onChange={(e) => onChange("gioiTinh", e.target.value)} name="radiogroup" defaultValue={nguoiDung?.gioiTinh}>
                            <Radio value={true}>Nữ</Radio>
                            <Radio value={false}>Nam</Radio>
                        </Radio.Group>
                    </div>
                    <div className='pb-3'>
                        <p className='bold'> Số điện thoại: </p>
                        <Input
                            value={nguoiDung?.sdt}
                            onChange={(e) => onChange("sdt", e.target.value)}
                            placeholder="Nhập số điện thoại"
                        />
                    </div>
                    <div className='pb-3'>
                        <p className='bold'> Email: </p>
                        <Input
                            value={nguoiDung?.email}
                            onChange={(e) => onChange("email", e.target.value)}
                            placeholder="Nhập email"
                        />
                    </div>
                    <div className='pb-3'>
                        <p className='bold'> Đơn vị: </p>
                        <TreeSelect
                            showSearch
                            treeDefaultExpandAll
                            allowClear
                            placeholder="Chọn đơn vị"
                            value={nguoiDung?.donVi}
                            style={{ width: '100%' }}
                            onChange={(value) => onChange("donVi", value)}
                            treeData={listDv}
                        />
                    </div>
                    <div className='pb-3'>
                        <p className='bold'> Quyền: </p>
                        <Select
                            mode="multiple"
                            tagRender={tagRender}
                            value={nguoiDung?.listQuyen}
                            placeholder="Chọn quyền"
                            style={{ width: '100%' }}
                            options={listQuyen}
                            onChange={(value) => onChange("listQuyen", value)}
                        />
                    </div>
                </div>
            )}
        </Modal>
    );
};

export default NguoiDungModal;
