<template>
  <div>
    <el-button @click="doCreateNewOrder">10元升级套餐</el-button>
    <div v-html="alipayWap" ref="alipayWap"></div>
  </div>
</template>
<script>
  import {createNewOrder} from '@/common/api';
  export default {
    // data() {
    //   return {
    //     msg: this.$route.query.data
    //   };
    // }, //使用这种方式也可以显示 msg
    data() {
      return {
        alipayWap:"",
        msg: null
      };
    },
    methods:{
      doCreateNewOrder(){
        createNewOrder({"param":null}).then(res=>{
          this.alipayWap = res;
          this.$nextTick(() => {
            this.$refs.alipayWap.children[0].submit()
          })
          console.log(res);
        })
      },
    },
    created() {
      this.msg = this.$route.query.message;
    }
  };
</script>
