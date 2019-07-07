<template>
  <div class="app-container">

    <el-form ref="topic" :rules="rules" :model="topic" status-icon label-position="left" label-width="100px" style="width: 800px; margin-left:50px;">
      <el-form-item label="专题标题" prop="title">
        <el-input v-model="topic.title"/>
      </el-form-item>
      <el-form-item label="专题子标题" prop="subtitle">
        <el-input v-model="topic.subtitle"/>
      </el-form-item>
      <el-form-item label="专题图片" prop="picUrl">
        <el-upload
          :headers="headers"
          :action="uploadPath"
          :show-file-list="false"
          :on-success="uploadPicUrl"
          class="avatar-uploader"
          accept=".jpg,.jpeg,.png,.gif">
          <img v-if="topic.picUrl" :src="topic.picUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"/>
        </el-upload>
      </el-form-item>
      <el-form-item label="专题内容" prop="content">
        <editor :init="editorInit" v-model="topic.content"/>
      </el-form-item>
      <el-form-item label="阅读量" prop="readCount">
        <el-input v-model="topic.readCount"/>
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确定</el-button>
    </div>

  </div>
</template>

<style>
.el-dialog {
  width: 800px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #20a0ff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 145px;
  height: 145px;
  display: block;
}
</style>

<script>
import { createTopic } from '@/api/topic'
import { createStorage, uploadPath } from '@/api/storage'
import BackToTop from '@/components/BackToTop'
import Editor from '@tinymce/tinymce-vue'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { getToken } from '@/utils/auth'

export default {
  name: 'TopicEdit',
  components: { BackToTop, Editor, Pagination },
  data() {
    return {
      uploadPath,
      id: 0,
      topic: {

      },
      addVisiable: false,
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 5,
        id: undefined,
        name: undefined,
        sort: 'add_time',
        order: 'desc'
      },
      selectedlist: [],
      rules: {
        title: [
          { required: true, message: '专题标题不能为空', trigger: 'blur' }
        ],
        subtitle: [
          { required: true, message: '专题子标题不能为空', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '专题内容不能为空', trigger: 'blur' }
        ]
      },
      editorInit: {
        language: 'zh_CN',
        convert_urls: false,
        height: 500,
        plugins: [
          'advlist anchor autolink autosave code codesample colorpicker colorpicker contextmenu directionality emoticons fullscreen hr image imagetools importcss insertdatetime link lists media nonbreaking noneditable pagebreak paste preview print save searchreplace spellchecker tabfocus table template textcolor textpattern visualblocks visualchars wordcount'
        ],
        toolbar: [
          'searchreplace bold italic underline strikethrough alignleft aligncenter alignright outdent indent  blockquote undo redo removeformat subscript superscript code codesample',
          'hr bullist numlist link image charmap preview anchor pagebreak insertdatetime media table emoticons forecolor backcolor fullscreen'
        ],
        images_upload_handler: function(blobInfo, success, failure) {
          const formData = new FormData()
          formData.append('file', blobInfo.blob())
          createStorage(formData)
            .then(res => {
              success(res.data.data.url)
            })
            .catch(() => {
              failure('上传失败，请重新上传')
            })
        }
      }
    }
  },
  computed: {
    headers() {
      return {
        'X-Litemall-Admin-Token': getToken()
      }
    }
  },
  created() {
  },
  methods: {
    handleCancel() {
      this.$router.push({ path: '/promotion/topic' })
    },
    handleSelectionChange(val) {
      this.selectedlist = val
    },
    handleConfirm() {
      this.$refs['topic'].validate(valid => {
        if (valid) {
          createTopic(this.topic).then(response => {
            this.$router.push({ path: '/promotion/topic' })
          })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    },
    uploadPicUrl: function(response) {
      this.topic.picUrl = response.data.url
    }
  }
}
</script>
