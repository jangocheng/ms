<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('user:add')" @click="showCreate">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="昵称" prop="nickName" style="width: 60px;"/>
      <el-table-column align="center" label="用户名" prop="userName" style="width: 60px;"/>
      <el-table-column align="center" label="手机号码" prop="phoneno" style="width: 60px;"/>
      <el-table-column align="center" label="邮箱" prop="email" style="width: 60px;"/>
      <el-table-column align="center"  label="角色" width="100" prop="roleInfo.id">
        <template slot-scope="scope">
          <!--<el-tag type="success" v-text="$store.getters.role" v-if="scope.row.roleId===1"></el-tag>--><!-- v-else-->
          <el-tag type="primary" v-text="scope.row.roleInfo.roleName"></el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="启用状态">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status===1">启用</el-tag>
          <el-tag type="warning" v-else>未启用</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" width="170"></el-table-column>
      <el-table-column align="center" label="最近修改时间" prop="updateTime" width="170"></el-table-column>
      <el-table-column align="center" label="管理" width="220" v-if="hasPerm('user:update') || hasPerm('user:delete')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button type="danger" icon="delete" v-if="hasPerm('user:delete') && scope.row.id!=userInfo.id "
                     @click="removeUser(scope.$index)">删除
          </el-button>
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempUser" label-position="left" label-width="80px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="用户名" required v-if="dialogStatus=='create'">
          <el-input type="text" v-model="tempUser.username">
          </el-input>
        </el-form-item>
        <el-form-item label="角色" required>
          <el-select v-model="tempUser.roleId" placeholder="请选择">
            <el-option
              v-for="item in roles"
              :key="item.id"
              :label="item.roleName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="昵称" required>
          <el-input type="text" v-model="tempUser.nickname">
          </el-input>
        </el-form-item>
        <el-form-item label="手机号码" required v-if="dialogStatus=='create'">
          <el-input type="text" v-model="tempUser.phoneNo">
          </el-input>
        </el-form-item>
        <el-form-item label="邮箱地址" required v-if="dialogStatus=='create'">
          <el-input type="text" v-model="tempUser.email">
          </el-input>
        </el-form-item>
        <el-form-item label="密码" v-if="dialogStatus=='create'" required>
          <el-input type="password" v-model="tempUser.password">
          </el-input>
        </el-form-item>
        <el-form-item label="新密码" v-else>
          <el-input type="password" v-model="tempUser.password" placeholder="不填则表示不修改">
          </el-input>
        </el-form-item>
        <el-form-item label="启用状态">
          <el-switch v-model="tempUser.status" activeColor="#13ce66" inactive-color="#ff4949"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createUser">创 建</el-button>
        <el-button type="primary" v-else @click="updateUser">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'
  import md5 from 'js-md5';
  import ElSwitch from "../../../node_modules/element-ui/packages/switch/src/component.vue";

  export default {
    components: {ElSwitch},
    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          pageNum: 1,//页码
          pageRow: 50,//每页条数
        },
        roles: [],//角色列表
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建用户'
        },
        tempUser: {
          username: '',
          phoneNo: '',
          email: '',
          password: '',
          nickname: '',
          roleId: '',
          userId: '',
          status: ''
        }
      }
    },
    created() {
      this.getList();
      if (this.hasPerm('user:add') || this.hasPerm('user:update')) {
        this.getAllRoles();
      }
    },
    computed: {
      ...mapGetters([
        'userInfo'
      ])
    },
    methods: {
      getAllRoles() {
        this.api({
          url: "/user/getAllRoles",
          method: "get"
        }).then(data => {
          this.roles = data;
        })
      },
      getList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/user/list/"+this.$store.getters.roleInfo.id,
          method: "get",
          headers: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.data;
          this.totalCount = data.totalSize;
        })/*.catch(e => {
          console.error(e);
          this.$message('数据加载失败:'+e);
          this.listLoading = false;
        })*/.catch(e => {
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
        })
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
      handleFilter() {
        //查询事件
        this.listQuery.pageNum = 1
        this.getList()
      },
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      showCreate() {
        //显示新增对话框
        this.tempUser.username = "";
        this.tempUser.phoneNo = "";
        this.tempUser.email = "";
        this.tempUser.password = "";
        this.tempUser.nickname = "";
        this.tempUser.roleId = "";
        this.tempUser.userId = "";
        this.tempUser.status = 0;
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        let user = this.list[$index];
        //this.tempUser = user;
        this.tempUser.userId = user.id;
        this.tempUser.roleId = user.roleInfo.id;
        this.tempUser.phoneNo = user.phoneno;
        this.tempUser.nickname = user.nickName;
        this.tempUser.status = user.status===1?true:false;
        this.tempUser.username = user.userName;
        this.tempUser.password = '';
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createUser() {
        if(this.validateForm()){
          this.tempUser.password = md5(this.tempUser.password+this.tempUser.username);
          //添加新用户
          this.api({
            url: "/user/add",
            method: "post",
            data: this.tempUser
          }).then(() => {
            this.$message.success("添加成功");
            this.getList();
            this.dialogFormVisible = false
          })
        }else{
          this.$message({
            message: '请填写必要信息',
            type: 'warning',
            duration: 1 * 1000
          })
        }
      },
      validateForm(){
        if(!this.tempUser.username){
          return false;
        }
        return true;

      },
      updateUser() {
        //修改用户信息
        this.tempUser.password = md5(this.tempUser.password+this.tempUser.username);
        this.api({
          url: "/user/update",
          method: "put",
          data: this.tempUser
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          if (this.userId === this.tempUser.userId) {
            msg = '修改成功,部分信息重新登录后生效'
          }
          this.$message({
            message: msg,
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              this.getList();
            }
          })
        })
      },
      removeUser($index) {
        let _vue = this;
        this.$confirm('确定删除此用户?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let user = _vue.list[$index];
//          user.status = '2';
          _vue.api({
            url: "/user/delete",
            method: "delete",
            data: user
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      },
    }
  }
</script>
