import React, { useEffect, useState } from 'react';
import { EyeOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { Layout, theme, Table, Divider, Button, Row, Col, Space, notification } from 'antd';
import CalendarService from '../Service/CalendarService';
import AddCalendar from './AddCalendar';

import EditCalendar from './EditCalendar';

const { Content } = Layout;

function ListCalendar() {
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedCalId, setSelectedCalId] = useState(null);
  const [cals, setCals] = useState([]);
  const [api, contextHolder] = notification.useNotification();


  useEffect(() => {
    loadCals();
  }, []);

  const loadCals = async () => {
    try {
      const response = await CalendarService.getCals();
      setCals(response.data);
    } catch (error) {
      console.error("Error loading Cals:", error);
    }
  };

  const handleDeleteCal = async (id) => {
    try {
      await CalendarService.deleteCal(id);
      api.success({
        message: 'Success',
        description: 'Cal deleted successfully!',
        showProgress: true,
      });
      loadCals();
    } catch (error) {
      console.error("Error deleting Cal:", error);
      api.error({
        message: 'Error',
        description: 'Cal deletion failed.',
        showProgress: true,
      });
    }
  };

  const handleEditCal = (id) => {
    setSelectedCalId(id);
    setIsEditModalOpen(true);
  };
  const handleAddCal = () => {
   
    setIsAddModalOpen(true);
  };


  const columns = [
    {
      title: 'Id',
      dataIndex: 'id',
      key: 'id',
      render: (text) => <a>{text}</a>,
    },
    {
      title: 'Name',
      dataIndex: 'nguoiDungID',
      key: 'nguoiDungID',
    },
    {
      title: 'Password',
      dataIndex: 'loaiCvID',
      key: 'loaiCvID',
    },
    {
      title: 'Email',
      dataIndex: 'diaDiem',
      key: 'diaDiem',
    },
    {
      title: 'Phone',
      dataIndex: 'noiDung',
      key: 'noiDung',
    },
    {
      title: 'Action',
      key: 'action',
      render: (text, record) => (
        <span>
          <Button type="primary" icon={<EyeOutlined />}></Button>
          <Button onClick={() => handleEditCal(record.id)} type="primary" icon={<EditOutlined />}></Button>
          <Button type="primary" onClick={() => handleDeleteCal(record.id)} icon={<DeleteOutlined />}></Button>
        </span>
      ),
    },
  ];

  const { token: { colorBgContainer, borderRadiusLG } } = theme.useToken();

  return (
    <Content
      style={{
        padding: 24,
        margin: 0,
        minHeight: 280,
        background: colorBgContainer,
        borderRadius: borderRadiusLG,
      }}
    >
      {contextHolder}
      <Row justify={'end'}>
        <Col></Col>
      </Row>
      <AddCalendar />
      <Button onClick={() => handleAddCal()}  type="primary" icon={<EditOutlined />}> Thêm lịch</Button>
      <Divider>DANH SÁCH NGƯỜI DÙNG</Divider>
      <Table columns={columns} dataSource={cals.map(cal => ({ ...cal, key: cal._id }))} />
      <AddCalendar
        visible={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        refreshCals={loadCals}
      />
      <EditCalendar
        visible={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        id={selectedCalId}
        refreshCals={loadCals}
      />
    </Content>
  );
}

export default ListCalendar;