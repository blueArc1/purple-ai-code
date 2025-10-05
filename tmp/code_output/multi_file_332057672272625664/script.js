// 博客交互功能
document.addEventListener('DOMContentLoaded', function() {
    const readButtons = document.querySelectorAll('.read-btn');
    
    readButtons.forEach(button => {
        button.addEventListener('click', function() {
            const postTitle = this.parentElement.querySelector('h3').textContent;
            alert(`正在打开文章: ${postTitle}`);
            // 实际应用中这里可以跳转到文章详情页
        });
    });

    // 导航链接平滑滚动
    const navLinks = document.querySelectorAll('nav a');
    navLinks.forEach(link => {
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