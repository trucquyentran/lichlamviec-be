import React, { useState } from 'react'
import { useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import { Button, Menu as MenuNav, Typography } from 'antd';

import SettingsIcon from '@mui/icons-material/Settings';
import DashboardIcon from '@mui/icons-material/Dashboard';
import MenuIcon from '@mui/icons-material/Menu';
import logoVnpt from "app/assets/images/vnptlogo-white.png"
import MenuOpenIcon from '@mui/icons-material/MenuOpen';
import EditCalendarIcon from '@mui/icons-material/EditCalendar';
import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
const { SubMenu } = MenuNav;
const Sidebar = ({ user }) => {
    console.log("tttk", user)
    const location = useLocation();
    const navigate = useNavigate();
    const [defaultSelectedKeys, setDefaultSelectedKeys] = useState([]);
    const items = [

        {
            key: '/quan-tri/dashboard',
            icon: <DashboardIcon />,
            label: 'Dashboard',

            roles: ['Admin'],

        },

        {
            key: '/',
            icon: <CalendarMonthIcon />,
            label: 'Lịch làm việc',
            roles: ['User', 'Admin'],
        },
        // {
        //     key: 'todo',
        //     icon: <EventAvailableIcon />,
        //     label: 'Todo',
        //     roles: ['User', 'Admin'],
        // },

        {
            key: 'sub1',
            label: 'Quản lý',
            icon: <SettingsIcon />,
            type: 'group',
            roles: ['Admin'],
            children: [
                {
                    key: '/donvi',
                    label: '• Đơn vị',
                    roles: ['Admin'],
                },
                {
                    key: '/nguoidung',
                    label: '•  Người dùng',
                    roles: ['Admin'],
                },

            ],
        },


    ];
    const [collapsed, setCollapsed] = useState(false);
    const toggleCollapsed = () => {
        setCollapsed(!collapsed);
    };
    useEffect(() => {

        const handleResize = () => {
            if (window.innerWidth < 1000) {
                setCollapsed(true);
            } else {
                setCollapsed(false);
            }
        };

        handleResize();
        window.addEventListener('resize', handleResize);

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);
    useEffect(() => {
        const path = location.pathname;
        setDefaultSelectedKeys([location.pathname, location.pathname + location?.search])
        let selectKey = []
        let openKey = []
        items?.forEach(element => {

        });


    }, [location]);
    const getDefaultOpenKeys = (path, list, keyP) => {
        let openKeyList = []
        let keyopen = ""
        list?.forEach(element => {
            if (path?.includes(element?.key)) {
                keyopen = keyP
            } else {
                openKeyList = [...openKeyList, ...getDefaultOpenKeys(path, element?.children, element?.key)]
            }
        });
        if (keyopen) {
            openKeyList = [...openKeyList, keyopen]
        }
        return openKeyList;
    }
    const handleMenuItemClick = (key) => {

        navigate(`${key}`);
    };
    const filterMenuItems = (items, roles) => {

        return items
            .filter(item => item.roles.some(role => roles.includes(role)))

            .map(item => {
                const filteredItem = { ...item };
                if (filteredItem.children) {
                    filteredItem.children = filterMenuItems(filteredItem.children, roles);
                }
                return filteredItem;
            });

    };

    const filteredMenuItems = filterMenuItems(items, user.listQuyen);

    const renderMenuItems = items => {
        return items.map(item => {
            if (item.children) {
                return (
                    <SubMenu key={item.key} icon={item?.icon} title={item?.label}>
                        {renderMenuItems(item.children)}
                    </SubMenu>
                );
            }
            return <MenuNav.Item key={item.key} icon={item.icon}>{item.label}</MenuNav.Item>;
        });
    };

    return (

        <div
            className='nav-bar-cus hide-on-899'
            style={{
                width: !collapsed ? "256px" : "60px",
            }}
        >
            {collapsed ?
                <div className=' eqwrttqwecq pointer' onClick={toggleCollapsed} >
                    <MenuOpenIcon />
                </div>
                : <div className='div-flex eqwrttqweq pointer' >
                    <MenuIcon onClick={toggleCollapsed}></MenuIcon>
                    {/* <img src={logoVnpt} className='logo-nav' onClick={() => navigate("/")} /> */}
                    <h5 className='logo-nav bold f-20'>CALENDAR</h5>
                </div>
            }

            <MenuNav
                defaultSelectedKeys={[location.pathname, location.pathname + location?.search]}
                defaultOpenKeys={getDefaultOpenKeys([location.pathname, location.pathname + location?.search], items, "")}
                mode="inline"
                theme="dark"
                inlineCollapsed={collapsed}
                // items={items}
                onClick={({ key }) => handleMenuItemClick(key)}
            >
                {renderMenuItems(filteredMenuItems)}
            </MenuNav>

        </div>


    );
};

export default Sidebar;
