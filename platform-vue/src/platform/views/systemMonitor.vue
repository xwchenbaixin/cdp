<template>
  <div>
    <iframe
      style="border:none"
      :width="searchTableWidth"
      :height="searchTableHeight"
      v-bind:src="reportUrl"
    ></iframe>
  </div>
</template>

<script>
  export default {
    name: 'SystemMonitor',
    methods: {
      widthHeight() {
        this.searchTableHeight = window.innerHeight -146;
        this.searchTableWidth = window.innerWidth - 280
      },
    },
    data() {
      return {
        reportUrl: 'http://192.168.1.80:7180/cmf/',
        searchTableHeight: 0,
        searchTableWidth: 0
      }
    },
    mounted() {
      window.onresize = () => {
        this.widthHeight(); // 自适应高宽度
      };
      this.$nextTick(function () {
        this.widthHeight();
      });
    },
    activated(){
      window.open("http://s3203y0292.zicp.vip/")
    },
    created() {
      // 从路由里动态获取 url地址   具体地址看libs下util.js里的 backendMenuToRoute  方法
      this.reportUrl ="http://192.168.1.80:7180/cmf/"

    },
    watch: {

    }
  }
</script>

<style scoped>

</style>
