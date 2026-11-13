import fs from 'node:fs';
import path from 'node:path';
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import AutoImport from 'unplugin-auto-import/vite';
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';
import viteCompression from 'vite-plugin-compression';

export default defineConfig(({ command, mode, ssrBuild }) => {
  if (command === 'serve') {
    console.log('is serve');
  } else {
    console.log('is build');
  }
  return {
    base: '/',
    resolve: {
      extensions: ['.js', '.vue', '.json'],
      alias: {
        '@': path.resolve(__dirname, 'src'),
      },
    },
    build: {
      outDir: 'dist',
    },
    plugins: [
      vue(),
      AutoImport({
        // 自动导入配置开启eslintrc，避免变量未定义报错
        eslintrc: {
          enabled: true,
        },
        imports: ['vue', 'vue-router'],
        dts: true,
      }),
      createSvgIconsPlugin({
        iconDirs: [path.resolve(__dirname, 'src/assets/icons')],
        symbolId: 'icon-[name]',
      }),
      viteCompression(),
    ],
    server: {
      open: true,
      port: 5174,
      host: '0.0.0.0',
      proxy: {
        '/api': {
          target: 'http://127.0.0.1:9090',
          changeOrigin: true,
          ws: true, // 开启websocket代理
        },
      },
    },
    // 全局Scss变量
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: `@import "src/styles/define.scss";`,
        },
      },
    },
  };
});
