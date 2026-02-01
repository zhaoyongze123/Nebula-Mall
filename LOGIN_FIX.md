# 登录跳转问题解决方案

## 问题症状
- 用户登录成功后无法跳转到首页
- 前端显示 token 已设置但后续请求 token 为 null

## 根本原因分析

### 1. 路由守卫逻辑错误（关键）
**文件**: `renren-fast-vue/src/router/index.js` (第44-50行)

**问题**: 
```javascript
// 错误的实现
beforeEnter (to, from, next) {
  let token = Vue.cookie.get('token')
  if (!token || !/\S/.test(token)) {
    clearLoginInfo()
    next({ name: 'login' })
  }
  next()  // ❌ 即使没有token也会调用next()，造成逻辑混乱
}
```

**解决方案**:
```javascript
// 正确的实现
beforeEnter (to, from, next) {
  let token = Vue.cookie.get('token')
  if (!token || !/\S/.test(token)) {
    clearLoginInfo()
    next({ name: 'login' })
  } else {
    next()  // ✅ 只在有token时才继续
  }
}
```

### 2. CORS 头配置不完整
**文件**: `renren-fast/src/main/java/io/renren/config/CorsConfig.java`

**问题**: 没有显式允许 `token` 请求头和响应头

**解决方案**:
```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("*")
        .allowCredentials(true)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")                    // ✅ 允许所有请求头
        .exposedHeaders("token")                // ✅ 暴露 token 头
        .maxAge(3600);
}
```

### 3. 登录响应处理
**文件**: `renren-fast-vue/src/views/common/login.vue` (第84-92行)

**改进**: 添加错误处理和 catch 块
```javascript
.then(({data}) => {
  if (data && data.code === 0) {
    console.log('[Login Success] Token:', data.token)
    this.$cookie.set('token', data.token)
    console.log('[After Set] Cookie token:', this.$cookie.get('token'))
    this.$router.replace({ name: 'home' })
  } else {
    this.getCaptcha()
    this.$message.error(data.msg)
  }
}).catch((e) => {
  console.error('[Login Error]', e)
  this.getCaptcha()
  this.$message.error('登录失败，请重试')
})
```

## 完整的登录流程

1. **用户提交登录表单** (`login.vue`)
   - 输入: username, password, uuid, captcha
   - POST `/sys/login`

2. **后端验证** (`SysLoginController.java`)
   - 验证验证码
   - 验证用户名密码
   - 生成 JWT token
   - 返回: `{code: 0, token: '...', expire: 43200}`

3. **前端保存 token** (`login.vue`)
   ```javascript
   this.$cookie.set('token', data.token)  // 设置 cookie
   ```

4. **前端请求时注入 token** (`httpRequest.js`)
   ```javascript
   http.interceptors.request.use(config => {
     const token = Vue.cookie.get('token')
     config.headers['token'] = token  // 在请求头中添加 token
     return config
   })
   ```

5. **后端验证 token** (`JWTFilter.java` + `JWTRealm.java`)
   - 从请求头提取 token
   - 从数据库验证 token
   - 检查过期时间

## 应用更改

### 后端
需要重新编译 `renren-fast` 项目:
```bash
cd renren-fast
mvn clean package -DskipTests
# 然后重新启动应用
```

### 前端
如果使用 Webpack 开发服务器，修改会自动热更新。
如果使用生产构建:
```bash
cd renren-fast-vue
npm run build  # 生成 dist 文件夹
gulp           # 打包输出到 renren-fast/src/main/resources/static/
```

## 测试步骤

1. **启动后端服务** (8080 端口)
2. **启动前端开发服务** (8081 端口)
3. **访问登录页面**: `http://localhost:8081`
4. **输入测试账号**:
   - 用户名: admin
   - 密码: admin (或根据数据库配置)
   - 验证码: 从图片读取或直接查看数据库

5. **观察浏览器开发者工具**:
   - Network 标签: 观察 /sys/login 请求和响应
   - 响应应该包含 `token` 字段
   - Storage 标签: 观察 Cookies 中是否有 `token`
   - 后续请求的请求头中应该包含 `token` 头

6. **验证跳转**:
   - 成功登录应该看到首页
   - 直接访问 `http://localhost:8081/#/` 应该自动跳转到 `/home` (带 token 时)
   - 移除 cookie 后重新访问应该返回登录页

## 常见问题排查

| 症状 | 可能原因 | 解决方案 |
|------|---------|--------|
| 验证码无法显示 | 后端 `/captcha.jpg` 接口异常 | 检查 `SysCaptchaService` |
| 登录返回 401 | token 为 null 或已过期 | 检查 `JWTRealm` 和 `SysUserTokenService` |
| token 无法保存 | Cookie 被浏览器拦截 | 检查 CORS 配置的 `allowCredentials: true` |
| 跳转后立即返回登录页 | 路由守卫逻辑错误 | 确保修复了 `beforeEnter` 的 else 分支 |
| 后续请求返回 401 | token 未在请求头中发送 | 检查 `httpRequest.js` 的请求拦截器 |

## 技术栈版本

- Vue: 2.5.16
- Element UI: 2.8.2
- Axios: 0.17.1
- Spring Boot: 3.5.10
- Shiro: JWT 认证
- JWT Token: 12 小时过期
- Token 存储: sys_user_token 表
