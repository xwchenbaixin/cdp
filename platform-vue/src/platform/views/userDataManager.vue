<template>
  <div>
    <el-form :inline="true" :model="searchForm" class="demo-form-inline">
      <el-form-item label="账号">
        <el-input v-model="searchForm.username" placeholder="账号" size="mini"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="searchForm.nickname" placeholder="昵称" size="mini"></el-input>
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input v-model="searchForm.phone" placeholder="手机号码" size="mini"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="searchForm.email" placeholder="邮箱" size="mini"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary"  size="mini" @click="refreshTable">查询</el-button>
      </el-form-item>
    </el-form>
    <cus-table
      size='mini'
      ref="tableData"
      :isSelection='true'
      :isPagination='true'
      :isHandle='true'
      :tableCols='tableCols'
      :tableHandles='tableHandles'
      :searchForm='searchForm'
      :url='url'
    >
    </cus-table>

    <el-dialog title="重置密码" :visible.sync="resetPassword" 	>
      <el-form :model="resetPasswordForm">
        <el-form-item label="新密码" label-width="120px">
          <el-input v-model="resetPasswordForm.newPassword" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="重新输入新密码" label-width="120px">
          <el-input v-model="resetPasswordForm.repeatPassword" autocomplete="off" ></el-input>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="resetPassword = false">取 消</el-button>
        <el-button type="primary" @click="doResetPassword">确 定</el-button>
      </div>

    </el-dialog>

    <el-dialog title="封禁账号" :visible.sync="banedUserDialog" style="text-align: center" width="400px" >
      <h1 style="color: red">确定封禁账号：<br>{{selectedRow.username}}</h1>
      <div slot="footer" class="dialog-footer">
        <el-button @click="banedUserDialog = false" size="small">取 消</el-button>
        <el-button type="primary" @click="doBanedUser" size="small">确 定</el-button>
      </div>

    </el-dialog>

    <el-dialog title="启用账号" :visible.sync="enabledUserDialog" style="text-align: center" width="400px">
      <h1>确定启用账号：<br>{{selectedRow.username}}</h1>
      <div slot="footer" class="dialog-footer">
        <el-button @click="enabledUserDialog = false" size="small">取 消</el-button>
        <el-button type="primary" @click="doEnabledUser" size="small">确 定</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>

  import cusTable from '@/common/components/custom-table'
  import {
    resetPassord,
    banedUser,
    enabledUser
  } from "@/common/api/";
  export default {
    name: 'UserDataManager',

    data () {

      return {
        selectedRow:{},
        enabledUserName:"",
        enabledUserDialog:false,

        banedUserName:"",
        banedUserDialog:false,

        resetPasswordForm:{
          id:"",
          newPassword:"",
          repeatPassword:""
        },
        resetPassword:false,
        url:"/userManager/listUserInfo",
        tableCols:[
          {label:'用户编号',width:'80',prop:'id'},
          {label:'账号',prop:'username'},
          {label:'密码',prop:'password'},
          {label:'昵称',prop:'nickname'},
          {label:'手机号码',prop:'phone'},
          {label:'邮箱',prop:'email'},
          {label:'状态',width:'60',prop:'status',formatter:this.userStatusFormat},
          {label:'类别',width:'60',prop:'type',formatter:this.userTypeFormat},
          {label:'创建时间',prop:'createTime'},
          {label:'操作',type:'Button',width:'240px',btnList:[
              {type:'warning',label:'重置密码',handle:this.initResetPassword},
              {type:'danger',label:'封禁',handle:row=>{this.selectedRow=row;this.banedUserDialog=true}},
              {type:'primary',label:'启用',handle:row=>{this.selectedRow=row;this.enabledUserDialog=true}}
            ]}
        ],
        tableHandles:[
          {label:'刷新',type:'default',handle:this.refreshTable}
        ],
        searchForm:{
          username:"",
          nickname:"",
          phone:"",
          email:""
        }

      }
    },
    components:{
      cusTable
    },
    methods:{
      doEnabledUser(){
        if(this.selectedRow.status==1){
          this.$message({
            message: '已启用，请勿重复操作',
            type: 'warning'
          });
          this.enabledUserDialog=false;
          return;
        }
        enabledUser({'param':{id:this.selectedRow.id}}).then(res=>{
          this.$message({
            message: '启用成功',
            type: 'success'
          });
          this.refreshTable();
        })
        this.enabledUserDialog=false;
      },
      doBanedUser(){
        if(this.selectedRow.status==0){
          this.$message({
            message: '已封禁，请勿重复操作',
            type: 'warning'
          });
          this.banedUserDialog=false;
          return;
        }
        banedUser({'param':{id:this.selectedRow.id}}).then(res=>{
          this.$message({
            message: '封禁成功',
            type: 'success'
          });
          this.refreshTable();
        })
        this.banedUserDialog=false;
      },

      initResetPassword(row){
        this.resetPassword=true;
        this.resetPasswordForm.id=row.id;
        this.resetPasswordForm.newPassword="";
        this.resetPasswordForm.repeatPassword="";

      },
      doResetPassword(){
        if(this.resetPasswordForm.newPassword==this.resetPasswordForm.repeatPassword){
          //密码长度在6-18之间，以字母开头，只能包含字符、数字和下划线。
          if(this.isRightPassword(this.resetPasswordForm.newPassword)){
            resetPassord(this.$qs.stringify(this.resetPasswordForm)).then(res=>{
              this.$message({
                message: '修改成功',
                type: 'success'
              });
              this.resetPassword=false;
              this.refreshTable();
            })
          }else {
            this.$message({
              message: '密码长度在6-18之间，以字母开头，只能包含字符、数字和下划线。',
              type: 'error'
            });
          }
        }else{
          this.$message({
            message: '两次输入密码不一致',
            type: 'error'
          });
        }
      },
      isRightPassword(psw){
        if(psw.length!=0){
         let  reg=/^[a-zA-Z]\w{5,17}$/;
          if(!reg.test(psw)){
            return false;
          }
          return true;
        }
        return false;
      },
      userStatusFormat(row, column, cellValue, index){
        return row.status==0?"封禁":"启用";
      },
      userTypeFormat(row, column, cellValue, index){
        if(row.type==1){
          return '会员'
        }else if(row.type==2){
          return '付费会员'
        }else if(row.type==3){
          return '管理员'
        }
      },
      refreshTable(row){
        this.$refs.tableData.refresh();
      }
    }
  }
</script>
