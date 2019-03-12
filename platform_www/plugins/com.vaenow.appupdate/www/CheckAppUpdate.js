cordova.define("com.vaenow.appupdate.CheckAppUpdate", function(require, exports, module) {
var exec = require('cordova/exec');

exports.coolMethod = function (name , arg0, success, error) {
    exec(success, error, 'CheckAppUpdate', name , arg0);
};
});
