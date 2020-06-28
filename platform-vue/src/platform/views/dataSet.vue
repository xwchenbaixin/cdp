<template>
  <div class="ces-main">
    <el-form :inline="true" :model="searchFormData" class="demo-form-inline">
      <el-form-item label="数据集名称">
        <el-input v-model="searchFormData.name" placeholder="数据集名称" size="mini"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="dataSetTableRefresh" size="mini">查询</el-button>
      </el-form-item>
    </el-form>
    <cus-table
      size='mini'
      ref="dataSetTable"
      :isSelection='true'
      :isPagination='true'
      :isHandle='true'
      :tableCols='tableCols'
      :tableHandles='tableHandles'
      :searchForm='searchFormData'
      :url="url"
    >
    </cus-table>


    <el-dialog title="数据预览" :visible.sync="dataSetDialog"  width="80%" top="3%"	>
      <cus-table
        size='mini'
        :isPagination='true'
        :tableData='dataSetTableData'
        :tableCols='dataSetTableCols'
      >
      </cus-table>
    </el-dialog>
  </div>
</template>

<script>
  import cusTable from '@/common/components/custom-table'
  import {
    getResultData,
    exportExcel
  } from "@/common/api/";
  export default {
    components:{
      cusTable
    },
    data () {
      return {
        dataSetTableData:[],
        dataSetTableCols:[],
        dataSetDialog:false,
        searchFormData:{
          name:""
        },
        url:"/dataSet/listDataSet",

        tableCols:[
          {label:'编号',prop:'id',width:'100'},
          {label:'数据集名称',prop:'name'},
          {label:'表名称',prop:'tableName'},
          {label:'表字段定义',prop:'tableDef','show-overflow-tooltip':true},
          {label:'查询脚本',prop:'script'},
          {label:'操作',type:'Button',width:'210px',btnList:[
              {type:'text',label:'数据操作',handle:this.dataOpreate},
              {type:'text',label:'数据预览',handle:this.getResultData},
              {type:'text',label:'导出数据',handle:this.doExportExcel}
            ]}
        ],
        tableHandles:[
          // {label:'合并数据',type:'primary',size:'mini',handle:row=>''},
          // {label:'删除数据',type:'danger',size:'mini',handle:row=>''},
          {label:'刷新',size:'mini',handle:this.dataSetTableRefresh},

        ]
      }
    },
    methods:{
      doExportExcel(row){
        exportExcel({
          param:row
        }).then(res=>{
          if(res.data&&res.data.length>0) {
            window.open(res.data[0].downloadUrl, "_blank");
            this.$message({
              message: '导出成功',
              type: 'success'
            });
          }
        })
      },
      dataOpreate(row){
        this.$router.push({name:'dataAnalyze',params: {id:row.id}})
      },
      dataSetTableRefresh(){
       this.$refs.dataSetTable.refresh();
      },

      getResultData(row){
        let that=this;
        this.dataSetTableCols=[];
        console.log(row)
        this.dataSetDialog=true;
        // this.collectedCols=[];
        // this.fullscreenLoading=true;
        getResultData({
          "param":row
        }).then(res=>{

          let colsObj=JSON.parse(row.tableDef)
          if(res.data!=null &&res.data.length>0){
            let item=res.data[0];
            for(let key in item){
              that.dataSetTableCols.push({label:key,prop:key,'show-overflow-tooltip':true});
            }
            console.log(that.dataSetTableCols)
            this.dataSetTableData=res.data;
          }else{
            colsObj.forEach(item=>{
              that.dataSetTableCols.push({label:item.userDefCol,prop:item.userDefCol,'show-overflow-tooltip':true});
            })
            this.dataSetTableData=res.data;
          }


        }).catch(res=>{
          this.$alert('获取数据失败', {
            confirmButtonText: '确定',
          });

        });

      },
    },

  }
</script>

<style>

</style>
