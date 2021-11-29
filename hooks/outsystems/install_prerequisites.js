var utils = require('./utilities');

console.log("Running hook to install push notifications pre-requisites");

module.exports = function (context) {
  var cordovaAbove8 = utils.isCordovaAbove(context, 8);
  if (cordovaAbove8) {
      var child_process = require('child_process'),
      deferral = require('q').defer();
  } else {
    var child_process = context.requireCordovaModule('child_process'),
    deferral = context.requireCordovaModule('q').defer();
  }


  var output = child_process.exec('npm install', {cwd: __dirname}, function (error) {
    if (error !== null) {
      console.log('exec error: ' + error);
      deferral.reject('npm installation failed');
    }
    else {
      deferral.resolve();
    }
  });

  return deferral.promise;
};
