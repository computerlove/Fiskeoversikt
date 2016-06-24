var path = require('path');
var webpack = require('webpack');

module.exports = {
    entry: './src/main/web/main.js',
    output: { path: __dirname + '/target/classes/assets', filename: 'bundle.js' },
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
    }
};
