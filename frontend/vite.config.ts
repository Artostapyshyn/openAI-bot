import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
export default defineConfig({
  base:"./login",
  plugins: [react()],
  server: {
    port: 3000,
  },
})
