const path = require('path');
const webpack = require('webpack');

const DEBUG = process.env.NODE_ENV !== 'production';

module.exports = {
    entry: {
        app : ['./src/main/js/main.tsx'],
        init : ['./src/main/js/init.ts']
    },
    output: {
        path: __dirname + '/target/classes/assets',
        filename: '[name].bundle.js',
        devtoolModuleFilenameTemplate: 'webpack:///[absolute-resource-path]',
        globalObject: "this" // https://github.com/webpack/webpack/issues/6642
    },
    devtool: 'source-map',
    resolve: {
        // Add '.ts' and '.tsx' as resolvable extensions.
        extensions: [".ts", ".tsx", ".js", ".jsx"]
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: ["awesome-typescript-loader"]
            },
            {
                test: /\.js$/,
                enforce: "pre",
                use: "source-map-loader"
            }

        ]
    },
    optimization: {
        splitChunks: {
            cacheGroups: {
                commons: { test: /[\\/]node_modules[\\/]/, name: "vendor", chunks: "all" }
            }
        }
    },
    plugins: [
        /*new webpack.optimize.CommonsChunkPlugin({ name: 'vendor', filename: 'vendor.bundle.js' }),*/
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': JSON.stringify(process.env.NODE_ENV || 'development')
            }
        })
    ],
    node: {
        console: true,
        fs: 'empty',
        net: 'empty',
        tls: 'empty'
    },
    mode: DEBUG ? "development" : "production"
};
