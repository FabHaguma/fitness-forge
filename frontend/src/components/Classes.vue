<template>
  <div class="page-container">
    <v-row>
      <v-col cols="12">
        <v-card class="cyberpunk-card mb-4">
          <v-card-title class="neon-text">
            <v-icon icon="mdi-calendar-clock" class="neon-icon mr-2"></v-icon>
            Class Management
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="showAddDialog = true" class="neon-btn">
              <v-icon left>mdi-plus</v-icon>
              Add Class
            </v-btn>
          </v-card-title>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" md="8">
        <v-card class="cyberpunk-card">
          <v-card-title class="neon-text">Class Schedule</v-card-title>
          <v-card-text>
            <v-calendar
              v-model="calendarDate"
              :events="classEvents"
              event-color="primary"
              class="cyberpunk-calendar"
              @click:event="handleEventClick"
            >
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card class="cyberpunk-card">
          <v-card-title class="neon-text">Upcoming Classes</v-card-title>
          <v-card-text>
            <v-list class="cyberpunk-list">
              <v-list-item
                v-for="classItem in upcomingClasses"
                :key="classItem.id"
                class="cyberpunk-list-item"
              >
                <v-list-item-content>
                  <v-list-item-title class="neon-text">{{ classItem.name }}</v-list-item-title>
                  <v-list-item-subtitle class="neon-text-secondary">
                    {{ classItem.schedule }} - {{ classItem.trainer }}
                  </v-list-item-subtitle>
                </v-list-item-content>
                <v-list-item-action>
                  <v-btn icon size="small" @click="editClass(classItem)" class="neon-icon">
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                </v-list-item-action>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Add/Edit Class Dialog -->
    <v-dialog v-model="showAddDialog" max-width="600px">
      <v-card class="cyberpunk-card">
        <v-card-title class="neon-text">
          {{ editingClass ? 'Edit Class' : 'Add New Class' }}
        </v-card-title>
        <v-card-text>
          <v-form @submit.prevent="saveClass">
            <v-text-field
              v-model="classForm.name"
              label="Class Name"
              required
              class="mb-3"
            ></v-text-field>
            <v-select
              v-model="classForm.type"
              :items="['Yoga', 'Pilates', 'Strength Training', 'Cardio', 'Zumba', 'CrossFit']"
              label="Class Type"
              required
              class="mb-3"
            ></v-select>
            <v-text-field
              v-model="classForm.schedule"
              label="Schedule (e.g., Mon 6:00 PM)"
              required
              class="mb-3"
            ></v-text-field>
            <v-select
              v-model="classForm.trainer"
              :items="trainers"
              label="Trainer"
              required
              class="mb-3"
            ></v-select>
            <v-text-field
              v-model="classForm.capacity"
              label="Capacity"
              type="number"
              required
              class="mb-3"
            ></v-text-field>
            <v-btn type="submit" color="primary" class="neon-btn">
              {{ editingClass ? 'Update' : 'Add' }} Class
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
const classes = ref([])
const trainers = ref(['John Smith', 'Sarah Johnson', 'Mike Davis', 'Emma Wilson'])
const calendarDate = ref(new Date().toISOString().substr(0, 10))
const showAddDialog = ref(false)
const editingClass = ref(null)

const classForm = ref({
  name: '',
  type: '',
  schedule: '',
  trainer: '',
  capacity: ''
})

// Mock data
const mockClasses = [
  { id: 1, name: 'Morning Yoga', type: 'Yoga', schedule: 'Mon 8:00 AM', trainer: 'John Smith', capacity: 20 },
  { id: 2, name: 'Strength Training', type: 'Strength Training', schedule: 'Tue 6:00 PM', trainer: 'Sarah Johnson', capacity: 15 },
  { id: 3, name: 'Cardio Blast', type: 'Cardio', schedule: 'Wed 7:00 PM', trainer: 'Mike Davis', capacity: 25 },
  { id: 4, name: 'Evening Pilates', type: 'Pilates', schedule: 'Thu 6:30 PM', trainer: 'Emma Wilson', capacity: 18 }
]

const upcomingClasses = computed(() => {
  return mockClasses.slice(0, 5)
})

const classEvents = computed(() => {
  return mockClasses.map(cls => ({
    id: cls.id,
    title: cls.name,
    start: new Date(), // Mock date
    end: new Date(),
    details: `${cls.type} - ${cls.trainer}`,
    color: '#ff0080'
  }))
})

// Methods
const fetchClasses = async () => {
  try {
    // TODO: Replace with actual API call
    classes.value = mockClasses
  } catch (error) {
    console.error('Error fetching classes:', error)
  }
}

const saveClass = async () => {
  try {
    if (editingClass.value) {
      // TODO: Update class via API
      console.log('Updating class:', classForm.value)
    } else {
      // TODO: Add new class via API
      console.log('Adding class:', classForm.value)
    }

    // Reset form and close dialog
    classForm.value = { name: '', type: '', schedule: '', trainer: '', capacity: '' }
    editingClass.value = null
    showAddDialog.value = false

    // Refresh classes list
    await fetchClasses()
  } catch (error) {
    console.error('Error saving class:', error)
  }
}

const editClass = (classItem) => {
  editingClass.value = classItem
  classForm.value = { ...classItem }
  showAddDialog.value = true
}

const handleEventClick = (event) => {
  console.log('Event clicked:', event.event)
}

onMounted(() => {
  fetchClasses()
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

.cyberpunk-calendar :deep(.v-calendar) {
  background: rgba(26, 26, 26, 0.8);
  border-radius: 8px;
}

.cyberpunk-calendar :deep(.v-calendar-header) {
  background: rgba(255, 0, 128, 0.1);
  color: #00ffff;
}

.cyberpunk-calendar :deep(.v-calendar-day) {
  color: #00ffff;
}

.cyberpunk-calendar :deep(.v-calendar-day:hover) {
  background: rgba(255, 0, 128, 0.2);
}

.event-content {
  font-size: 0.8rem;
  color: white;
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

.mb-3 {
  margin-bottom: 12px;
}
</style>