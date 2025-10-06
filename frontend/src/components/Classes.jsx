import React from 'react'
import Card from '@mui/material/Card'
import CardContent from '@mui/material/CardContent'
import Typography from '@mui/material/Typography'

export default function Classes() {
  return (
    <div>
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5" color="secondary">Class Management</Typography>
        </CardContent>
      </Card>
    </div>
  )
}
