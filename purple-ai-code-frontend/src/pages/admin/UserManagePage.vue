<template>
  <div id="userManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
      </a-form-item>
      <a-form-item>
        <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider />
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="doTableChange"
      rowKey="id"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <template v-if="editableData[record.id]">
            <a-input v-model:value="editableData[record.id].userAvatar" placeholder="输入头像URL" />
          </template>
          <template v-else>
            <a-image :src="record.userAvatar" :width="50" />
          </template>
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <template v-if="editableData[record.id]">
            <a-select v-model:value="editableData[record.id].userRole" style="width: 100px">
              <a-select-option value="admin">管理员</a-select-option>
              <a-select-option value="user">普通用户</a-select-option>
            </a-select>
          </template>
          <template v-else>
            <div v-if="record.userRole === 'admin'">
              <a-tag color="green">管理员</a-tag>
            </div>
            <div v-else>
              <a-tag color="blue">普通用户</a-tag>
            </div>
          </template>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <div class="editable-row-operations" style="display: flex; gap: 8px; align-items: center">
            <template v-if="editableData[record.id]">
              <a-button type="primary" size="small" @click="save(record.id)">保存</a-button>
              <a-popconfirm title="确定撤销吗?" @confirm="cancel(record.id)">
                <a-button size="small">取消</a-button>
              </a-popconfirm>
            </template>
            <template v-else>
              <a-button type="primary" size="small" @click="edit(record.id)">编辑</a-button>
              <a-button danger size="small" @click="doDelete(record.id)">删除</a-button>
            </template>
          </div>
        </template>
        <template v-else-if="['userAccount', 'userName', 'userProfile'].includes(column.dataIndex)">
          <template v-if="editableData[record.id]">
            <a-input
              v-model:value="editableData[record.id][column.dataIndex]"
              :placeholder="column.title"
            />
          </template>
          <template v-else>
            {{ record[column.dataIndex] }}
          </template>
        </template>
        <template v-else>
          {{ record[column.dataIndex] }}
        </template>
      </template>
    </a-table>
  </div>
</template>
<script lang="ts" setup>
import { computed, onMounted, reactive, ref, type UnwrapRef } from 'vue'
import { listUserVoByPage, deleteUser, updateUser } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { cloneDeep } from 'lodash-es'
const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    width: 180,
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    width: 120,
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    width: 120,
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
    width: 80,
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
    width: 200,
    ellipsis: true, // 文本过长自动显示省略号
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    width: 100,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: 160,
    fixed: 'right',
  },
]

// 数据
const data = ref<API.UserVO[]>([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 3,
})

// 获取数据
const fetchData = async () => {
  const res = await listUserVoByPage({
    ...searchParams,
  })
  if (res.data.data) {
    data.value = res.data.data.records ?? []
    total.value = res.data.data.totalRow ?? 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`,
  }
})

// 表格变化处理
const doTableChange = (page: any) => {
  searchParams.pageNum = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

// 搜索数据
const doSearch = () => {
  searchParams.pageNum = 1
  fetchData()
}

// 删除数据
const doDelete = async (id: number) => {
  if (!id) {
    return
  }
  const res = await deleteUser({ id })
  if (res.data.code === 0) {
    message.success('删除成功')
    // 刷新数据
    fetchData()
  } else {
    message.error('删除失败')
  }
}

// 修改数据
const editableData: UnwrapRef<Record<string, API.UserVO>> = reactive({})

const edit = (id: number) => {
  editableData[id] = cloneDeep(data.value.filter((item) => id === item.id)[0])
}

/**
 * 保存修改的数据到后端
 * @param id 要保存的用户记录ID
 */
const save = async (id: number) => {
  try {
    const userToUpdate = editableData[id]
    if (!userToUpdate) {
      message.error('找不到要更新的数据')
      return
    }

    // 调用后端API更新用户信息
    const res = await updateUser({
      id: userToUpdate.id,
      userName: userToUpdate.userName,
      userAvatar: userToUpdate.userAvatar,
      userProfile: userToUpdate.userProfile,
      userRole: userToUpdate.userRole,
    })

    if (res.data.code === 0) {
      message.success('更新成功')
      // 刷新数据列表
      fetchData()
    } else {
      message.error('更新失败：' + (res.data.message || '未知错误'))
    }
  } catch (error) {
    message.error('更新过程中发生错误')
    console.error('更新用户失败:', error)
  } finally {
    // 无论成功与否，都清除编辑状态
    delete editableData[id]
  }
}

/**
 * 取消编辑操作
 * @param id 要取消编辑的用户记录ID
 */
const cancel = (id: number) => {
  delete editableData[id]
}
</script>

