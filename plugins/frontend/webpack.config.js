const path = require('path');
const webpack = require('webpack');

const DEBUG = process.env.NODE_ENV !== 'production';

module.exports = {
    entry: {
        app : ['babel-polyfill', './src/main/js/main.tsx'],
        vendor : ['react', 'react-dom', 'react-router', 'qwest', 'omnipotent', 'omniscient', 'immstruct', 'immutable']
    },
    output: {
        path: __dirname + '/target/classes/assets',
        filename: 'bundle.js',
        devtoolModuleFilenameTemplate: 'webpack:///[absolute-resource-path]'
    },
    devtool: DEBUG ? 'cheap-module-eval-source-map' : 'source-map',
    resolve: {
        // Add '.ts' and '.tsx' as resolvable extensions.
        extensions: ["", ".ts", ".tsx", ".js", ".jsx"]
    },
    module: {
        loaders: [
            { test: /\.tsx?$/, loader: "babel!ts" },
            {
                test: /.jsx?$/,
                loader: 'babel',
                exclude: /node_modules/,
                query: {
                    presets: ['es2015', 'react']
                }
            },
            {test: /\.json$/, loader: 'json-loader'}
        ],
        preLoaders: [
            // All output '.js' files will have any sourcemaps re-processed by 'source-map-loader'.
            { test: /\.js$/, loader: "source-map-loader" }
        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin(/* chunkName= */"vendor", /* filename= */"vendor.bundle.js"),
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
