<template>
  <a-layout-header class="header">
    <a-row :wrap="false">
      <!-- 左侧：Logo和标题 -->
      <a-col flex="200px">
        <RouterLink to="/">
          <div class="header-left">
            <img class="logo" src="@/assets/logo.png" alt="Logo" />
            <h1 class="site-title">AI生成前端应用</h1>
          </div>
        </RouterLink>
      </a-col>
      <!-- 中间：导航菜单 -->
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="menuItems"
          @click="handleMenuClick"
        />
      </a-col>
      <!-- 右侧：用户操作区域 -->
      <a-col>
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            <a-dropdown>
              <a-space>
                <a-avatar :src="loginUserStore.loginUser.userAvatar" />
                {{ loginUserStore.loginUser.userName ?? '无名' }}
              </a-space>
              <template #overlay>
                <a-menu>
                  <a-menu-item @click="showUserCenter">
                    <UserOutlined />
                    个人信息
                  </a-menu-item>
                  <a-menu-divider />
                  <a-menu-item @click="dologout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" @click="router.push('/user/login')">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </a-layout-header>

  <!-- 个人中心编辑模态框 -->
  <Modal
    v-model:open="isUserCenterModalVisible"
    title="个人信息"
    okText="保存"
    cancelText="取消"
    @ok="handleSaveUserInfo"
    @cancel="handleCancelEdit"
    width="600px"
  >
    <Form ref="userFormRef" :model="userForm" layout="vertical">
      <Form.Item
        label="用户名"
        name="userName"
        :rules="[{ required: true, message: '请输入用户名' }]"
      >
        <Input v-model:value="userForm.userName" placeholder="请输入用户名" />
      </Form.Item>

      <Form.Item label="头像URL" name="userAvatar">
        <Input v-model:value="userForm.userAvatar" placeholder="请输入头像URL" />
      </Form.Item>

      <Form.Item label="个人简介" name="userProfile">
        <Input.TextArea
          v-model:value="userForm.userProfile"
          placeholder="请输入个人简介"
          :rows="4"
        />
      </Form.Item>
    </Form>
  </Modal>
</template>

<script setup lang="ts">
import { computed, h, ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { MenuProps, message, Modal, Form, Input, Select } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { HomeOutlined, LogoutOutlined, UserOutlined } from '@ant-design/icons-vue'
import { userLogout, updateUser } from '@/api/userController.ts'
import { cloneDeep } from 'lodash-es'
import checkAccess from '@/access/checkAccesss'
import ACCESS_ENUM from '@/access/accessEnum'

// 用户注销
const dologout = async () => {
  const res = await userLogout()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}

const loginUserStore = useLoginUserStore()

const router = useRouter()
// 当前选中菜单
const selectedKeys = ref<string[]>(['/'])
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  selectedKeys.value = [to.path]
})

// 菜单配置项
const originItems = [
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '首页',
    title: '首页',
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
    title: '用户管理',
  },
]

// // 过滤菜单项
// const filterMenus = (menus = [] as MenuProps['items']) => {
//   return menus?.filter((menu) => {
//     const menuKey = menu?.key as string
//     if (menuKey?.startsWith('/admin')) {
//       const loginUser = loginUserStore.loginUser
//       if (!loginUser || loginUser.userRole !== 'admin') {
//         return false
//       }
//     }
//     return true
//   })
// }

/**
 * 将菜单配置转换为路由项格式
 * @param {any} menu - 菜单配置对象
 * @returns {Object} 转换后的路由项对象
 */
const menuToRouteItem = (menu: any) => {
  // 创建路由项对象，保留原始菜单的关键属性
  const routeItem = {
    ...menu,
    meta: {
      // 默认元数据
      hideInMenu: false,
      access: '',
      // 如果菜单中已有meta属性，则合并
      ...menu.meta,
    },
  }

  // 根据菜单key确定访问权限
  if (menu.key && menu.key.startsWith('/admin')) {
    routeItem.meta.access = ACCESS_ENUM.ADMIN
  }

  return routeItem
}

/**
 * 根据权限过滤菜单项
 * @param {MenuProps['items']} menus - 菜单项数组
 * @returns {MenuProps['items']} 过滤后的菜单项数组
 */
const items = (menus: MenuProps['items']) => {
  return menus.filter((menu) => {
    // 将menu转换为路由item格式
    const item = menuToRouteItem(menu)
    if (item.meta?.hideInMenu) {
      return false
    }
    // 根据权限过滤菜单，有权限则返回 true，保留该菜单
    return checkAccess(loginUserStore.loginUser, item.meta?.access as string)
  })
}

// 展示在菜单的路由数组
const menuItems = computed<MenuProps['items']>(() => items(originItems))

// 处理菜单点击
const handleMenuClick: MenuProps['onClick'] = (e) => {
  const key = e.key as string
  selectedKeys.value = [key]
  // 跳转到对应页面
  if (key.startsWith('/')) {
    router.push(key)
  }
}

// 个人中心相关状态
const isUserCenterModalVisible = ref(false)
const userForm = reactive({
  userName: '',
  userAvatar: '',
  userProfile: '',
})
const userFormRef = ref()

/**
 * 显示个人中心编辑表单
 */
const showUserCenter = () => {
  // 初始化表单数据
  const currentUser = loginUserStore.loginUser
  if (currentUser) {
    userForm.userName = currentUser.userName || ''
    userForm.userAvatar = currentUser.userAvatar || ''
    userForm.userProfile = currentUser.userProfile || ''
  }
  isUserCenterModalVisible.value = true
}

/**
 * 保存用户信息
 */
const handleSaveUserInfo = async () => {
  try {
    // 调用后端API更新用户信息
    const res = await updateUser({
      id: loginUserStore.loginUser.id,
      userName: userForm.userName,
      userAvatar: userForm.userAvatar,
      userProfile: userForm.userProfile,
      userRole: loginUserStore.loginUser.userRole,
    })

    if (res.data.code === 0) {
      message.success('更新成功')
      // 更新本地存储的用户信息
      const updatedUser = cloneDeep(loginUserStore.loginUser)
      updatedUser.userName = userForm.userName
      updatedUser.userAvatar = userForm.userAvatar
      updatedUser.userProfile = userForm.userProfile
      loginUserStore.setLoginUser(updatedUser)

      // 关闭模态框
      isUserCenterModalVisible.value = false
    } else {
      message.error('更新失败：' + (res.data.message || '未知错误'))
    }
  } catch (error) {
    message.error('更新过程中发生错误')
    console.error('更新用户信息失败:', error)
  }
}

/**
 * 取消编辑
 */
const handleCancelEdit = () => {
  isUserCenterModalVisible.value = false
}
</script>

<style scoped>
.header {
  background: #fff;
  padding: 0 24px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  height: 48px;
  width: 48px;
}

.site-title {
  margin: 0;
  font-size: 18px;
  color: #1890ff;
}

.ant-menu-horizontal {
  border-bottom: none !important;
}
</style>
