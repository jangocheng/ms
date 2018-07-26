<!-- 用户操作日志 -->
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <span>选择日期：</span>
          <el-date-picker
            v-model="filter.date"
            align="left"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            size="small"
            @change="getList"
            :picker-options="pickerOptions">
          </el-date-picker>
          <span style="margin-left: 10px;">电话号码：</span>
          <el-input
            align="left"
            size="small"
            placeholder="请输入电话号码"
            v-model="filter.phoneNo"
            prefix-icon="el-icon-search"
            clearable>
          </el-input>

          <!--<el-button type="primary" icon="el-icon-delete" size="small" @click="clearCondition">清空筛选</el-button>-->
          <el-button type="primary" icon="el-icon-search" v-if="hasPerm('opLog:list')" @click="getList" size="small">查询
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table ref="multipleTable" :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row @row-click="selectCurow" @selection-change="handleSelectionChange">
      <!--<el-table-column
        type="selection"
        width="55">
      </el-table-column>-->
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column  align="center" label="手机号码" prop="phoneno"/>
      <el-table-column  align="center" label="操作类型" prop="opDesc"/>
      <el-table-column  align="center" label="设备型号" prop="deviceModel"/>
      <el-table-column  align="center" label="操作系统版本" prop="osVersion"/>
      <el-table-column  align="center" label="MAC地址" prop="opDevice"/>
      <el-table-column  align="center" label="GPS" prop="opGps"/>
      <el-table-column  align="center" label="操作时间" prop="opTime"/>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="filter.pageNum"
      :page-size="filter.pageRow"
      :total="totalCount"
      :page-sizes="[20, 50, 100,500]"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>

  </div>
</template>
<style scoped>
  .filter-container{
    display: inline-block;
    float: left;
  }
  .filter-container .el-input{
    width: 200px;
    margin-right: 20px;
  }
</style>
<script>
  import ElSwitch from "../../../node_modules/element-ui/packages/switch/src/component.vue";
  import ElSpinner from "../../../node_modules/element-ui/packages/spinner/src/spinner.vue";
  import ElInput from "../../../node_modules/element-ui/packages/input/src/input.vue";
  export default {
    components: {
      ElInput,
      ElSpinner,
      ElSwitch},
    data(){
      return {
        totalCount: 0,//总条目数
        list: [],// 表格数据
        listLoading: false,
        filter: {
          pageNum: 1,
          pageRow: 10,
          date: '',// 选中过滤日期
          phoneNo: ''// 电话号码
        },
        selected: [],// 选择的表格行
        pickerOptions: {// 日期选框
          disabledDate(time) {
            return time.getTime() > Date.now();
          },
          shortcuts: [{
            text: '今天',
            onClick(picker) {
              picker.$emit('pick', new Date());
            }
          }, {
            text: '昨天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit('pick', date);
            }
          }, {
            text: '一周前',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }]
        },
      };
    },
    created(){
      this.getList();
    },
    methods:{
      getList(){
        /*if(!status){
            status = 0;
        }*/
        this.listLoading = true;
        this.api({
          url: '/user/opLog/list?date='+this.filter.date + '&phoneNo='+ this.filter.phoneNo,
          method: 'get',
          headers: {
            pageRow: this.filter.pageRow,
            pageNum: this.filter.pageNum
          }
        }).then(data => {
          this.listLoading = false;
          this.list = data.data;
          this.totalCount = data.totalSize;
        }).catch(e => {
          if(e.response && e.response.data){// 异常时处理
            this.$message({
              showClose: true,
              message: e.response.data.msg,
              type: e.response.data.code,
              duration: 500,
              onClose: () => {
                if(e.response.data.code === '12'){// 如果是遇到登录超时情况，回到登录页面
                  this.$store.dispatch('FedLogOut').then(() => {
                    location.reload()// 为了重新实例化vue-router对象 避免bug
                  })
                }
              }
            });
          }else{
            this.$message.error(`数据加载失败：${JSON.stringify(e)}`)
            this.listLoading = false;
          }
        });
      },
      handleSelectionChange(row){// 选中状态
        this.selected = [];// 先清空
        row.forEach(r =>{
//          if(this.selected.indexOf(r.id) === -1){
          this.selected.push(r.id);// 再放进去
//          }
        });

      },
      selectCurow(row){
        this.$refs.multipleTable.toggleRowSelection(row);
      },
      getIndex($index) {
        //表格序号
        return (this.filter.pageNum - 1) * this.filter.pageRow + $index + 1
      },
      handleSizeChange(val) {
        //改变每页数量
        this.filter.pageRow = val
        this.getList();
      },
      handleCurrentChange(val) {
        //改变页码
        this.filter.pageNum = val
        this.getList();
      }/*,
      clearCondition(){
        debugger
        this.$refs.selectStatus.call("clear")

      }*/,
      createRole(){

      },
      updateRole(){// 更新

      }

    }
  }
</script>


