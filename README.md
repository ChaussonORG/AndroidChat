#ChatLibrary说明文档
##数据库
1. 使用 [Realm](https://realm.io/cn/) 数据库
2. realm数据库使用时注意线程问题，即realm对象的创建和使用要在同一个线程中。

##核心内容
1. db：数据库操作相关，主要有两张表：消息表和联系人表
2. emojicon：emoji表情处理
3. BaseChatFragment：实现聊天基础功能，包括发送语音，图片，表情
4. ChatNotifier:通知栏通知
5. ChatPathUtil：图片录音等文件存储路径
6. ChatUtil：模型转换工具类
7. SmileUtils：处理表情（和iOS表情兼容）
8. DateUtils：格式化日期
9. BaseChatRow：聊天对话页面每一个item的基类，定义了通用的UI和点击事件，如头像，昵称，时间戳等。新增聊天类型时，继承该类，并且在自己的布局中定义通用的id
10. MessageAdapter：聊天对话页面列表的适配器
11. ChatExtendMenu:聊天加号按钮扩展菜单
12. BaseChatPrimaryMenu：聊天页面键盘菜单布局
13. ChatVoiceRecorder：录制语音
14. BaseMessage：业务使用的模型
15. ChatMessage：操作数据库使用的模型，具体字段见注释，其中messagebody存取的是json字符串，方便之后扩展字段而不改变表结构
16. ChatConstant：聊天相关的常量

##接收消息
主要有三个接收消息的入口

1. BaseChatFragment：处于聊天当前页面
2. MainActivity(应用的主页)：不处于聊天当前页面，应用在前台
3. ChatHelper:应用处于后台运行

##其他
1. Library参考环信Demo书写
2. 华为手机要在应用权限中开启自启动才能收到消息。
3. Library依赖basemodule中的部分内容，如下载文件等
4. 如需扩展新的消息字段，在主module中扩展，libary留有相应接口，具体可参照红包消息



