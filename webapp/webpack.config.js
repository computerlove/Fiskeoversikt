var path = require('path');
var webpack = require('webpack');

// TODO set NODE_ENV=production og inkluder UglifyJsPlugin
module.exports = {
    entry: {
        app : './src/main/webapp/main.js',
        vendor : ['react', 'react-dom', 'react-router', 'qwest', 'omnipotent', 'omniscient', 'immstruct', 'immutable']
    },
    output: { path: __dirname + '/src/main/webapp/dist', filename: 'bundle.js' },
    devtool: 'source-map',
    module: {
        loaders: [
            {
                test: /.jsx?$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                query: {
                    presets: ['es2015', 'react']
                }
            }
        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin(/* chunkName= */"vendor", /* filename= */"vendor.bundle.js")/*,
        new webpack.optimize.UglifyJsPlugin()*/
    ]
};
