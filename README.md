# TelephoneInterceptDemo

> 一个电话拦截的demo, 涉及到了aidl,反射等技术

##主要功能

* 自定义归属地拦截, demo中以河北号码为例
* 拦截的电话不在通话记录中显示, 干净利落

##demo思路

1. 程序开始执行便拷贝号码数据库到自己工程目录下(GZip解压缩数据库)
2. 开启服务, 获取来电号码(LocationService.java)
3. 根据来电号码获取到归属地(AddressDao.java)
4. 匹配拦截归属地, 就挂断电话(反射+aidl技术)



##声明

> 本demo可用于学习使用, 请勿用作非法途径, 一旦追究与作者无关.
