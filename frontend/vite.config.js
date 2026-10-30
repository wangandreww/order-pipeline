import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// The browser only talks to Vite (port 5173). Vite forwards /api/* to the right Spring Boot service,
// so neither backend needs CORS settings.
const toService = (target) => ({
  target,
  rewrite: (path) => path.replace(/^\/api/, ''),
})

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api/orders': toService('http://localhost:8080'),
      '/api/products': toService('http://localhost:8081'),
    },
  },
})
