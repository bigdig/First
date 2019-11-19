<style lang="less">
    @import './own-space.less';
</style>

<template>
    <div>
        <Card>
            <p slot="title">
                <Icon type="person"></Icon>
                个人信息
            </p>
            <div>
                <Form 
                    ref="userForm"
                    :model="userForm" 
                    :label-width="100" 
                    label-position="right"
                >
                    <FormItem label="姓名：">
                        <span style="width:100px;margin-right: 15px;">{{ userForm.name }}</span><Button type="primary" size="small" @click="showEditPassword">修改密码</Button>
                    </FormItem>
                    <!--<FormItem label="部门：">-->
                        <!--<span>{{ userForm.department }}</span>-->
                    <!--</FormItem>-->
                    <!--<FormItem label="登录密码：">-->

                    <!--</FormItem>-->
                    <!--<FormItem label="舆情监测账号：">-->
                        <!--<span style="width:100px;margin-right: 15px;">{{ userForm.yqAccount }}</span><Button type="primary" size="small" @click="showEditYqPassword">设置密码</Button>-->
                    <!--</FormItem>-->
                    <!--<div>-->
                        <!--<Button type="text" style="width: 100px;" @click="cancelEditUserInfor">取消</Button>-->
                        <!--<Button type="primary" style="width: 100px;" :loading="save_loading" @click="saveEdit">保存</Button>-->
                    <!--</div>-->
                </Form>
            </div>
        </Card>
        <Modal v-model="editPasswordModal" :closable='false' :mask-closable=false >
            <h3 slot="header" style="color:#2D8CF0">修改密码</h3>
            <Form ref="editPasswordForm" :model="editPasswordForm" :label-width="100" label-position="right" :rules="passwordValidate">
                <FormItem label="原密码" prop="oldPassword" :error="oldPassError">
                    <Input type="password" v-model="editPasswordForm.oldPassword" placeholder="请输入现在使用的密码" ></Input>
                </FormItem>
                <FormItem label="新密码" prop="password">
                    <Input type="password" v-model="editPasswordForm.password" placeholder="请输入新密码，至少6位字符" ></Input>
                </FormItem>
                <FormItem label="确认新密码" prop="password2">
                    <Input type="password" v-model="editPasswordForm.password2" placeholder="请再次输入新密码" ></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancelEditPass">取消</Button>
                <Button type="primary" :loading="savePassLoading" @click="saveEditPass">保存</Button>
            </div>
        </Modal>
        <Modal v-model="editYqPasswordModal" :closable='false' :mask-closable=false :width="500">
            <h3 slot="header" style="color:#2D8CF0">修改密码</h3>
            <Form ref="editYqPasswordForm" :model="editYqPasswordForm" :label-width="150" label-position="right" :rules="yqPasswordValidate">
                <FormItem label="舆情监测系统密码" prop="yqPwd">
                    <Input type="password" v-model="editYqPasswordForm.yqPwd" placeholder="请输入舆情系统密码" ></Input>{{editYqPasswordForm.yqPwd}}
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancelEditPass">取消</Button>
                <Button type="primary" :loading="savePassLoading" @click="saveEditYqPass">保存</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
export default {
    name: 'ownspace_index',
    data () {
        const validePhone = (rule, value, callback) => {
            var re = /^1[0-9]{10}$/;
            if (!re.test(value)) {
                callback(new Error('请输入正确格式的手机号'));
            } else {
                callback();
            }
        };
        const valideRePassword = (rule, value, callback) => {
            if (value !== this.editPasswordForm.password) {
                callback(new Error('两次输入密码不一致'));
            } else {
                callback();
            }
        };
        return {
           userForm: {
             name:''
           },
            uid: '', // 登录用户的userId
            save_loading: false,
            editPasswordModal: false, // 修改密码模态框显示
            savePassLoading: false,
            oldPassError: '',
            editYqPasswordModal: false, // 修改密码模态框显示

            editPasswordForm: {
              oldPassword: '',
              password: '',
              password2: ''
            },
            editYqPasswordForm: {
              yqPwd: ''
            },
            passwordValidate: {
              oldPassword: [
                    { required: true, message: '请输入原密码', trigger: 'blur' }
                ],
              password: [
                    { required: true, message: '请输入新密码', trigger: 'blur' },
                    { min: 6, message: '请至少输入6个字符', trigger: 'blur' },
                    { max: 18, message: '最多输入18个字符', trigger: 'blur' }
                ],
              password2: [
                    { required: true, message: '请再次输入新密码', trigger: 'blur' },
                    { validator: valideRePassword, trigger: 'blur' }
                ]
            },
            yqPasswordValidate: {
              yqPwd: [
                { required: true, message: '请输入舆情系统密码', trigger: 'blur' }
              ]
            }
        };
    },
    methods: {

        showEditPassword () {
            this.editPasswordModal = true;
        },
        showEditYqPassword () {
            this.editYqPasswordModal = true;
        },
        cancelEditUserInfor () {
            this.$store.commit('removeTag', 'ownspace_index');
            localStorage.pageOpenedList = JSON.stringify(this.$store.state.app.pageOpenedList);
            let lastPageName = '';
            if (this.$store.state.app.pageOpenedList.length > 1) {
                lastPageName = this.$store.state.app.pageOpenedList[1].name;
            } else {
                lastPageName = this.$store.state.app.pageOpenedList[0].name;
            }
            this.$router.push({
                name: lastPageName
            });
        },

        cancelEditPass () {
            this.editPasswordModal = false;
            this.editYqPasswordModal = false;
        },
        saveEditPass () {
            this.$refs['editPasswordForm'].validate((valid) => {
                if (valid) {
                  this.savePassLoading = true;
                    // you can write ajax request here
                  this.$axiosInstance({
                    method: 'post',
                    url: '/sys/user/update/password',
                    data: this.editPasswordForm
                  }).then((resp) => {
                      if (resp.data.httpCode === 200) {
                        this.$Notice.success({
                          title: '密码修改成功',
                        });
                        this.cancelEditPass();
                      }else {
                        this.$Modal.warning({
                          content: resp.data.msg
                        });
                      }
                      this.savePassLoading = false;
                    },
                    (error) => {

                    }
                  );
                }
            });
        },
      saveEditYqPass () {
        this.$refs['editYqPasswordForm'].validate((valid) => {
          if (valid) {
            this.savePassLoading = true;
            this.$axiosInstance({
              method: 'post',
              url: '/sys/user/update/yQpassword',
              data: this.editYqPasswordForm
            }).then((resp) => {
                if (resp.data.httpCode === 200) {
                  this.$Notice.success({
                    title: '密码修改成功',
                  });
                  this.cancelEditPass();
                }else {
                  this.$Modal.warning({
                    content: resp.data.msg
                  });
                }
                this.savePassLoading = false;
              },
              (error) => {

              }
            );
          }
        });
      },
        init () {
          let userInfo = JSON.parse(localStorage.userInfo);

          this.userForm.name = userInfo.userName;
          this.userForm.yqAccount = userInfo.yqAccount;
        },
    },
    mounted () {
        this.init();
    }
};
</script>
