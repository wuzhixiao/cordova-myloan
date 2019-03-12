cordova.define("com.plugin.br.cordova.googlemapplugin.GoogleMapPlugin", function(require, exports, module) {
var exec = require('cordova/exec');

exports.coolMethod = function (name,arg0, success, error) {
    exec(success, error, 'GoogleMapPlugin', name, arg0);
};

});
