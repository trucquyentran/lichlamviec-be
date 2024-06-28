import React, { useEffect, useState } from 'react';
import { Button, Form, Input, Select, Modal, DatePicker, notification } from 'antd';
import moment from 'moment';
import UserService from '../../Service/UserService';

const { Option } = Select;

function EditUs ( { visible, onCancel, editingUser, refreshUsers } ) 
{
    const [ form ] = Form.useForm();
    // const [ users, setUsers ] = useState( [] );
    const [ api, contextHolder ] = notification.useNotification();

    useEffect( () =>
    {
        // Load editingUser data into the form when it changes
        if ( editingUser )
        {
            form.setFieldsValue( {
                _id: editingUser._id,
                hoten: editingUser.hoten,
                gioitinh: editingUser.gioitinh,
                ngaysinh: moment( editingUser.ngaysinh, 'YYYY-MM-DD' ),
                diachi: editingUser.diachi,
                sdt: editingUser.sdt,
                email: editingUser.email,
            } );
        }
    }, [ editingUser, form ] );

    // Validator function for checking duplicate SĐT
    const validateSDT = async (_, value) => {
        if (!value) {
            return Promise.resolve();
        }
        const userId = form.getFieldValue('_id');
        const result = await UserService.checkDuplicateSDT(value, userId);
        if (result) {
            return Promise.reject('SĐT đã tồn tại!');
        }
        return Promise.resolve();
    };

    // Validator function for checking duplicate Email
    const validateEmail = async (_, value) => {
        if (!value) {
            return Promise.resolve();
        }
        const userId = form.getFieldValue('_id');
        const result = await UserService.checkDuplicateEmail(value, userId);
        if (result) {
            return Promise.reject('Email đã tồn tại!');
        }
        return Promise.resolve();
    };


    const update = async () =>
    {
        try
        {
            await form.validateFields();
            const userId = form.getFieldValue( '_id' );
            await UserService.updateUser( form.getFieldsValue(), userId );
            api.success( {
                message: 'Success',
                description: 'User updated successfully!',
                showProgress: true,
            } );
            form.resetFields();
            onCancel( false );
            refreshUsers();

        } catch ( error )
        {
            console.error( "Error updating user:", error );
            api.error( {
                message: 'Error',
                description: 'User update failed.',
                showProgress: true,
            } );
        }
    };
    return (

        <Modal
            title="Edit User"
            visible={ visible }
            onCancel={ onCancel }
            footer={ [
                <Button key="cancel" onClick={ onCancel }>
                    Cancel
                </Button>,
                <Button htmlType="button" onClick={ () => form.resetFields() }>Reset</Button>,
                <Button key="submit" type="primary" onClick={ () => form.submit() }>Save</Button>,
            ] }
        >
            { contextHolder }
            <Form
                form={ form }
                onFinish={ update }
                style={ { maxWidth: 600 } }
            >
                {/* Hidden field to store user ID */ }
                <Form.Item name="_id" style={ { display: 'none' } }>
                    <Input type="hidden" />
                </Form.Item>

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
                <Form.Item name="sdt" label="SĐT" rules={ [ { required: true, message: 'Vui lòng nhập SĐT!' } ,  { validator: validateSDT }] } >
                    <Input type='number' placeholder="Nhập vào số điện thoại"  />
                </Form.Item>
                <Form.Item name="email" label="Email" rules={ [ { required: true, message: 'Vui lòng nhập email!' },  { validator: validateEmail } ] }>
                    <Input type='email' placeholder="Nhập vào địa chỉ email" />
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default EditUs;
