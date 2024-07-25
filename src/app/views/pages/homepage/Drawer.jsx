import React from 'react';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import { ListItemIcon } from '@mui/material';

export default function DrawerSide({ open, setOpen, setViewCal }) {
    const views = [
        { label: 'Week', value: 'timeGridWeek' },
        { label: 'Day', value: 'timeGridDay' },
        { label: 'Month', value: 'dayGridMonth' },
        { label: 'Year', value: 'multiMonthYear' },
        { label: 'List', value: 'listWeek' },
    ];

    const handleItemClick = (view) => {
        if (typeof setViewCal =='function') {
            setViewCal(view); // Make sure setViewCal is a function
        } else {
            console.error('setViewCal is not a function');
        }
    };

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
                        <ListItemButton onClick={() => handleItemClick(view.value)}>
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
        <Drawer open={open} onClose={toggleDrawer(false)}>
            {DrawerList}
        </Drawer>
    );
}
