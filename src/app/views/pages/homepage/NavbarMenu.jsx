import React, { useState } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import AccountCircle from '@mui/icons-material/AccountCircle';
import Switch from '@mui/material/Switch';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormGroup from '@mui/material/FormGroup';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import { Avatar, Modal } from 'antd';
import { useSelector } from 'react-redux';
import Services from 'app/services';
import NotificationsRoundedIcon from '@mui/icons-material/NotificationsRounded';
import AccountCircleRoundedIcon from '@mui/icons-material/AccountCircleRounded';
import LogoutRoundedIcon from '@mui/icons-material/LogoutRounded';
import DrawerSide from './Drawer';
import { Button } from '@mui/material';

import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import { ListItemIcon } from '@mui/material';
export default function NavbarMenu(onViewChange   ) {
    const taiKhoan = useSelector(state => state.taiKhoan);
   
    const [anchorElUser, setAnchorElUser] = React.useState(null);
    const [openModal, setOpenModal] = useState(false);
    const [open, setOpen] = useState(false);

    const showDrawer = () =>  {
      setOpen(true);
    };

    const handleOpenUserMenu = (event) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };


    const handleCancel = () => {
        setOpenModal(false);
    };
    const showModal = () => {
        setOpenModal(true);
    };

    const handleOk = () => {
        setOpenModal(false);
        try {
            Services.getTaiKhoanService().dangXuat();
            setOpenModal(false);
        } catch (error) {
            console.log(error);
        }
    };
    function stringAvatar(name) {
        const nameParts = name?.split(' ');
        const lastName = nameParts[nameParts.length - 1];
        return {
            sx: {
                bgcolor: '#177E89',
            },
            children: `${lastName[0]}`,
        };
    }
    const views = [
        { label: 'Week', value: 'timeGridWeek' },
        { label: 'Day', value: 'timeGridDay' },
        { label: 'Month', value: 'dayGridMonth' },
        { label: 'Year', value: 'multiMonthYear' },
        { label: 'List', value: 'listWeek' },
    ];



    const toggleDrawer = (open) => () => {
        setOpen(open);
    };

    const DrawerList = (
        <Box
            sx={{ width: 250 }}
            role="presentation"
        >
            <List>
                {views.map((view) => (
                    <ListItem key={view.value} disablePadding>
                      <ListItemButton onClick={() => onViewChange(view.value)}>
                            <ListItemText primary={view.label} />
                        </ListItemButton>
                    </ListItem>
                ))}
            </List>
            <Divider />
            <List>
                {['All mail', 'Trash', 'Spam'].map((text, index) => (
                    <ListItem key={text} disablePadding>
                          <ListItemButton>
                            <ListItemIcon>
                                {index % 2 === 0 ? <InboxIcon /> : <MailIcon />}
                            </ListItemIcon>
                            <ListItemText primary={text} />
                        </ListItemButton>
                    </ListItem>
                ))}
            </List>
        </Box>
    );

    return (

        <AppBar position="static" className='xrqwdasd qqweqweeqr' >
            <Toolbar>
            <Drawer open={open} onClose={toggleDrawer(false)}>
            {DrawerList}
        </Drawer>
                <IconButton
                    size="large"
                    edge="start"

                    aria-label="menu"
                    sx={{ mr: 2, display: { xs: 'flex',sm:'flex', md: 'none', lg:'none' }}}
                >
                    <MenuIcon onClick={showDrawer} />
                    
                </IconButton>
                <Typography color='black' variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    Calendar
                   
                </Typography>

                <div>
                    <IconButton size="large" >
                        <NotificationsRoundedIcon />
                    </IconButton>


                    <IconButton
                        size="large"
                        aria-label="account of current user"
                        aria-controls="menu-appbar"
                        aria-haspopup="true"
                        onClick={handleOpenUserMenu}

                    >
                        <Avatar {...stringAvatar(taiKhoan?.hoTen)} />
                    </IconButton>


                    <Menu
                        sx={{ mt: '45px' }}
                        id="menu-appbar"
                        anchorEl={anchorElUser}
                        anchorOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                        keepMounted
                        transformOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                        open={Boolean(anchorElUser)}
                        onClose={handleCloseUserMenu}
                    >
                     
                        <MenuItem>
                          <AccountCircleRoundedIcon className='mr-1' fontSize="small"/>  <Typography textAlign="center" >{taiKhoan?.hoTen}</Typography>
                        </MenuItem>
                        <MenuItem onClick={() => {
                            showModal();
                        }}>
                         <LogoutRoundedIcon className='mr-1' fontSize="small"/>   <Typography textAlign="center">Đăng xuất</Typography>
                        </MenuItem>
                    </Menu>
                    <Modal
                        title="Đăng xuất"
                        open={openModal}
                        onOk={handleOk}
                        onCancel={handleCancel}
                        okText="Đồng ý"
                        cancelText="Hủy"
                        okButtonProps={{ danger: true }}
                    >
                        <p>Bạn có chắc đăng xuất không?</p>
                    </Modal>
                    
                </div>

            </Toolbar>

        </AppBar>

    );
}