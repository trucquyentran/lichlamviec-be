import React, { useEffect, useState } from "react";
import { Button, Form, Input, Space, DatePicker, Select, Modal, notification } from 'antd';
import UserService from '../../Service/UserService';
import AddIcon from '@mui/icons-material/Add';
import { Content } from 'antd/es/layout/layout';
const { Option } = Select;
function EditUs ( { refreshUsers } )
{
    const [ form ] = Form.useForm();
    const [ open, setOpen ] = useState( false );
    // const [ users, setUsers ] = useState( [] );
    const [ api, contextHolder ] = notification.useNotification();

    const onFinish = async ( values ) =>
    {
        try
        {
            await UserService.createUser( values );
            api.success( {
                message: 'Success',
                description: 'User registered successfully!',
                showProgress: true,
            } );
            form.resetFields();
            refreshUsers();
            setOpen( false ); // Đóng Modal sau khi submit thành công
        } catch ( error )
        {
            console.error( "Error registering user:", error );
            api.error( {
                message: 'Error',
                description: 'User registration failed.',
                showProgress: true,
            } );
        }
    };

    const showModal = () =>
    {
        setOpen( true );
    };

    const hideModal = () =>
    {

        setOpen( false );
    };

    return (
        <>
            <Content style={ {
                marginBottom: '1rem',
                display: 'flex', justifyContent: 'flex-end'
            } }
            >

                <Button type="primary" onClick={ () =>
                {
                    showModal();
                } }>
                    <AddIcon />New
                </Button>
            </Content>
            <Modal
                title="Add  User"
                visible={ open }
                onCancel={ hideModal }
                footer={ [
                    <Button key="cancel" onClick={ hideModal }>
                        Cancel
                    </Button>,
                    <Button htmlType="button" onClick={ () => form.resetFields() }>Reset</Button>,
                    <Button key="submit" type="primary" onClick={ () => form.submit() }>
                        Submit
                    </Button>,
                ] }
            >
                { contextHolder }
                <Form
                    form={ form }
                    onFinish={ onFinish }
                    style={ { maxWidth: 600 } }
                >
                    <Form.Item name="hoten" label="Họ tên" rules={ [ { required: true, message: 'Vui lòng nhập họ tên!' } ] }>
                        <Input placeholder="Nhập vào Họ tên" />
                    </Form.Item>
                    <Form.Item name="gioitinh" label="Giới tính" rules={ [ { required: true, message: 'Vui lòng chọn giới tính!' } ] }>
                        <Select placeholder="Chọn giới tính" style={ { width: '100%' } }>
                            <Option value="Nam">Nam</Option>
                            <Option value="Nữ">Nữ</Option>
                        </Select>
                    </Form.Item>
                    <Form.Item name="ngaysinh" label="Ngày sinh" rules={ [ { required: true, message: 'Vui lòng chọn ngày sinh!' } ] }>
                        <DatePicker placeholder="Chọn ngày sinh" style={ { width: '100%' } } format="DD-MM-YYYY" />
                    </Form.Item>
                    <Form.Item name="diachi" label="Địa chỉ" rules={ [ { required: true, message: 'Vui lòng nhập địa chỉ!' } ] }>
                        <Input placeholder="Nhập vào địa chỉ" />
                    </Form.Item>
                    <Form.Item name="sdt" label="SĐT" rules={ [ { required: true, message: 'Vui lòng nhập SĐT!' } ] }>
                        <Input type='number' placeholder="Nhập vào số điện thoại" />
                    </Form.Item>
                    <Form.Item name="email" label="Email" rules={ [ { required: true, message: 'Vui lòng nhập email!' } ] }>
                        <Input type='email' placeholder="Nhập vào địa chỉ email" />
                    </Form.Item>

                </Form>
            </Modal>
        </>
    );
};
export default EditUs;
