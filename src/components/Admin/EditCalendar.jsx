import React, { useState, useEffect } from 'react';
import { Modal, Form, Select, Input,notification } from 'antd';

import CalendarService from '../Service/CalendarService';

const EditCalendar = ({ visible, onClose, id, refreshCals }) => {
  const [form] = Form.useForm();
  const [confirmLoading] = useState(false);
  const [cals, setCals] = useState([]);
  const [api, contextHolder] = notification.useNotification();

  useEffect(() => {
    if (id) {
      CalendarService.getCalById(id).then(res => {
        form.setFieldsValue(res.data);

      });
    }
    CalendarService.getCals().then(res => {
      setCals(res.data);
    });
  }, [id]);

  const handleOk = () => {
    try {
      form
      .validateFields()
      .then(values => {
       
        CalendarService.updateCal(values,id).then(() => {
        
          api.success({
            message: 'Success',
            description: 'Cal edit successfully!',
            showProgress: true,
          });
          onClose();
          refreshCals();
        });
      })
    } catch (error) {
      api.error({
        message: 'Error',
        description: 'Failed to create Cal!',
        showProgress: true,
      });
    }
    
  };

  return (
    <Modal
      title="Sửa lịch"
      visible={visible}
      onOk={handleOk}
      confirmLoading={confirmLoading}
      onCancel={onClose}
    >
      {contextHolder}
      <Form form={form} labelCol={{ span: 5 }} wrapperCol={{ span: 19 }} layout="horizontal">
        <Form.Item label="Loại công việc" name="loaiCvID" rules={[{ required: true, message: 'Please input the title!' }]}>
          <Input />
        </Form.Item>
        <Form.Item label="Địa điểm" name="diaDiem" rules={[{ required: true, message: 'Please input the title!' }]}>
          <Input />
        </Form.Item>
        <Form.Item label="Nội dung" name="noiDung" rules={[{ required: true, message: 'Please input the title!' }]}>
          <Input />
        </Form.Item>
        <Form.Item label="Ghi chú" name="ghiChu" rules={[{ required: true, message: 'Please input the title!' }]}>
          <Input />
        </Form.Item>
        <Form.Item label="Nhân viên" name="nguoiDungID" rules={[{ required: true, message: 'Please select an employee!' }]}>
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

export default EditCalendar;
