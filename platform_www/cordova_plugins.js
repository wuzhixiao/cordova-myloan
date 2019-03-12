cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
  {
    "id": "cordova-plugin-splashscreen.SplashScreen",
    "file": "plugins/cordova-plugin-splashscreen/www/splashscreen.js",
    "pluginId": "cordova-plugin-splashscreen",
    "clobbers": [
      "navigator.splashscreen"
    ]
  },
  {
    "id": "cordova-plugin-ionic-webview.IonicWebView",
    "file": "plugins/cordova-plugin-ionic-webview/src/www/util.js",
    "pluginId": "cordova-plugin-ionic-webview",
    "clobbers": [
      "Ionic.WebView"
    ]
  },
  {
    "id": "cordova-clipboard.Clipboard",
    "file": "plugins/cordova-clipboard/www/clipboard.js",
    "pluginId": "cordova-clipboard",
    "clobbers": [
      "cordova.plugins.clipboard"
    ]
  },
  {
    "id": "cordova-plugin-contacts.contacts",
    "file": "plugins/cordova-plugin-contacts/www/contacts.js",
    "pluginId": "cordova-plugin-contacts",
    "clobbers": [
      "navigator.contacts"
    ]
  },
  {
    "id": "cordova-plugin-contacts.Contact",
    "file": "plugins/cordova-plugin-contacts/www/Contact.js",
    "pluginId": "cordova-plugin-contacts",
    "clobbers": [
      "Contact"
    ]
  },
  {
    "id": "cordova-plugin-contacts.convertUtils",
    "file": "plugins/cordova-plugin-contacts/www/convertUtils.js",
    "pluginId": "cordova-plugin-contacts"
  },
  {
    "id": "cordova-plugin-contacts.ContactAddress",
    "file": "plugins/cordova-plugin-contacts/www/ContactAddress.js",
    "pluginId": "cordova-plugin-contacts",
    "clobbers": [
      "ContactAddress"
    ]
  },
  {
    "id": "cordova-plugin-contacts.ContactError",
    "file": "plugins/cordova-plugin-contacts/www/ContactError.js",
    "pluginId": "cordova-plugin-contacts",
    "clobbers": [
      "ContactError"
    ]
  },
  {
    "id": "cordova-plugin-contacts.ContactField",
    "file": "plugins/cordova-plugin-contacts/www/ContactField.js",
    "pluginId": "cordova-plugin-contacts",
    "clobbers": [
      "ContactField"
    ]
  },
  {
    "id": "cordova-plugin-contacts.ContactFindOptions",
    "file": "plugins/cordova-plugin-contacts/www/ContactFindOptions.js",
    "pluginId": "cordova-plugin-contacts",
    "clobbers": [
      "ContactFindOptions"
    ]
  },
  {
    "id": "cordova-plugin-contacts.ContactName",
    "file": "plugins/cordova-plugin-contacts/www/ContactName.js",
    "pluginId": "cordova-plugin-contacts",
    "clobbers": [
      "ContactName"
    ]
  },
  {
    "id": "cordova-plugin-contacts.ContactOrganization",
    "file": "plugins/cordova-plugin-contacts/www/ContactOrganization.js",
    "pluginId": "cordova-plugin-contacts",
    "clobbers": [
      "ContactOrganization"
    ]
  },
  {
    "id": "cordova-plugin-contacts.ContactFieldType",
    "file": "plugins/cordova-plugin-contacts/www/ContactFieldType.js",
    "pluginId": "cordova-plugin-contacts",
    "merges": [
      ""
    ]
  },
  {
    "id": "cordova-plugin-ionic-keyboard.keyboard",
    "file": "plugins/cordova-plugin-ionic-keyboard/www/android/keyboard.js",
    "pluginId": "cordova-plugin-ionic-keyboard",
    "clobbers": [
      "window.Keyboard"
    ]
  },
  {
    "id": "cordova-plugin-android-permissions.Permissions",
    "file": "plugins/cordova-plugin-android-permissions/www/permissions.js",
    "pluginId": "cordova-plugin-android-permissions",
    "clobbers": [
      "cordova.plugins.permissions"
    ]
  },
  {
    "id": "cordova-plugin-nativestorage.mainHandle",
    "file": "plugins/cordova-plugin-nativestorage/www/mainHandle.js",
    "pluginId": "cordova-plugin-nativestorage",
    "clobbers": [
      "NativeStorage"
    ]
  },
  {
    "id": "cordova-plugin-nativestorage.LocalStorageHandle",
    "file": "plugins/cordova-plugin-nativestorage/www/LocalStorageHandle.js",
    "pluginId": "cordova-plugin-nativestorage"
  },
  {
    "id": "cordova-plugin-nativestorage.NativeStorageError",
    "file": "plugins/cordova-plugin-nativestorage/www/NativeStorageError.js",
    "pluginId": "cordova-plugin-nativestorage"
  },
  {
    "id": "cordova-plugin-appversion.RareloopAppVersion",
    "file": "plugins/cordova-plugin-appversion/www/app-version.js",
    "pluginId": "cordova-plugin-appversion",
    "clobbers": [
      "AppVersion"
    ]
  },
  {
    "id": "cordova-plugin-getuisdk.GeTuiSdkPlugin",
    "file": "plugins/cordova-plugin-getuisdk/android/www/GeTuiSdk.js",
    "pluginId": "cordova-plugin-getuisdk",
    "clobbers": [
      "GeTuiSdkPlugin"
    ]
  },
  {
    "id": "com.vaenow.appupdate.CheckAppUpdate",
    "file": "plugins/com.vaenow.appupdate/www/CheckAppUpdate.js",
    "pluginId": "com.vaenow.appupdate",
    "clobbers": [
      "cordova.plugins.CheckAppUpdate"
    ]
  },
  {
    "id": "com.plugin.br.cordova.pkg.PackageCordovaPlugin",
    "file": "plugins/com.plugin.br.cordova.pkg/www/PackageCordovaPlugin.js",
    "pluginId": "com.plugin.br.cordova.pkg",
    "clobbers": [
      "cordova.plugins.PackageCordovaPlugin"
    ]
  },
  {
    "id": "com.plugin.br.cordova.CommUtil",
    "file": "plugins/com.plugin.br.cordova/www/CommUtil.js",
    "pluginId": "com.plugin.br.cordova",
    "clobbers": [
      "cordova.plugins.CommUtil"
    ]
  },
  {
    "id": "com.plugin.br.cordova.contact.ContactCordovaPlugin",
    "file": "plugins/com.plugin.br.cordova.contact/www/ContactCordovaPlugin.js",
    "pluginId": "com.plugin.br.cordova.contact",
    "clobbers": [
      "cordova.plugins.ContactCordovaPlugin"
    ]
  },
  {
    "id": "com.plugin.br.cordova.entry.EntryCordovaPlugin",
    "file": "plugins/com.plugin.br.cordova.entry/www/EntryCordovaPlugin.js",
    "pluginId": "com.plugin.br.cordova.entry",
    "clobbers": [
      "cordova.plugins.EntryCordovaPlugin"
    ]
  },
  {
    "id": "com.plugin.br.cordova.face.FacePlugin",
    "file": "plugins/com.plugin.br.cordova.face/www/FacePlugin.js",
    "pluginId": "com.plugin.br.cordova.face",
    "clobbers": [
      "cordova.plugins.FacePlugin"
    ]
  },
  {
    "id": "com.plugin.br.cordova.appsfyler.AppsFlyerPlugin",
    "file": "plugins/com.plugin.br.cordova.appsfyler/www/AppsFlyerPlugin.js",
    "pluginId": "com.plugin.br.cordova.appsfyler",
    "clobbers": [
      "cordova.plugins.AppsFlyerPlugin"
    ]
  },
  {
    "id": "com.plugin.br.cordova.device.DeviceBr",
    "file": "plugins/com.plugin.br.cordova.device/www/DeviceBr.js",
    "pluginId": "com.plugin.br.cordova.device",
    "clobbers": [
      "cordova.plugins.DeviceBr"
    ]
  },
  {
    "id": "cordova-plugin-camera.Camera",
    "file": "plugins/cordova-plugin-camera/www/CameraConstants.js",
    "pluginId": "cordova-plugin-camera",
    "clobbers": [
      "Camera"
    ]
  },
  {
    "id": "cordova-plugin-camera.CameraPopoverOptions",
    "file": "plugins/cordova-plugin-camera/www/CameraPopoverOptions.js",
    "pluginId": "cordova-plugin-camera",
    "clobbers": [
      "CameraPopoverOptions"
    ]
  },
  {
    "id": "cordova-plugin-camera.camera",
    "file": "plugins/cordova-plugin-camera/www/Camera.js",
    "pluginId": "cordova-plugin-camera",
    "clobbers": [
      "navigator.camera"
    ]
  },
  {
    "id": "cordova-plugin-camera.CameraPopoverHandle",
    "file": "plugins/cordova-plugin-camera/www/CameraPopoverHandle.js",
    "pluginId": "cordova-plugin-camera",
    "clobbers": [
      "CameraPopoverHandle"
    ]
  },
  {
    "id": "com.plugin.br.cordova.googlemapplugin.GoogleMapPlugin",
    "file": "plugins/com.plugin.br.cordova.googlemapplugin/www/GoogleMapPlugin.js",
    "pluginId": "com.plugin.br.cordova.googlemapplugin",
    "clobbers": [
      "cordova.plugins.GoogleMapPlugin"
    ]
  }
];
module.exports.metadata = 
// TOP OF METADATA
{
  "cordova-plugin-whitelist": "1.3.3",
  "cordova-plugin-splashscreen": "5.0.2",
  "cordova-plugin-ionic-webview": "2.2.0",
  "cordova-clipboard": "1.2.1",
  "cordova-plugin-contacts": "3.0.1",
  "cordova-plugin-ionic-keyboard": "2.1.3",
  "cordova-plugin-android-permissions": "1.0.0",
  "cordova-plugin-nativestorage": "2.3.2",
  "cordova-plugin-appversion": "1.0.0",
  "cordova-plugin-getuisdk": "1.0.9",
  "com.vaenow.appupdate": "1",
  "com.plugin.br.cordova.pkg": "1.0.0",
  "com.plugin.br.cordova": "1.0.0",
  "com.plugin.br.cordova.contact": "1.0.0",
  "com.plugin.br.cordova.entry": "1.0.0",
  "com.plugin.br.cordova.face": "1.0.0",
  "com.plugin.br.cordova.appsfyler": "1",
  "com.plugin.br.cordova.device": "1.0.0",
  "cordova-plugin-camera": "4.0.3",
  "com.plugin.br.cordova.googlemapplugin": "1.0.0"
};
// BOTTOM OF METADATA
});