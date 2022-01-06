# kotlin多平台

---

## 目录结构
~~~
android : 安卓
common ：代码共享模块
    client : 客户端共享代码
    compose-ui : compose共享ui
    logic ： 全部可共享的逻辑代码
desktop : pc桌面
server : 服务器
web : 网页
km-processor : ksp编译插件
~~~

---

### https说明

km.cer和km.p12为https的证书和密钥，由于这两个文件与ip绑定，因此IP变化后需要重新生成证书和密钥；

---

* ios模块目前没有mac电脑，暂无

