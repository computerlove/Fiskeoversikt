var path = require('path');
var webpack = require('webpack');

// TODO set NODE_ENV=production og inkluder UglifyJsPlugin
module.exports = {
    entry: {
        app : ['babel-polyfill', './src/main/js/main.ts'],
        vendor : ['react', 'react-dom', 'react-router', 'qwest', 'omnipotent', 'omniscient', 'immstruct', 'immutable']
    },
    output: {
        path: __dirname + '/target/classes/assets',
        filename: 'bundle.js'
    },
    devtool: 'source-map',
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
            }
        ],
        preLoaders: [
            // All output '.js' files will have any sourcemaps re-processed by 'source-map-loader'.
            { test: /\.js$/, loader: "source-map-loader" }
        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin(/* chunkName= */"vendor", /* filename= */"vendor.bundle.js")/*,
        new webpack.optimize.UglifyJsPlugin()*/
    ]
};
