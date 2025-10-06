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
import Select from '@mui/material/Select'
import MenuItem from '@mui/material/MenuItem'
import IconButton from '@mui/material/IconButton'
import EditIcon from '@mui/icons-material/Edit'
import DeleteIcon from '@mui/icons-material/Delete'
import Box from '@mui/material/Box'
import Snackbar from '@mui/material/Snackbar'
import Alert from '@mui/material/Alert'

export default function Members() {
  const [members, setMembers] = useState([])
  const [loading, setLoading] = useState(false)
  const [openDialog, setOpenDialog] = useState(false)
  const [editingMember, setEditingMember] = useState(null)
  const [form, setForm] = useState({ name: '', age: '', gender: '', contact: '' })
  const [snack, setSnack] = useState({ open: false, message: '', severity: 'info' })

  const API_BASE = 'http://localhost:8080/members'

  const showSnack = (message, severity = 'success') => {
    setSnack({ open: true, message, severity })
  }

  const fetchMembers = async () => {
    setLoading(true)
    try {
      const res = await fetch(API_BASE)
      if (!res.ok) throw new Error('Network error')
      const data = await res.json()
      setMembers(data)
    } catch (err) {
      console.warn('Backend not available, using mock data', err)
      setMembers([
        { id: 1, name: 'John Doe', age: 30, gender: 'Male', contact: 'john@example.com' },
        { id: 2, name: 'Jane Smith', age: 25, gender: 'Female', contact: 'jane@example.com' }
      ])
      showSnack('Using fallback data (backend unreachable)', 'warning')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchMembers() }, [])

  const openAdd = () => {
    setEditingMember(null)
    setForm({ name: '', age: '', gender: '', contact: '' })
    setOpenDialog(true)
  }

  const openEdit = (member) => {
    setEditingMember(member)
    setForm({ name: member.name || '', age: member.age || '', gender: member.gender || '', contact: member.contact || '' })
    setOpenDialog(true)
  }

  const saveMember = async () => {
    // basic validation
    if (!form.name) { showSnack('Name is required', 'error'); return }
    try {
      let res
      if (editingMember) {
        res = await fetch(`${API_BASE}/${editingMember.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(form)
        })
      } else {
        res = await fetch(API_BASE, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(form)
        })
      }

      if (res.ok) {
        showSnack(editingMember ? 'Member updated' : 'Member added', 'success')
        setOpenDialog(false)
        setEditingMember(null)
        await fetchMembers()
      } else {
        let errorMessage = 'Save failed'
        try {
          const errorData = await res.json()
          if (errorData.message) {
            errorMessage = errorData.message
          } else if (errorData.violations && errorData.violations.length > 0) {
            // Handle validation violations
            errorMessage = errorData.violations.map(v => v.message).join(', ')
          } else if (errorData.errors && Array.isArray(errorData.errors)) {
            // Alternative error format
            errorMessage = errorData.errors.join(', ')
          }
        } catch (parseError) {
          // If JSON parsing fails, use status text
          errorMessage = `Save failed: ${res.status} ${res.statusText}`
        }
        console.error('Save failed', res.status, errorMessage)
        showSnack(errorMessage, 'error')
      }
    } catch (err) {
      console.error('Save error', err)
      showSnack('Save failed (network error)', 'error')
    }
  }

  const deleteMember = async (member) => {
    if (!confirm(`Delete member ${member.name}?`)) return
    try {
      const res = await fetch(`${API_BASE}/${member.id}`, { method: 'DELETE' })
      if (res.ok) {
        showSnack('Member deleted', 'success')
        await fetchMembers()
      } else {
        let errorMessage = 'Delete failed'
        try {
          const errorData = await res.json()
          if (errorData.message) {
            errorMessage = errorData.message
          }
        } catch (parseError) {
          errorMessage = `Delete failed: ${res.status} ${res.statusText}`
        }
        showSnack(errorMessage, 'error')
      }
    } catch (err) {
      console.error('Delete error', err)
      showSnack('Delete failed (network error)', 'error')
    }
  }

  return (
    <div>
      <Card sx={{ mb: 2 }}>
        <CardContent sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <Typography variant="h5" color="secondary">Member Management</Typography>
          <Button variant="contained" color="primary" onClick={openAdd}>Add Member</Button>
        </CardContent>
      </Card>

      <Card>
        <CardContent>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Name</TableCell>
                <TableCell>Age</TableCell>
                <TableCell>Gender</TableCell>
                <TableCell>Contact</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {members.map((m) => (
                <TableRow key={m.id}>
                  <TableCell>{m.id}</TableCell>
                  <TableCell>{m.name}</TableCell>
                  <TableCell>{m.age}</TableCell>
                  <TableCell>{m.gender}</TableCell>
                  <TableCell>{m.contact}</TableCell>
                  <TableCell>
                    <Box sx={{ display: 'flex', gap: 1 }}>
                      <IconButton size="small" onClick={() => openEdit(m)}><EditIcon /></IconButton>
                      <IconButton size="small" onClick={() => deleteMember(m)}><DeleteIcon /></IconButton>
                    </Box>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} fullWidth maxWidth="sm">
        <DialogTitle>{editingMember ? 'Edit Member' : 'Add Member'}</DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'grid', gap: 2, mt: 1 }}>
            <TextField label="Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} fullWidth />
            <TextField label="Age" type="number" value={form.age} onChange={(e) => setForm({ ...form, age: e.target.value })} />
            <Select value={form.gender} onChange={(e) => setForm({ ...form, gender: e.target.value })} displayEmpty>
              <MenuItem value="">Select gender</MenuItem>
              <MenuItem value="Male">Male</MenuItem>
              <MenuItem value="Female">Female</MenuItem>
              <MenuItem value="Other">Other</MenuItem>
            </Select>
            <TextField label="Contact" value={form.contact} onChange={(e) => setForm({ ...form, contact: e.target.value })} fullWidth />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button variant="contained" color="primary" onClick={saveMember}>{editingMember ? 'Update' : 'Add'}</Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={snack.open} autoHideDuration={4000} onClose={() => setSnack({ ...snack, open: false })}>
        <Alert onClose={() => setSnack({ ...snack, open: false })} severity={snack.severity} sx={{ width: '100%' }}>{snack.message}</Alert>
      </Snackbar>
    </div>
  )
}
