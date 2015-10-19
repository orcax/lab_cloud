'use strict';

/**
 * @ngdoc service
 * @name prjApp.Generalservice
 * @description
 * # Generalservice
 * Service in the prjApp.
 */
angular.module('prjApp')
	.service('Generalservice', function Generalservice($http, $q, $rootScope,
		$location, $localStorage, $sessionStorage, inform, $anchorScroll) {

		var informConfig = {
			type: 'info',
			ttl: 3000,
		};
		var errorInformConfig = {
			ttl: 3000,
			type: 'danger'
		};

		this.getCurrentDate = function(){
			var d = new Date();
			d.setHours(0);
			d.setMinutes(0);
			d.setSeconds(1);
			return d;
		};

		this.cloneObject = function(obj) {
			if (null == obj || "object" != typeof obj) return obj;
			var copy = obj.constructor();
			for (var attr in obj) {
				if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
			}
			return copy;
		}

		this.toTop = function() {
			$location.hash('pageHeader');
			$anchorScroll();
		};

		this.getDataWrapper = function() {
			return {
				"callStatus": "",
				"errorCode": "",
				"token": "",
				"accountId": "",
				"data": {},
				"numPerPage": 0,
				"currPageNum": 0,
				"totalItemNum": 0,
				"totalPageNum": 0
			};
		};

		this.generalPost = function(url, data) {
			var deferred = $q.defer();
			$http.post(url, data)
				.success(function(data, status, headers, config) {
					deferred.resolve(data);
				})
				.error(function(data, status, headers, config) {
					deferred.reject(data);
				});
			return deferred.promise;
		};

		this.generalGet = function(url, data) {
			var deferred = $q.defer();
			$http.get(url, data)
				.success(function(data, status, headers, config) {
					deferred.resolve(data);
				})
				.error(function(data, status, headers, config) {
					deferred.reject(data);
				});
			return deferred.promise;
		};

		this.generalUpdate = function(url, data) {
			var deferred = $q.defer();
			$http.update(url, data)
				.success(function(data, status, headers, config) {
					deferred.resolve(data);
				})
				.error(function(data, status, headers, config) {
					deferred.reject(data);
				});
			return deferred.promise;
		};

		//提示错误
		this.informError = function(data) {
			inform.add(data, errorInformConfig);
		};
		//普通提示
		this.inform = function(data) {
			inform.add(data, informConfig);
		};

	});