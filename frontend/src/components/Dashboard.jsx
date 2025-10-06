import React, { useEffect, useState } from 'react'
import Card from '@mui/material/Card'
import CardContent from '@mui/material/CardContent'
import Typography from '@mui/material/Typography'
import { motion } from 'framer-motion'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, BarChart, Bar, PieChart, Pie, Cell } from 'recharts'

export default function Dashboard() {
  const [totalMembers, setTotalMembers] = useState(0)
  const [totalClasses, setTotalClasses] = useState(0)
  const [totalRevenue, setTotalRevenue] = useState(0)

  // Mock data for charts
  const revenueData = [
    { month: 'Jan', revenue: 4000 },
    { month: 'Feb', revenue: 3000 },
    { month: 'Mar', revenue: 5000 },
    { month: 'Apr', revenue: 4500 },
    { month: 'May', revenue: 6000 },
    { month: 'Jun', revenue: 5500 },
  ]

  const attendanceData = [
    { class: 'Yoga', attendance: 25 },
    { class: 'Zumba', attendance: 30 },
    { class: 'Strength', attendance: 20 },
    { class: 'Pilates', attendance: 15 },
  ]

  const memberStatusData = [
    { name: 'Active', value: 35, color: '#00ffff' },
    { name: 'Inactive', value: 7, color: '#ff0080' },
  ]

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
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
    >
      <Card sx={{ mb: 2, background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 20px #00ffff' }}>
        <CardContent>
          <Typography variant="h5" color="secondary" sx={{ textShadow: '0 0 10px #00ffff' }}>Admin Dashboard</Typography>
          <Typography variant="body1" color="text.secondary">Welcome to Fitness Forge</Typography>
        </CardContent>
      </Card>

      <div style={{ display: 'flex', gap: 16, marginBottom: 16 }}>
        <motion.div
          initial={{ scale: 0.9 }}
          animate={{ scale: 1 }}
          transition={{ delay: 0.1 }}
          style={{ flex: 1 }}
        >
          <Card sx={{ background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 15px #ff0080' }}>
            <CardContent>
              <Typography color="secondary">Total Members</Typography>
              <Typography variant="h4" color="primary" sx={{ textShadow: '0 0 10px #ff0080' }}>{totalMembers}</Typography>
            </CardContent>
          </Card>
        </motion.div>
        <motion.div
          initial={{ scale: 0.9 }}
          animate={{ scale: 1 }}
          transition={{ delay: 0.2 }}
          style={{ flex: 1 }}
        >
          <Card sx={{ background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 15px #00ffff' }}>
            <CardContent>
              <Typography color="secondary">Active Classes</Typography>
              <Typography variant="h4" color="primary" sx={{ textShadow: '0 0 10px #00ffff' }}>{totalClasses}</Typography>
            </CardContent>
          </Card>
        </motion.div>
        <motion.div
          initial={{ scale: 0.9 }}
          animate={{ scale: 1 }}
          transition={{ delay: 0.3 }}
          style={{ flex: 1 }}
        >
          <Card sx={{ background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 15px #ff0080' }}>
            <CardContent>
              <Typography color="secondary">Total Revenue</Typography>
              <Typography variant="h4" color="primary" sx={{ textShadow: '0 0 10px #ff0080' }}>${totalRevenue}</Typography>
            </CardContent>
          </Card>
        </motion.div>
      </div>

      <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap' }}>
        <motion.div
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ delay: 0.4 }}
          style={{ flex: 1, minWidth: 300 }}
        >
          <Card sx={{ background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 20px #00ffff' }}>
            <CardContent>
              <Typography variant="h6" color="secondary" sx={{ textShadow: '0 0 10px #00ffff' }}>Revenue Trend</Typography>
              <LineChart width={300} height={200} data={revenueData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#333" />
                <XAxis dataKey="month" stroke="#00ffff" />
                <YAxis stroke="#00ffff" />
                <Tooltip contentStyle={{ backgroundColor: '#1a1a1a', border: '1px solid #00ffff' }} />
                <Legend />
                <Line type="monotone" dataKey="revenue" stroke="#ff0080" strokeWidth={2} />
              </LineChart>
            </CardContent>
          </Card>
        </motion.div>
        <motion.div
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ delay: 0.5 }}
          style={{ flex: 1, minWidth: 300 }}
        >
          <Card sx={{ background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 20px #ff0080' }}>
            <CardContent>
              <Typography variant="h6" color="secondary" sx={{ textShadow: '0 0 10px #ff0080' }}>Class Attendance</Typography>
              <BarChart width={300} height={200} data={attendanceData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#333" />
                <XAxis dataKey="class" stroke="#00ffff" />
                <YAxis stroke="#00ffff" />
                <Tooltip contentStyle={{ backgroundColor: '#1a1a1a', border: '1px solid #ff0080' }} />
                <Legend />
                <Bar dataKey="attendance" fill="#00ffff" />
              </BarChart>
            </CardContent>
          </Card>
        </motion.div>
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.6 }}
          style={{ flex: 1, minWidth: 300 }}
        >
          <Card sx={{ background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 20px #00ffff' }}>
            <CardContent>
              <Typography variant="h6" color="secondary" sx={{ textShadow: '0 0 10px #00ffff' }}>Member Status</Typography>
              <PieChart width={300} height={200}>
                <Pie
                  data={memberStatusData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {memberStatusData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </CardContent>
          </Card>
        </motion.div>
      </div>
    </motion.div>
  )
}
