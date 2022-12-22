SpringBoot3.0 + MybatisPlus了解jdk17新特性并使用（有史以来最快的JAVA）306：恢复始终严格的浮点语义JDK17再次严格要求所有浮点计算,不需要添加 strictfp 关键字356：增强型伪随机数发生器随机数生成器统一由 RandomGeneratorFactory生成382 新的 macOS 渲染管道，391 macOS/AArch64 平台支持398 弃用即将删除的Applet API403 强封装JDK的内部API可以通过设置参数–add-export或–add-opens来指定哪些类可以被访问406 Switch模式匹配（预览）（后续JDK版本中会扶正，也可能删除）switch开始支持instanceof删除 RMI 激活机制由于web技术的发展，有关过滤请求、身份认证、安全性等问题都已经在web服务领域得到解决，但这些机制都不在RMI的激活模型中409 密封类类被 sealed 修饰，只允许（permits）类继承，继承的类必须有 final 或者 no-sealed 来修饰。接口被 sealed 修饰，只允许（permits）类实现，实现的类必须有 final 或者 no-sealed 来修饰。410 删除实验性 AOT 和 JIT 编译器AOT(Ahead Of Time 运行前编译)即jaotc，可以将java代码编译成二进制，JVM直接用这些二进制，而不是在运行时再花时间用JIT（Just in Time 即时编译）编译。AOT优点在于占用内存低，启动速度快，缺点是在程序运行前编译会使程序的安装时间增加，牺牲java的一致性；JIT优点在于吞吐量高，可以根据当前情况实时编译生成最优机器指令，缺点是编译需要占用运行时资源，启动速度较慢412 外部函数和内存 API（孵化器）这个新功能与JNI（java本地接口）有关，JNI允许java程序与程序以外的代码或数据做交互，常见的JNI即Thread类里的start()方法。JNI只能和以C和C++语言编写的库进行交互，在通用性上有所不足，且JNI无法监控JVM以外的代码运行情况，外部可以通过getStaticField等函数访问JDk内部，甚至改变在final修饰下的字段值，本质上JNI是不安全的。因此java开发人员觉得有一个更加安全易用的，基于java模型的JNI替代API，就有了这个孵化器414 矢量 API（二次孵化）这是一个矢量API，用于矢量计算，日常开发中不会用到415 特定于上下文的反序列化过滤器可能会有利用反序列化攻击程序的情况发生，为了避免这类问题JDK9新增了反序列化过滤器，JDK17在此基础上又新增了基于特定上下文的反序列化过滤器，可以通过JVM范围的过滤器工厂配置特定于上下文和动态选择的反序列化过滤器