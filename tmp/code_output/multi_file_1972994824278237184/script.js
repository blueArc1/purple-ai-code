document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const username = this.querySelector('input[type="text"]').value;
    const password = this.querySelector('input[type="password"]').value;
    if(username && password) {
        alert('登录成功！欢迎 ' + username);
        this.reset();
    } else {
        alert('请填写完整的登录信息');
    }
});