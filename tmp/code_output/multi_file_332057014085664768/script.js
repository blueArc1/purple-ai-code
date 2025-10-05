// 博客交互功能
document.addEventListener('DOMContentLoaded', function() {
    const readMoreButtons = document.querySelectorAll('.read-more');
    
    readMoreButtons.forEach(button => {
        button.addEventListener('click', function() {
            const articleTitle = this.closest('.blog-card').querySelector('h3').textContent;
            alert(`正在加载文章: ${articleTitle}`);
            // 这里可以添加实际的文章加载逻辑
        });
    });

    // 平滑滚动导航
    document.querySelectorAll('.nav-links a').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                targetElement.scrollIntoView({ behavior: 'smooth' });
            }
        });
    });
});