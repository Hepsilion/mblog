# Mblog 部署说明
---
1. 准备工作
   - 安装 Jdk8
   - 安装图片处理工具：GraphicsMagick1.3.20，[下载地址][1]
   - 安装 Maven
   - 准备 IDE (如果你不看源码，可以忽略下面的步骤，直接通过Maven编译war包)

2. IDE 需要配置的东西
   - 编码方式设为UTF-8
   - 配置Maven
   - 设置Jdk8
   关于这些配置,网上有一大把的资料,所以此处不再重复。
   
3. 获取代码导入到IDE
   - 从 http://git.oschina.net/mtons/mblog 拉取最新代码
   - 导入到IDE的时候请选择以Maven的方式导入
   - 项目配置参考 [系统配置手册][2]
   
4. 配置完毕
   - 启动项目，在控制台看到`Mblog加载完毕`的信息后，表示启动成功
   - 打开浏览器输入：http//localhost/mblog/ (此处仅是示例,具体具体端口因人而异)，访问成功即部署完毕
   - 后台管理的地址是 /admin, 如果你是管理员账号点导航栏的头像会看到"后台管理"
   - 启动成功后,你应该去后台的`系统配置`里配置你的网站信息等。
   
5. 常见问题总结
   - java.io.FileNotFoundException: gm 应该是你 GraphicsMagick 安装不成功或者重启下电脑再试试
   - 进入系统后, 菜单加载不出来, 那应该是你没有导 db_init.sql
   - 点标签显示乱码, 那应该是你的应用服务器 URIEncoding 没设


  [1]: http://www.graphicsmagick.org/download.html
  [2]: https://www.zybuluo.com/langhsu/note/165905