##本项目是搭建并实现网路直播平台，实现主播直播和用户观看等功能，采用VUE+spring boot结合，构建对应的平台

##1.ive——client为前端
    1.env.development，env.production env.test 均为环境变量     
    2.package.json 为项目依赖配置
    3.vite.config.js 为vite构建配置
          
##2.live-server为后端
    1.drvs存录播文件（这里更改为放在"D:\data\srs-live\live-server\dvrs"下）
    2.target存放编译文件
    3.D:\xiaozuzuoye\live-server\src\main存放对应的后端配置文件
    4.D:\xiaozuzuoye\live-server\src\main\java\com\winter\app\controller直播回调
    5.pom为Maven 项目的 pom.xml 配置文件




##3.database为设计的数据库
    包括用户信息、直播间名称、平台附录设置等内容


##4.live.html为设计的用户观看的网页
    通过url，直接连接到SRS的HTTP-FLV端口，实现播放的功能

##5.开播流程
    1.docker：live-db与srs处于running状态
    2.运行application.java
    3.连接到数据库
    4.cmd+npm run dev 打开网页
    5.登录开播
    6.导入obs推流
    7.客户端观看