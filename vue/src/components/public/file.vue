<!-- 文件管理 -->
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <span style="margin-left: 10px;font-weight: bolder">Bucket：</span>
          <el-select size="small" clearable v-model="filter.bucket" :placeholder="options[0]" @change="getList">
            <el-option
              v-for="item in options"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>

          <el-button type="primary" icon="el-icon-search" v-if="hasPerm('file:list')" @click="getList" size="small">查询
          </el-button>
          <el-button type="primary" icon="el-icon-plus" style="float: right;margin-top: 4px;" v-if="hasPerm('file:upload')" @click="dialogFormVisible = true" size="small">上传文件</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table ref="multipleTable" :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row @row-click="selectCurow" @selection-change="handleSelectionChange">
      <el-table-column align="center" label="序号" width="50">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column  align="center" label="Bucket" prop="bucketName" width="120"/>
      <el-table-column  align="center" label="文件名" prop="key" width="250"/>
      <el-table-column  align="center" label="访问url" prop="url">
        <!--<template slot-scope="scope">
          <a :href="scope.row.url" target="_blank" text-decoration="underline">{{scope.row.url}}</a>
        </template>-->
      </el-table-column>
      <el-table-column  align="center" label="类型" width="100">
        <template slot-scope="scope">
          <!--<el-tag type="info" v-if="scope.row.moneyDownStatus===1">未放款</el-tag>-->
          <el-tag v-if="scope.row.size===0">文件夹</el-tag>
          <el-tag type="success" v-else="">文件</el-tag>
        </template>
      </el-table-column>
      <el-table-column  align="center" label="大小" prop="strSize" width="100"/>
      <el-table-column  align="center" label="上传时间" prop="lastModified" width="250"/>
      <el-table-column  align="center" label="管理" width="180">
        <template slot-scope="scope">
          <el-button type="primary" size="small" icon="edit" @click="downloadFile(scope.row)" v-if="hasPerm('file:download')">下载</el-button>
          <el-button type="danger" size="small" icon="delete" @click="deleteFile(scope.row)" v-if="hasPerm('file:delete')">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="filter.pageNum"
      :page-size="filter.pageRow"
      :total="totalCount"
      :page-sizes="[20, 50, 50, 100,500,1000]"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 新增/编辑对话框 -->
    <el-dialog title="上传文件" :visible.sync="dialogFormVisible" width="20%" center>
      <el-form class="small-space" label-position="left" style="margin-left: 70px">
        <el-upload
          class="upload-demo"
          ref="upload"
          :action="'/ms-api/public/file/upload/'+this.filter.bucket"
          :auto-upload="false">
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
        </el-upload>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
    </el-dialog>
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
        options: [],// bucket列表
        filter: {
          pageNum: 1,
          pageRow: 10,
          bucket: '',// bucket
          startFileName: null//分页开始的文件名
        },
        selected: [],// 选择的表格行
        dialogFormVisible: false
      };
    },
    created(){
      this.getBucketList();
//      this.getList();
    },
    methods:{
      getBucketList(){
        this.api({
          url: '/public/file/list/buckets',
          method: 'get'
        }).then(data => {
          this.options = data;
          this.filter.bucket = data[0];
          this.getList();
        }).catch(e => {
          this.handlerException(e);
        });
      },
      submitUpload() {
        this.$refs.upload.submit();
        this.dialogFormVisible = false;
        this.getList();
      },
      handlerException(e){// 处理异常
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
      },
      getList(){
        this.listLoading = true;
        this.api({
          url: '/public/file/list/'+this.filter.bucket+'/'+this.filter.pageRow+'?startFileName='+this.filter.startFileName,
          method: 'get',
        }).then(data => {
          this.listLoading = false;
          this.list = data.fileInfos;
          this.totalCount = data.totalCount;
        }).catch(e => {
          this.handlerException(e);
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
        this.filter.pageRow = val;
        if(this.filter.pageNum !== 1){
          this.filter.startFileName = this.list[val-1].key;
        }else{
          this.filter.startFileName = null;
        }
        this.getList();
      },
      handleCurrentChange(val) {
        //改变页码 NOTE 这里往前翻页会有bug，因为阿里云oss并不提供实际的分页(只是从某个文件开始显示多少个文件)，这种机制在往前翻页时无法确定起始文件，所以只有回到起始页
        if(this.filter.pageNum > val) {// 所有从后往前翻的都直接跳到第一页
          val = 1;
          this.filter.pageNum = val;
          console.log('记录一下，这是由于阿里云oss伪分页限制');
          this.$message.info("从后往前翻页直接回到首页");
        }else if(val - this.filter.pageNum >1){// 所有跨数字翻页的都只跳到下一页
          this.filter.pageNum += this.filter.pageNum;
          val = this.filter.pageNum;
          this.$message.info("跨页跳转只跳转到下一页");
        }else{
          this.filter.pageNum = val;
        }
        if(val !== 1){
          this.filter.startFileName = this.list[this.filter.pageRow-1].key;
        }else{
          this.filter.startFileName = null;
        }
        this.getList();
      }/*,
      clearCondition(){
        debugger
        this.$refs.selectStatus.call("clear")

      }*/,
      downloadFile(row){// 文件下载
        // 因为ajax无法通过返回二进制数据的方式实现浏览器弹窗下载，所以直接使用浏览器跳转到这个页面的方式实现
        window.location.href ='/ms-api/public/file/download/'+this.filter.bucket+'?fileName='+row.key;
        /*this.api({
          method: 'GET',
          url: '/public/file/download/'+this.filter.bucket+'?fileName='+row.key,
        });*/
      },
      deleteFile(row){// 删除文件
        debugger
        this.$confirm('确定删除此用户?', '提示', {
          confirmButtonText: '确定',
//          showCancelButton: true,
          type: 'warning'
        }).then(() => {
          this.api({
            method: 'DELETE',
            url: '/public/file/delete/'+this.filter.bucket,
            data: [row.key]
          }).then(() =>{
            this.getList();
          }).catch(() => {
            _vue.$message.error("删除失败")
          });
        });

      }

    }
  }
</script>

