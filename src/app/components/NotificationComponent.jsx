import React, { useState, useEffect } from 'react';
import CircleNotificationsRoundedIcon from '@mui/icons-material/CircleNotificationsRounded';
import { notification, Space } from 'antd';
import '../assets/css/noti.css';

const NotificationComponent = () => {
  const [messages, setMessages] = useState([]);
  const [enabled, setEnabled] = useState(true);
  const [threshold, setThreshold] = useState(3);
  const [api, contextHolder] = notification.useNotification();

  useEffect(() => {
    const ws = new WebSocket('ws://localhost:8080/notifications');

    ws.onopen = () => {
      console.log('WebSocket connected');
    };

    ws.onmessage = (event) => {
      setMessages((prevMessages) => [...prevMessages, event.data]);

      if (enabled) {
        api.open({
          description: (
            <div dangerouslySetInnerHTML={{ __html: event.data }} />
          ),
          duration: null,
          icon: (
            <CircleNotificationsRoundedIcon
              style={{
                color: '#3b6f9e',
                fontSize: 'xxx-large',
              }}
            />
          ),
        });
      }
    };

    ws.onclose = () => {
      console.log('WebSocket disconnected');
    };

    return () => {
      ws.close();
    };
  }, [enabled, api]);

  return (
    <div>
      {contextHolder}
      <Space>
        {/* Add other components or elements here */}
      </Space>
    </div>
  );
};

export default NotificationComponent;
