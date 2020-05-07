<!--
<template>
  <div class="groupRegisterBox">
    &lt;!&ndash; 内容 &ndash;&gt;
    <div class="container">
      &lt;!&ndash; eui &ndash;&gt;
      <div class="row">
        <div class="col col-xs-12 col-sm-8 col-sm-offset-2">
          <div class="box-header with-border">
            <h3 class="box-title">团体领队注册信息</h3>
          </div>
          <el-form ref="form1"
                   :rules="rules1"
                   :model="postObj"
                   :size="isPC?'medium':'mini'"
                   :label-width="isPC?'auto':false"
                   :disabled="false">
            <el-form-item label="团体账号(邮箱)" prop="email">
              <el-input v-model="postObj.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="postObj.password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input type="password" v-model="postObj.confirmPassword" placeholder="请确认密码"></el-input>
            </el-form-item>

            <el-form-item label="联系人姓名" prop="linkMan">
              <el-input  v-model="postObj.linkMan" placeholder="请输入联系人姓名"></el-input>
            </el-form-item>

            <el-form-item label="单位名称" prop="company">
              <el-input  v-model="postObj.company" placeholder="请输入单位名称"></el-input>
            </el-form-item>

            <el-form-item label="所属部门" prop="department">
              <el-input  v-model="postObj.department" placeholder="请输入所属部门"></el-input>
            </el-form-item>

            <el-form-item label="通信地址" prop="address">
              <el-input  v-model="postObj.address" placeholder="请输入通信地址"></el-input>
            </el-form-item>

            <el-form-item label="邮政编码" prop="postalCode">
              <el-input  v-model="postObj.postalCode" placeholder="请输入邮政编码"></el-input>
            </el-form-item>

            <el-form-item label="手机号" prop="mobile">
              <el-input  v-model="postObj.mobile" placeholder="请输入手机号"></el-input>
            </el-form-item>
          </el-form>
          <div style="text-align:right;padding:20px 0 50px;overflow:hidden;">
            <button type="button" class="btn btn-success btn-lg pull-left" :class="{'btn-sm':!isPC}" @click="backToLogin()">
              <i class="glyphicon glyphicon-arrow-left"></i>&emsp;取消注册
            </button>
            <button type="button" @click="handleSubmit('form1')" class="btn btn-success btn-lg pull-right" :class="{'btn-sm':!isPC}">提交&emsp;<i class="glyphicon glyphicon-ok"></i></button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

  export default {
    data(){

      var validPhone=(rule, value, callback)=>{
        if(value){
          if(!(/^1[3456789]\d{9}$/.test(value))){
            callback(new Error('手机号格式不正确'));
          }else{
            callback();
          }
        }else{
          callback();
        }
      };
      var validConfirmPassword=(rule, value, callback)=>{
        if(value){
          if(value != this.postObj.password){
            callback(new Error('确认密码和密码不一致'));
          }else{
            callback();
          }
        }else{
          callback();
        }
      }
      return{
        isPC:tools.isPC(),
        postObj:{
          email:"",//团体账号 电子邮箱
          password:"",//密码
          confirmPassword:"",//确认密码
          linkMan:"",//联系人姓名
          company:"",//单位名称
          department:"",//所属部门
          address:"",//通信地址
          postalCode:"",//邮政编码
          mobile:"",//手机号
        },
        rules1:{
          email:[
            { required: true, message: '请填写邮箱', trigger: 'change' },
            { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
          ],
          password:[
            { required: true, message: '请填写密码', trigger: 'change' },
          ],
          confirmPassword:[
            { required: true, message: '请确认密码', trigger: 'change' },
            { validator: validConfirmPassword, trigger: 'change' },
          ],
          linkMan:[
            { required: true, message: '请输入联系人姓名', trigger: 'change' }
          ],
          company:[
            { required: true, message: '请输入单位名称', trigger: 'change' }
          ],
          department:[
            { required: true, message: '请输入所属部门', trigger: 'change' }
          ],
          address:[
            { required: true, message: '请输入通信地址', trigger: 'change' }
          ],
          postalCode:[
            { required: true, message: '请输入邮政编码', trigger: 'change' }
          ],
          mobile:[
            { required: true, message: '请输入手机号', trigger: 'change' },
            { validator: validPhone, trigger: 'change' }
          ]

        },
      }
    },
    created(){
      var self=this;
      self.groupRegisterInit();//初始化
    },
    mounted(){
    },
    watch:{

    },
    computed:{
    },
    methods:{
      groupRegisterInit(){//初始化方法
        var self=this;

      },
      getToken(){//获取token
        api.get(global.xuehui+'/qiniu/token',null,res=>{
          this.token=res.data;
        },err=>{
          console.log(err);
        })
      },
      backToLogin(){
        this.$router.go(-1);
      },
      handleSubmit(formName){//提交
        var self=this;
        this.$refs[formName].validate((valid) => {
          if (valid) {

          }
        });
      }
    }
  }
</script>

<style lang="scss" scoped>
</style>
-->
