import React, { useEffect, useState } from 'react'
import Card from '@mui/material/Card'
import CardContent from '@mui/material/CardContent'
import Typography from '@mui/material/Typography'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import IconButton from '@mui/material/IconButton'
import DeleteIcon from '@mui/icons-material/Delete'
import Snackbar from '@mui/material/Snackbar'
import Alert from '@mui/material/Alert'
import { motion } from 'framer-motion'

export default function Bookings() {
  const [bookings, setBookings] = useState([])
  const [loading, setLoading] = useState(false)
  const [snack, setSnack] = useState({ open: false, message: '', severity: 'info' })

  const API_BASE = 'http://localhost:8080/bookings'

  const showSnack = (message, severity = 'success') => {
    setSnack({ open: true, message, severity })
  }

  const fetchBookings = async () => {
    setLoading(true)
    try {
      const res = await fetch(API_BASE)
      if (!res.ok) throw new Error('Network error')
      const data = await res.json()
      setBookings(data)
    } catch (err) {
      console.warn('Backend not available, using mock data', err)
      setBookings([
        { id: 1, member: { name: 'John Doe' }, classSession: { name: 'Yoga' }, bookingDate: '2023-10-01' },
        { id: 2, member: { name: 'Jane Smith' }, classSession: { name: 'Zumba' }, bookingDate: '2023-10-02' }
      ])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchBookings()
  }, [])

  const handleCancel = async (id) => {
    try {
      const res = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' })
      if (!res.ok) throw new Error('Cancel failed')
      showSnack('Booking cancelled')
      fetchBookings()
    } catch (err) {
      showSnack('Error cancelling booking', 'error')
    }
  }

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
    >
      <Card sx={{ mb: 2, background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 20px #00ffff' }}>
        <CardContent>
          <Typography variant="h5" color="secondary" sx={{ textShadow: '0 0 10px #00ffff' }}>Booking Management</Typography>
        </CardContent>
      </Card>

      <Card sx={{ background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 15px #ff0080' }}>
        <CardContent>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sx={{ color: '#00ffff' }}>Member</TableCell>
                <TableCell sx={{ color: '#00ffff' }}>Class</TableCell>
                <TableCell sx={{ color: '#00ffff' }}>Date</TableCell>
                <TableCell sx={{ color: '#00ffff' }}>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {bookings.map((booking) => (
                <TableRow key={booking.id}>
                  <TableCell sx={{ color: '#ffffff' }}>{booking.member?.name}</TableCell>
                  <TableCell sx={{ color: '#ffffff' }}>{booking.classSession?.name}</TableCell>
                  <TableCell sx={{ color: '#ffffff' }}>{booking.bookingDate}</TableCell>
                  <TableCell>
                    <IconButton onClick={() => handleCancel(booking.id)} sx={{ color: '#ff0080' }}>
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Snackbar open={snack.open} autoHideDuration={4000} onClose={() => setSnack({ ...snack, open: false })}>
        <Alert severity={snack.severity} sx={{ width: '100%' }}>{snack.message}</Alert>
      </Snackbar>
    </motion.div>
  )
}
