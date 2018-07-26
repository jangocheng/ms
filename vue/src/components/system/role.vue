<!--角色列表 -->
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('role:add')" @click="showCreate">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="旗下用户">
                <el-tag v-if="props.row.userList.length === 0" type="danger">无 </el-tag>
                <el-tag v-else v-for="userInfo in props.row.userList" :key="userInfo.id" type="primary" >{{userInfo.userName}}</el-tag>
            </el-form-item>
            <el-form-item label="拥有权限">
              <el-tag v-if="props.row.permissionList.length === 0" type="danger">无 </el-tag>
              <el-tree
                :data="buildTree(list[0].permissionList,true)"
                show-checkbox
                check-strictly
                node-key="id"
                :default-checked-keys="props.row.existsPermissons"
                v-else
              />
              <!--<el-tag v-else v-for="permissionInfo in props.row.permissionList" type="primary" >{{permissionInfo.pName}}</el-tag>-->
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="编码" prop="code" style="width: 60px;"/>
      <el-table-column align="center" label="角色名" prop="roleName" style="width: 60px;"/>
      <el-table-column align="center" label="描述" prop="desc" style="width: 60px;"/>
      <el-table-column align="center" label="启用状态">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status==1">启用</el-tag>
          <el-tag type="warning" v-else>未启用</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" style="width: 60px;"/>
      <el-table-column align="center" label="最近修改时间" prop="updateTime" width="170"></el-table-column>
      <el-table-column align="center" label="管理" width="220" v-if="hasPerm('role:update') || hasPerm('role:delete')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.row)" v-if="hasPerm('role:update')">修改</el-button>
          <!-- 不能删除自己 -->
          <el-button type="danger" icon="delete" @click="removeRole(scope.row)" v-if="scope.row.id != roleInfo.id && hasPerm('role:delete')">删除
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="data" label-position="left" label-width="80px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="编码" required>
          <el-input type="text" v-model="data.code">
          </el-input>
        </el-form-item>
        <el-form-item label="角色名" required>
          <el-input type="text" v-model="data.roleName">
          </el-input>
        </el-form-item>
        <el-form-item label="描述" margin-left="20px;" >
          <el-input type="textarea" v-model="data.desc">
          </el-input>
        </el-form-item>
        <el-form-item label="启用状态">
          <el-switch v-model="data.status" activeColor="#13ce66" inactive-color="#ff4949"/>
        </el-form-item>
        <el-form-item label="对应权限" >
          <!--<el-tree
            :data="permissionList"
            show-checkbox
            node-key="id"
            ref="tree"
            v-if="dialogStatus=='create'"
            >
          </el-tree>-->
          <el-tree
            :data="permissionList"
            show-checkbox
            node-key="id"
            ref="tree"
            check-strictly
            :default-checked-keys="data.selectedPermission"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createRole">创 建</el-button>
        <el-button type="primary" v-else @click="updateRole">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'
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
          roleName: "",
          desc: "",
          status: "",
          selectedPermission: [],
        },
        permissionList: null,
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建角色'
        }
      };
    },
    created(){
      this.getList();
    },
    computed: {
      ...mapGetters([
        'roleInfo'
      ])
    },
    methods:{
      buildTree(permissionList,disabled){// 权限树形结构
        for(let index in permissionList){
          let permission = permissionList[index];
          permission.label = permission.pName;
          permission.disabled = disabled;
          if(permission.children.length > 0){
            this.buildTree(permission.children,disabled);
          }
        }
        return permissionList;
      },
      checkChange(){// 选中的权限
        console.log(this.$refs.tree.getCheckedKeys())

      },
      getList(){
        this.listLoading = true;
        this.api({
          url: '/role/list',
          method: 'get',
          headers: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.roleInfo.data;
          this.permissionList = this.buildTree(data.permissionList,false);
          console.log(this.list)
          this.totalCount = data.totalSize;
        }).catch(e => {
          this.$message.error(`数据加载失败：${e.code}`)
          this.listLoading = false;
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
        this.data.roleName="";
        this.data.desc="";
        this.data.status="";
        this.data.selectedPermission=[];
      },
      createRole(){
        this.data.selectedPermission = this.$refs.tree.getCheckedKeys();
        if(this.validateForm()) {
//          console.log(this.data)
          this.api({
            url: '/role/add',
            method: 'POST',
            data: this.data,
          }).then(() => {
            this.$message.success("添加成功");
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
      validateForm(){// 表单校验
        if(this.data.code === '') {
          return false;
        }
        if(this.data.roleName === '') {
          return false;
        }
        if(this.data.code === '') {
          return false;
        }
        return true;
      },
      showUpdate(roleInfo){
        this.data.id= roleInfo.id;
        this.data.code= roleInfo.code;
        this.data.roleName= roleInfo.roleName;
        this.data.desc= roleInfo.desc;
        this.data.status= (roleInfo.status === '1'?true:false);
        // 避免树节点选中失效
        if(this.$refs.tree){
          this.$refs.tree.setCheckedKeys(roleInfo.existsPermissons);
        }else{
         this.data.selectedPermission=roleInfo.existsPermissons;
        }
        this.dialogStatus = 'update';
        this.dialogFormVisible = true;
      },
      closeDialog(){
        this.dialogFormVisible = false
        this.clearData();
        this.$refs.tree.setCheckedNodes([]);

      },
      updateRole(){// 更新
        this.data.selectedPermission = this.$refs.tree.getCheckedKeys();
        if(this.validateForm()) {
          this.api({
            url: '/role/update',
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
      removeRole(roleInfo) {// 删除角色
        let confirmMsg = '确认删除此角色(不可撤销)？';
        if(roleInfo.userList.length > 0){// 如果此角色下存在关联用户，提示
          confirmMsg = `此角色存在${roleInfo.userList.length}个用户，执行此操作将删除用户角色关联！` + confirmMsg;
        }
        this.$confirm(confirmMsg, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.api({
            url: '/role/delete/'+roleInfo.id,
            method: 'delete'
          }).then(() => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            //重新加载
            this.getList();
          }).catch(e => {
            this.$message({
              type: 'error',
              message: '删除失败：'+e.response
            });
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    }
  }
</script>
<style>
  .el-tree{
    background: transparent !important;
    color: inherit !important;
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
