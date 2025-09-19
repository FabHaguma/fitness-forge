import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'

// ApexCharts
import VueApexCharts from 'vue3-apexcharts'

// Router
import router from './router.js'

const vuetify = createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'dark',
    themes: {
      dark: {
        dark: true,
        colors: {
          primary: '#ff0080',
          secondary: '#00ffff',
          accent: '#ffea00',
          background: '#0a0a0a',
          surface: '#1a1a1a',
        }
      }
    }
  }
})

createApp(App).use(vuetify).use(router).use(VueApexCharts).mount('#app')
