<template>
  <div class="page-container">
    <v-row>
      <v-col cols="12">
        <v-card class="cyberpunk-card mb-4">
          <v-card-title class="neon-text">
            <v-icon icon="mdi-chart-line" class="neon-icon mr-2"></v-icon>
            Reports & Analytics Dashboard
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="exportReport" class="neon-btn">
              <v-icon left>mdi-download</v-icon>
              Export CSV
            </v-btn>
          </v-card-title>
        </v-card>
      </v-col>
    </v-row>

    <!-- Key Metrics Row -->
    <v-row>
      <v-col cols="12" md="3">
        <v-card class="cyberpunk-card metric-card">
          <v-card-text class="text-center">
            <div class="metric-icon">
              <v-icon size="48" class="neon-icon">mdi-account-group</v-icon>
            </div>
            <div class="metric-value">{{ totalMembers }}</div>
            <div class="metric-label">Total Members</div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="3">
        <v-card class="cyberpunk-card metric-card">
          <v-card-text class="text-center">
            <div class="metric-icon">
              <v-icon size="48" class="neon-icon">mdi-cash</v-icon>
            </div>
            <div class="metric-value">${{ totalRevenue }}</div>
            <div class="metric-label">Total Revenue</div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="3">
        <v-card class="cyberpunk-card metric-card">
          <v-icon size="48" class="neon-icon">mdi-calendar-clock</v-icon>
            </div>
            <div class="metric-value">{{ totalClasses }}</div>
            <div class="metric-label">Active Classes</div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="3">
        <v-card class="cyberpunk-card metric-card">
          <v-card-text class="text-center">
            <div class="metric-icon">
              <v-icon size="48" class="neon-icon">mdi-calendar-check</v-icon>
            </div>
            <div class="metric-value">{{ totalBookings }}</div>
            <div class="metric-label">Total Bookings</div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Charts Row -->
    <v-row>
      <v-col cols="12" md="6">
        <v-card class="cyberpunk-card chart-card">
          <v-card-title class="neon-text">Revenue Trends</v-card-title>
          <v-card-text>
            <apexchart
              type="line"
              :options="revenueChartOptions"
              :series="revenueSeries"
              height="300"
            ></apexchart>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="6">
        <v-card class="cyberpunk-card chart-card">
          <v-card-title class="neon-text">Class Popularity</v-card-title>
          <v-card-text>
            <apexchart
              type="radialBar"
              :options="popularityChartOptions"
              :series="popularitySeries"
              height="300"
            ></apexchart>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" md="6">
        <v-card class="cyberpunk-card chart-card">
          <v-card-title class="neon-text">Member Demographics</v-card-title>
          <v-card-text>
            <apexchart
              type="donut"
              :options="demographicsChartOptions"
              :series="demographicsSeries"
              height="300"
            ></apexchart>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="6">
        <v-card class="cyberpunk-card chart-card">
          <v-card-title class="neon-text">Attendance Heatmap</v-card-title>
          <v-card-text>
            <div class="heatmap-container">
              <div
                v-for="day in 30"
                :key="day"
                class="heatmap-cell"
                :style="{ backgroundColor: getHeatmapColor(day) }"
                :title="`Day ${day}: ${Math.floor(Math.random() * 50)} attendees`"
              ></div>
            </div>
            <div class="heatmap-legend">
              <span class="legend-item">Less</span>
              <div class="legend-gradient"></div>
              <span class="legend-item">More</span>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Data Tables -->
    <v-row>
      <v-col cols="12" md="6">
        <v-card class="cyberpunk-card">
          <v-card-title class="neon-text">Top Classes by Attendance</v-card-title>
          <v-card-text>
            <v-data-table
              :headers="classHeaders"
              :items="topClasses"
              class="cyberpunk-table"
              dense
              hide-default-footer
            ></v-data-table>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="6">
        <v-card class="cyberpunk-card">
          <v-card-title class="neon-text">Recent Transactions</v-card-title>
          <v-card-text>
            <v-data-table
              :headers="transactionHeaders"
              :items="recentTransactions"
              class="cyberpunk-table"
              dense
              hide-default-footer
            ></v-data-table>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import VueApexCharts from 'vue3-apexcharts'

// Data
const totalMembers = ref(0)
const totalRevenue = ref(0)
const totalClasses = ref(0)
const totalBookings = ref(0)

const classHeaders = [
  { title: 'Class Name', key: 'name' },
  { title: 'Attendance', key: 'attendance' },
  { title: 'Revenue', key: 'revenue' }
]

const transactionHeaders = [
  { title: 'Date', key: 'date' },
  { title: 'Member', key: 'member' },
  { title: 'Amount', key: 'amount' }
]

// Mock data
const topClasses = [
  { name: 'Morning Yoga', attendance: 85, revenue: 1200 },
  { name: 'Strength Training', attendance: 72, revenue: 960 },
  { name: 'Cardio Blast', attendance: 68, revenue: 850 },
  { name: 'Evening Pilates', attendance: 55, revenue: 720 }
]

const recentTransactions = [
  { date: '2025-09-19', member: 'John Doe', amount: 50 },
  { date: '2025-09-18', member: 'Jane Smith', amount: 75 },
  { date: '2025-09-17', member: 'Bob Johnson', amount: 50 },
  { date: '2025-09-16', member: 'Alice Brown', amount: 100 }
]

// Chart configurations
const revenueChartOptions = {
  chart: {
    type: 'line',
    background: 'transparent',
    foreColor: '#00ffff'
  },
  colors: ['#ff0080', '#00ffff'],
  theme: {
    mode: 'dark'
  },
  xaxis: {
    categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep']
  },
  grid: {
    borderColor: 'rgba(255, 0, 128, 0.2)'
  }
}

const revenueSeries = [{
  name: 'Revenue',
  data: [1200, 1400, 1100, 1600, 1800, 1500, 1900, 2100, 2300]
}]

const popularityChartOptions = {
  chart: {
    type: 'radialBar',
    background: 'transparent'
  },
  colors: ['#ff0080', '#00ffff', '#ffea00', '#00ff80'],
  labels: ['Yoga', 'Strength', 'Cardio', 'Pilates'],
  theme: {
    mode: 'dark'
  }
}

const popularitySeries = [85, 72, 68, 55]

const demographicsChartOptions = {
  chart: {
    type: 'donut',
    background: 'transparent'
  },
  colors: ['#ff0080', '#00ffff', '#ffea00'],
  labels: ['Male', 'Female', 'Other'],
  theme: {
    mode: 'dark'
  }
}

const demographicsSeries = [45, 50, 5]

// Methods
const fetchDashboardData = async () => {
  try {
    // TODO: Replace with actual API calls
    totalMembers.value = 156
    totalRevenue.value = 12500
    totalClasses.value = 12
    totalBookings.value = 234
  } catch (error) {
    console.error('Error fetching dashboard data:', error)
  }
}

const exportReport = () => {
  // TODO: Implement CSV export
  console.log('Exporting report...')
  alert('CSV export functionality will be implemented with backend integration')
}

const getHeatmapColor = (day) => {
  const intensity = Math.random()
  if (intensity < 0.3) return 'rgba(0, 255, 255, 0.3)'
  if (intensity < 0.6) return 'rgba(255, 0, 128, 0.5)'
  return 'rgba(255, 234, 0, 0.7)'
}

onMounted(() => {
  fetchDashboardData()
})
</script>

<style scoped>
.page-container {
  animation: fadeIn 0.5s ease-in;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.cyberpunk-card {
  background: rgba(26, 26, 26, 0.9);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 0, 128, 0.3);
  border-radius: 12px;
  box-shadow: 0 0 20px rgba(255, 0, 128, 0.1);
  transition: all 0.3s ease;
}

.cyberpunk-card:hover {
  box-shadow: 0 0 30px rgba(255, 0, 128, 0.3);
  transform: translateY(-2px);
}

.metric-card {
  position: relative;
  overflow: hidden;
}

.metric-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 3px;
  background: linear-gradient(90deg, transparent, #00ffff, transparent);
  animation: scan 3s infinite;
}

@keyframes scan {
  0% { left: -100%; }
  50% { left: 100%; }
  100% { left: -100%; }
}

.metric-icon {
  margin-bottom: 15px;
}

.metric-value {
  font-size: 2.2rem;
  font-weight: bold;
  color: #ff0080;
  text-shadow: 0 0 15px #ff0080;
  margin: 10px 0;
}

.metric-label {
  color: #00ffff;
  font-size: 0.9rem;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.chart-card {
  position: relative;
}

.chart-card::after {
  content: '';
  position: absolute;
  top: 10px;
  right: 10px;
  width: 20px;
  height: 20px;
  background: radial-gradient(circle, #00ffff, #ff0080);
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.7;
  }
}

.heatmap-container {
  display: grid;
  grid-template-columns: repeat(10, 1fr);
  gap: 4px;
  margin-bottom: 20px;
}

.heatmap-cell {
  width: 20px;
  height: 20px;
  border-radius: 2px;
  transition: all 0.3s ease;
}

.heatmap-cell:hover {
  transform: scale(1.2);
  box-shadow: 0 0 10px rgba(255, 0, 128, 0.5);
}

.heatmap-legend {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}

.legend-item {
  color: #00ffff;
  font-size: 0.8rem;
}

.legend-gradient {
  width: 100px;
  height: 8px;
  background: linear-gradient(to right, rgba(0, 255, 255, 0.3), rgba(255, 0, 128, 0.7));
  border-radius: 4px;
}

.neon-text {
  color: #00ffff !important;
  text-shadow: 0 0 5px #00ffff;
}

.neon-icon {
  color: #ff0080 !important;
  filter: drop-shadow(0 0 3px #ff0080);
}

.neon-btn {
  background: linear-gradient(45deg, #ff0080, #00ffff) !important;
  color: white !important;
  border: none !important;
}

.cyberpunk-table :deep(.v-data-table-header) {
  background: rgba(255, 0, 128, 0.1);
}

.cyberpunk-table :deep(.v-data-table-header th) {
  color: #00ffff !important;
  font-weight: bold;
}

.cyberpunk-table :deep(.v-data-table-row) {
  transition: all 0.3s ease;
}

.cyberpunk-table :deep(.v-data-table-row:hover) {
  background: rgba(255, 0, 128, 0.1);
  box-shadow: 0 0 10px rgba(255, 0, 128, 0.2);
}

.mr-2 {
  margin-right: 8px;
}

.mb-4 {
  margin-bottom: 16px;
}
</style>