cordova.define("com.plugin.br.cordova.CommUtil", function(require, exports, module) {
var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'CommUtil', 'coolMethod', [arg0]);
};

});
