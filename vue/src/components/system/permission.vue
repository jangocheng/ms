<!--权限列表 -->
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('permission:add')" @click="showCreate">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--<el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="权限CODE" prop="pCode" style="width: 60px;"/>
      <el-table-column align="center" label="权限名" prop="pName" style="width: 60px;"/>
      <el-table-column align="center" label="描述" prop="desc" style="width: 60px;"/>
      <el-table-column align="center" label="创建时间" prop="createTime" style="width: 60px;"/>
      <el-table-column align="center" label="最近修改时间" prop="updateTime" width="170"></el-table-column>
      <el-table-column align="center" label="管理" width="220" v-if="hasPerm('permission:update') || hasPerm('permission:delete')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)" v-if="hasPerm('permission:update')">修改</el-button>
          <el-button type="danger" icon="delete" @click="removeUser(scope.$index)" v-if="hasPerm('permission:delete')">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>-->
    <div class="treeGrid">
      <tree-grid :columns="columns" :tree-structure="true" :data-source="dataSource"></tree-grid>
    </div>
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
        <el-form-item label="父级权限">
          <el-tree
            :data="permissionTreeList"
            show-checkbox
            node-key="id"
            ref="tree"
            check-strictly
            :default-checked-keys="[data.parentId]"
          />
        </el-form-item>
        <el-form-item label="权限标志" required v-if="dialogStatus=='create'">
          <el-input type="text" v-model="data.pCode">
          </el-input>
        </el-form-item>
        <el-form-item label="权限名" required>
          <el-input type="text" v-model="data.pName">
          </el-input>
        </el-form-item>
        <el-form-item label="描述" margin-left="20px;" >
          <el-input type="textarea" v-model="data.desc">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createPermission">创 建</el-button>
        <el-button type="primary" v-else @click="updatePermission">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import TreeGrid from '@/components/treeTable'
  export default {
    components: {TreeGrid},
    data(){
      return {
        columns: [
          {
            text: '权限名',
            dataIndex: 'pName'
          },
          {
            text: '权限Code',
            dataIndex: 'pCode'
          },
          {
            text: '描述',
            dataIndex: 'desc'
          },
          {
            text: '创建时间',
            dataIndex: 'createTime'
          },
          {
            text: '最近修改时间',
            dataIndex: 'updateTime'
          }
        ],
        dataSource: [],//树形表格数据
        totalCount: 0,//总条目数
        list: [],// 表格数据
        listLoading: false,
        permissionTreeList: null,
        listQuery: {
          pageNum: 1,
          pageRow: 20
        },
        data: {
          id: "",
          pCode: "",
          pName: "",
          parentId: "",
          desc: "",
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建权限'
        },
      };
    },
    created(){
      this.getList();
    },
    methods:{
      buildTree(permissionList,disabled){// 权限树形结构
        for(let index in permissionList){
          let permission = permissionList[index];
          permission.label = permission.pName;
          permission.disabled = disabled;
          if(permission.children.length > 0){
            this.buildTree(permission.children,disabled);// FIXME 为避免复杂只支持选择一级菜单，如果实在要实现，把true改为false
          }
        }
        return permissionList;
      },
      getList(){
        this.listLoading = true;
        this.api({
          url: '/permission/list',
          method: 'get',
          headers: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.dataSource = data.data;
//          this.list = data.data;
          this.totalCount = data.totalSize;
          this.permissionTreeList = this.buildTree(data.data,false);
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
        this.data.id="";
        this.data.pCode="";
        this.data.pName="";
        this.data.desc="";
        this.data.parentId="";
        this.dialogStatus = 'create';
        this.dialogFormVisible = true;
      },
      validateForm(){// 表单校验
        if(this.$refs.tree.getCheckedKeys().length > 1){
          return "父级菜单可以不选或只能选择一个";
        }
        debugger
        if(this.data.pCode === '' || this.data.pName === '') {
          return "请填写必要信息";
        }
        return null;

      },
      createPermission(){
        // 选中的父级菜单
        if(this.$refs.tree.getCheckedKeys().length > 0){
          this.data.parentId = this.$refs.tree.getCheckedKeys()[0];
        }
        let msg = this.validateForm();// 校验表单输入
        if(!msg) {
          console.log(this.data);
          this.api({// 添加操作
            url: '/permission/add',
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
          this.$message.warning(msg);
        }
      },
      updatePermission(){// 更新

      }

    }
  }
</script>

