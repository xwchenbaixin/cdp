<template>
  <el-container style="height: 100%;width: 100%;">
    <el-aside width="15%" style="height: 100%">
      <el-container style="height: 100%">
        <el-header style="  width: 100%;padding: 0px;text-align: center" >
          <h1>数据采集系统</h1>
        </el-header>
        <el-main style="padding: 0">
          <el-menu
            :default-active="activeIndex"
            style="text-align: left;height: 100%"
            router
          >
            <MenuTree :menuTreeData="menuTreeData" @menu-click="menuClick"></MenuTree>
          </el-menu>
        </el-main>
      </el-container>

    </el-aside>

    <el-main style="width: 85%;padding: 0px;padding-left: 5px">
      <el-container style="height: 100%">
        <el-header style="  width: 100%;padding: 0px;" >
          <el-menu  class="el-menu-demo" mode="horizontal" >

            <el-submenu index="2" style="float: right;">
              <template slot="title">{{userType}}，欢迎您：{{user.nickname}}</template>
              <el-menu-item index="2-1" @click="upgradeUserDialog=true" v-if="user.type==1">升级</el-menu-item>
              <el-menu-item index="2-2">详细信息</el-menu-item>
              <el-menu-item index="2-3" @click="doLogout">退出</el-menu-item>
            </el-submenu>
          </el-menu>

        </el-header>

        <el-main style="padding: 0;padding-right: 5px;">
          <el-tabs v-model="tabs.editableTabsValue" type="border-card"  @tab-remove="removeTab" @tab-click="tabClick" class="cus-tab" >
            <el-tab-pane

              key="indexView"
              label="首页"
              name="indexView"
            >

            </el-tab-pane>


            <el-tab-pane
              v-for="(item, index) in tabs.editableTabs"
              :key="item.name"
              :label="item.title"
              :name="item.name"
              v-if="item.name!='indexView'"
              closable
            >

            </el-tab-pane>
            <keep-alive> <router-view></router-view> </keep-alive>

          </el-tabs>
        </el-main>
      </el-container>


    </el-main>

    <el-container style="height: 100%">

    </el-container>
    <el-dialog
      title="提示"
      :visible.sync="upgradeUserDialog"
      v-if="upgradeUserDialog"
      width="30%">
      <UpgradeUser></UpgradeUser>
      <span slot="footer" class="dialog-footer">
        <el-button @click="upgradeUserDialog = false">取 消</el-button>
        <el-button type="primary" @click="upgradeUserDialog = false">确 定</el-button>
      </span>
    </el-dialog>

  </el-container>

</template>

<script>
  import MenuTree from "@/common/components/menu-tree/";
  import UpgradeUser from "./upgradeUser"
  import {logout} from "@/common/api"
  //import {createNewOrder} from '@/common/api';
  export default {
    components: {
      MenuTree,
      UpgradeUser
    },
    data() {
      return {
        user:JSON.parse(this.$store.state.user.user),

        userType:"",
        upgradeUserDialog:false,
        activeIndex: this.$route.path.split("/")[1],
        menuTreeData:[],
        tsbActiveName:"",
        tabs:{
          editableTabsValue: '',
          editableTabs: []
        }
      };
    },
    methods:{
      formatUserType(){
        console.log(this.$store.state)
        if(this.user.type==1){
          this.userType="普通会员";
        }else if(this.user.type==2){
          this.userType="付费会员";
        }else if(this.user.type==3){
          this.userType="管理员";
        }
      },

      doLogout(){
        logout().then(res=>{
            this.$router.push({path: "/login"});

        })
      },
      menuClick(value){

        if(!this.tabIsExists(value.name)){
          this.addTab(value.name,value.alias);
        }


      },
      tabIsExists(name){
        let that=this;
        let flag=false;

        this.tabs.editableTabs.forEach(v=>{
          if(v.name==name){
            that.tabs.editableTabsValue=name;
            flag=true;
            return false;
          }
        });
        return flag;
      },
      menuNodeIsExists(name){
        let flag=false;
        this.SysPram.menuData.forEach(v=>{
          if(v.name==name){
            flag=true;
            return false;
          }
        })
        return flag;
      },
      addTab(name,alias) {
        this.tabs.editableTabs.push({
          title: alias,
          name: name
        });
        this.tabs.editableTabsValue = name;
      },
      removeTab(targetName) {
        let tabs = this.tabs.editableTabs;
        let activeName = this.tabs.editableTabsValue;
        console.log(this.tabs.editableTabs)
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              let nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                activeName = nextTab.name;
                this.$router.push(activeName);
              }
            }
          });
        }

        this.tabs.editableTabsValue = activeName;
        this.tabs.editableTabs = tabs.filter(tab => tab.name !== targetName);
      },
      initMenu(){
        this.menuTreeData=this.SysPram.menuTreeData;
      },
      tabClick(e){
        let nowRouteName=this.$route.path.split("/")[1];
        if(e.name!=nowRouteName)
          this.$router.push(e.name);
      },
      initTabs(){
        let tabName=this.$route.path.split("/")[1];

        let alias=this.$route.meta.title;
        if(!this.tabIsExists(tabName)&&this.menuNodeIsExists(tabName)){
          this.addTab(tabName,alias);
        }
      }
    },
    mounted() {
      this.initTabs();
      this.initMenu();
      this.formatUserType();
    },
    watch:{
      "$route" : {
        handler(){
          //权限的数据存入store，菜单的数据存入store
          this.initTabs();
          this.activeIndex= this.$route.path.split("/")[1];
        }
      }
    }
  };
</script>

<style>
  .cus-tab{
    height: calc(100% - 2px)
  }
  .cus-tab>.el-tabs__content{
    height: calc(100% - 69px);
  }
  .el-tabs__content{
    overflow: auto;
  }
</style>

