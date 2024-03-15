import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import {createSvgIconsPlugin} from "vite-plugin-svg-icons";
import {prismjsPlugin} from 'vite-plugin-prismjs';
import {visualizer} from "rollup-plugin-visualizer";
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import Icons from 'unplugin-icons/vite';
import IconsResolver from 'unplugin-icons/resolver';
import viteCompression from 'vite-plugin-compression';

export default defineConfig({
    base: '/',
    plugins: [
        vue(),
        createSvgIconsPlugin({
            iconDirs: [path.resolve(process.cwd(), 'src/assets/icons/svg')],
            symbolId: 'icon-[dir]-[name]',
        }),
        prismjsPlugin({
            //按需配置：['json','java'] //全部配置：'all'
            languages: []
        }),
        //dist分析插件
        visualizer({
            filename: 'stats.html',
            open: false,
            gzipSize: true,
            brotliSize: true
        }),
        //gzip压缩
        viteCompression({
            deleteOriginFile: false,
            verbose: true,
            disable: true,
            threshold: 10240,
            algorithm: 'gzip',
            ext: '.gz'
        }),
        Components({
            resolvers: [
                //ElementPlusResolver(),
                IconsResolver({
                    enabledCollections: ['ep'],
                }),
            ]
        }),
        AutoImport({
            include: [
                /\.[tj]sx?$/, // .ts, .tsx, .js, .jsx
                /\.vue$/,
                /\.vue\?vue/, // .vue
                /\.md$/ // .md
            ],
            imports: ['vue', 'vue-router', 'pinia'],
            resolvers: [
                //ElementPlusResolver(),
                IconsResolver({
                    prefix: 'Icon',
                }),
            ],
        }),
        Icons({
            autoInstall: true,
        }),
    ],
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
    },
    server: {
        host: '0.0.0.0',
        port: 5173,
        cors: true,
        //构建镜像时，请关闭此选项
        open: false,
        proxy: {
            '/api': {
                //基于docker-compose构建
                target: 'http://backend:8080',
                //本地构建
                //target: 'http://localhost:8080',
                //远程构建
                //target: 'https://yourDomain:port',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ''),
            }
        }
    },
    build: {
        chunkSizeWarningLimit: 1000,
        rollupOptions: {
            output: {
                chunkFileNames: 'static/js/[name]-[hash].js',
                entryFileNames: 'static/js/[name]-[hash].js',
                assetFileNames: 'static/[ext]/[name]-[hash].[ext]',
                manualChunks(id) {
                    if (id.includes('node_modules')) {
                        return id
                            .toString()
                            .split('node_modules/')[1]
                            .split('/')[0]
                            .toString()
                    }
                }
            }
        }
    }
})
