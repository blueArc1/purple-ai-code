// 移动端菜单切换
document.getElementById('menuToggle').addEventListener('click', function() {
    alert('菜单功能开发中...');
});

// 阅读更多按钮交互
document.getElementById('readMore').addEventListener('click', function() {
    const article = document.querySelector('article');
    const newContent = document.createElement('p');
    newContent.textContent = '这是更多内容...感谢您的关注！';
    article.appendChild(newContent);
    this.style.display = 'none';
});

// 页面加载完成后的效果
window.addEventListener('load', function() {
    document.querySelector('article').style.opacity = '0';
    document.querySelector('article').style.transition = 'opacity 0.5s ease-in';
    setTimeout(() => {
        document.querySelector('article').style.opacity = '1';
    }, 100);
});