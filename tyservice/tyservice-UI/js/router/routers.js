define(['app'], function (app) {
    app.config(['$stateProvider', '$urlRouterProvider', 'appInterface', function ($stateProvider, $urlRouterProvider, appInterface) {
        var headTemplate = {
            templateUrl: '/view/head.html?v=' + appInterface.version,
            controllerUrl: 'controller/headCtrl',
            controller: 'headCtrl'
        };
        var dataTemplate = {
            templateUrl: '/view/data.html?v=' + appInterface.version
        };
        var dataGridTemplate = {
            templateUrl: '/view/dataGrid.html?v=' + appInterface.version
        };
        var dataListTemplate = {
            templateUrl: '/view/dataList.html?v=' + appInterface.version
        };
        $urlRouterProvider.otherwise('login');
        $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: '/view/login.html?v=' + appInterface.version,
                controllerUrl: 'controller/loginCtrl',
                controller: 'loginCtrl',
                isLogin: false
            })
            .state('master', {
                abstract: true,
                url: '/master',
                templateUrl: '/view/master.html?v=' + appInterface.version,
                controllerUrl: 'controller/masterCtrl',
                controller: 'masterCtrl'
            })
            .state('master.home', {
                url: '/home?from',
                views: {
                    'main': {
                        templateUrl: '/view/home.html?v=' + appInterface.version,
                        controllerUrl: 'controller/homeCtrl',
                        controller: 'homeCtrl'
                    },
                    'head': headTemplate
                },
                isLogin: true
            })
            .state('master.home.mycust', {
                url: '/mycust',
                views: {
                    'home-main': {
                        templateUrl: '/view/homeMyCust.html?v=' + appInterface.version,
                        controllerUrl: 'controller/homeMyCustCtrl',
                        controller: 'homeMyCustCtrl'
                    }
                },
                isLogin: true,
                menuName: '首页'
            })
            .state('master.home.cust', {
                url: '/cust?index',
                views: {
                    'home-main': {
                        // templateUrl: dataTemplate.templateUrl,
                        // controllerUrl: 'controller/homeCustCtrl',
                        // controller: 'homeCustCtrl'
                        templateUrl: '/view/customer.html?v=' + appInterface.version,
                        controllerUrl: 'controller/customerCtrl',
                        controller: 'customerCtrl'
                    }
                },
                isLogin: true,
                menuName: '首页'
            })
            .state('master.home.activity', {
                url: '/activity',
                views: {
                    'home-main': {
                        templateUrl: '/view/homeActivity.html?v=' + appInterface.version,
                        controllerUrl: 'controller/homeActivityCtrl',
                        controller: 'homeActivityCtrl'
                    }
                },
                isLogin: true,
                menuName: '客户活动记录'
            })
            .state('master.home.schedule', {
                url: '/schedule',
                views: {
                    'home-main': {
                        templateUrl: '/view/homeSchedule.html?v=' + appInterface.version,
                        controllerUrl: 'controller/homeScheduleCtrl',
                        controller: 'homeScheduleCtrl'
                    }
                },
                isLogin: true,
                menuName: '活动日程表'
            })
            .state('master.system', {
                url: '/system',
                views: {
                    'main': {
                        templateUrl: '/view/system.html?v=' + appInterface.version,
                        controllerUrl: 'controller/systemCtrl',
                        controller: 'systemCtrl'
                    }
                },
                isLogin: true,
                menuName: '系统管理'
            })
            .state('master.system.person', {
                url: '/person',
                views: {
                    'system-main': {
                        templateUrl: '/view/person.html?v=' + appInterface.version,
                        controllerUrl: 'controller/personCtrl',
                        controller: 'personCtrl'
                    },
                    'system-head': headTemplate,
                    'system-data': dataTemplate
                },
                isLogin: true,
                menuName: '账户管理'
            })
            .state('master.system.department', {
                url: '/department',
                views: {
                    'system-main': {
                        templateUrl: '/view/department.html?v=' + appInterface.version,
                        controllerUrl: 'controller/departmentCtrl',
                        controller: 'departmentCtrl'
                    },
                    'system-head': headTemplate,
                    'system-data': dataGridTemplate
                },
                isLogin: true,
                menuName: '部门管理'
            })
            .state('master.system.role', {
                url: '/role',
                views: {
                    'system-main': {
                        templateUrl: '/view/role.html?v=' + appInterface.version,
                        controllerUrl: 'controller/roleCtrl',
                        controller: 'roleCtrl'
                    },
                    'system-head': headTemplate,
                    'system-data': dataTemplate
                },
                isLogin: true,
                menuName: '角色管理'
            })
            .state('master.system.session', {
                url: '/session',
                views: {
                    'system-main': {
                        templateUrl: '/view/session.html?v=' + appInterface.version,
                        controllerUrl: 'controller/sessionCtrl',
                        controller: 'sessionCtrl'
                    },
                    'system-head': headTemplate,
                    'system-data': dataTemplate
                },
                isLogin: true,
                menuName: '会话管理'
            })
            .state('master.system.dictionary', {
                url: '/dictionary',
                views: {
                    'system-main': {
                        templateUrl: '/view/dictionary.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dictionaryCtrl',
                        controller: 'dictionaryCtrl'
                    },
                    'system-head': headTemplate,
                    'system-data': dataTemplate
                },
                isLogin: true,
                menuName: '字典管理'
            })
            .state('master.system.log', {
                url: '/log',
                views: {
                    'system-main': {
                        templateUrl: '/view/log.html?v=' + appInterface.version,
                        controllerUrl: 'controller/logCtrl',
                        controller: 'logCtrl'
                    },
                    'system-head': headTemplate,
                    'system-data': dataTemplate
                },
                isLogin: true,
                menuName: '日志管理'
            })
            .state('master.institutional', {
                url: '/institutional?from&index&flag',
                views: {
                    'main': {
                        templateUrl: '/view/institutional.html?v=' + appInterface.version,
                        controllerUrl: 'controller/institutionalCtrl',
                        controller: 'institutionalCtrl'
                    },
                    'head': headTemplate
                },
                isLogin: true,
                menuName: '机构管理'
            })
            .state('master.institutionaldetail', {
                url: '/institutionaldetail?id&index&from',
                views: {
                    'main': {
                        templateUrl: '/view/institutionalDetail.html?v=' + appInterface.version,
                        controllerUrl: 'controller/institutionalDetailCtrl',
                        controller: 'institutionalDetailCtrl'
                    },
                    'head': headTemplate
                },
                isLogin: true,
                menuName: '机构详情'
            })
            .state('master.institutionaldetail.cust', {
                url: '/cust?custId',
                views: {
                    'inst-main': {
                        templateUrl: '/view/portraitSearch.html?v=' + appInterface.version,
                        controllerUrl: 'controller/portraitSearchCtrl',
                        controller: 'portraitSearchCtrl'
                    },
                    'inst-data': dataListTemplate
                },
                isLogin: true,
                menuName: '机构详情'
            })
            .state('master.customer', {
                url: '/customer?from&index',
                views: {
                    'main': {
                        templateUrl: '/view/customer.html?v=' + appInterface.version,
                        controllerUrl: 'controller/customerCtrl',
                        controller: 'customerCtrl'
                    },
                    'head': headTemplate
                },
                isLogin: true,
                menuName: '客户管理'
            })
            .state('master.customeractivitysign', {
                url: '/customersign',
                views: {
                    'main': {
                        templateUrl: '/view/customerActivitySign.html?v=' + appInterface.version,
                        controllerUrl: 'controller/customerActivitySignCtrl',
                        controller: 'customerActivitySignCtrl'
                    },
                    'head': headTemplate,
                    'data': dataTemplate
                },
                isLogin: true,
                menuName: '客户服务记录管理'
            })
            .state('master.meetingcust', {
                url: '/meetingcust',
                views: {
                    'main': {
                        templateUrl: '/view/meetingCustomer.html?v=' + appInterface.version,
                        controllerUrl: 'controller/meetingCustomerCtrl',
                        controller: 'meetingCustomerCtrl'
                    },
                    'head': headTemplate,
                    'data': dataTemplate
                },
                isLogin: true,
                menuName: '客户信息 > 潜在客户审核'
            })
            .state('master.preinst', {
                url: '/preinst',
                views: {
                    'main': {
                        templateUrl: '/view/preInstitutional.html?v=' + appInterface.version,
                        controllerUrl: 'controller/preInstitutionalCtrl',
                        controller: 'preInstitutionalCtrl'
                    },
                    'head': headTemplate,
                    'data': dataTemplate
                },
                isLogin: true,
                menuName: '机构信息 > 潜在机构审核'
            })
            .state('master.portrait', {
                abstract: true,
                url: '/portrait?from',
                views: {
                    'main': {
                        templateUrl: '/view/portrait.html?v=' + appInterface.version,
                        controllerUrl: 'controller/portraitCtrl',
                        controller: 'portraitCtrl'
                    }
                },
                isLogin: true,
                menuName: '标签管理'
            })
            .state('master.portrait.overview', {
                url: '/overview',
                // views: {
                //     'portrait-main': {
                //         templateUrl: '/view/portraitOverview.html?v=' + appInterface.version,
                //         controllerUrl: 'controller/portraitOverviewCtrl',
                //         controller: 'portraitOverviewCtrl'
                //     },
                //     'portrait-head': headTemplate
                // },
                // isLogin: true,
                // menuName: '总览'
                views: {
                    'portrait-main': {
                        templateUrl: '/view/portraitTag.html?v=' + appInterface.version,
                        controllerUrl: 'controller/portraitTagCtrl',
                        controller: 'portraitTagCtrl'
                    },
                    'portrait-head': headTemplate
                },
                isLogin: true,
                menuName: '标签管理'
            })
            .state('master.portrait.search', {
                url: '/search?custId&index',
                views: {
                    'portrait-main': {
                        templateUrl: '/view/portraitSearch.html?v=' + appInterface.version,
                        controllerUrl: 'controller/portraitSearchCtrl',
                        controller: 'portraitSearchCtrl'
                    },
                    'portrait-head': headTemplate,
                    'portrait-data': dataListTemplate
                },
                isLogin: true,
                menuName: '客户详情'
            })
            .state('master.resource', {
                abstract: true,
                url: '/resource?from',
                views: {
                    'main': {
                        templateUrl: '/view/resource.html?v=' + appInterface.version,
                        controllerUrl: 'controller/resourceCtrl',
                        controller: 'resourceCtrl'
                    },
                    'head': headTemplate
                },
                isLogin: true,
                menuName: '资源库管理'
            })
            .state('master.resource.company', {
                url: '/company',
                views: {
                    'resource-main': {
                        templateUrl: '/view/company.html?v=' + appInterface.version,
                        controllerUrl: 'controller/companyCtrl',
                        controller: 'companyCtrl'
                    },
                    'resource-data': dataTemplate
                },
                isLogin: true,
                menuName: '资源库管理'
            })
            .state('master.resource.expert', {
                url: '/expert',
                views: {
                    'resource-main': {
                        templateUrl: '/view/expert.html?v=' + appInterface.version,
                        controllerUrl: 'controller/expertCtrl',
                        controller: 'expertCtrl'
                    },
                    'resource-data': dataTemplate
                },
                isLogin: true,
                menuName: '资源库管理'
            })
            .state('master.resource.project', {
                url: '/project?id&index',
                views: {
                    'resource-main': {
                        templateUrl: '/view/project.html?v=' + appInterface.version,
                        controllerUrl: 'controller/projectCtrl',
                        controller: 'projectCtrl'
                    },
                    'resource-data': dataTemplate
                },
                isLogin: true,
                menuName: '资源库管理'
            })
            .state('master.staff', {
                url: '/staff?from',
                views: {
                    'main': {
                        templateUrl: '/view/staff.html?v=' + appInterface.version,
                        controllerUrl: 'controller/staffCtrl',
                        controller: 'staffCtrl'
                    },
                    'head': headTemplate,
                    'data': dataTemplate
                },
                isLogin: true,
                menuName: '人员信息管理'
            })
            .state('master.audit', {
                url: '/audit?from',
                views: {
                    'main': {
                        templateUrl: '/view/audit.html?v=' + appInterface.version,
                        controllerUrl: 'controller/auditCtrl',
                        controller: 'auditCtrl'
                    },
                    'head': headTemplate,
                    'data': dataTemplate
                },
                isLogin: true,
                menuName: '消息审核'
            })
            .state('master.send', {
                url: '/send?from&index',
                views: {
                    'main': {
                        templateUrl: '/view/send.html?v=' + appInterface.version,
                        controllerUrl: 'controller/sendCtrl',
                        controller: 'sendCtrl'
                    },
                    'head': headTemplate,
                    'data': dataTemplate
                },
                isLogin: true,
                menuName: '发送日志'
            })
            .state('master.template', {
                url: '/template',
                views: {
                    'main': {
                        templateUrl: '/view/template.html?v=' + appInterface.version,
                        controllerUrl: 'controller/templateCtrl',
                        controller: 'templateCtrl'
                    },
                    'head': headTemplate
                },
                isLogin: true,
                menuName: '消息模板管理'
            })
            .state('master.sendloginfo', {
                url: '/sendloginfo?id&index',
                views: {
                    'main': {
                        templateUrl: '/view/sendLogInfo.html?v=' + appInterface.version,
                        controllerUrl: 'controller/sendLogInfoCtrl',
                        controller: 'sendLogInfoCtrl'
                    },
                    'head': headTemplate,
                    'data': dataTemplate
                },
                isLogin: true,
                menuName: '发送日志 > 日志详情'
            })
            .state('master.datamanager', {
                url: '/datamanager',
                views: {
                    'main': {
                        templateUrl: '/view/dataManager.html?v=' + appInterface.version,
                        controllerUrl: 'controller/systemCtrl',
                        controller: 'systemCtrl'
                    }
                },
                isLogin: true,
                menuName: '客户服务数据管理'
            })
            .state('master.datamanager.statement', {
                url: '/statement?index&stock',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerStatement.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerStatementCtrl',
                        controller: 'dataManagerStatementCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataTemplate
                },
                isLogin: true,
                menuName: '晨会发言'
            })
            .state('master.datamanager.delegate', {
                url: '/delegate',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerDelegate.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerDelegateCtrl',
                        controller: 'dataManagerDelegateCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataTemplate
                },
                isLogin: true,
                menuName: '委托课题'
            })
            .state('master.datamanager.salon', {
                url: '/salon?index&customer',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerSalon.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerSalonCtrl',
                        controller: 'dataManagerSalonCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataGridTemplate
                },
                isLogin: true,
                menuName: '沙龙管理'
            })
            .state('master.datamanager.meeting', {
                url: '/meeting?index&customer',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerMeeting.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerMeetingCtrl',
                        controller: 'dataManagerMeetingCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataTemplate
                },
                isLogin: true,
                menuName: '电话会议'
            })
            .state('master.datamanager.language', {
                url: '/language?index&stock',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerLanguage.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerLanguageCtrl',
                        controller: 'dataManagerLanguageCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataTemplate
                },
                isLogin: true,
                menuName: '语音微路演'
            })
            .state('master.datamanager.research', {
                url: '/research?index&customer',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerResearch.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerResearchCtrl',
                        controller: 'dataManagerResearchCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataTemplate
                },
                isLogin: true,
                menuName: '调研'
            })
            .state('master.datamanager.roadshow', {
                url: '/roadshow?index&customer',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerRoadshow.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerRoadshowCtrl',
                        controller: 'dataManagerRoadshowCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataTemplate
                },
                isLogin: true,
                menuName: '路演反路演'
            })
            .state('master.datamanager.other', {
                url: '/other?index&customer',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerOther.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerOtherCtrl',
                        controller: 'dataManagerOtherCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataTemplate
                },
                isLogin: true,
                menuName: '其他'
            })
            .state('master.datamanager.meal', {
                url: '/meal?index&customer',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerMeal.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerMealCtrl',
                        controller: 'dataManagerMealCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataTemplate
                },
                isLogin: true,
                menuName: '午/晚餐交流'
            })
            .state('master.datamanager.myservice', {
                url: '/myservice?index&customer',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerMyService.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerMyServiceCtrl',
                        controller: 'dataManagerMyServiceCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataTemplate
                },
                isLogin: true,
                menuName: '专项服务'
            })
            .state('master.datamanager.stock', {
                url: '/stock?id&index&model',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerStock.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerStockCtrl',
                        controller: 'dataManagerStockCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataTemplate
                },
                isLogin: true,
                menuName: '股票推荐'
            })
            .state('master.datamanager.customer', {
                url: '/customer?id&index&model',
                views: {
                    'datamanager-main': {
                        templateUrl: '/view/dataManagerCustomer.html?v=' + appInterface.version,
                        controllerUrl: 'controller/dataManagerCustomerCtrl',
                        controller: 'dataManagerCustomerCtrl'
                    },
                    'datamanager-head': headTemplate,
                    'datamanager-data': dataTemplate
                },
                isLogin: true,
                menuName: '参与客户'
            })
            .state('master.resource.customer', {
                url: '/customer?id&index',
                views: {
                    'resource-main': {
                        templateUrl: '/view/projectCustomer.html?v=' + appInterface.version,
                        controllerUrl: 'controller/projectCustomerCtrl',
                        controller: 'projectCustomerCtrl'
                    },
                    'resource-data': dataTemplate
                },
                isLogin: true,
                menuName: '项目库 > 参与客户'
            })
            .state('master.resource.pjcompany', {
                url: '/pjcompany?id&index',
                views: {
                    'resource-main': {
                        templateUrl: '/view/projectCompany.html?v=' + appInterface.version,
                        controllerUrl: 'controller/projectCompanyCtrl',
                        controller: 'projectCompanyCtrl'
                    },
                    'resource-data': dataTemplate
                },
                isLogin: true,
                menuName: '项目库 > 参与上市公司'
            })
            .state('master.resource.pjexpert', {
                url: '/pjexpert?id&index',
                views: {
                    'resource-main': {
                        templateUrl: '/view/projectExpert.html?v=' + appInterface.version,
                        controllerUrl: 'controller/projectExpertCtrl',
                        controller: 'projectExpertCtrl'
                    },
                    'resource-data': dataTemplate
                },
                isLogin: true,
                menuName: '项目库 > 参与专家'
            })
            .state('master.projectdetail', {
                abstract: true,
                url: '/projectdetail?id&index',
                views: {
                    'main': {
                        templateUrl: '/view/projectDetail.html?v=' + appInterface.version,
                        controllerUrl: 'controller/projectDetailCtrl',
                        controller: 'projectDetailCtrl'
                    },
                    'head': headTemplate
                },
                isLogin: true,
                menuName: '项目库'
            })
            .state('master.projectdetail.jour', {
                url: '/jour',
                views: {
                    'detail-data': {
                        templateUrl: dataTemplate.templateUrl,
                        controllerUrl: 'controller/jourCtrl',
                        controller: 'jourCtrl'
                    }
                },
                isLogin: true,
                menuName: '项目库详情'
            })
            .state('master.projectdetail.track', {
                url: '/track',
                views: {
                    'detail-main': {
                        controllerUrl: 'controller/trackCtrl',
                        controller: 'trackCtrl'
                    },
                    'detail-data': dataTemplate
                },
                isLogin: true,
                menuName: '项目库详情'
            })
    }]);
});
