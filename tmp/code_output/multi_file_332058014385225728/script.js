// 移动端菜单切换
document.getElementById('menuToggle').addEventListener('click', function() {
    document.getElementById('navMenu').classList.toggle('show');
});

// 阅读更多按钮交互
document.querySelectorAll('.read-more').forEach(button => {
    button.addEventListener('click', function() {
        const articleTitle = this.parentElement.querySelector('h3').textContent;
        alert(`正在加载文章: ${articleTitle}`);
    });
});

// 平滑滚动效果
document.querySelectorAll('nav a').forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault();
        const targetId = this.getAttribute('href');
        if(targetId !== '#') {
            document.querySelector(targetId)?.scrollIntoView({ behavior: 'smooth' });
        }
    });
});