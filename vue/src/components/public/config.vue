<!--系统数据列表 -->
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('config:add')" @click="showCreate">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="配置项">
              <span>{{ props.row.data }}</span>
            </el-form-item>
            <el-form-item label="扩展项一">
              <span>{{ props.row.data1 }}</span>
            </el-form-item>
            <el-form-item label="扩展项二">
              <span>{{ props.row.data2 }}</span>
            </el-form-item>
            <el-form-item label="扩展项三">
              <span>{{ props.row.data3 }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="类型" prop="type" style="width: 60px;"/>
      <el-table-column align="center" label="类型描述" prop="desc" style="width: 60px;"/>
      <el-table-column align="center" label="编码" prop="code" style="width: 60px;"/>
      <el-table-column align="center" label="创建时间" prop="createTime" style="width: 60px;"/>
      <el-table-column align="center" label="管理" width="220" v-if="hasPerm('config:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.row)" v-if="hasPerm('config:update')">修改</el-button>
        </template>
      </el-table-column>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="data" label-position="left" label-width="120px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="类型" required>
          <el-input type="text" disabled v-model="data.type">
          </el-input>
        </el-form-item>
        <el-form-item label="类型描述" required>
          <el-input type="text" disabled v-model="data.desc">
          </el-input>
        </el-form-item>
        <el-form-item label="编码" required>
          <el-input type="text" disabled v-model="data.code">
          </el-input>
        </el-form-item>
        <el-form-item label="配置数据" required>
          <el-input type="textarea" v-model="data.data" style="width: 300px;">
          </el-input>
        </el-form-item>
        <el-form-item label="扩展配置项一">
          <el-input type="textarea" v-model="data.data1" style="width: 300px;">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createConfig">创 建</el-button>
        <el-button type="primary" v-else @click="updateRole">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data(){
      return {
        totalCount: 0,//总条目数
        list: [],// 表格数据
        listLoading: false,
        listQuery: {
          pageNum: 1,
          pageRow: 50
        },
        data: {
          id: "",
          code: "",
          desc: "",
          type: "",
          data: "",
          data1: "",
          data2: "",
          data3: ""
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '修改配置',
          create: '新建配置'
        }
      };
    },
    created(){
      this.getList();
    },
    methods:{
      getList(){
        this.listLoading = true;
        this.api({
          url: '/public/config/list',
          method: 'get',
          headers: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data;
          console.log(this.list)
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
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val
        this.handleFilter();
      },
      handleCurrentChange(val) {
        //改变页码
        this.listQuery.pageNum = val
        this.getList();
      },
      showCreate(){
        this.clearData();
        this.dialogStatus = 'create';
        this.dialogFormVisible = true;
      },
      clearData(){
        this.data.id="";
        this.data.code="";
        this.data.desc="";
        this.data.type="";
        this.data.data="";
        this.data.data1="";
        this.data.data2="";
        this.data.data3="";
      },
      createConfig(){
      },
      validateForm(){// 表单校验
        if(this.data.code === '') {
          return false;
        }
        if(this.data.type === '') {
          return false;
        }
        if(this.data.desc === '') {
          return false;
        }
        if(this.data.data === '') {
          return false;
        }
        return true;
      },
      showUpdate(configInfo){
        this.data.id=configInfo.id;
        this.data.code=configInfo.code;
        this.data.desc=configInfo.desc;
        this.data.type=configInfo.type;
        this.data.data=configInfo.data;
        this.data.data1=configInfo.data1;
        this.data.data2=configInfo.data2;
        this.data.data3=configInfo.data3;
        this.dialogStatus = 'update';
        this.dialogFormVisible = true;
      },
      closeDialog(){
        this.dialogFormVisible = false
        this.clearData();

      },
      updateRole(){// 更新
        if(this.validateForm()) {
          this.api({
            url: '/public/config/update',
            method: 'PUT',
            data: this.data,
          }).then(() => {
            this.$message.success("修改成功,权限刷新需要重新登录");
            this.getList();
            this.dialogFormVisible = false
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
              this.$message.error(`保存数据失败：${e.code}`)
            }
          });
        }else{
          this.$message.warning("请填写必要信息");
        }
      },
    }
  }
</script>
<style>
  .el-tree{
    background: transparent !important;
    color: inherit !important;
  }
  textarea{
    min-height: 130px;
  }
  .el-checkbox__input.is-disabled.is-checked .el-checkbox__inner {
    background-color: green;
    border-color: green;
  }
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
