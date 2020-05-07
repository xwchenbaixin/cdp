<template>
  <div class="ces-main">

    <ces-table
      size='mini'
      :isSelection='true'
      :isIndex='true'
      :isPagination='true'
      :isHandle='true'
      :tableData='tableData'
      :tableCols='tableCols'
      :tableHandles='tableHandles'
    >
    </ces-table>
  </div>
</template>

<script>
  import cesTable from '@/common/components/custom-table'
  export default {
    data () {
      return {
      // 表格
        tableData:[
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
          {name:'dytt数据集',tableName:'dyttAAfeawfa',tableDef:'{{name:movie,alias:"电影名称"}}}',script:'select * from dyttAAfeawfa',status:1},
        ],
        tableCols:[
          {label:'数据集名称',prop:'name'},
          {label:'表名称',prop:'tableName'},
          {label:'表字段定义',prop:'tableDef'},
          {label:'查询脚本',prop:'script'},
          {label:'状态',prop:'status'},
          {label:'操作',type:'Button',width:'120px',btnList:[
              {type:'text',label:'查看',handle:row=>''},
              {type:'text',label:'预览',handle:row=>''},
              {type:'text',label:'分析',handle:row=>''}
            ]}
        ],
        tableHandles:[
          {label:'合并',type:'primary',size:'small',handle:row=>''},
          {label:'删除',type:'danger',size:'small',handle:row=>''},

        ]
      }
    },
    components:{
      cesTable
    }
  }
</script>

<style>

</style>
