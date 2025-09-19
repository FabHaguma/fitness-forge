<template>
  <v-app class="cyberpunk-bg">
    <v-navigation-drawer
      v-model="drawer"
      temporary
      class="cyberpunk-drawer"
    >
      <v-list density="compact" nav class="cyberpunk-list">
        <v-list-item
          v-for="item in menuItems"
          :key="item.title"
          :to="item.path"
          class="cyberpunk-list-item"
        >
          <v-list-item-icon>
            <v-icon :icon="item.icon" class="neon-icon"></v-icon>
          </v-list-item-icon>
          <v-list-item-title class="neon-text">{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar class="cyberpunk-header" density="compact">
      <v-app-bar-nav-icon
        @click="drawer = !drawer"
        class="neon-icon"
      ></v-app-bar-nav-icon>
      <v-toolbar-title class="neon-text cyberpunk-title">
        <v-icon icon="mdi-dumbbell" class="neon-icon mr-2"></v-icon>
        FITNESS FORGE
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon class="neon-icon">
        <v-icon>mdi-account-circle</v-icon>
      </v-btn>
    </v-app-bar>

    <v-main class="cyberpunk-main">
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
import { ref } from 'vue'

const drawer = ref(false)

const menuItems = [
  { title: 'Dashboard', path: '/', icon: 'mdi-view-dashboard' },
  { title: 'Members', path: '/members', icon: 'mdi-account-group' },
  { title: 'Classes', path: '/classes', icon: 'mdi-calendar-clock' },
  { title: 'Bookings', path: '/bookings', icon: 'mdi-calendar-check' },
  { title: 'Reports', path: '/reports', icon: 'mdi-chart-line' }
]
</script>

<style scoped>
.cyberpunk-bg {
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 50%, #0a0a0a 100%);
  min-height: 100vh;
}

.cyberpunk-header {
  background: rgba(26, 26, 26, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid #ff0080;
  box-shadow: 0 0 20px rgba(255, 0, 128, 0.3);
}

.cyberpunk-title {
  font-family: 'Orbitron', monospace;
  font-weight: bold;
  text-shadow: 0 0 10px #00ffff;
  letter-spacing: 2px;
}

.cyberpunk-drawer {
  background: rgba(26, 26, 26, 0.95);
  backdrop-filter: blur(10px);
  border-right: 1px solid #00ffff;
}

.cyberpunk-list {
  padding: 20px 0;
}

.cyberpunk-list-item {
  margin: 5px 10px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.cyberpunk-list-item:hover {
  background: rgba(255, 0, 128, 0.1);
  box-shadow: 0 0 15px rgba(255, 0, 128, 0.3);
  transform: translateX(5px);
}

.neon-text {
  color: #00ffff;
  text-shadow: 0 0 5px #00ffff;
}

.neon-icon {
  color: #ff0080 !important;
  filter: drop-shadow(0 0 3px #ff0080);
}

.cyberpunk-main {
  background: transparent;
  padding: 20px;
}

.mr-2 {
  margin-right: 8px;
}
</style>
