import Main from '@/views/Main.vue'

// 不作为Main组件的子页面展示的页面单独写，如下
export const loginRouter = {
  path: '/login',
  name: 'login',
  meta: {
    title: '研究所办公小助手 - 登录'
  },
  component: () => import('@/views/login.vue')
}

export const page404 = {
  path: '/*',
  name: 'error-404',
  meta: {
    title: '404-页面不存在'
  },
  component: () => import('@/views/error-page/404.vue')
}

export const page403 = {
  path: '/403',
  meta: {
    title: '403-权限不足'
  },
  name: 'error-403',
  component: () => import('@//views/error-page/403.vue')
}

export const page500 = {
  path: '/500',
  meta: {
    title: '500-服务端错误'
  },
  name: 'error-500',
  component: () => import('@/views/error-page/500.vue')
}

export const preview = {
  path: '/preview/:audityId/:operatorId',
  name: 'preview',
  component: () => import('@/views/prplat/audity-h5.vue')
}

export const locking = {
  path: '/locking',
  name: 'locking',
  component: () => import('@/views/main-components/lockscreen/components/locking-page.vue')
}

// 作为Main组件的子页面展示但是不在左侧菜单显示的路由写在otherRouter里
export const otherRouter = {
  path: '/',
  name: 'otherRouter',
  redirect: '/home',
  component: Main,
  children: [
    //{ path: 'home', title: {i18n: 'home'}, name: 'home_index', component: () => import('@/views/home/home.vue') },
    {
      path: 'ownspace',
      title: '个人中心',
      name: 'ownspace_index',
      component: () => import('@/views/own-space/own-space.vue')
    },
    {
      path: 'order/:order_id',
      title: '订单详情',
      name: 'order-info',
      component: () => import('@/views/advanced-router/component/order-info.vue')
    }, // 用于展示动态路由
    {
      path: 'shopping',
      title: '购物详情',
      name: 'shopping',
      component: () => import('@/views/advanced-router/component/shopping-info.vue')
    }, // 用于展示带参路由
    {path: 'message', title: '消息中心', name: 'message_index', component: () => import('@/views/message/message.vue')},
    // 用于展示带参路由
    {
      path: 'coop/:coopId',
      title: '合作记录',
      name: 'coop-manage',
      icon: 'man',
      component: () => import('@/views/coop/coop-manage.vue')
    },

  ]
}

// 作为Main组件的子页面展示并且在左侧菜单显示的路由写在appRouter里
export const appRouter = [
  {
    path: '/home',
    icon: 'ios-grid-view',
    name: 'pr-index',
    title: '首页',
    component: Main,
    children: [
      {path: '', title: {i18n: 'home'}, name: 'home_index', component: () => import('@/views/home/home.vue')},
    ]
  }, {
    path: '/in',
    icon: 'ios-grid-view',
    name: 'in-manage',
    title: '消息与通知',
    component: Main,
    children: [
      {
        path: 'inform',
        access: '5',
        title: '消息管理',
        name: 'inform-manage',
        icon: 'clipboard',
        component: () => import('@/views/prplat/inform-manage.vue')
      },
      {
        path: 'notice',
        access: '6',
        title: '通知管理',
        name: 'notice-manage',
        icon: 'soup-can',
        component: () => import('@/views/prplat/notice-manage.vue')
      },

    ]
  }, {
    path: '/ga',
    icon: 'ios-grid-view',
    name: 'ga-manage',
    title: '综合业务',
    component: Main,
    children: [
      {
        path: 'activity',
        access: '7',
        title: '活动征集',
        name: 'activity-manage',
        icon: 'person',
        component: () => import('@/views/prplat/activity-manage.vue')
      },
      {
        path: 'vote',
        access: '8',
        title: '投票管理',
        name: 'vote-manage',
        icon: 'soup-can',
        component: () => import('@/views/prplat/vote-manage.vue')
      },
      {
        path: 'remind',
        access: '11',
        title: '流程提醒',
        name: 'remind-manage',
        icon: 'person',
        component: () => import('@/views/prplat/remind-manage.vue')
      }, {
        path: 'audity',
        access: '12',
        title: '信息审批',
        name: 'audity-manage',
        icon: 'soup-can',
        component: () => import('@/views/prplat/audity-manage.vue')
      },
      {
        path: 'audity_commit',
        access: '20',
        title: '提交审批',
        name: 'audity-commit',
        icon: 'soup-can',
        component: () => import('@/views/prplat/audity-commit.vue')
      },
      {
        path: 'card',
        access: '15',
        name: 'card-manage',
        title: '名片管理',
        icon: 'person',
        component: () => import('@/views/prplat/card-manage.vue')
      },
    ]
  }, {
    path: '/general',
    icon: 'ios-grid-view',
    name: 'general-manage',
    title: '信息配置',
    component: Main,
    children: [
      {
        path: 'file',
        access: '16',
        name: 'file-manage',
        title: '文件库',
        icon: 'person',
        component: () => import('@/views/prplat/file-manage.vue')
      },
      {
        path: 'meeting',
        access: '17',
        name: 'meeting-manager',
        title: '晨会',
        icon: 'person',
        component: () => import('@/views/prplat/meeting-manage.vue')
      },
      {
        path: 'group',
        access: '18',
        name: 'group-manage',
        title: '用户分组',
        icon: 'person',
        component: () => import('@/views/prplat/group-manage.vue')
      },

    ]
  },

  {
    path: '/system',
    icon: 'ios-grid-view',
    name: 'system',
    title: '系统管理',
    component: Main,
    children: [
      {
        path: 'account',
        access: '14',
        title: '账户管理',
        name: 'account-manage',
        icon: 'person',
        component: () => import('@/views/system/account-manage.vue')
      },
      {
        path: 'department',
        access: '9',
        title: '机构管理',
        name: 'department-manage',
        icon: 'soup-can',
        component: () => import('@/views/system/department-manage.vue')
      },
      {
        path: 'role',
        access: '10',
        title: '角色管理',
        name: 'role-manage',
        icon: 'man',
        component: () => import('@/views/system/role-manage.vue')
      },
      {
        path: 'log',
        access: '13',
        title: '日志管理',
        name: 'log-manage',
        icon: 'clipboard',
        component: () => import('@/views/system/log-manage.vue')
      },
    ]
  },
]

// 所有上面定义的路由都要写在下面的routers里
export const routers = [
  loginRouter,
  otherRouter,
  preview,
  locking,
  ...appRouter,
  page500,
  page403,
  page404
]
