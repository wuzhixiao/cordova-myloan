cordova.define("com.plugin.br.cordova.face.FacePlugin", function(require, exports, module) {
var exec = require('cordova/exec');

exports.coolMethod = function (name,arg0, success, error) {
    exec(success, error, 'FacePlugin', name, [arg0]);
};

});
