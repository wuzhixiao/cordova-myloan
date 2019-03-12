cordova.define("com.plugin.br.cordova.entry.EntryCordovaPlugin", function(require, exports, module) {
var exec = require('cordova/exec');

exports.coolMethod = function (name , arg0, success, error) {
    exec(success, error, 'EntryCordovaPlugin', name , arg0);
};

});
