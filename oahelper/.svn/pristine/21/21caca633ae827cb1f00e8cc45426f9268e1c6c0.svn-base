<style lang="less">
    @import "./main.less";
</style>
<template>
    <div class="main" :class="{'main-hide-text': shrink}" >
        <!--<div class="sidebar-menu-con" :style="{width: shrink?'60px':'200px', overflow: shrink ? 'visible' : 'auto'}">-->

        <!--</div>-->
        <!--:style="{paddingLeft: shrink?'60px':'200px'}"-->
        <div class="main-header-con" >
            <div class="main-header">
                <div class="navicon-con" style="padding-left: 100px;padding-top:10px;">
                    <img style="width: 100%;height: 100%;" src="../images/logo.png"/>
                </div>
                <!--<div class="header-middle-con">-->
                    <!--<div class="main-breadcrumb">-->
                        <!--<breadcrumb-nav :currentPath="currentPath"></breadcrumb-nav>-->
                    <!--</div>-->
                <!--</div>-->
                <div class="header-avator-con">
                    <div>
                        <!--<full-screen v-model="isFullScreen" @on-change="fullscreenChange"></full-screen>-->
                        <!--<lock-screen></lock-screen>-->
                        <!--<message-tip v-model="mesCount"></message-tip>-->
                        <!--<theme-switch></theme-switch>-->

                        <div class="user-dropdown-menu-con">
                            <Row type="flex" justify="end" align="middle" class="user-dropdown-innercon">
                                <Dropdown transfer trigger="click" @on-click="handleClickUserDropdown">
                                    <a href="javascript:void(0)">
                                        <span class="main-user-name">{{ userName }}</span>
                                        <Icon type="arrow-down-b"></Icon>
                                    </a>
                                    <DropdownMenu slot="list">
                                        <DropdownItem name="ownSpace">个人中心</DropdownItem>
                                        <DropdownItem name="loginout" divided>退出登录</DropdownItem>
                                    </DropdownMenu>
                                </Dropdown>
                                <!--<Avatar :src="avatorPath" style="background: #619fe7;margin-left: 10px;"></Avatar>-->
                            </Row>
                        </div>
                    </div>
                </div>

            </div>
            <!--<div class="tags-con">-->
                <!--<tags-page-opened :pageTagsList="pageTagsList"></tags-page-opened>-->
            <!--</div>-->
            <div class="menus-con">
                <Menu mode="horizontal" ref="sideMenu" :active-name="$route.name"  width="auto" @on-select="changeMenu" :theme="theme1" style="background-color: #213351;position: static;"><!-- -->
                    <template v-for="item in menuList">
                        <MenuItem v-if="item.children.length<=1" :name="item.children[0].name" :key="'menuitem' + item.name">
                            <!--<Icon :type="item.children[0].icon || item.icon"  :key="'menuicon' + item.name"></Icon>-->
                            <span :key="'title' + item.name">{{ itemTitle(item.children[0]) }}</span>
                        </MenuItem>
                        <Submenu v-if="item.children.length > 1" :name="item.name" :key="item.name">
                            <template slot="title">
                                <!--<Icon :type="item.icon" ></Icon>-->
                                <span >{{ itemTitle(item) }}</span>
                            </template>
                            <template v-for="child in item.children">
                                <MenuItem :name="child.name" :key="'menuitem' + child.name">
                                    <Icon :type="child.icon"  :key="'icon' + child.name"></Icon>
                                    <span :key="'title' + child.name">{{ itemTitle(child) }}</span>
                                </MenuItem>
                            </template>
                        </Submenu>
                    </template>
                </Menu>
            </div>
        </div>
        <!--:style="{left: shrink?'60px':'200px'}"-->
        <div class="single-page-con" >
            <div class="single-page">
                <keep-alive :include="cachePage">
                    <router-view></router-view>
                </keep-alive>
            </div>
        </div>
    </div>
</template>
<script>
    import shrinkableMenu from './main-components/shrinkable-menu/shrinkable-menu.vue';
    import tagsPageOpened from './main-components/tags-page-opened.vue';
    import breadcrumbNav from './main-components/breadcrumb-nav.vue';
    import fullScreen from './main-components/fullscreen.vue';
    import lockScreen from './main-components/lockscreen/lockscreen.vue';
    import messageTip from './main-components/message-tip.vue';
    import themeSwitch from './main-components/theme-switch/theme-switch.vue';
    import Cookies from 'js-cookie';
    import util from '@/libs/util.js';
    
    export default {
        components: {
            shrinkableMenu,
            tagsPageOpened,
            breadcrumbNav,
            fullScreen,
            lockScreen,
            messageTip,
            themeSwitch
        },
        data () {
            return {
                shrink: false,
                theme1: 'dark',
                userName: '',
                isFullScreen: false,
                openedSubmenuArr: this.$store.state.app.openedSubmenuArr,
            };
        },
        computed: {
            menuList () {
                return this.$store.state.app.menuList;
            },
            pageTagsList () {
                return this.$store.state.app.pageOpenedList; // 打开的页面的页面对象
            },
            currentPath () {
                return this.$store.state.app.currentPath; // 当前面包屑数组
            },
            avatorPath () {
                return localStorage.avatorImgPath;
            },
            cachePage () {
                return this.$store.state.app.cachePage;
            },
            lang () {
                return this.$store.state.app.lang;
            },
            menuTheme () {
                return this.$store.state.app.menuTheme;
            },
            mesCount () {
                return this.$store.state.app.messageCount;
            }
        },
        methods: {
            init () {
                let pathArr = util.setCurrentPath(this, this.$route.name);
                this.$store.commit('updateMenulist');
                if (pathArr.length >= 2) {
                    this.$store.commit('addOpenSubmenu', pathArr[1].name);
                }
                let userInfo = JSON.parse(localStorage.userInfo);
                this.userName = userInfo.userName;//Cookies.get('user');
                let messageCount = 3;
                this.messageCount = messageCount.toString();
                this.checkTag(this.$route.name);
                this.$store.commit('setMessageCount', 3);
            },
            toggleClick () {
                this.shrink = !this.shrink;
            },
            handleClickUserDropdown (name) {
                if (name === 'ownSpace') {
                    util.openNewPage(this, 'ownspace_index');
                    this.$router.push({
                        name: 'ownspace_index'
                    });
                } else if (name === 'loginout') {
                    // 退出登录
                    this.$store.commit('logout', this);
                    this.$store.commit('clearOpenedSubmenu');
                    this.$router.push({
                        name: 'login'
                    });
                }
            },
            checkTag (name) {
                let openpageHasTag = this.pageTagsList.some(item => {
                    if (item.name === name) {
                        return true;
                    }
                });
                if (!openpageHasTag) { //  解决关闭当前标签后再点击回退按钮会退到当前页时没有标签的问题
                    util.openNewPage(this, name, this.$route.params || {}, this.$route.query || {});
                }
            },
            handleSubmenuChange (val) {
                // console.log(val)
            },
            beforePush (name) {
                // if (name === 'accesstest_index') {
                //     return false;
                // } else {
                //     return true;
                // }
                return true;
            },
            fullscreenChange (isFullScreen) {
                // console.log(isFullScreen);
            },
            itemTitle (item) {
              if (typeof item.title === 'object') {
                return this.$t(item.title.i18n);
              } else {
                return item.title;
              }
            },
          changeMenu (name) {
              //console.log(name);
               if(name === 'yq-monitor'){
                 //跳转舆情检测系统
                 // 打开一个页面
                 let newWin = window.open('about:blank');
                // 建议给一个提示信息
                 //newWin.document.body.innerHTML = '加载中...';
                 let url = "http://ddjt.capitalink.cn/login.php";
                 console.log(url);
                 newWin.location.href = url;

//                 this.$axiosInstance({
//                   method: 'post',
//                   url: '/pr/prYqcount/read/yqAccount'
//                 }).then((resp) => {
//                   console.log(resp);
//                   if (resp && resp.data.httpCode === 200) {
//                     let url = "http://ddjt.capitalink.cn/login.php?username=" + resp.data.data.username + "&token=" + resp.data.data.token;
//                     console.log(url);
//                     newWin.location.href = url;
//                   }else{
//                     newWin.close();
//                   }
//                 })
               }else{
                 let willpush = true;
                 if (this.beforePush !== undefined) {
                   if (!this.beforePush(name)) {
                     willpush = false;
                   }
                 }
                 if (willpush) {
                   this.$router.push({
                     name: name
                   });
                 }
               }

                //this.$emit('on-change', name);
            },

        },
        watch: {
            '$route' (to) {
                this.$store.commit('setCurrentPageName', to.name);
                let pathArr = util.setCurrentPath(this, to.name);
                if (pathArr.length > 2) {
                    this.$store.commit('addOpenSubmenu', pathArr[1].name);
                }
                this.checkTag(to.name);
                localStorage.currentPageName = to.name;
            },
            lang () {
                util.setCurrentPath(this, this.$route.name); // 在切换语言时用于刷新面包屑
            }
        },
        mounted () {
            this.init();
        },
        created () {
            // 显示打开的页面的列表
            this.$store.commit('setOpenedList');
        }
    };
</script>
