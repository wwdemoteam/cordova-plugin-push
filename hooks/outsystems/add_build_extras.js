var fs = require('fs');
var path = require('path');

module.exports = function (context) {
    var srcPath = path.resolve(context.opts.plugin.dir, 'build-extras.gradle');
    var destPath = path.resolve(context.opts.projectRoot, 'platforms', 'android', 'build-extras.gradle');

    // Copy the build-extras.gradle file
    fs.copyFileSync(srcPath, destPath);
    console.log('build-extras.gradle added.');
};
