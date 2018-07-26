<!--spv放款 -->
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
          <span style="margin-left: 10px;">放款状态：</span>
          <el-select ref="selectStatus" size="small" clearable v-model="filter.status" placeholder="请选择" @change="getList">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <!--<el-button type="primary" icon="el-icon-delete" size="small" @click="clearCondition">清空筛选</el-button>-->
          <el-button type="primary" icon="el-icon-plus" v-if="hasPerm('spv:down')" style="float: right;margin-top: 4px;" @click="showConfirm" size="small">放款
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table ref="multipleTable" :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row @row-click="selectCurow" @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column align="center" label="序号">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column  align="center" label="借款人" prop="loanerName"/>
      <el-table-column align="center" label="放款状态" width="100">
          <template slot-scope="scope">
            <!--<el-tag type="info" v-if="scope.row.moneyDownStatus===1">未放款</el-tag>-->
            <el-tag v-if="scope.row.moneyDownStatus===11">未放款</el-tag>
            <el-tag type="warning" v-else-if="scope.row.moneyDownStatus===21">未放款-额度超限</el-tag>
            <el-tag type="success" v-else-if="scope.row.moneyDownStatus===31">放款完成</el-tag>
            <el-tag type="danger" v-else="">取消放款</el-tag>
          </template>
      </el-table-column>
      <el-table-column align="center" label="借款人身份证" prop="loanerIdcardNum" width="180"/>
      <el-table-column align="center" label="借款协议编号" prop="contractNum" width="180"/>
      <el-table-column align="center" label="借款日期" prop="loanDate" width="180"/>
      <el-table-column align="center" label="产品名称" prop="commodityFullName" width="180"/>
      <!--<el-table-column align="center" label="transNo" prop="transNo" style="width: 60px;"/>-->
      <el-table-column align="center" label="借款金额" prop="loanMoneyAmount" style="width: 60px;"/>
      <el-table-column align="center" label="商户贴息金额" prop="allowanceAmount" style="width: 60px;"/>
      <el-table-column align="center" label="实放款金额" prop="moneyDownAmount" style="width: 60px;"/>
      <el-table-column align="center" label="提货确认日期" prop="rewardConfirmDate" width="160"/>
      <el-table-column align="center" label="借款期数" prop="installmentNum" style="width: 60px;"/>
      <el-table-column align="center" label="商户名称" prop="businessName" style="width: 60px;"/>
      <el-table-column align="center" label="网银户名" prop="payeeBankHoldName" style="width: 60px;"/>
      <el-table-column align="center" label="网银账号" prop="payeeBankNum" width="180"/>
      <el-table-column align="center" label="开户省" prop="createAccountProvince" style="width: 60px;"/>
      <el-table-column align="center" label="开户市" prop="createAccountCity" style="width: 60px;"/>
      <el-table-column align="center" label="开户支行" prop="createAccountAgency" style="width: 60px;"/>
      <el-table-column align="center" label="对公/对私" prop="payeeBankIsPublic" style="width: 60px;"/>
      <el-table-column align="center" label="网银联行号" prop="payeeBankCorrespondentNumber" style="width: 60px;"/>
      <el-table-column align="center" label="渠道" content="SPV" style="width: 60px;"/>
      <el-table-column align="center" label="放款时间" prop="moneyDownDate" style="width: 60px;"/>
      <el-table-column align="center" label="操作人" prop="operatorName" style="width: 60px;"/>
      <el-table-column align="center" label="备注" prop="remarks" style="width: 60px;"/>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow"
      :total="totalCount"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>

  </div>
</template>
<script>
  import ElSwitch from "../../../node_modules/element-ui/packages/switch/src/component.vue";
  import ElSpinner from "../../../node_modules/element-ui/packages/spinner/src/spinner.vue";
  export default {
    components: {
      ElSpinner,
      ElSwitch},
    data(){
      return {
        totalCount: 0,//总条目数
        list: [],// 表格数据
        listLoading: false,
        listQuery: {
          pageNum: 1,
          pageRow: 10
        },
        filter: {
          date: '',// 选中过滤日期
          status: ''// 选中过滤状态
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
        options: [{
          value: '0',
          label: '全部'
        },{
          value: '11',
          label: '未放款'
        },{
          value: '21',
          label: '未放款-额度超限'
        },{
          value: '31',
          label: '放款完成'
        },{
          value: '41',
          label: '取消放款'
        },

        ],
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
        this.listQuery.status = this.filter.status === ''?0:this.filter.status;
        this.listQuery.loanDate = this.filter.date?this.filter.date:'';
        debugger
        this.listLoading = true;
        this.api({
          url: '/loan/spv/list',
          method: 'get',
          headers: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.data;
          this.totalCount = data.totalItems;
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
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val
        this.getList();
      },
      handleCurrentChange(val) {
        //改变页码
        this.listQuery.pageNum = val
        this.getList();
      }/*,
      clearCondition(){
        debugger
        this.$refs.selectStatus.call("clear")

      }*/,
      showConfirm(){//确认是否放款
        if(this.selected.length > 0){
          this.$confirm("确定对此单合同放款？", '提示', {
            type: 'warning'
          }).then(() => {
            // 调用后台接口放款
            this.api({
              url: '/loan/spv/downSpv',
              method: 'post',
              data: {"ids":this.selected,"userName":this.$store.getters.userInfo.userName,"userCode":this.$store.getters.userInfo.userCode}
            }).then(() => {
              this.$message({
                type: 'success',
                message: '放款成功',
                onClose: () => {
                  this.getList();// 重新加载
                }
              })
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
                this.$message.error(`保存数据失败：${e.msg}`)
              }
            });
          });
        }else{
          this.$message.warning('未选择记录');
        }
      },
      createRole(){

      },
      updateRole(){// 更新

      }

    }
  }
</script>

