import React, { useEffect, useState } from 'react'
import Card from '@mui/material/Card'
import CardContent from '@mui/material/CardContent'
import Typography from '@mui/material/Typography'
import Button from '@mui/material/Button'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import Dialog from '@mui/material/Dialog'
import DialogTitle from '@mui/material/DialogTitle'
import DialogContent from '@mui/material/DialogContent'
import DialogActions from '@mui/material/DialogActions'
import TextField from '@mui/material/TextField'
import IconButton from '@mui/material/IconButton'
import EditIcon from '@mui/icons-material/Edit'
import DeleteIcon from '@mui/icons-material/Delete'
import Box from '@mui/material/Box'
import Snackbar from '@mui/material/Snackbar'
import Alert from '@mui/material/Alert'
import { motion } from 'framer-motion'

export default function MembershipPlans() {
  const [plans, setPlans] = useState([])
  const [loading, setLoading] = useState(false)
  const [openDialog, setOpenDialog] = useState(false)
  const [editingPlan, setEditingPlan] = useState(null)
  const [form, setForm] = useState({ name: '', price: '', duration: '' })
  const [snack, setSnack] = useState({ open: false, message: '', severity: 'info' })

  const API_BASE = 'http://localhost:8080/membership-plans'

  const showSnack = (message, severity = 'success') => {
    setSnack({ open: true, message, severity })
  }

  const fetchPlans = async () => {
    setLoading(true)
    try {
      const res = await fetch(API_BASE)
      if (!res.ok) throw new Error('Network error')
      const data = await res.json()
      setPlans(data)
    } catch (err) {
      console.warn('Backend not available, using mock data', err)
      setPlans([
        { id: 1, name: 'Monthly', price: 50, duration: '1 month' },
        { id: 2, name: 'Quarterly', price: 120, duration: '3 months' }
      ])
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchPlans()
  }, [])

  const handleSave = async () => {
    try {
      const method = editingPlan ? 'PUT' : 'POST'
      const url = editingPlan ? `${API_BASE}/${editingPlan.id}` : API_BASE
      const body = { ...form, price: parseFloat(form.price) }
      const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      })
      if (!res.ok) throw new Error('Save failed')
      showSnack(editingPlan ? 'Plan updated' : 'Plan added')
      setOpenDialog(false)
      setForm({ name: '', price: '', duration: '' })
      setEditingPlan(null)
      fetchPlans()
    } catch (err) {
      showSnack('Error saving plan', 'error')
    }
  }

  const handleEdit = (plan) => {
    setEditingPlan(plan)
    setForm({ name: plan.name, price: plan.price, duration: plan.duration })
    setOpenDialog(true)
  }

  const handleDelete = async (id) => {
    try {
      const res = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' })
      if (!res.ok) throw new Error('Delete failed')
      showSnack('Plan deleted')
      fetchPlans()
    } catch (err) {
      showSnack('Error deleting plan', 'error')
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
          <Typography variant="h5" color="secondary" sx={{ textShadow: '0 0 10px #00ffff' }}>Membership Plans</Typography>
          <Button
            variant="contained"
            color="primary"
            onClick={() => setOpenDialog(true)}
            sx={{ mt: 1, boxShadow: '0 0 10px #ff0080' }}
          >
            Add Plan
          </Button>
        </CardContent>
      </Card>

      <Card sx={{ background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 15px #ff0080' }}>
        <CardContent>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sx={{ color: '#00ffff' }}>Name</TableCell>
                <TableCell sx={{ color: '#00ffff' }}>Price</TableCell>
                <TableCell sx={{ color: '#00ffff' }}>Duration</TableCell>
                <TableCell sx={{ color: '#00ffff' }}>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {plans.map((plan) => (
                <TableRow key={plan.id}>
                  <TableCell sx={{ color: '#ffffff' }}>{plan.name}</TableCell>
                  <TableCell sx={{ color: '#ffffff' }}>${plan.price}</TableCell>
                  <TableCell sx={{ color: '#ffffff' }}>{plan.duration}</TableCell>
                  <TableCell>
                    <IconButton onClick={() => handleEdit(plan)} sx={{ color: '#00ffff' }}>
                      <EditIcon />
                    </IconButton>
                    <IconButton onClick={() => handleDelete(plan.id)} sx={{ color: '#ff0080' }}>
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} PaperProps={{ sx: { backgroundColor: '#1a1a1a', color: '#ffffff' } }}>
        <DialogTitle sx={{ color: '#00ffff' }}>{editingPlan ? 'Edit Plan' : 'Add Plan'}</DialogTitle>
        <DialogContent>
          <TextField
            fullWidth
            label="Name"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            sx={{ mt: 2, '& .MuiInputLabel-root': { color: '#00ffff' }, '& .MuiOutlinedInput-root': { '& fieldset': { borderColor: '#00ffff' } } }}
          />
          <TextField
            fullWidth
            label="Price"
            type="number"
            value={form.price}
            onChange={(e) => setForm({ ...form, price: e.target.value })}
            sx={{ mt: 2, '& .MuiInputLabel-root': { color: '#00ffff' }, '& .MuiOutlinedInput-root': { '& fieldset': { borderColor: '#00ffff' } } }}
          />
          <TextField
            fullWidth
            label="Duration"
            value={form.duration}
            onChange={(e) => setForm({ ...form, duration: e.target.value })}
            sx={{ mt: 2, '& .MuiInputLabel-root': { color: '#00ffff' }, '& .MuiOutlinedInput-root': { '& fieldset': { borderColor: '#00ffff' } } }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)} sx={{ color: '#ff0080' }}>Cancel</Button>
          <Button onClick={handleSave} sx={{ color: '#00ffff' }}>Save</Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={snack.open} autoHideDuration={4000} onClose={() => setSnack({ ...snack, open: false })}>
        <Alert severity={snack.severity} sx={{ width: '100%' }}>{snack.message}</Alert>
      </Snackbar>
    </motion.div>
  )
}