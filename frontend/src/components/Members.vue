<template>
  <div class="page-container">
    <v-row>
      <v-col cols="12">
        <v-card class="cyberpunk-card mb-4">
          <v-card-title class="neon-text">
            <v-icon icon="mdi-account-group" class="neon-icon mr-2"></v-icon>
            Member Management
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="showAddDialog = true" class="neon-btn">
              <v-icon left>mdi-plus</v-icon>
              Add Member
            </v-btn>
          </v-card-title>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <v-card class="cyberpunk-card">
          <v-card-text>
            <v-data-table
              :headers="headers"
              :items="members"
              class="cyberpunk-table"
              :loading="loading"
            >
              <template v-slot:item.actions="{ item }">
                <v-btn icon size="small" @click="editMember(item)" class="neon-icon">
                  <v-icon>mdi-pencil</v-icon>
                </v-btn>
                <v-btn icon size="small" @click="deleteMember(item)" color="error" class="neon-icon">
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Add/Edit Member Dialog -->
    <v-dialog v-model="showAddDialog" max-width="600px">
      <v-card class="cyberpunk-card">
        <v-card-title class="neon-text">
          {{ editingMember ? 'Edit Member' : 'Add New Member' }}
        </v-card-title>
        <v-card-text>
          <v-form @submit.prevent="saveMember">
            <v-text-field
              v-model="memberForm.name"
              label="Name"
              required
              class="mb-3"
            ></v-text-field>
            <v-text-field
              v-model="memberForm.age"
              label="Age"
              type="number"
              required
              class="mb-3"
            ></v-text-field>
            <v-select
              v-model="memberForm.gender"
              :items="['Male', 'Female', 'Other']"
              label="Gender"
              required
              class="mb-3"
            ></v-select>
            <v-text-field
              v-model="memberForm.contact"
              label="Contact"
              required
              class="mb-3"
            ></v-text-field>
            <v-btn type="submit" color="primary" class="neon-btn">
              {{ editingMember ? 'Update' : 'Add' }} Member
            </v-btn>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// Data
const members = ref([])
const loading = ref(false)
const showAddDialog = ref(false)
const editingMember = ref(null)

const headers = [
  { title: 'ID', key: 'id' },
  { title: 'Name', key: 'name' },
  { title: 'Age', key: 'age' },
  { title: 'Gender', key: 'gender' },
  { title: 'Contact', key: 'contact' },
  { title: 'Actions', key: 'actions', sortable: false }
]

const memberForm = ref({
  name: '',
  age: '',
  gender: '',
  contact: ''
})

// Methods
const fetchMembers = async () => {
  loading.value = true
  try {
    const response = await fetch('http://localhost:8080/members')
    if (response.ok) {
      members.value = await response.json()
    } else {
      // Fallback to mock data if backend is not running
      console.warn('Backend not available, using mock data')
      members.value = [
        { id: 1, name: 'John Doe', age: 30, gender: 'Male', contact: 'john@example.com' },
        { id: 2, name: 'Jane Smith', age: 25, gender: 'Female', contact: 'jane@example.com' },
        { id: 3, name: 'Bob Johnson', age: 35, gender: 'Male', contact: 'bob@example.com' }
      ]
    }
  } catch (error) {
    console.error('Error fetching members:', error)
    // Fallback to mock data
    members.value = [
      { id: 1, name: 'John Doe', age: 30, gender: 'Male', contact: 'john@example.com' },
      { id: 2, name: 'Jane Smith', age: 25, gender: 'Female', contact: 'jane@example.com' },
      { id: 3, name: 'Bob Johnson', age: 35, gender: 'Male', contact: 'bob@example.com' }
    ]
  } finally {
    loading.value = false
  }
}

const saveMember = async () => {
  try {
    let response
    if (editingMember.value) {
      // Update member via API
      response = await fetch(`http://localhost:8080/members/${editingMember.value.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(memberForm.value)
      })
    } else {
      // Add new member via API
      response = await fetch('http://localhost:8080/members', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(memberForm.value)
      })
    }

    if (response.ok) {
      // Reset form and close dialog
      memberForm.value = { name: '', age: '', gender: '', contact: '' }
      editingMember.value = null
      showAddDialog.value = false

      // Refresh members list
      await fetchMembers()
    } else {
      console.error('Failed to save member:', response.statusText)
      // For demo purposes, still update the UI
      memberForm.value = { name: '', age: '', gender: '', contact: '' }
      editingMember.value = null
      showAddDialog.value = false
      await fetchMembers()
    }
  } catch (error) {
    console.error('Error saving member:', error)
    // For demo purposes, still update the UI
    memberForm.value = { name: '', age: '', gender: '', contact: '' }
    editingMember.value = null
    showAddDialog.value = false
    await fetchMembers()
  }
}

const editMember = (item) => {
  editingMember.value = item
  memberForm.value = { ...item }
  showAddDialog.value = true
}

const deleteMember = async (item) => {
  if (confirm('Are you sure you want to delete this member?')) {
    try {
      const response = await fetch(`http://localhost:8080/members/${item.id}`, {
        method: 'DELETE'
      })

      if (response.ok) {
        // Refresh members list
        await fetchMembers()
      } else {
        console.error('Failed to delete member:', response.statusText)
        // For demo purposes, still update the UI
        await fetchMembers()
      }
    } catch (error) {
      console.error('Error deleting member:', error)
      // For demo purposes, still update the UI
      await fetchMembers()
    }
  }
}

onMounted(() => {
  fetchMembers()
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

.mb-3 {
  margin-bottom: 12px;
}
</style>