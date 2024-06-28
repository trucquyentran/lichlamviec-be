import React, { useEffect, useState } from 'react';
import { Divider, Table, Button, Form, Space, Select, theme, Layout, notification } from 'antd';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import moment from 'moment';
import UserService from '../../Service/UserService';
import EditUs from './EditUser';
import AddUsserComponet from './AddUsser';
import BreadcrumbComponent from './Breadcrumb';
const { Option } = Select;

function ListUser2 ()
{

    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken();

    const { Content } = Layout;

    const [ form ] = Form.useForm();
    const [ users, setUsers ] = useState( [] );
    const [ selectionType, setSelectionType ] = useState( 'checkbox' );
    const [ modalVisible, setModalVisible ] = useState( false );
    const [ editingUser, setEditingUser ] = useState( null ); // Thêm state để lưu thông tin người dùng đang được chỉnh sửa
    const [ api, contextHolder ] = notification.useNotification();
    const breadcrumbItems = [ 'Home', 'List User' ];


    useEffect( () =>
    {
        loadUsers();
    }, [] );

    const loadUsers = async () =>
    {
        try
        {
            const response = await UserService.getUsers();
            setUsers( response.data );
        } catch ( error )
        {
            console.error( "Error loading users:", error );

        }
    };

    const editUser = ( record ) =>
    {
        form.setFieldsValue( record ); // Đưa dữ liệu của record vào form
        setEditingUser( record ); // Lưu record vào state để biết đang chỉnh sửa người dùng nào
        setModalVisible( true ); // Hiển thị Modal khi click Edit
    };

    const handleCancel = () =>
    {
        setEditingUser( null ); // Xóa thông tin người dùng đang chỉnh sửa
        setModalVisible( false ); // Ẩn Modal
    };

    const handleDeleteUser = async ( _id ) =>
    {
        try
        {
            await UserService.deleteUser( _id );
            api.success( {
                message: 'Success',
                description: 'User deleted successfully!',
                showProgress: true,
            } );
            loadUsers();
        } catch ( error )
        {
            console.error( "Error deleting user:", error );
            api.error( {
                message: 'Error',
                description: 'User deletion failed.',
                showProgress: true,
            } );

        }
    };

    const formatDate = ( dateString ) =>
    {
        return moment( dateString ).format( 'DD-MM-YYYY' );
    };

    const columns = [
        { title: '#', dataIndex: '_id' },
        { title: 'Họ tên', dataIndex: 'hoten', render: ( text, record ) => <a onClick={ () => editUser( record ) }>{ text }</a> },
        { title: 'Giới tính', dataIndex: 'gioitinh' },
        { title: 'Ngày sinh', dataIndex: 'ngaysinh', render: ( text ) => formatDate( text ) },
        { title: 'Đi động', dataIndex: 'sdt' },
        { title: 'Email', dataIndex: 'email' },
        { title: 'Địa chỉ', dataIndex: 'diachi' },
        {
            title: 'Action', dataIndex: '_id', render: ( text, record ) => (
                <Space>
                    <Button type="primary" onClick={ () => editUser( record ) } ><EditIcon fontSize='16px' /></Button>
                    <Button type="primary" danger onClick={ () => handleDeleteUser( record._id ) }><DeleteIcon fontSize='16px' /></Button>

                </Space>
            ),
        },
    ];

    return (
        <>
            <BreadcrumbComponent items={ breadcrumbItems } />

            <Content style={ {
                padding: 24,
                margin: 0,
                minHeight: 280,
                background: colorBgContainer,
                borderRadius: borderRadiusLG,
            } }>
                { contextHolder }
                <div>
                    <EditUs
                        visible={ modalVisible }
                        onCancel={ handleCancel }
                        editingUser={ editingUser } // Truyền thông tin người dùng đang chỉnh sửa vào EditUs component
                        refreshUsers={ loadUsers }
                    />

                    <AddUsserComponet
                        refreshUsers={ loadUsers }
                    />
                    <Divider />
                    <Table
                        rowSelection={ {
                            type: selectionType,
                            onChange: ( selectedRowKeys, selectedRows ) =>
                            {
                                console.log( `selectedRowKeys: ${ selectedRowKeys }`, 'selectedRows: ', selectedRows );
                            },
                        } }
                        columns={ columns }
                        dataSource={ users.map( user => ( { ...user, key: user._id } ) ) }
                    />
                </div>
            </Content>
        </>
    );
}

export default ListUser2;
