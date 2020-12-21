# xunwu
# 基于springboot的找房源系统

#### 介绍
基于springboot实现的找房源系统，模仿链家租房界面与功能，用来学习springboot与elasticsearch

#### 软件架构

- 搜索引擎：Elasticsearch
- 基础框架：springboot
- 数据库：MySql、Spring Data JPA
- 图片存储：七牛云
- 登录验证：阿里云短信服务
- 地图服务：百度地图V3
- 前端：thymeleaf、bootstrap、h-ui前端框架
- 消息队列：kafka
- session缓存：redis

#### 安装教程

1.  安装elasticsearch5.6.8
2.  安装kafka2.11
3.  安装redis5.0.10

#### 使用说明

1.  resource/db下有数据库脚本，执行即可创建数据库，管理员admin密码admin
2.  百度地图、阿里云、七牛云使用需要在配置文件中替换为自己的密钥
3.  启动前需要构建elasticsearch索引，也在resource/db，用postman发送put请求即可构建
#### 已修复Bug及现存Bug
1.  预约冲突Bug已修复
2.  手机号登录bug已修复
3.  用户空值查询无结果Bug已修复
4.  用户与管理员登录失败跳转未修复
5.  预约看房不能立刻刷新数据(前端)
6.  添加房源页三级联动查不到数据地铁线路会保留上一次的数据(前端)
