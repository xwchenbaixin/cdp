<template>
  <div>
    <el-form :inline="true" :model="searchForm" class="demo-form-inline">
      <el-form-item label="订单编号" >
        <el-input  placeholder="订单编号" size="mini" v-model="searchForm.id"></el-input>
      </el-form-item>

      <el-form-item label="订单名称">
        <el-input  placeholder="订单名称" size="mini" v-model="searchForm.orderName"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary"  size="mini" @click="tableRefresh">查询</el-button>
      </el-form-item>
    </el-form>
    <cus-table
      size='mini'
      ref="tableData"
      :isSelection='true'
      :isIndex='true'
      :isPagination='true'
      :isHandle='true'
      :tableCols='tableCols'
      :tableHandles='tableHandles'
      :searchForm="searchForm"
      :url="url"
    >
    </cus-table>
  </div>
</template>

<script>

  import { codemirror } from 'vue-codemirror'
  import cusTable from '@/common/components/custom-table'
  export default {
    name: 'UserDataManager',

    data () {

      return {
        url:"/orderManager/listOrders",

        tableCols:[
          {label:'订单编号',prop:'id'},
          {label:'订单名称',prop:'orderName'},
          {label:'订单状态',prop:'status',formatter:this.statusFormatter},
          {label:'订单类别',prop:'orderType',formatter:this.orderTypeFormatter},
          {label:'创建时间',prop:'createTime'},
          {label:'付款时间',prop:'payTime'},

        ],
        tableHandles:[
          {label:'刷新',type:'default',handle:this.tableRefresh}
        ],
        searchForm:{
          id:"",
          orderName:""
        }
      }
    },
    components:{
      cusTable
    },
    methods:{
      statusFormatter(row, column, cellValue, index){
        if(row.status==0){
          return "已创建未付款";
        }else if(row.status==1){
          return "已付款"
        }else if(row.status==3){
          return "订单失效"
        }

      },
      orderTypeFormatter(row, column, cellValue, index){
        if(row.orderType==0){
          return "会员购买";
        }

      },
      tableRefresh(){
        this.$refs.tableData.refresh();
      },
    }
  }
</script>
