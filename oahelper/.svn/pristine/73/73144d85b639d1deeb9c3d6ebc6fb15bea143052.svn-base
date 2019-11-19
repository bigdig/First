const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const merge = require('webpack-merge');
const webpackBaseConfig = require('./webpack.base.config.js');
const fs = require('fs');
const package = require('../package.json');

fs.open('./build/env.js', 'w', function(err, fd) {
    const buf = 'export default "development";';
    fs.write(fd, buf, 0, buf.length, 0, function(err, written, buffer) {});
});

module.exports = merge(webpackBaseConfig, {
    devtool: '#source-map',
    output: {
        publicPath: '/dist/',
        filename: '[name].js',
        chunkFilename: '[name].chunk.js'
    },
    plugins: [
        new ExtractTextPlugin({
            filename: '[name].css',
            allChunks: true
        }),
        new webpack.optimize.CommonsChunkPlugin({
            name: ['vender-exten', 'vender-base'],
            minChunks: Infinity
        }),
        new HtmlWebpackPlugin({
            title: '研究所办公小助手 v' + package.version,
            filename: '../index.html',
            inject: false
        }),
        new CopyWebpackPlugin([
            {
                from: 'src/views/main-components/theme-switch/theme'
            },
            {
                from: 'src/views/my-components/text-editor/tinymce'
            }
        ], {
            ignore: [
                'text-editor.vue'
            ]
        })
    ],
    //设置跨域代理
    devServer: {
        historyApiFallback: true,
        hot: true,
        inline: true,
        stats: { colors: true },
        proxy: {
            //匹配代理的url
            '/login': {
                // 目标服务器地址
                target: 'https://tytool.tfzq.com',
                //路径重写
                //pathRewrite: {'^/login' : '/login'},
                changeOrigin: true
            },
            '/sys/*': {
                // 目标服务器地址
                target: 'https://tytool.tfzq.com',
                //路径重写
                //pathRewrite: {'^/sys' : '/sys'},
                changeOrigin: true
            },
            '/pr/*': {
                // 目标服务器地址
                target: 'https://tytool.tfzq.com',
                //路径重写
                //pathRewrite: {'^/sys' : '/sys'},
                changeOrigin: true
            },
            '/bizspace/*': {
                // 目标服务器地址
                target: 'https://tytool.tfzq.com',
                //路径重写
                //pathRewrite: {'^/sys' : '/sys'},
                changeOrigin: true
            },
            '/unauthorized': {
                // 目标服务器地址
                target: 'https://tytool.tfzq.com',
                //路径重写
                //pathRewrite: {'^/sys' : '/sys'},
                changeOrigin: true
            },
          '/uploadfile': {
                // 目标服务器地址
                target: 'https://tytool.tfzq.com',
                //路径重写
                //pathRewrite: {'^/sys' : '/sys'},
                changeOrigin: true
            },
        }
    }
});
