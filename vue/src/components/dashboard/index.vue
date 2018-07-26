<template>
  <div class="dashboard-container" id="userInfo">
    <template>
      <!--<el-carousel :interval="2000" type="card" height="600px">
        <el-carousel-item v-for="item in items" :key="item">
          <img :src="item"/>
        </el-carousel-item>
      </el-carousel>-->
      <!--<div>
        <label for="xxx">用户名：</label>
      </div>-->
      <div id="userDetail">
        <h2 style="text-align: center;">个人信息</h2>
        <table>
          <tr>
            <td>用户名</td>
            <td class="info">{{this.$store.getters.userInfo.userName}}</td>
          </tr>
          <tr>
            <td>手机号</td>
            <td class="info">{{this.$store.getters.userInfo.phoneno}}</td>
          </tr>
          <tr>
            <td>邮箱地址</td>
            <td class="info">{{this.$store.getters.userInfo.email}}</td>
          </tr>
          <tr>
            <td>用户别名</td>
            <td class="info">{{this.$store.getters.userInfo.nickName}}</td>
          </tr>
          <tr>
            <td>角色名</td>
            <td class="info">{{this.$store.getters.roleInfo.roleName}}</td>
          </tr>
        </table>

      </div>
    </template>
    </div>
</template>
<!--<script>
  import ElFormItem from "../../../node_modules/element-ui/packages/form/src/form-item.vue";
  import ElHeader from "../../../node_modules/element-ui/packages/header/src/main.vue";

  export default {
    components: {
      ElHeader,
      ElFormItem},
    name: 'dashboard',

    data() {
      return {
        items: ["http://img2.cgwon.com/b/img_1565376.jpg","https://b.zol-img.com.cn/soft/6/304/ceQCzVIZ1vhs.bmp","http://i1.3conline.com/images/piclib/201009/10/batch/1/69162/1284081549234dqw7w00gcg.jpg"]
      }
    },

  }
</script>-->

<style>
  #userInfo{
    margin:0;
    padding:0;
    width:100%;
    height:100%;
  }
  #userDetail{
    margin:0 auto;
    width:500px;
    height:auto;
  }
  #userDetail table{
    margin:0 auto;
    font-weight: bolder;
  }
  #userDetail .info{
    width: 300px;
    text-align: right;
    color: blue;
  }
  /*.el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 500px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }*/
</style>
