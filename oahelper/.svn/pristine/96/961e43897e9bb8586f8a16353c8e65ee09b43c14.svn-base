<style lang="less">
    @import './login.less';
</style>

<template>
    <div class="login" @keydown.enter="handleSubmit">

        <img style="margin-top: 30px ;margin-left: 70px;" src="../images/logo.png"></img>
        <div style="margin: 40px 150px 50px 150px;">
            <Row type="flex" justify="center" align="middle">
                <Col :xs="24" :sm="8" span="8" >
                <div class="login-con" >
                    <Card :bordered="false" :dis-hover="true" >
                        <div class="form-con">
                            <Form ref="loginForm" :model="form" :rules="rules" label-position="top">
                                <p class="login-title">
                                    <span style="">欢迎登录</span>
                                </p>
                                <br/>
                                <FormItem prop="userName" label="用户名">
                                    <Input type="text" v-model="form.userName" size="large" placeholder="请输入用户名" >
                                    <span slot="append">
                                            <Icon :size="16" type="person"></Icon>
                                        </span>
                                    </Input>
                                </FormItem>
                                <FormItem prop="password" label="密码">
                                    <Input type="password" size="large" v-model="form.password" placeholder="请输入密码">
                                    <span slot="append">
                                            <Icon :size="14" type="locked"></Icon>
                                        </span>
                                    </Input>
                                </FormItem>
                                <FormItem prop="adlogin" label="">
                                    <input type="checkbox" size="large" v-on:click="CheckItem(from)" v-model="form.adlogin" name="checkbox" />AD登录
                                </FormItem>
                                <FormItem>
                                    <Button style="background-color: #F58221" @click="handleSubmit" type="primary" long>登录</Button>
                                </FormItem>
                            </Form>
                            <!--<p class="login-tip">输入任意用户名和密码即可</p>-->
                        </div>
                    </Card>
                </div>
                </Col>
                <Col  :xs="24" :sm="16" span="16" >
                <div class="companyImg" style="height: 400px">

                </div>
                <!--<img style="width:100%;height: 420px" src="../images/company.png"/>-->
                </col>
            </row>
        </div>
        <div>
            <p style="text-align: center;color: #999999">版权所有：天风证券股份有限公司</p>
        </div>
    </div>
</template>

<script>
  import Cookies from 'js-cookie';

  export default {
    data () {
      return {
        form: {
          userName: '',
          password: '',
          adlogin:true
        },
        rules: {
          userName: [
            {required: true, message: '账号不能为空', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '密码不能为空', trigger: 'blur'}
          ],
        }
      };
    },
    methods: {
      CheckItem:function(from){
        this.from.adlogin = !this.from.adlogin;
        console.log(this.froms);
      },
      handleSubmit () {
        this.$refs.loginForm.validate((valid) => {
          if (valid) {
            this.$axiosInstance({
              method: 'post',
              url: '/login',
              data: {
                account: this.form.userName,
                password: this.form.password,
                adlogin: this.form.adlogin
              }
            }).then((resp) => {
                if (resp.data.httpCode === 200) {
                  this.getUserInfo();
                }
                else {
                  alert(resp.data.msg);
                }
              },
              (error) => {

              }
            );
          }
        });
      },
      getUserInfo () {
        this.$axiosInstance({
          method: 'post',
          url: '/sys/user/read/current'
        }).then((resp) => {
            if (resp.data.httpCode === 200) {
              Cookies.set('user', this.form.userName);
              this.$store.commit('setUserInfo', JSON.stringify(resp.data.user));
              //this.$store.commit('setAvator', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3448484253,3685836170&fm=27&gp=0.jpg');
              var menuIds = [];
              //2级菜单
              for(let i=0;i<resp.data.menus.length;i++){
                console.log(resp.data.menus[i].menuName);
                menuIds.push(resp.data.menus[i].id);
                let children = resp.data.menus[i].menuBeans;
                for(let j =0; j< children.length; j++) {
                  console.log(children[j].menuName);
                  menuIds.push(children[j].id);
                }
              }
              this.$store.commit('setMenuItems', JSON.stringify(resp.data.menus));
              this.$store.commit('setMenus', JSON.stringify(menuIds));
              this.$store.commit('updateMenulist');
              this.$router.push({
                name: 'home_index'
              });
            }
            else {
              alert(resp.data.msg);
            }
          },
          (error) => {

          }
        );

      }
    }
  };
</script>

<style>

</style>
