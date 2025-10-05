document.querySelector('.login-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const username = this.querySelector('input[type="text"]').value;
    const password = this.querySelector('input[type="password"]').value;
    alert(`用户名: ${username}\n密码: ${password}`);
});