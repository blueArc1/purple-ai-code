import ACCESS_ENUM from "./accessEnum";

/**
 * 检查用户是否有访问权限
 * @param loginUser 当前登录用户
 * @param needAccess 需要的访问权限
 * @returns 有无权限
 */
const checkAccess = (loginUser: any, needAccess = ACCESS_ENUM.NOT_LOGIN) => {
  // 获取当前登录用户具有的权限（如果没有 loginUser，则表示未登录）
  const loginUserAccess = loginUser?.userRole ?? ACCESS_ENUM.NOT_LOGIN;
  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    return true;
  }
  // 如果用户登录才能访问
  if (needAccess === ACCESS_ENUM.USER) {
    // 如果用户没登录，那么表示无权限
    if (loginUserAccess === ACCESS_ENUM.USER) {
      return false;
    }
  }
  // 如果是管理员才能访问
  if (needAccess === ACCESS_ENUM.ADMIN) {
    // 如果用户不是管理员，那么表示无权限
    if (loginUserAccess !== ACCESS_ENUM.ADMIN) {
      return false;
    }
  }
  return true;
}

export default checkAccess;
