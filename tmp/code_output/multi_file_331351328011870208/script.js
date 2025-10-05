// 登录表单验证和提交处理
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const usernameError = document.getElementById('usernameError');
    const passwordError = document.getElementById('passwordError');
    
    // 表单提交事件
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // 清除之前的错误信息
        clearErrors();
        
        // 验证表单
        const isValid = validateForm();
        
        if (isValid) {
            // 模拟登录过程
            simulateLogin();
        }
    });
    
    // 实时验证用户名
    usernameInput.addEventListener('blur', function() {
        validateUsername();
    });
    
    // 实时验证密码
    passwordInput.addEventListener('blur', function() {
        validatePassword();
    });
    
    // 表单验证函数
    function validateForm() {
        const isUsernameValid = validateUsername();
        const isPasswordValid = validatePassword();
        
        return isUsernameValid && isPasswordValid;
    }
    
    // 验证用户名
    function validateUsername() {
        const username = usernameInput.value.trim();
        
        if (username === '') {
            usernameError.textContent = '用户名不能为空';
            return false;
        }
        
        if (username.length < 3) {
            usernameError.textContent = '用户名至少需要3个字符';
            return false;
        }
        
        if (username.length > 20) {
            usernameError.textContent = '用户名不能超过20个字符';
            return false;
        }
        
        return true;
    }
    
    // 验证密码
    function validatePassword() {
        const password = passwordInput.value;
        
        if (password === '') {
            passwordError.textContent = '密码不能为空';
            return false;
        }
        
        if (password.length < 6) {
            passwordError.textContent = '密码至少需要6个字符';
            return false;
        }
        
        return true;
    }
    
    // 清除错误信息
    function clearErrors() {
        usernameError.textContent = '';
        passwordError.textContent = '';
    }
    
    // 模拟登录过程
    function simulateLogin() {
        const loginBtn = document.querySelector('.login-btn');
        const originalText = loginBtn.textContent;
        
        // 显示加载状态
        loginBtn.textContent = '登录中...';
        loginBtn.disabled = true;
        
        // 模拟API请求延迟
        setTimeout(function() {
            // 在实际应用中，这里会发送登录请求到服务器
            alert('登录成功！在实际应用中，这里会跳转到用户主页。');
            
            // 恢复按钮状态
            loginBtn.textContent = originalText;
            loginBtn.disabled = false;
            
            // 在实际应用中，这里会处理登录成功后的页面跳转
            // window.location.href = '/dashboard';
        }, 1500);
    }
    
    // 添加键盘快捷键支持
    document.addEventListener('keydown', function(e) {
        // 按Enter键提交表单
        if (e.key === 'Enter' && (e.target === usernameInput || e.target === passwordInput)) {
            loginForm.dispatchEvent(new Event('submit'));
        }
    });
});