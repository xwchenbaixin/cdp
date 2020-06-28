<template>
  <div style="height: 100%;">
    <el-row :gutter="10" style="height: 100%">
      <el-col :span="12" style="height:100%;">
        <div class="codemirror">
          <codemirror ref="myCm"
                      v-model="formData.querySql"
                      :options="cmOptions"
                      @ready="onCmReady"
                      @focus="onCmFocus"
                      @input="onCmCodeChange">
          </codemirror>
        </div>

      </el-col>
      <el-col :span="12" style="height:100%">
        <el-row :gutter="10" style="height:calc(50% - 20px);">
          <el-col :span="24" style="height: 100%;">
            <div id="myChart" v-show="usingEcharts" :style="{width: '100%', height: '100%'}"></div>

          </el-col>
        </el-row>
        <el-row :gutter="10" style="height:30px;margin: 5px 0 5px 0;">
          <el-col :span="3">
            <el-button type="primary" size="mini" @click="execute">提交</el-button>
          </el-col>
          <el-col :span="3" v-if="showSaveButton">
            <el-button  type="primary" size="mini" @click="doSaveScript">保存</el-button>
          </el-col>
          <el-col :span="4">
            <el-select placeholder="启用图表" v-model="usingEcharts"  style="float:left;" @change="isUsingEcharts" size="mini">
              <el-option label="启用图表" :value="true"></el-option>
              <el-option label="禁用图表" :value="false"></el-option>
            </el-select>
          </el-col>

          <el-col :span="4">
            <el-select placeholder="图类别" v-if="usingEcharts" v-model="echartsType"  style="float:left;" @change="changeEchartsType" size="mini">
              <el-option label="柱状图" value="bar"></el-option>
              <el-option label="折线图" value="line"></el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            <span>{{executeMessage}}</span>
          </el-col>
        </el-row>
        <el-row :gutter="10" style="height:calc(50% - 20px);overflow: auto;">
          <el-col :span="24" style="height: 100%;">
            <cus-table
              v-if="isShowTable"
              size='mini'
              :isSelection='true'
              :isIndex='true'
              :isPagination='true'
              :tableData='tableData'
              :tableCols='tableCols'
            >
            </cus-table>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <el-dialog
      title="提示"
      :visible.sync="showErrorMessage"
      width="800px"
      height="600px">
      <el-input
        type="textarea"
        :rows="20"
        readonly
        v-model="errorInfo">
      </el-input>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="showErrorMessage = false">确 定</el-button>
      </span>
    </el-dialog>
    </div>
</template>

<script>
  import cusTable from '@/common/components/custom-table'
  import { codemirror } from 'vue-codemirror'
  import sqlFormatter from "sql-formatter"
  import 'codemirror/mode/sql/sql.js'
  // theme css
  import 'codemirror/theme/solarized.css'
  import 'codemirror/theme/darcula.css'
  // require active-line.js
  import 'codemirror/addon/selection/active-line.js'
  // closebrackets
  import 'codemirror/addon/edit/closebrackets.js'
  // keyMap
  import 'codemirror/mode/clike/clike.js'
  import 'codemirror/addon/edit/matchbrackets.js'
  import 'codemirror/addon/comment/comment.js'
  import 'codemirror/addon/dialog/dialog.js'
  import 'codemirror/addon/dialog/dialog.css'
  import 'codemirror/addon/search/searchcursor.js'
  import 'codemirror/addon/search/search.js'
  import 'codemirror/keymap/emacs.js'
  import {
    dataAnalyze,
    getDataSetById,
    saveScript
  } from "@/common/api/";

  export default {
    components:{
      codemirror,
      cusTable
    },
    watch:{
      '$route'(){
        if(this.$route.path=='/dataAnalyze'&& this.$route.params.id!=undefined) {
          getDataSetById({'param':{id:this.$route.params.id}}).then(res=>{
            this.formData.querySql=res.data[0].script;
            this.isShowTable=false;
            this.showSaveButton=true;
          })
        }
      }
    },
    data() {
      return {
        //错误信息
        showErrorMessage:false,
        errorInfo:"",
        executeMessage:"",
        usingEcharts:false,
        showSaveButton:false,
        myChart:Object,
        echartsType:'bar',
        echartsOption:{
          title: { text: '分析视图' },
          tooltip: {},
          xAxis: {
            data: []
          },
          yAxis: {},
          series: [{
            name: '统计1',
            type: 'bar',
            data: []
          }]
        },
        isShowTable:false,
        tableData:[],
        tableCols:[],

        formData:{
          querySql:''
        },

        cmOptions: {
          tabSize: 4,
          styleActiveLine: true,
          lineNumbers: true,
          line: true,
          mode: 'text/x-mysql',
          theme: 'darcula light'
        }
      }
    },
    computed: {
      codemirror() {
        return this.$refs.myCm.codemirror
      }
    },
    mounted(){
      if(this.$route.path=='/dataAnalyze'&& this.$route.params.id!=undefined) {
        getDataSetById({'param':{id:this.$route.params.id}}).then(res=>{
          this.formData.querySql=res.data[0].script;
          this.showSaveButton=true;
        })
      }
      //初始化echarts实例
      this.myChart= this.$echarts.init(document.getElementById('myChart'))

    },
    methods:{
      doSaveScript(){
        saveScript({
          "param":{
            id:this.$route.params.id,
            script:this.formData.querySql
          }
        }).then(res=>{
          if(res.status==200){
            this.$message({
              message: "保存成功",
              type: 'success'
            });
          }
        })
      },
      changeEchartsType(value){
        for(let key in this.echartsOption.series){
          this.echartsOption.series[key].type=value;
        }

        this.drawCharts();
      },
      isUsingEcharts(value){
        if(value){
          let that=this;
          setTimeout(function(){that.drawCharts();}, 200);

        }
      },
      choiceDataSet(){

      },
      saveScript(){
        if(this.$route.params.id==undefined){
          this.choiceDataSet();
        }else{

        }
      },
      drawCharts(){
        // 绘制图表
        this.myChart.setOption(this.echartsOption);
        this.myChart.resize();
      },
      isEchartsData(key){
        if(key=='e_name'||key=='e_type'){
          return true;
        }
        return false;
      },
      getEchartsParamName(key){
        return key.replace('e_','');
      },
      drawChartsByResData(data){
        this.errorInfo="";

        // 绘制图表
        if(data.length>0 &&this.usingEcharts) {
          if(data.length>10){
            this.errorInfo+='总数据条目数不超过10';
            this.showErrorMessage=true;
            return ;
          }

          //构造列
          let item = data[0];
          let xAxisData = [];
          for (let key in item) {
            if(!this.isEchartsData(key)) {
              xAxisData.push(key);
            }
          }
          //构造表数据
          let series = [];

          let errorIndex=1;
          for (let i in data) {
            let echartsNode={
              data:[]
            };
            item = data[i];
            for (let key in item) {
              //非Echarts属性，
              if(this.isEchartsData(key)) {
                //Echarts属性构造
                let eParam=this.getEchartsParamName(key);
                echartsNode[eParam]=item[key];
              }else{
                let num = Number(item[key]);
                if (!isNaN(num)) {
                  echartsNode.data.push(num)
                } else {
                  this.errorInfo+=errorIndex+++"......\n列:"+key+"\n值:"+item[key]+"\n错误信息：无法转为数值型，默认设置为0\n";
                  echartsNode.data.push(0);
                }
              }
            }
            //如果name 和type未设置 默认使用 i 和bar
            if(echartsNode.name==undefined){
              echartsNode['name']=i;
            }
            if(echartsNode.type==undefined){
              echartsNode['type']=this.echartsType;
            }

            series.push(echartsNode);
          }
          this.myChart.clear();
          this.myChart= this.$echarts.init(document.getElementById('myChart'))
          this.echartsOption.xAxis.data = xAxisData;
          this.echartsOption.series=series;

          this.myChart.setOption(this.echartsOption);
          this.myChart.resize();

          if(this.errorInfo.length>0){
            this.showErrorMessage=true;
          }
        }


      },
      showSQL(val){
        this.formData.querySql = val?val:''
      },
      onCmReady(cm) {
        this.codemirror.setSize("-webkit-fill-available", "auto")
      },
      onCmFocus(cm) {
        // console.log('the editor is focus!', cm)
      },
      onCmCodeChange(newCode) {
        this.formData.querySql = newCode;
        this.$emit('codeChange',this.formData.querySql)
      },
      execute(){
        this.executeMessage="";
        if(this.formData.querySql.length==0){
          this.$message({
            message: "请输入内容",
            type: 'warning'
          });
          return;
        }
        this.isShowTable=false;
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
        }).catch(res=>{
          this.executeMessage="执行失败";
        })
      }
    }
  }
</script>
<style>
  .CodeMirror{
    height: 100% !important;
  }
  .codemirror >>> .CodeMirror-scroll {
    min-height: 180px;
  }
  .codemirror{
    height: 100%;
  }
  .vue-codemirror{
    height: 100%;
  }
</style>
