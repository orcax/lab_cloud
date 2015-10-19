'use strict';

/**
 * @ngdoc directive
 * @name prjApp.directive:labLoginForm
 * @description
 * # labLoginForm
 */
angular.module('prjApp')
    .directive('labLoginForm', function () {
        return {
            restrict: 'E',
            controller: function (LoginService, $location) {
                this.saveCurrentUser = function (user, password, isKeepLogin) {
                    LoginService.saveCurrentUser(user, password, isKeepLogin);
                };

                this.login = function (userName, userPWD) {
                    alert(userName);
                    $location.path('/');
                };
            },
            link: function postLink(scope, element, attrs, controller) {
                var loginBtn = element.find('.login-btn');

                var userName = element.find('input[name="userName"]');
                var userPWD = element.find('input[name="userPWD"]');


                loginBtn.click(function () {
                    clearInputError();
                    var result = checkInputNull(userName, userPWD);
                    if (result) {
                        controller.saveCurrentUser(scope.userName, scope.userPWD, true);
                    }
                });

                function clearInputError() {
                    element.find('div span.red').text('');
                }

                function checkInputNull(userName, userPWD) {
                    var result = true;
                    if (userName.val() == null || userName.val() == '' ||
                        userPWD.val() == null || userPWD.val() == '') {
                        element.find('div span.red').text('登录名或密码不能为空');
                        result = false;
                    }
                    return result;
                }
            }
        };
    });
