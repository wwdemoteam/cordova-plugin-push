var fs = require('fs');
var path = require('path');

module.exports = function (context) {
    var destPath = path.resolve(context.opts.projectRoot, 'platforms', 'android', 'build-extras.gradle');

    // Remove the build-extras.gradle file
    fs.unlinkSync(destPath);
    console.log('build-extras.gradle removed.');
};
