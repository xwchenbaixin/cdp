<template>
  <el-container style="height: 100%;width: 100%;">
    <el-aside width="15%" style="height: 100%">
      <el-container style="height: 100%">
        <el-header style="  width: 100%;padding: 0px;" >
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
              <template slot="title">我的工作台</template>
              <el-menu-item index="2-1">选项1</el-menu-item>
              <el-menu-item index="2-2">选项2</el-menu-item>
              <el-menu-item index="2-3">选项3</el-menu-item>
            </el-submenu>
          </el-menu>

        </el-header>

        <el-main style="padding: 0;padding-right: 5px">
          <el-tabs v-model="tabs.editableTabsValue" type="border-card"  @tab-remove="removeTab" @tab-click="tabClick" style="height: calc(100% - 2px)">
            <el-tab-pane
              v-for="(item, index) in tabs.editableTabs"
              :key="item.name"
              :label="item.title"
              :name="item.name"
              closable
            >
              <keep-alive> <router-view></router-view> </keep-alive>
            </el-tab-pane>


          </el-tabs>
        </el-main>
      </el-container>


    </el-main>

    <el-container style="height: 100%">

    </el-container>
  </el-container>

</template>

<script>
  import MenuTree from "@/common/components/menu-tree/";
  //import {getMenuTree} from '@/common/api';
  export default {
    components: {
      MenuTree: MenuTree
    },
    data() {
      return {
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
</style>

