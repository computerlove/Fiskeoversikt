const path = require('path');
const webpack = require('webpack');

const DEBUG = process.env.NODE_ENV !== 'production';

module.exports = {
    entry: {
        app : ['babel-polyfill', './src/main/js/main.tsx'],
        vendor : [
            'js-joda',
            'react',
            'react-dom',
            'react-redux',
            'redux',
            'redux-thunk',
            'web-request'
        ]
    },
    output: {
        path: __dirname + '/target/classes/assets',
        filename: 'bundle.js',
        devtoolModuleFilenameTemplate: 'webpack:///[absolute-resource-path]'
    },
    devtool: DEBUG ? 'cheap-module-eval-source-map' : 'source-map',
    resolve: {
        // Add '.ts' and '.tsx' as resolvable extensions.
        extensions: [".ts", ".tsx", ".js", ".jsx"]
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: ["babel-loader", "ts-loader"]
            },{
                test: /.jsx?$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                options: {
                    presets: ['es2015', 'react']
                }
            }, {
                test: /\.js$/,
                enforce: "pre",
                use: "source-map-loader"
            }

        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin({ name: 'vendor', filename: 'vendor.bundle.js' }),
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
    }
};
