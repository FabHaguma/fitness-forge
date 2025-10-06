import React, { useEffect, useState } from 'react'
import Card from '@mui/material/Card'
import CardContent from '@mui/material/CardContent'
import Typography from '@mui/material/Typography'

export default function Dashboard() {
  const [totalMembers, setTotalMembers] = useState(0)
  const [totalClasses, setTotalClasses] = useState(0)
  const [totalRevenue, setTotalRevenue] = useState(0)

  useEffect(() => {
    // try fetch from backend
    fetch('http://localhost:8080/reports/dashboard')
      .then((r) => r.ok ? r.json() : Promise.reject())
      .then((data) => {
        setTotalMembers(data.totalMembers || 0)
        setTotalRevenue(data.totalRevenue || 0)
      })
      .catch(() => {
        setTotalMembers(42)
        setTotalRevenue(12500)
      })
    setTotalClasses(8)
  }, [])

  return (
    <div>
      <Card sx={{ mb: 2, background: 'rgba(26,26,26,0.9)' }}>
        <CardContent>
          <Typography variant="h5" color="secondary">Admin Dashboard</Typography>
          <Typography variant="body1" color="text.secondary">Welcome to Fitness Forge</Typography>
        </CardContent>
      </Card>

      <div style={{ display: 'flex', gap: 16 }}>
        <Card sx={{ flex: 1 }}>
          <CardContent>
            <Typography color="secondary">Total Members</Typography>
            <Typography variant="h4" color="primary">{totalMembers}</Typography>
          </CardContent>
        </Card>
        <Card sx={{ flex: 1 }}>
          <CardContent>
            <Typography color="secondary">Active Classes</Typography>
            <Typography variant="h4" color="primary">{totalClasses}</Typography>
          </CardContent>
        </Card>
        <Card sx={{ flex: 1 }}>
          <CardContent>
            <Typography color="secondary">Total Revenue</Typography>
            <Typography variant="h4" color="primary">${totalRevenue}</Typography>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
