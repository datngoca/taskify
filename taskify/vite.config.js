import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path' // 1. Import thư viện path
// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      // 2. Định nghĩa ký tự '@' trỏ về thư mục 'src'
      '@': path.resolve(__dirname, './src'),
    },
  },
})
