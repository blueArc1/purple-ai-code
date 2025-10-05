// 简单的交互功能
document.addEventListener('DOMContentLoaded', function() {
    // 为导航链接添加平滑滚动
    const navLinks = document.querySelectorAll('.nav-links a');
    
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            alert('导航功能已触发！');
        });
    });

    // 为文章卡片添加悬停效果
    const postCards = document.querySelectorAll('.post-card');
    
    postCards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
            this.style.transition = 'transform 0.3s ease';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    console.log('博客网站加载完成！');
});