var path = require('path');
var webpack = require('webpack');

module.exports = {
    entry: {
        app : './src/main/webapp/main.js',
        vendor : ['react', 'react-dom']
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
        new webpack.optimize.CommonsChunkPlugin(/* chunkName= */"vendor", /* filename= */"vendor.bundle.js"),
        new webpack.optimize.UglifyJsPlugin()
    ]
};
