import React, { useState, useEffect } from 'react';
import {
  Modal,
  Form,
  Input,
  Select,
  notification
} from 'antd';

import CalendarService from '../Service/CalendarService';

const ThemLich = ({ visible, onClose,refreshCals}) => {
   const [form] = Form.useForm();
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
      console.error("Error loading cals:", error);
    }
  };

  const onFinish = async (values) => {
    try {
      await CalendarService.createCal(values);
      form.resetFields();
      api.success({
        message: 'Success',
        description: 'cal created successfully!',
        showProgress: true,
      });
      refreshCals();
      onClose();
    } catch (error) {
      api.error({
        message: 'Error',
        description: 'Failed to create cal!',
        showProgress: true,
      });
    }
   };

  return (
    <Modal
   
    visible ={visible}
      title="Thêm lịch"
     
      onCancel={onClose}
      onOk={() => {
        form.validateFields().then(values => {
          onFinish(values);
        }).catch(info => {
          console.log('Validate Failed:', info);
        });
      }}
    >
    
         {contextHolder}
       <Form
         form={form}
         onFinish={onFinish}
        
        labelCol={{ span: 3 }}
        wrapperCol={{ span: 21 }}
         layout="horizontal"
       >
           <Form.Item
           label="Loại công việc"
           name="loaiCvID"
          rules={[{ required: true, message: 'Please input the title!' }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Địa điểm"
          name="diaDiem"
          rules={[{ required: true, message: 'Please input the title!' }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Nội dung"
          name="noiDung"
          rules={[{ required: true, message: 'Please input the title!' }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
           label="Ghi chú"
           name="ghiChu"
           rules={[{ required: true, message: 'Please input the title!' }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Nhân viên"
          name="nguoiDungID"
           rules={[{ required: true, message: 'Please select an employee!' }]}
         >
           <Select>
             {cals.map(cal => (
               <Select.Option key={cal.id} value={cal.id}>
                 {cal.id}
               </Select.Option>
            ))}
          </Select>
        </Form.Item>
       
       </Form>
    </Modal>
  );
};

export default ThemLich;
