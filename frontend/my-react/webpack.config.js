const path = require('path');

module.exports = {
    entry: path.join(__dirname, "src", "index.js"),
}

webpackConfig.module.rules.push({
    test: /\.mjs$/,
    include: /node_modules/,
    type: "javascript/auto"
});