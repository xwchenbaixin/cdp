<template>
  <div>
    <div style="margin-bottom: 10px;float: right;">

      <el-button  size="small" @click="getDataCollectData">刷新</el-button>
      <el-button type="primary" size="small" @click="dataOperate('add',null)">新增</el-button>
      <el-button type="danger" size="small" @click="deleteData(null)">删除</el-button>
    </div>

    <el-table
      ref="multipleTable"
      :data="tableData"
      @selection-change="handleSelectionChange"
      border
      style="width: 100%">

      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        prop="id"
        label="序号"
        width="55">
      </el-table-column>
      <el-table-column
        prop="collectName"
        label="名称">
      </el-table-column>
      <el-table-column
        prop="collectUrl"
        label="网址">
      </el-table-column>
      <el-table-column
        prop="status"
        label="状态"
        :formatter="statusFormat">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        width="160">
        <template slot-scope="scope">
          <el-button @click="editDialog(scope.row)" type="text" size="small">编辑</el-button>
          <el-button @click="webmagicAsyncStart(scope.row.id)" type="text" size="small" :disabled="scope.row.status==3">
            {{
                scope.row.status!=3?"执行":"正在执行"
            }}
          </el-button>
          <el-button @click="deleteData(scope.row.id)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="margin:10px;text-align: left">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="paginationData.currPage"
        :page-sizes="[10, 20, 50]"
        :page-size.sync="paginationData.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="paginationData.total">

      </el-pagination>
    </div>


    <el-dialog :title="dialogTitleName" :visible.sync="operateDialog" width="70%" top="2%"
               :center="true" :close-on-click-modal="false"
    >
      <el-form :model="formData">
        <el-form-item label="名称" :label-width="formLabelWidth">
          <el-input v-model="formData.collectName" autocomplete="off" :readonly="operateType=='view'"></el-input>
        </el-form-item>
        <el-form-item label="采集地址" :label-width="formLabelWidth">
          <el-input v-model="formData.collectUrl" autocomplete="off" :readonly="operateType=='view'"></el-input>
        </el-form-item>
        <el-form-item label="数据域" :label-width="formLabelWidth">
          <el-input v-model="formData.dataDomain" autocomplete="off" :readonly="operateType=='view'"></el-input>
        </el-form-item>
<!--        ,0-xpath，1-url，2-按钮，3-递归参数表达式-->
        <el-form-item label="下一页类型" :label-width="formLabelWidth">
          <el-select v-model="formData.nextPageType" placeholder="请选择下一页类型"  :disabled="operateType=='view'" style="float:left;">
            <el-option label="xpath" :value="0"></el-option>
            <el-option label="url" :value="1"></el-option>
            <el-option label="点击按钮" :value="2"></el-option>
            <el-option label="递归参数URL" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="下一页表达式" :label-width="formLabelWidth">
          <el-input v-model="formData.nextPage" autocomplete="off" :readonly="operateType=='view'"></el-input>
        </el-form-item>
        <el-form-item label="需爬取页数" :label-width="formLabelWidth">
          <el-input v-model="formData.nextPageTotal" autocomplete="off" :readonly="operateType=='view'"></el-input>
        </el-form-item>
        <el-form-item label="请求头" :label-width="formLabelWidth">
          <el-input v-model="formData.headers" autocomplete="off" :readonly="operateType=='view'" type="textarea" :autosize="{minRows: 2, maxRows: 6}"></el-input>
        </el-form-item>

        <el-form-item label="爬取参数设置" :label-width="formLabelWidth">
          <div style="margin-bottom: 5px;float: right;">
            <el-button type="primary" size="small" @click="paramRuleOperate('add',null)">新增</el-button>
          </div>

          <el-table
            :data="formData.collectParam"
            border
            style="width: 100%">
            <el-table-column
              prop="alias"
              label="中文名称">
            </el-table-column>
            <el-table-column
              prop="name"
              label="字段名称">
            </el-table-column>
            <el-table-column
              prop="xpath"
              label="XPATH表达式">
            </el-table-column>
            <el-table-column
              prop="regex"
              label="正则表达式">
            </el-table-column>
            <el-table-column
              prop="dataType"
              label="数据类型"
              :formatter="dataTypeFormat"
           >
            </el-table-column>

            <el-table-column
              fixed="right"
              label="操作"
              width="150">
              <template slot-scope="scope">
                <el-button @click="paramRuleOperate('edit',scope.row)"  size="mini">编辑</el-button>
                <el-button @click="deleteParamRule(scope.row.id)"  type="danger" size="mini">删除</el-button>
              </template>

            </el-table-column>
          </el-table>



        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="operateDialog = false">关 闭</el-button>
        <el-button type="primary" @click="dataCollectHandle">保 存</el-button>
        <el-button type="warning" @click="webmagicSyncRun" v-loading.fullscreen.lock="fullscreenLoading">预 览</el-button>
      </div>
    </el-dialog>

<!--    :close-on-click-modal="false"-->
    <el-dialog :title="paramRuleDialogName" :visible.sync="paramRuleDialog" 	:close-on-click-modal="false">
      <el-form :model="paramRuleFormData">
        <el-form-item label="中文名称" :label-width="formLabelWidth">
          <el-input v-model="paramRuleFormData.alias" autocomplete="off" :readonly="paramOperateType=='view'"></el-input>
        </el-form-item>
        <el-form-item label="字段名称" :label-width="formLabelWidth">
          <el-input v-model="paramRuleFormData.name" autocomplete="off" :readonly="paramOperateType=='view'"></el-input>
        </el-form-item>
        <el-form-item label="XPATH表达式" :label-width="formLabelWidth">
          <el-input v-model="paramRuleFormData.xpath" autocomplete="off" :readonly="paramOperateType=='view'"></el-input>
        </el-form-item>
        <el-form-item label="正则表达式" :label-width="formLabelWidth">
          <el-input v-model="paramRuleFormData.regex" autocomplete="off" :readonly="paramOperateType=='view'"></el-input>
        </el-form-item>
        <el-form-item label="数据类型" :label-width="formLabelWidth">
          <el-select v-model="paramRuleFormData.dataType" placeholder="请选择下一页类型"  :disabled="paramOperateType=='view'" style="float:left;">
            <el-option label="文本" :value="0"></el-option>
            <el-option label="图片" :value="1"></el-option>
          </el-select>
        </el-form-item>


      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="paramRuleDialog = false">取 消</el-button>
        <el-button type="primary" @click="paramRuleOk">确 定</el-button>
      </div>

    </el-dialog>

    <el-dialog title="数据预览" :visible.sync="collectedDataDialog"  width="80%" top="3%"	>
      <ces-table
        size='mini'
        :isPagination='true'
        :tableData='collectedData'
        :tableCols='collectedCols'
      >
      </ces-table>
    </el-dialog>
  </div>
</template>

<script>
  import cesTable from '@/common/components/custom-table'
  import {
    listCdpCollectDef,
    getCollectDefById,
    addCdpCollectDef,
    updateCdpCollectDef,
    deleteCdpCollectDefByIds,
    webmagicSyncRun,
    webmagicAsyncStart
  } from "@/common/api/";
  export default {
    name: 'DataCollect',
    components:{
      cesTable
    },
    data() {
      return {
        fullscreenLoading:false,
        collectedDataDialog:false,
        collectedData:[],
        collectedCols:[],
        paginationData:{
          currPage: 1,
          pageSize:10,
          total:0,
        },
        multipleSelection: [],
        operateDialog:false,
        paramRuleDialog:false,
        operateType:"",
        paramOperateType:"",
        dialogTitleName:"",
        paramRuleDialogName:"",
        formLabelWidth:"120px",
        formData:{
          collectParam: [],
          collectName: "",
          collectUrl: "",
          dataDomain: "",
          id: "",
          nextPage: "",
          nextPageTotal: 0,
          nextPageType: 0,
          dataSetDefId:0,
          userId: null,
          headers:""
        },
        paramRuleFormData:{
          id:0,
          name: "",
          dataType: 0,
          xpath: "",
          regex: "",
          alias: ""
        },
        tableData: []
      }
    },
    methods: {
      webmagicAsyncStart(id){
        webmagicAsyncStart({
          "param":{
            "id":id
          }
        }).then(res=>{
          this.$message({
            message: '已经开始执行',
            type: 'success'
          });
          this.getDataCollectData();
        })
      },

      statusFormat(row, column, cellValue, index){
        if(row.status==0)
          return "未执行"
        else if(row.status==1)
          return "执行失败"
        else if(row.status==2)
          return "执行成功"
        else if(row.status==3)
          return "正在执行"
        return row.status;
      },
      webmagicSyncRun(){
        this.collectedCols=[];
        this.fullscreenLoading=true;
        webmagicSyncRun({
          "param":{
            ...this.formData,
            collectParam:JSON.stringify(this.formData.collectParam),
            nextPageTotal:this.formData.nextPageTotal<=3?this.formData.nextPageTotal:3
          }
        }).then(res=>{
          let that=this;
          this.formData.collectParam.forEach(item=>{
            that.collectedCols.push({label:item.alias,prop:item.name});
          })
          this.collectedData=res.data;
          this.fullscreenLoading=false;
          this.collectedDataDialog=true;
        }).catch(res=>{
          this.fullscreenLoading=false;
          this.$alert('执行失败', '执行信息', {
            confirmButtonText: '确定',
          });

        });

      },
      deleteParamRule(id){
        let that=this;
        this.formData.collectParam.forEach(function(item, index, arr) {
          if(item.id == id) {
            that.formData.collectParam.splice(index, 1);
          }
        });

      },
      dataCollectHandle(){
        if(this.formData.collectParam.length==0){
          this.$message({
            message: '请添加爬取参数',
            type: 'error'
          });
          return ;
        }
        if(this.operateType=="add") {
          addCdpCollectDef({
            "param":{
              ...this.formData,
              collectParam:JSON.stringify(this.formData.collectParam)
            }
          }).then(res=>{
            this.$message({
              message: '添加成功',
              type: 'success'
            });
            this.getDataCollectData();
          }).catch(res=>{
            this.$message({
              message: '添加失败',
              type: 'error'
            });
          });

        }
        else if(this.operateType=="edit") {
          updateCdpCollectDef({
            "param":{
              ...this.formData,
              collectParam:JSON.stringify(this.formData.collectParam)
            }
          }).then(res=>{
            this.$message({
              message: '保存成功',
              type: 'success'
            });
            this.getDataCollectData();
          }).catch(res=>{
            console.log(res+"：：：：哈哈报错了")
          });
        }
      },

      dataTypeFormat(row, column, cellValue, index){
        return row.dataType==0?"文本":"图片";
      },

      paramRuleOk() {
        if(this.paramOperateType=='add') {
          let maxId=0;
          if(this.formData.collectParam.length>0){
            this.formData.collectParam.forEach(item=>{
              if(item.id>=maxId){
                maxId=item.id;
              }
            })
          }
          this.paramRuleFormData.id=maxId+1;
          this.formData.collectParam.push(this.paramRuleFormData);
        }else if(this.paramOperateType=='edit'){
          for(let i=0;i<this.formData.collectParam.length;i++){
            let item=this.formData.collectParam[i];
            if(item.id==this.paramRuleFormData.id) {
              console.log(this.paramRuleFormData)
              Object.assign(this.formData.collectParam[i],this.paramRuleFormData);
              break;
            }
          }

        }
        this.paramRuleDialog=false
      },
      paramRuleOperate(type,data){
        this.paramOperateType=type;
        this.clearParamRuleFormData();

        if(type=="add")
          this.paramRuleDialogName="新增";
        else if(type=="edit")
          this.paramRuleDialogName="编辑";
        else if(type=="view")
          this.paramRuleDialogName="查看";
        if(data!=null) {
          this.paramRuleFormData=this.deepCopyData(data);
          console.log(this.paramRuleFormData)
        }
        this.paramRuleDialog=true;
      },
      viewDialog(row) {
        this.dataOperate('view',row);
      },
      editDialog(row) {
        this.dataOperate('edit',row);
      },
      handleSizeChange(val) {
        this.getDataCollectData();
      },
      handleCurrentChange(val) {
        this.getDataCollectData();
      },
      deleteData(id) {
        let ids=[];
        if(id==null){
          this.multipleSelection.forEach(item =>{
            ids.push(item.id)
          })
          if(ids.length==0){
            this.$message({
              message: '请至少选择一条数据',
              type: 'warning'
            });
            return;
          }
        }else{
          ids.push(id)
        }

        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          customClass:'top10'
        }).then(() => {
          deleteCdpCollectDefByIds({
            "ids":ids
          }).then(res=>{
            this.$message({
              message: '删除成功',
              type: 'success'
            });
            this.getDataCollectData();
          }).catch(res=>{
            this.$message({
              message: '删除失败，请联系管理员',
              type: 'error'
            });
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });


        this.$refs.multipleTable.clearSelection();
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },

      dataOperate(type,data){
        this.operateType=type;
        this.clearFormData();
        if(type=="add")
          this.dialogTitleName="新增";
        else if(type=="edit")
          this.dialogTitleName="编辑";
        else if(type=="view")
          this.dialogTitleName="查看";
        if(data!=null) {
          this.formData=this.deepCopyData(data);
          if(this.formData.collectParam.length>0) {
            this.formData.collectParam = JSON.parse(this.formData.collectParam);
          }
        }
        this.operateDialog=true;

      },
      clearFormData(){
        this.formData={
          collectParam: [],
          collectName: "",
          collectUrl: "",
          dataDomain: "",
          id: "",
          nextPage: "",
          nextPageTotal: 0,
          nextPageType: 0,
          dataSetDefId:0,
          userId: null,
          headers:""
        }
      },
      clearParamRuleFormData(){
        this.paramRuleFormData={
          id:0,
          name: "",
          dataType: 0,
          xpath: "",
          regex: "",
          alias: ""
        }
      },
      deepCopyData(data){
        return JSON.parse(JSON.stringify(data));
      },
      getDataCollectData(){
        listCdpCollectDef({
          "pagination":this.paginationData
        }).then(res=>{
          this.tableData=res.data;
          this.paginationData.total=res.total;
        }).catch(res=>{
          console.log(res+"：：：：哈哈报错了")
        });
      }
    },
    watch:{

    },
    computed: {

    },
    mounted() {
      this.getDataCollectData();
      //console.log(this.paginationData)

    }
  }
</script>

<style >

</style>
