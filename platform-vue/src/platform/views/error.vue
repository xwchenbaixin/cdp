<template>
  <div>
    <h1>登录错误：{{msg}}</h1>
  </div>
</template>
<script>
  export default {
    // data() {
    //   return {
    //     msg: this.$route.query.data
    //   };
    // }, //使用这种方式也可以显示 msg
    data() {
      return {
        msg: null
      };
    },
    created() {
      this.msg = this.$route.query.message;
    }
  };
dataAnalyze(this.$qs.stringify({sql:this.formData.querySql})).then(res=>{
  if(res.status!=200){
    this.$message({
      message: res.message,
      type: 'error'
    });
    this.executeMessage="执行失败";
    return ;
  }
  this.tableCols=new Array();
  if(res.data&&res.data.length>0){
    let item=res.data[0];
    for(let key in item){
      this.tableCols.push({label:key,prop:key});
    }
    this.tableData=res.data;
  }
  //重新绘图
  //条件为：查询结果只有一条，所有数据类型均为Integer型，
  this.drawChartsByResData(res.data);
  this.isShowTable=true;
  this.executeMessage="执行成功";
})
</script>
