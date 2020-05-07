<template>
  <div>
    <div style="margin-bottom: 5px;float: right;">
      <el-button type="primary" size="small" @click="dataOperate('add',null)">新增</el-button>
      <el-button type="danger" size="small" @click="toggleSelection">删除</el-button>
    </div>

    <el-table
      ref="multipleTable"
      :data="tableData.slice((paginationData.currentPage-1)*paginationData.pageSize,paginationData.currentPage*paginationData.pageSize)"
      @selection-change="handleSelectionChange"
      border
      style="width: 100%">
      <el-table-column
        type="selection"
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
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button @click="editDialog(scope.row)" type="text" size="small">编辑</el-button>
          <el-button @click="editDialog(scope.row)" type="text" size="small">执行</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="margin:10px;text-align: left">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="paginationData.currentPage"
        :page-sizes="[10, 20, 50]"
        :page-size.sync="paginationData.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="paginationData.total">

      </el-pagination>
    </div>


    <el-dialog :title="dialogTitleName" :visible.sync="operateDialog" width="70%" top="2%"
               :center="true"
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
                <el-button @click="paramRuleOperate('delete',scope.row)"  type="danger" size="mini">删除</el-button>
              </template>

            </el-table-column>
          </el-table>



        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="operateDialog = false">取 消</el-button>
        <el-button type="primary" @click="operateDialog = false">确 定</el-button>
        <el-button type="warning" @click="operateDialog = false">预 览</el-button>
      </div>
    </el-dialog>

<!--    :close-on-click-modal="false"-->
    <el-dialog :title="paramRuleDialogName" :visible.sync="paramRuleDialog" 	>
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
          <el-select v-model="paramRuleFormData.datatype" placeholder="请选择下一页类型"  :disabled="paramOperateType=='view'" style="float:left;">
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

  </div>
</template>

<script>
  export default {
    name: 'DataCollect',
    data() {
      return {
        paginationData:{
          currentPage: 1,
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
          userId: null,
          headers:""
        },
        paramRuleFormData:{
          id:0,
          name: "",
          datatype: 0,
          xpath: "",
          regex: "",
          alias: ""
        },
        tableData: []
      }
    },
    methods: {
      dataTypeFormat(row, column, cellValue, index){
        return row.datatype==0?"文本":"图片";
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
        }
        this.paramRuleDialog=true;
      },
      viewDialog(row) {
        this.dataOperate('view',row);
        console.log(row);
      },
      editDialog(row) {
        this.dataOperate('edit',row);
        console.log(row);
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
      },
      handleCurrentChange(val) {

        console.log(`当前页: `+this.paginationData.currentPage);
        console.log(this.tableData.slice((this.paginationData.currentPage-1)*this.paginationData.pageSize,this.paginationData.currentPage*this.paginationData.pageSize).length)
      },
      toggleSelection() {
        console.log(this.multipleSelection);
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
          userId: null,
          headers:""
        }
      },
      clearParamRuleFormData(){
        this.paramRuleFormData={
          id:0,
          name: "",
            datatype: 0,
            xpath: "",
            regex: "",
            alias: ""
        }
      },
      deepCopyData(data){
        return JSON.parse(JSON.stringify(data));
      }
    },
    watch:{
      tableData(newVal,oldVal) {
        this.paginationData.total=newVal.length;
      }
    },
    computed: {

    },
    mounted() {
      this.tableData=[
        {
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },{
          "id": "1",
          "collectName": "电影天堂采集",
          "collectParam": [{
            "id":1,
            "name": "moviename",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/text()",
            "regex": "[\\u4e00-\\u9fa5].*",
            "alias": "电影名称"

          }, {
            "id":2,
            "name": "url",
            "datatype": 0,
            "xpath": "//tr[2]/td[2]/b/a/@href",
            "regex": "",
            "alias": "链接"
          }, {
            "id":3,
            "name": "desc",
            "datatype": 0,
            "xpath": "//tr[4]/td/text()",
            "regex ": "",
            "alias": "描述"
          }],
          "dataDomain": "//*[@id=\"header\"]/div/div[3]/div[3]/div[2]/div[2]/div[2]/ul/table/tbody",
          "userId": null,
          "nextPage": "//*[@id='header']/div/div[3]/div[3]/div[2]/div[2]/div[2]/div/a[text()='下一页']/@href",
          "nextPageType": 0,
          "collectUrl": "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html",
          "nextPageTotal": 1,
          "headers":"{\n" +
            "'Connection': 'keep-alive',\n" +
            "'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36',\n" +
            "'Accept': '*/*',\n" +
            "'Sec-Fetch-Site': 'same-origin',\n" +
            "'Sec-Fetch-Mode': 'cors',\n" +
            "'Sec-Fetch-Dest': 'empty',\n" +
            "'Accept-Encoding': 'gzip, deflate, br',\n" +
            "'Accept-Language': 'zh-CN,zh;q=0.9',\n" +
            "'Cookie': 'operatorName=%E7%B3%BB%E7%BB%9F%E7%AE%A1%E7%90%86%E5%91%98; username=admin; operator_code=admin'\n" +
            "}"
        },
      ];
      //console.log(this.paginationData)

    }
  }
</script>

<style scoped>

</style>
