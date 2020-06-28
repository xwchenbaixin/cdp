<template>

  <div>
    <el-container>
      <el-main >
        <!-- 第一列栅格布局 -->
        <el-row :gutter="20" style="text-align: center;">
          <el-col :span="6" class="col" >
            <el-card class="box-card" >
              <div slot="header" class="clearfix" style="color: #67c23a;">
                <span >执行完成</span>
              </div>
              <div style="color: #67c23a;font-size: 70px">
                {{this.timedTaskStatus.success}}
              </div>
            </el-card>

          </el-col>

          <el-col :span="6" class="col">
          <el-card class="box-card">
            <div slot="header" class="clearfix" style="color: #409eff">
              <span >执行中</span>
            </div>
            <div style="color: #409eff;font-size: 70px">
              {{this.timedTaskStatus.inExecution}}
            </div>
          </el-card>
        </el-col>
          <el-col :span="6" class="col">
            <el-card class="box-card">
              <div slot="header" class="clearfix" style="color: #e6a23c">
                <span >未执行</span>
              </div>
              <div style="color: #e6a23c;font-size: 70px">
                {{this.timedTaskStatus.notExecute}}
              </div>
            </el-card>

          </el-col>
        <el-col :span="6" class="col">
          <el-card class="box-card">
            <div slot="header" class="clearfix" style="color: #909399">
              <span >暂停中</span>
            </div>
            <div style="color: #909399;font-size: 70px">
              {{this.timedTaskStatus.pause}}
            </div>
          </el-card>

        </el-col>
        </el-row>
        <!-- 第二列布局 -->
        <br>
        <el-row>

          <el-col :span="24" >
            <el-card class="box-card">
              <cus-table
                ref="tableData"
                size='mini'
                :isIndex='true'
                :isPagination='true'
                :isHandle='true'
                :tableCols='tableCols'
                :url="url"
              >
              </cus-table>
            </el-card>


          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>

  import { codemirror } from 'vue-codemirror'

  import {
    listTimedTask
  } from "@/common/api/";
  import cusTable from '@/common/components/custom-table'
  export default {

    data () {
      return {
        thimer:Object,
        timedTaskStatus:{
          success:0,
          inExecution:0,
          notExecute:0,
          pause:0
        },
        url:"/timedtask/listTimedTask",
        tableCols:[
          {label:'定时器名字',prop:'taskName'},
          {label:'上次触发时间',prop:'lastTime'},
          {label:'下一次触发时间',prop:'nextTime',formatter:this.nextTimeFormatter},
          {label:'触发状态',prop:'status',formatter:this.statusFormatter}
        ]
      }
    },
    components:{
      cusTable
    },
    activated(){
      this.initTimedTaskStatus();
    },
    created(){

    },
    methods:{

      statusFormatter(row, column, cellValue, index){
        if(row.status==0){
          return "未执行";
        } else if(row.status==1){
          return "执行中";
        } else if(row.status==2){
          return "执行完成";
        } else if(row.status==3){
          return "暂停";
        }
      },
      nextTimeFormatter(row, column, cellValue, index){
        if(row.status==3){
          return "任务已暂停";
        }else{
          return row.nextTime;
        }
      },
      initTimedTaskStatus(){
        this.timedTaskStatus={
          success:0,
          inExecution:0,
          notExecute:0,
          pause:0
        };
        let that=this;
        listTimedTask({
          param:null
        }).then(res=>{
          res.data.forEach((val, index)=>{
            if(val.status==0){
              that.timedTaskStatus.notExecute+=1;
            } else if(val.status==1){
              that.timedTaskStatus.inExecution+=1;
            } else if(val.status==2){
              that.timedTaskStatus.success+=1;
            } else if(val.status==3){
              that.timedTaskStatus.pause+=1;
            }
          })
        }).then(()=>{
          that.$refs.tableData.refresh();
        })
      },

    }

  }
</script>


<style>


</style>
