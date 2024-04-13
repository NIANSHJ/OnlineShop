import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue()
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: 'localhost',
    port: 5173,
    cors: true,
    proxy: {
      '/front': {//获取路径中包含了/front的请求
        target: 'http://localhost:8080',//前台服务所在的源
        changeOrigin: true,//修改源
        rewrite: (path) => path.replace(/^\/front/, '')//front替换为''
      },
      '/back': {//获取路径中包含了/back的请求
        target: 'http://localhost:8081',//后台服务所在的源
        changeOrigin: true,//修改源
        rewrite: (path) => path.replace(/^\/back/, '')//back替换为''
      }
    }
  }
})


