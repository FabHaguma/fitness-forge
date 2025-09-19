<template>
  <div class="dashboard-container">
    <v-row>
      <v-col cols="12">
        <v-card class="cyberpunk-card mb-4">
          <v-card-title class="neon-text">
            <v-icon icon="mdi-view-dashboard" class="neon-icon mr-2"></v-icon>
            Admin Dashboard
          </v-card-title>
          <v-card-text>
            <p class="neon-text">Welcome to the Fitness Forge Management System</p>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" md="4">
        <v-card class="cyberpunk-card stat-card">
          <v-card-title class="neon-text">
            <v-icon icon="mdi-account-group" class="neon-icon mr-2"></v-icon>
            Total Members
          </v-card-title>
          <v-card-text>
            <div class="stat-number">{{ totalMembers }}</div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card class="cyberpunk-card stat-card">
          <v-card-title class="neon-text">
            <v-icon icon="mdi-calendar-clock" class="neon-icon mr-2"></v-icon>
            Active Classes
          </v-card-title>
          <v-card-text>
            <div class="stat-number">{{ totalClasses }}</div>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card class="cyberpunk-card stat-card">
          <v-card-title class="neon-text">
            <v-icon icon="mdi-cash" class="neon-icon mr-2"></v-icon>
            Total Revenue
          </v-card-title>
          <v-card-text>
            <div class="stat-number">${{ totalRevenue }}</div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <v-card class="cyberpunk-card">
          <v-card-title class="neon-text">
            <v-icon icon="mdi-chart-line" class="neon-icon mr-2"></v-icon>
            Recent Activity
          </v-card-title>
          <v-card-text>
            <v-list class="cyberpunk-list">
              <v-list-item class="cyberpunk-list-item">
                <v-list-item-icon>
                  <v-icon class="neon-icon">mdi-account-plus</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title class="neon-text">New member registered</v-list-item-title>
                  <v-list-item-subtitle class="neon-text-secondary">2 hours ago</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item class="cyberpunk-list-item">
                <v-list-item-icon>
                  <v-icon class="neon-icon">mdi-calendar-check</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title class="neon-text">Class booking confirmed</v-list-item-title>
                  <v-list-item-subtitle class="neon-text-secondary">4 hours ago</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item class="cyberpunk-list-item">
                <v-list-item-icon>
                  <v-icon class="neon-icon">mdi-cash</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title class="neon-text">Invoice generated</v-list-item-title>
                  <v-list-item-subtitle class="neon-text-secondary">6 hours ago</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// Placeholder data - will be replaced with API calls
const totalMembers = ref(0)
const totalClasses = ref(0)
const totalRevenue = ref(0)

onMounted(() => {
  // TODO: Fetch data from backend APIs
  totalMembers.value = 42
  totalClasses.value = 8
  totalRevenue.value = 12500
})
</script>

<style scoped>
.dashboard-container {
  animation: fadeIn 1s ease-in;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
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

.stat-card {
  text-align: center;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, transparent, #00ffff, transparent);
  animation: scan 3s infinite;
}

@keyframes scan {
  0% { left: -100%; }
  50% { left: 100%; }
  100% { left: -100%; }
}

.stat-number {
  font-size: 2.5rem;
  font-weight: bold;
  color: #ff0080;
  text-shadow: 0 0 15px #ff0080;
  margin: 20px 0;
}

.neon-text {
  color: #00ffff !important;
  text-shadow: 0 0 5px #00ffff;
}

.neon-text-secondary {
  color: #888 !important;
}

.neon-icon {
  color: #ff0080 !important;
  filter: drop-shadow(0 0 3px #ff0080);
}

.cyberpunk-list {
  background: transparent;
}

.cyberpunk-list-item {
  border-radius: 8px;
  margin: 5px 0;
  transition: all 0.3s ease;
}

.cyberpunk-list-item:hover {
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