<template>
  <div class="app-container">
    <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-width="300px">
      <el-form-item label="每天可获得次数" prop="number">
        <el-input v-model="dataForm.number"/>
      </el-form-item>
      <el-form-item label="每次可获得积分" prop="credits">
        <el-input v-model="dataForm.credits"/>
      </el-form-item>
      <el-form-item label="多少积分等于1元" prop="creditsRatio">
        <el-input v-model="dataForm.creditsRatio"/>
      </el-form-item>
      <el-form-item>
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="update">确定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getCreditsRule, saveCreditsRule } from '@/api/credits'

export default {
  name: 'CreditsRule',
  data() {
    return {
      dataForm: {
        number: '',
        credits: '',
        credits_ratio: ''
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    init: function() {
      getCreditsRule().then(response => {
        this.dataForm = response.data.data
      })
    },
    cancel() {
      this.init()
    },
    update() {
      saveCreditsRule(this.dataForm)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '积分规则配置成功'
          })
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    }
  }
}
</script>
