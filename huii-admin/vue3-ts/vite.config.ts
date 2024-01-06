import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import {createSvgIconsPlugin} from "vite-plugin-svg-icons";
import {prismjsPlugin} from 'vite-plugin-prismjs';

export default defineConfig({
    base: '/',
    plugins: [
        createSvgIconsPlugin({
            iconDirs: [path.resolve(process.cwd(), 'src/assets/icons/svg')],
            symbolId: 'icon-[dir]-[name]',
        }),
        vue(),
        prismjsPlugin({
            languages: 'all',//按需配置：['json','java']
        }),
    ],
    server: {
        host: '0.0.0.0',
        port: 5173,
        cors: true,
        open: true,
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ''),
            }
        }
    },
    resolve: {
        alias: {
            "@": path.resolve(__dirname, "./src")
        }
    },
    css: {
        preprocessorOptions: {
            scss: {
                javascriptEnabled: true,
                additionalData: '@import "./src/styles/variable.scss";',
            },
        },
    }
})
