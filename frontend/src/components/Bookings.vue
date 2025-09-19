<template>
  <div class="page-container">
    <v-row>
      <v-col cols="12">
        <v-card class="cyberpunk-card mb-4">
          <v-card-title class="neon-text">
            <v-icon icon="mdi-calendar-check" class="neon-icon mr-2"></v-icon>
            Booking Management
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="showBookDialog = true" class="neon-btn">
              <v-icon left>mdi-plus</v-icon>
              Book Class
            </v-btn>
          </v-card-title>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" md="8">
        <v-card class="cyberpunk-card">
          <v-card-title class="neon-text">All Bookings</v-card-title>
          <v-card-text>
            <v-data-table
              :headers="bookingHeaders"
              :items="bookings"
              class="cyberpunk-table"
              :loading="loading"
            >
              <template v-slot:item.status="{ item }">
                <v-chip
                  :color="getStatusColor(item.status)"
                  size="small"
                  class="status-chip"
                >
                  {{ item.status }}
                </v-chip>
              </template>
              <template v-slot:item.actions="{ item }">
                <v-btn
                  v-if="item.status === 'booked'"
                  icon
                  size="small"
                  @click="cancelBooking(item)"
                  color="error"
                  class="neon-icon"
                >
                  <v-icon>mdi-cancel</v-icon>
                </v-btn>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card class="cyberpunk-card mb-4">
          <v-card-title class="neon-text">Available Classes</v-card-title>
          <v-card-text>
            <v-list class="cyberpunk-list">
              <v-list-item
                v-for="classItem in availableClasses"
                :key="classItem.id"
                class="cyberpunk-list-item"
                @click="selectClassForBooking(classItem)"
              >
                <v-list-item-content>
                  <v-list-item-title class="neon-text">{{ classItem.name }}</v-list-item-title>
                  <v-list-item-subtitle class="neon-text-secondary">
                    {{ classItem.schedule }} - {{ classItem.trainer }}
                  </v-list-item-subtitle>
                  <v-list-item-subtitle class="neon-text-secondary">
                    Available spots: {{ classItem.availableSpots }}
                  </v-list-item-subtitle>
                </v-list-item-content>
                <v-list-item-action>
                  <v-icon class="neon-icon">mdi-plus-circle</v-icon>
                </v-list-item-action>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>

        <v-card class="cyberpunk-card">
          <v-card-title class="neon-text">Quick Stats</v-card-title>
          <v-card-text>
            <div class="stat-item">
              <span class="stat-label">Total Bookings:</span>
              <span class="stat-value">{{ bookings.length }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">Active Bookings:</span>
              <span class="stat-value">{{ activeBookings }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">Cancelled:</span>
              <span class="stat-value">{{ cancelledBookings }}</span>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Book Class Dialog -->
    <v-dialog v-model="showBookDialog" max-width="600px">
      <v-card class="cyberpunk-card">
        <v-card-title class="neon-text">Book a Class</v-card-title>
        <v-card-text>
          <v-form @submit.prevent="bookClass">
            <v-select
              v-model="bookingForm.memberId"
              :items="members"
              item-title="name"
              item-value="id"
              label="Select Member"
              required
              class="mb-3"
            ></v-select>
            <v-select
              v-model="bookingForm.classSessionId"
              :items="availableClasses"
              item-title="name"
              item-value="id"
              label="Select Class"
              required
              class="mb-3"
            ></v-select>
            <v-btn type="submit" color="primary" class="neon-btn">
              Book Class
            </v-btn>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

// Data
const bookings = ref([])
const members = ref([])
const availableClasses = ref([])
const loading = ref(false)
const showBookDialog = ref(false)

const bookingHeaders = [
  { title: 'Member', key: 'memberName' },
  { title: 'Class', key: 'className' },
  { title: 'Date', key: 'date' },
  { title: 'Status', key: 'status' },
  { title: 'Actions', key: 'actions', sortable: false }
]

const bookingForm = ref({
  memberId: '',
  classSessionId: ''
})

// Mock data
const mockBookings = [
  { id: 1, memberName: 'John Doe', className: 'Morning Yoga', date: '2025-09-20', status: 'booked' },
  { id: 2, memberName: 'Jane Smith', className: 'Strength Training', date: '2025-09-21', status: 'booked' },
  { id: 3, memberName: 'Bob Johnson', className: 'Cardio Blast', date: '2025-09-19', status: 'cancelled' }
]

const mockMembers = [
  { id: 1, name: 'John Doe' },
  { id: 2, name: 'Jane Smith' },
  { id: 3, name: 'Bob Johnson' }
]

const mockAvailableClasses = [
  { id: 1, name: 'Morning Yoga', schedule: 'Mon 8:00 AM', trainer: 'John Smith', availableSpots: 15 },
  { id: 2, name: 'Evening Pilates', schedule: 'Thu 6:30 PM', trainer: 'Emma Wilson', availableSpots: 12 },
  { id: 3, name: 'Cardio Blast', schedule: 'Wed 7:00 PM', trainer: 'Mike Davis', availableSpots: 8 }
]

const activeBookings = computed(() => {
  return bookings.value.filter(b => b.status === 'booked').length
})

const cancelledBookings = computed(() => {
  return bookings.value.filter(b => b.status === 'cancelled').length
})

// Methods
const fetchBookings = async () => {
  loading.value = true
  try {
    // TODO: Replace with actual API call
    bookings.value = mockBookings
    members.value = mockMembers
    availableClasses.value = mockAvailableClasses
  } catch (error) {
    console.error('Error fetching bookings:', error)
  } finally {
    loading.value = false
  }
}

const bookClass = async () => {
  try {
    // TODO: Book class via API
    console.log('Booking class:', bookingForm.value)

    // Reset form and close dialog
    bookingForm.value = { memberId: '', classSessionId: '' }
    showBookDialog.value = false

    // Refresh bookings list
    await fetchBookings()
  } catch (error) {
    console.error('Error booking class:', error)
  }
}

const cancelBooking = async (booking) => {
  if (confirm('Are you sure you want to cancel this booking?')) {
    try {
      // TODO: Cancel booking via API
      console.log('Cancelling booking:', booking.id)

      // Refresh bookings list
      await fetchBookings()
    } catch (error) {
      console.error('Error cancelling booking:', error)
    }
  }
}

const selectClassForBooking = (classItem) => {
  bookingForm.value.classSessionId = classItem.id
  showBookDialog.value = true
}

const getStatusColor = (status) => {
  switch (status) {
    case 'booked': return 'success'
    case 'cancelled': return 'error'
    default: return 'primary'
  }
}

onMounted(() => {
  fetchBookings()
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

.status-chip {
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.cyberpunk-list {
  background: transparent;
}

.cyberpunk-list-item {
  border-radius: 8px;
  margin: 5px 0;
  transition: all 0.3s ease;
  cursor: pointer;
}

.cyberpunk-list-item:hover {
  background: rgba(255, 0, 128, 0.1);
  box-shadow: 0 0 10px rgba(255, 0, 128, 0.2);
  transform: translateX(5px);
}

.stat-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding: 8px 0;
  border-bottom: 1px solid rgba(255, 0, 128, 0.2);
}

.stat-label {
  color: #00ffff;
  font-weight: 500;
}

.stat-value {
  color: #ff0080;
  font-weight: bold;
  font-size: 1.1em;
}

.mr-2 {
  margin-right: 8px;
}

.mb-4 {
  margin-bottom: 16px;
}

.mb-3 {
  margin-bottom: 12px;
}
</style>