import React from 'react'
import Card from '@mui/material/Card'
import CardContent from '@mui/material/CardContent'
import Typography from '@mui/material/Typography'
import Chart from 'react-apexcharts'

export default function Reports() {
  const series = [{ name: 'Revenue', data: [1200, 1400, 1100, 1600, 1800, 1500] }]
  const options = { chart: { background: 'transparent', foreColor: '#00ffff' }, xaxis: { categories: ['Jan','Feb','Mar','Apr','May','Jun'] } }

  return (
    <div>
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5" color="secondary">Reports & Analytics</Typography>
        </CardContent>
      </Card>

      <Card>
        <CardContent>
          <Chart options={options} series={series} type="line" height={320} />
        </CardContent>
      </Card>
    </div>
  )
}
