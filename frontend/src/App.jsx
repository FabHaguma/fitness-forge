import React from 'react'
import { Routes, Route, Link } from 'react-router-dom'
import { createTheme, ThemeProvider } from '@mui/material/styles'
import CssBaseline from '@mui/material/CssBaseline'
import Box from '@mui/material/Box'
import AppBar from '@mui/material/AppBar'
import Toolbar from '@mui/material/Toolbar'
import IconButton from '@mui/material/IconButton'
import Typography from '@mui/material/Typography'
import Drawer from '@mui/material/Drawer'
import List from '@mui/material/List'
import ListItem from '@mui/material/ListItem'
import ListItemIcon from '@mui/material/ListItemIcon'
import ListItemText from '@mui/material/ListItemText'
import DashboardIcon from '@mui/icons-material/Dashboard'
import PeopleIcon from '@mui/icons-material/People'
import EventIcon from '@mui/icons-material/Event'
import BookOnlineIcon from '@mui/icons-material/BookOnline'
import BarChartIcon from '@mui/icons-material/BarChart'

import Dashboard from './components/Dashboard'
import Members from './components/Members'
import Classes from './components/Classes'
import Bookings from './components/Bookings'
import Reports from './components/Reports'

const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: { main: '#ff0080' },
    secondary: { main: '#00ffff' }
  },
  typography: {
    fontFamily: 'Orbitron, sans-serif'
  }
})

export default function App() {
  const [open, setOpen] = React.useState(false)
  const menu = [
    { title: 'Dashboard', path: '/', icon: <DashboardIcon /> },
    { title: 'Members', path: '/members', icon: <PeopleIcon /> },
    { title: 'Classes', path: '/classes', icon: <EventIcon /> },
    { title: 'Bookings', path: '/bookings', icon: <BookOnlineIcon /> },
    { title: 'Reports', path: '/reports', icon: <BarChartIcon /> }
  ]

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ display: 'flex' }}>
        <AppBar position="fixed" color="primary" sx={{ zIndex: (t) => t.zIndex.drawer + 1 }}>
          <Toolbar>
            <IconButton color="inherit" edge="start" onClick={() => setOpen(!open)} sx={{ mr: 2 }}>
              <span className="mdi mdi-dumbbell"></span>
            </IconButton>
            <Typography variant="h6" noWrap component="div">FITNESS FORGE</Typography>
          </Toolbar>
        </AppBar>

        <Drawer variant="persistent" open={open} sx={{ '& .MuiDrawer-paper': { width: 240, background: 'rgba(26,26,26,0.95)' } }}>
          <Toolbar />
          <List>
            {menu.map((m) => (
              <ListItem button component={Link} to={m.path} key={m.title} onClick={() => setOpen(false)}>
                <ListItemIcon sx={{ color: '#ff0080' }}>{m.icon}</ListItemIcon>
                <ListItemText primary={m.title} sx={{ color: '#00ffff' }} />
              </ListItem>
            ))}
          </List>
        </Drawer>

        <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
          <Toolbar />
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/members" element={<Members />} />
            <Route path="/classes" element={<Classes />} />
            <Route path="/bookings" element={<Bookings />} />
            <Route path="/reports" element={<Reports />} />
          </Routes>
        </Box>
      </Box>
    </ThemeProvider>
  )
}
