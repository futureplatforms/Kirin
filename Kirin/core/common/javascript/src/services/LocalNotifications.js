/*
   Copyright 2011 Future Platforms

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

defineServiceModule("LocalNotifications", function (require, exports) {
	
	var api = require("api-utils"),
		kirin = require("kirin"),
		backend;
	
	exports.onLoad = function (proxy) {
		backend = proxy;
	};
	
   	exports.onUnload = function () {
   		backend = null;
   	};
	
	exports.scheduleNotification = function (notificationConfig) {
		notificationConfig = api.normalizeAPI({
			'string': {
				mandatory: ['text']
			},
			
			'number': {
				mandatory: ['id', 'timeMillisSince1970']
			}
			
			/*
			'boolean': {
				defaults: {
						'vibrate': false,
						'sound': false
				}
			}
			*/
		}, notificationConfig );
		
		backend.scheduleNotification_atTime_withId_(notificationConfig.text, notificationConfig.timeMillisSince1970, notificationConfig.id);
	};
	
	exports.cancelNotification = function (id) {
		backend.cancelNotification_(id);
	};
	
	
});