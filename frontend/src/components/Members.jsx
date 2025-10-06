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

export default function Members() {
  const [members, setMembers] = useState([])

  useEffect(() => {
    fetch('http://localhost:8080/members')
      .then((r) => r.ok ? r.json() : Promise.reject())
      .then(setMembers)
      .catch(() => setMembers([
        { id: 1, name: 'John Doe', age: 30, gender: 'Male', contact: 'john@example.com' }
      ]))
  }, [])

  return (
    <div>
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5" color="secondary">Member Management</Typography>
          <Button variant="contained" color="primary" sx={{ mt: 1 }}>Add Member</Button>
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
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  )
}
