// script.js
document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const inputs = this.querySelectorAll('input');
    alert(`登录成功！用户名: ${inputs[0].value}`);
    inputs[0].value = inputs[1].value = '';
});