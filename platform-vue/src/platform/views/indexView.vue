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
      :pagination='pagination'
    >
    </ces-table>
  </div>
</template>

<script>
  import cesTable from '@/common/components/custom-table'
  export default {
    data () {

      let sexs=[{label:'男',value:'M'},{label:'女',value:'F'}]
      let sexProps={label:'label',value:'value'}
      let intersts=[{label:'羽毛球',value:'badminton'},{label:'篮球',value:'basketball'}]
      let interstProps={label:'label',value:'value'}
      return {
// 查询表单
        searchData:{
          name:null,
          age:null,
          sex:null,
          interst:null
        },
        searchForm:[
          {type:'Input',label:'姓名',prop:'name',width:'180px',placeholder:'请输入姓名...'},
          {type:'Input',label:'年龄',prop:'age',width:'180px',placeholder:'请输入年龄...'},
          {type:'Select',label:'性别',prop:'sex',width:'180px',options:sexs,props:sexProps,change:row=>'',placeholder:'请选择性别...'},
          {type:'Checkbox',label:'爱好',width:'180px',prop:'interst',checkboxs:intersts,props:interstProps}
        ],
        searchHandle:[
          {label:'查询',type:'primary',handle:()=>''},
          {label:'重置',type:'primary',handle:()=>''}
        ],

// 表格
        tableData:[
          {name:'张三',age:'12',sex:'男',interst:'女'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'张三',age:'12',sex:'男',interst:'女'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'},
          {name:'筱华',age:'27',sex:'女',interst:'羽毛球'}
        ],
        tableCols:[
          {label:'姓名',prop:'name'},
          {label:'年龄',prop:'age'},
          {label:'性别',prop:'sex'},
          {label:'爱好',prop:'interst'},
          {label:'操作',type:'Button',btnList:[
              {type:'primary',label:'编辑',handle:row=>''},
              {type:'danger',label:'删除',handle:row=>''}
            ]}
        ],
        tableHandles:[
          {label:'新增',type:'primary',handle:row=>''}
        ],
        pagination:{
          pageSize:10,
          pageNum:1,
          total:15
        }
      }
    },
    components:{
      cesTable
    }
  }
</script>

<style>

</style>
