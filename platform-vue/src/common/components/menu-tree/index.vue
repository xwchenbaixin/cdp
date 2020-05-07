<template>
  <div >

    <template v-for="nowTree in menuTreeData">
      <!-- 最后一级菜单 -->
      <el-menu-item v-if="nowTree.children.length==0&&nowTree.node"
                    :key="nowTree.node.id" :data="nowTree" :index="nowTree.node.name"
                    @click="menuClick(nowTree.node)"
      >
        <i :class="nowTree.node.icon"></i>
        <span slot="title">{{nowTree.node.alias}}</span>
      </el-menu-item>

      <!-- 此菜单下还有子菜单 -->
      <el-submenu v-if="nowTree.children.length!=0&&nowTree.node"
                  :key="nowTree.node.id" :data="nowTree" :index="nowTree.node.name">
        <template slot="title">
          <i :class="nowTree.node.icon"></i>
          <span> {{nowTree.node.alias}}</span>
        </template>
        <!-- 递归 -->
        <menuTree :menuTreeData="nowTree.children" @menu-click="menuClick"></menuTree>
      </el-submenu>
    </template>

  </div>
</template>

<script>
  export default {
    name: 'menuTree',
    props: ['menuTreeData'],
    data() {
      return {}
    },
    methods: {
      menuClick(value){
        this.$emit('menu-click',value);
      }
    }
  }
</script>

