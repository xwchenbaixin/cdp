
<template>
  <div>
    <el-card class="login-form-layout">
      <el-form
        autocomplete="off"
        ref="signUpForm"
        :model="signUpForm"
        :rules="signUpFormRules"
        label-position="left"
      >
        <div style="text-align: center">
          <svg-icon icon-class="login-mall" style="width: 56px;height: 56px;color: #409EFF"></svg-icon>
        </div>
        <h2 class="login-title color-main">注册</h2>
        <el-form-item prop="username">
          <el-input
            name="username"
            type="text"
            v-model="signUpForm.username"
            autocomplete="off"
            placeholder="请输入用户名"
          >
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            name="password"
            type="password"
            v-model="signUpForm.password"
            autocomplete="off"
            placeholder="请输入密码"
          >
          </el-input>
        </el-form-item>

        <el-form-item prop="nickname">
          <el-input
            name="nickname"
            v-model="signUpForm.nickname"
            type="text"
            autocomplete="off"
            placeholder="请输入昵称"
          >
          </el-input>
        </el-form-item>

        <el-form-item prop="phone">
          <el-input
            name="phone"
            v-model="signUpForm.phone"
            type="text"
            autocomplete="off"
            placeholder="请输入手机号码"
          >
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input
            name="phone"
            v-model="signUpForm.email"
            type="text"
            autocomplete="off"
            placeholder="请输入邮箱"
          >
          </el-input>
        </el-form-item>

        <el-form-item >
          <el-button
            style="width: 100%"
            type="primary"
            :loading="loading"
            @click.native.prevent="handleLogin"
          >注册</el-button>
        </el-form-item>
        <el-row style="text-align: center; ">
          <el-link type="primary">忘记密码</el-link>
          <el-link type="primary" @click="$router.push('login')">登录</el-link>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
  import {registUser} from "../api";
  export default {
    name: "signUp",
    data() {
      return {
        signUpFormRules:{
          username: [
            { required: true, message: '用户名不能为空'},
            {pattern: /^[A-Za-z]{3,16}$/,message:"用户名必须都为字母,且长度在3-16（含）之间"}
          ],
          nickname:[
            { required: true, message: '昵称不能为空'},
            {min: 2, max: 8, message: '长度在 3 到 5 个字符'}
          ],
          password: [
            { required: true, message: '密码不能为空'},
            {pattern: /^[a-zA-Z]\w{5,17}$/,message:"密码长度在6-18之间，以字母开头，只能包含字符、数字和下划线。"}
          ],
          phone:[
            { required: true, message: '手机不能为空'},
            {pattern: /^1[3|4|5|7|8][0-9]\d{8}$/,message:"请输入正确的手机号码"}
          ],
          email:[
            {  required: true, message: '邮箱不能为空'},
            {  type:"email", message: '请填写正确的邮箱'},
          ]
        },
        signUpForm: {
          username: "",
          nickname:"",
          password: "",
          phone:"",
          email:""
        },
        loading: false,
        pwdType: "password",
      };
    },
    methods: {
      showPwd() {
        if (this.pwdType === "password") {
          this.pwdType = "";
        } else {
          this.pwdType = "password";
        }
      },
      handleLogin() {
        this.$refs.signUpForm.validate(valid => {
          if (valid) {
            this.loading = true;

            registUser({
              param:this.signUpForm
            }).then(res=>{
              if(res.status==200){
                this.$message({ message: "注册成功，返回登录界面", type: 'success'});
                this.$router.push("/login");
              }else {
                this.$message({ message: "注册失败", type: 'error'});
              }
              this.loading = false;
            }).catch(res=>{
              this.$message({ message: "注册失败", type: 'error'});
              this.loading = false;
            });

          } else {
            // eslint-disable-next-line no-console
            console.log("参数验证不合法！");
            return false;
          }
        });
      }
    }
  };
</script>

<style scoped>
  .login-form-layout {
    position: absolute;
    left: 0;
    right: 0;
    width: 360px;
    margin: 140px auto;
    border-top: 10px solid #409eff;
  }

  .login-title {
    text-align: center;
  }

  .login-center-layout {
    background: #409eff;
    width: auto;
    height: auto;
    max-width: 100%;
    max-height: 100%;
    margin-top: 200px;
  }
</style>
