import React, { useState } from 'react'
import Card from '@mui/material/Card'
import CardContent from '@mui/material/CardContent'
import Typography from '@mui/material/Typography'
import Button from '@mui/material/Button'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, BarChart, Bar } from 'recharts'
import { motion } from 'framer-motion'

export default function Reports() {
  const [data] = useState([
    { month: 'Jan', revenue: 1200, attendance: 150 },
    { month: 'Feb', revenue: 1400, attendance: 180 },
    { month: 'Mar', revenue: 1100, attendance: 120 },
    { month: 'Apr', revenue: 1600, attendance: 200 },
    { month: 'May', revenue: 1800, attendance: 220 },
    { month: 'Jun', revenue: 1500, attendance: 190 }
  ])

  const exportCSV = () => {
    const csvContent = 'data:text/csv;charset=utf-8,' + 
      'Month,Revenue,Attendance\n' + 
      data.map(row => `${row.month},${row.revenue},${row.attendance}`).join('\n')
    const encodedUri = encodeURI(csvContent)
    const link = document.createElement('a')
    link.setAttribute('href', encodedUri)
    link.setAttribute('download', 'reports.csv')
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  }

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
    >
      <Card sx={{ mb: 2, background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 20px #00ffff' }}>
        <CardContent>
          <Typography variant="h5" color="secondary" sx={{ textShadow: '0 0 10px #00ffff' }}>Reports & Analytics</Typography>
          <Button
            variant="contained"
            color="primary"
            onClick={exportCSV}
            sx={{ mt: 1, boxShadow: '0 0 10px #ff0080' }}
          >
            Export CSV
          </Button>
        </CardContent>
      </Card>

      <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap' }}>
        <motion.div
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ delay: 0.1 }}
          style={{ flex: 1, minWidth: 300 }}
        >
          <Card sx={{ background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 15px #ff0080' }}>
            <CardContent>
              <Typography variant="h6" color="secondary" sx={{ textShadow: '0 0 10px #ff0080' }}>Revenue Trend</Typography>
              <LineChart width={300} height={200} data={data}>
                <CartesianGrid strokeDasharray="3 3" stroke="#333" />
                <XAxis dataKey="month" stroke="#00ffff" />
                <YAxis stroke="#00ffff" />
                <Tooltip contentStyle={{ backgroundColor: '#1a1a1a', border: '1px solid #ff0080' }} />
                <Legend />
                <Line type="monotone" dataKey="revenue" stroke="#ff0080" strokeWidth={2} />
              </LineChart>
            </CardContent>
          </Card>
        </motion.div>
        <motion.div
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ delay: 0.2 }}
          style={{ flex: 1, minWidth: 300 }}
        >
          <Card sx={{ background: 'rgba(26,26,26,0.9)', boxShadow: '0 0 15px #00ffff' }}>
            <CardContent>
              <Typography variant="h6" color="secondary" sx={{ textShadow: '0 0 10px #00ffff' }}>Attendance Trend</Typography>
              <BarChart width={300} height={200} data={data}>
                <CartesianGrid strokeDasharray="3 3" stroke="#333" />
                <XAxis dataKey="month" stroke="#00ffff" />
                <YAxis stroke="#00ffff" />
                <Tooltip contentStyle={{ backgroundColor: '#1a1a1a', border: '1px solid #ff0080' }} />
                <Legend />
                <Bar dataKey="attendance" fill="#00ffff" />
              </BarChart>
            </CardContent>
          </Card>
        </motion.div>
      </div>
    </motion.div>
  )
}
