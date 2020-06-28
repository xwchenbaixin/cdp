<template>

  <div>
    <el-form :inline="true" :model="searchForm" class="demo-form-inline">
      <el-form-item label="定时任务名称">
        <el-input v-model="searchForm.taskName" placeholder="定时任务名称" size="mini"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="tableRefresh" size="mini">查询</el-button>
      </el-form-item>
    </el-form>
              <cus-table
                ref="tableData"
                size='mini'
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

    <el-dialog title="新增定时器" :visible.sync="addTimedTaskDialog" 	>
      <el-form :model="formData">
        <el-form-item label="任务名称" label-width="150px">
          <el-input v-model="formData.taskName" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="任务类别" label-width="150px">
          <el-select placeholder="选择任务类别" v-model="formData.taskType"  style="float:left;"  >
            <el-option label="数据采集" :value="0"></el-option>
            <el-option label="数据分析" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据采集/分析名称" label-width="150px" >
          <el-select
            v-model="formData.param"
            filterable
            remote
            reserve-keyword
            placeholder="选择定时任务"
            :remote-method="listDataCollect"
            @focus="listDataCollect('')"
            :loading="selectLoading">
            <el-option
              v-for="item in collectDefOption"
              :key="item.value"
              :label="item.collectName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="Cron表达式" label-width="150px">
          <el-input v-model="formData.cron" autocomplete="off" ></el-input>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="addTimedTaskDialog = false">取 消</el-button>
        <el-button type="primary" @click="doAddTimedTaskJob">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="新增定时器" :visible.sync="updateDialog" 	>
      <el-form :model="updateFormData">
        <el-form-item label="任务名称" label-width="150px">
          <el-input v-model="updateFormData.taskName" autocomplete="off" ></el-input>
        </el-form-item>

        <el-form-item label="Cron表达式" label-width="150px">
          <el-input v-model="updateFormData.cron" autocomplete="off" ></el-input>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="updateDialog = false">取 消</el-button>
        <el-button type="primary" @click="doUpdateTimedTaskJob">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

  import cusTable from '@/common/components/custom-table'
  import {
    listCdpCollectDef,
    addTimedTaskJob,
    updateTimedTask,
    pauseTask,
    resumeTask,
    deleteTask
  } from "@/common/api/";


  export default {
    data () {

      return {
        addTimedTaskDialog:false,
        updateDialog:false,
        updateFormData:{
          id:"",
          taskName:"",
          cron:""
        },
        selectLoading:false,
        collectDefOption:[],
        url:"/timedtask/listTimedTask",
        // 查询表单
        formData:{
          taskName:"",
          taskType:"",
          param:"",
          cron:""
        },
        searchForm:{
          taskName:""
        },
        tableCols:[
          {label:'定时器名字',prop:'taskName'},
          {label:'上次触发时间',prop:'lastTime'},
          {label:'下一次触发时间',prop:'nextTime',formatter:this.nextTimeFormatter},
          {label:'触发状态',prop:'status',formatter:this.statusFormatter},
          {label:'操作',type:'Button',width:'160px',btnList:[
              {type:'text',label:'暂停',handle:this.doPauseTask},
              {type:'text',label:'启用',handle:this.doResumeTask},
              {type:'text',label:'修改',handle:row=>{this.updateDialog=true;this.updateFormData=row}},
              {type:'text',label:'删除',handle:this.doDeleteTask},
            ]}
        ],
        tableHandles:[
          {label:'刷新',type:'default',handle:this.tableRefresh},
          {label:'新增',type:'primary',handle:row=>{this.addTimedTaskDialog=true;}}
        ]
      }
    },
    methods:{

      doDeleteTask(row){
        this.$alert('确定删除该任务？', '确认框', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(()=>{
            deleteTask({
              "param":{
                id:row.id,
                serialNum:row.serialNum
              }
            }).then(res=>{
              this.$message({
                type: 'success',
                message: `删除成功`
              });
              this.tableRefresh();
            })
        });
      },
      doResumeTask(row){
        if(row.status!=3){
          this.$message({
            type: 'warning',
            message: `只能重启已暂停任务`
          });
          return ;
        }
        this.$alert('确定重启该任务？', '确认框', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(()=>{
          resumeTask({
            "param":{
              id:row.id,
              serialNum:row.serialNum
            }
          }).then(res=>{
            this.$message({
              type: 'success',
              message: `重启成功`
            });
            this.tableRefresh();
          })
        });
      },
      doPauseTask(row){
        if(row.status==3){
          this.$message({
            type: 'warning',
            message: `任务已经暂停，请勿重复操作`
          });
          return ;
        }
        this.$alert('确定暂停该任务？', '确认框', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(()=>{
          pauseTask({
            "param":{
              id:row.id,
              serialNum:row.serialNum
            }
          }).then(res=>{
            this.$message({
              type: 'success',
              message: `暂停成功`
            });
            this.tableRefresh();
          })
        });
      },
      doUpdateTimedTaskJob(){
        updateTimedTask({
          "param":this.updateFormData
        }).then(res=>{
          this.$message({
            message: '更新成功',
            type: 'success'
          });
          this.updateDialog=false;
          this.tableRefresh();
        })
      },

      doAddTimedTaskJob(){
        addTimedTaskJob({
          "param":this.formData
        }).then(res=>{
          this.$message({
            message: '添加成功',
            type: 'success'
          });
          this.addTimedTaskDialog=false;
          this.tableRefresh();
        })
      },
      tableRefresh(){
        this.$refs.tableData.refresh();
      },
      listDataCollect(query){
        listCdpCollectDef({"param":{collectName:query}}).then(res=>{
          this.collectDefOption = res.data.filter(item => {
            return item.collectName.toLowerCase()
              .indexOf(query.toLowerCase()) > -1;
          });
        })  ;
      },
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
    },
    components:{
      cusTable
    }
  }
</script>


<style>


</style>
