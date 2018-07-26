<!--菜单列表 -->
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('menu:add')" @click="showCreate">添加
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
      <el-table-column align="center" label="菜单标志" prop="mCode" style="width: 60px;"/>
      <el-table-column align="center" label="对应路径" prop="mPath" style="width: 60px;"/>
      <el-table-column align="center" label="菜单名" prop="mName" style="width: 60px;"/>
      <el-table-column align="center" label="图标" prop="mIcon" style="width: 60px;"/>
      <el-table-column align="center" label="页面url" prop="mUrl" style="width: 60px;"/>
      <el-table-column align="center" label="父级菜单" prop="parentId" style="width: 60px;"/>
      <el-table-column align="center" label="显示顺序" prop="mOrder" style="width: 60px;"/>
      <el-table-column align="center" label="描述" prop="desc" style="width: 60px;"/>
      <el-table-column align="center" label="创建时间" prop="createTime" width="180"/>
      <el-table-column align="center" label="最近修改时间" prop="updateTime" width="170"></el-table-column>
      <el-table-column align="center" label="管理" width="220" v-if="hasPerm('menu:update') || hasPerm('menu:delete')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)" v-if="hasPerm('menu:update')">修改</el-button>
          <el-button type="danger" icon="delete" @click="removeUser(scope.$index)" v-if="hasPerm('menu:delete')">删除
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
        <el-form-item label="父级菜单">
          <el-tree
            :data="menuTreeList"
            show-checkbox
            node-key="id"
            ref="tree"
            check-strictly
            :default-checked-keys="[data.parentId]"
          />
        </el-form-item>
        <el-form-item label="菜单编码" required v-if="dialogStatus=='create'">
          <el-input type="text" v-model="data.mCode">
          </el-input>
        </el-form-item>
        <el-form-item label="菜单名" required>
          <el-input type="text" v-model="data.mName">
          </el-input>
        </el-form-item>
        <el-form-item label="图标" required>
          <el-radio v-model="data.mIcon" label="example"><svg-icon :icon-class="`example`"/></el-radio>
          <el-radio v-model="data.mIcon" label="eye"><svg-icon :icon-class="`eye`"/></el-radio>
          <el-radio v-model="data.mIcon" label="form"><svg-icon :icon-class="`form`"/></el-radio>
          <el-radio v-model="data.mIcon" label="money"><svg-icon :icon-class="`money`"/></el-radio>
          <el-radio v-model="data.mIcon" label="password"><svg-icon :icon-class="`password`"/></el-radio>
          <el-radio v-model="data.mIcon" label="role"><svg-icon :icon-class="`role`"/></el-radio>
          <el-radio v-model="data.mIcon" label="table"><svg-icon :icon-class="`table`"/></el-radio>
          <el-radio v-model="data.mIcon" label="tree"><svg-icon :icon-class="`tree`"/></el-radio>
          <el-radio v-model="data.mIcon" label="user"><svg-icon :icon-class="`user`"/></el-radio>
        </el-form-item>
        <el-form-item label="跳转URL">
          <el-input type="text" v-model="data.mUrl">
          </el-input>
        </el-form-item>
        <el-form-item label="显示顺序" required>
          <el-input-number v-model="data.mOrder" :min="1" :max="10" label="显示顺序"></el-input-number>
        </el-form-item>
        <el-form-item label="描述" margin-left="20px;" >
          <el-input type="textarea" v-model="data.desc">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createMenu">创 建</el-button>
        <el-button type="primary" v-else @click="updateRole">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import TreeGrid from '@/components/treeTable'
  export default {
    name: "",
    components: {TreeGrid},
    data(){
      return {
        columns: [
          {
            text: '菜单名',
            dataIndex: 'mName'
          },
          {
            text: '菜单Code',
            dataIndex: 'mCode'
          },
          {
            text: '图标',
            dataIndex: 'mIcon'
          },
          {
            text: '跳转url',
            dataIndex: 'mUrl'
          },
          {
            text: '显示顺序',
            dataIndex: 'mOrder'
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
        menuTreeList: null,
        listQuery: {
          pageNum: 1,
          pageRow: 50
        },
        data: {
          id: "",
          mCode: "",
          mName: "",
          mIcon: "",
          mUrl: "",
          parentId: "",
          mOrder: "",
          desc: "",
          status: "",
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建角色'
        },
      };
    },
    created(){
      this.getList();
    },
    methods:{
      edit(id){

      },
      buildTree(menuList,disabled){// 权限树形结构
        for(let index in menuList){
          let menu = menuList[index];
          menu.label = menu.mName;
          menu.disabled = disabled;
          if(menu.children.length > 0){
            this.buildTree(menu.children,true);// FIXME 为避免复杂只支持选择一级菜单，如果实在要实现，把true改为false
          }
        }
        return menuList;
      },
      getList(){
        this.listLoading = true;
        this.api({
          url: '/menu/list',
          method: 'get',
          headers: this.listQuery
        }).then(data => {
          console.log(data);
          this.listLoading = false;
          this.dataSource = data.data;
          this.totalCount = data.totalSize;
          this.menuTreeList = this.buildTree(data.data,false);
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
        this.data.mCode="";
        this.data.mName="";
        this.data.mUrl="";
        this.data.desc="";
        this.data.mOrder="";
        this.data.status="";
        this.data.parentId="";
        this.dialogStatus = 'create';
        this.dialogFormVisible = true;
      },
      createMenu(){
        // 选中的父级菜单
        if(this.$refs.tree.getCheckedKeys().length > 0){
          this.data.parentId = this.$refs.tree.getCheckedKeys()[0];
        }
        let msg = this.validateForm();// 校验表单输入
        if(!msg) {
          console.log(this.data);
          this.api({// 添加操作
            url: '/menu/add',
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
      validateForm(){// 表单校验
        if(this.$refs.tree.getCheckedKeys().length > 1){
          return "父级菜单可以不选或只能选择一个";
        }
        debugger
        if(this.data.mCode === '' || this.data.mName === ''|| this.data.mOrder === '') {
          return "请填写必要信息";
        }
        return null;

      },
      updateRole(){// 更新

      }

    }
  }
</script>
<style>
.el-radio+.el-radio {
  margin-left: 0px;
}
</style>

