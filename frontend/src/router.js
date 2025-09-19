import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from './components/Dashboard.vue'
import Members from './components/Members.vue'
import Classes from './components/Classes.vue'
import Bookings from './components/Bookings.vue'
import Reports from './components/Reports.vue'

const routes = [
  { path: '/', component: Dashboard },
  { path: '/members', component: Members },
  { path: '/classes', component: Classes },
  { path: '/bookings', component: Bookings },
  { path: '/reports', component: Reports }
]

export default createRouter({
  history: createWebHistory(),
  routes
})