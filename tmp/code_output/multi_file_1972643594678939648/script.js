// 获取表单和消息元素
const loginForm = document.getElementById('loginForm');
const message = document.getElementById('message');

// 添加表单提交事件监听器
loginForm.addEventListener('submit', function(event) {
    event.preventDefault(); // 阻止表单默认提交行为
    
    // 获取输入的用户名和密码
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    // 简单的验证逻辑：用户名和密码不能为空
    if (username.trim() === '' || password.trim() === '') {
        message.textContent = '用户名和密码不能为空！';
        message.style.color = '#d9534f'; // 红色错误消息
    } else {
        // 模拟登录成功（实际应用中应发送到服务器验证）
        message.textContent = '登录成功！';
        message.style.color = '#28a745'; // 绿色成功消息
        // 可以在这里添加跳转逻辑，例如：window.location.href = 'dashboard.html';
    }
});