cordova.define("com.plugin.br.cordova.contact.ContactCordovaPlugin", function(require, exports, module) {
var exec = require('cordova/exec');

exports.coolMethod = function (name,arg0, success, error) {
    exec(success, error, 'ContactCordovaPlugin', name, [arg0]);
};

});
