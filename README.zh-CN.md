<h1 align="center"> <a href="https://www.cuba-platform.cn/"><img src="https://gitee.com/cuba-platform/cuba/tree/master/img/Cuba_Logo.png" alt="CUBA 平台" width="600" align="center"></a>
</h1>

<h4 align="center">Java 企业级 Web 应用程序快速开发框架</h4>
  
<p align="center">
<a href="http://www.apache.org/licenses/LICENSE-2.0"><img src="https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat" alt="license" title=""></a>
<a href="https://travis-ci.org/cuba-platform/cuba"><img src="https://travis-ci.org/cuba-platform/cuba.svg?branch=master" alt="Build Status" title=""></a>
</p>

<div align="center">
  <h3>
    <a href="https://www.cuba-platform.cn/" target="_blank">
      主页
    </a>
    <span> | </span>
    <a href="https://www.cuba-platform.cn/learn/live-demo" target="_blank">
      在线示例
    </a>
    <span> | </span>
    <a href="https://www.cuba-platform.cn/documentation" target="_blank">
      文档
    </a>
    <span> | </span>
    <a href="https://www.cuba-platform.cn/guides" target="_blank">
      指南
    </a>
    <span> | </span>
    <a href="https://forum.cuba-platform.cn" target="_blank">
      论坛
    </a>
  </h3>
</div>

<p align="center">
<a href="https://twitter.com/CubaPlatform" target="_blank"><img src="https://gitee.com/cuba-platform/cuba/tree/master/img/twitter.png" height="36px" alt="" title=""></a>
<a href="https://www.facebook.com/CUBAplatform/" target="_blank"><img src="https://gitee.com/cuba-platform/cuba/tree/master/img/facebook.png" height="36px" margin-left="20px" alt="" title=""></a>
<a href="https://www.linkedin.com/company/cuba-platform/" target="_blank"><img src="https://gitee.com/cuba-platform/cuba/tree/master/img/linkedin.png" height="36px" margin-left="20px" alt="" title=""></a>
<a href="https://www.youtube.com/c/CubaPlatform" target="_blank"><img src="https://gitee.com/cuba-platform/cuba/tree/master/img/youtube.png" height="36px" margin-left="20px" alt="" title=""></a>
</p>
  
[CUBA 平台](https://www.cuba-platform.com) 是使用富网页接口技术开发企业级应用程序的高级别快速开发框架。

使用该平台的最简单方式就是 [下载](https://www.cuba-platform.cn/tools/) CUBA Studio 开发工具，并创建一个新项目。该平台的最新发行版本会自动从工件仓库下载。

也可以用源码构建一个快照版本并在项目中使用。

如需贡献源码改动，请访问 Github [源库](https://github.com/cuba-platform/cuba) ，并阅读 [贡献源码](https://github.com/cuba-platform/cuba/blob/master/CONTRIBUTING.md) 了解如何向平台提交源码改动。

## 使用源码构建

如需使用源码构建平台，需要安装：

-   Java 8 Development Kit (JDK)
-   [CUBA Gradle Plugin](https://github.com/cuba-platform/cuba-gradle-plugin)

假设已经在以下目录克隆了 CUBA Gradle Plugin 和 CUBA：

```
work/
    cuba/
    cuba-gradle-plugin/
```

打开终端进入 `work` 文件夹并运行下列命令开始构建和安装插件至本地 Maven 仓库（`~/.m2`）：

```
cd cuba-gradle-plugin
gradlew install
```

然后，切换至 CUBA 文件夹以相同的命令构建和安装：

```
cd ../cuba
gradlew install
```

## 使用快照版本

编辑项目的 `build.gradle` 文件。修改 `ext.cubaVersion` 属性，添加 `mavenLocal()` 至 `repositories`，示例：

```
buildscript {
    ext.cubaVersion = '7.3-SNAPSHOT'
    repositories {
        mavenLocal()
        maven { ...
```

现在可以使用本地仓库的快照 CUBA 版本构建并部署您的项目了：

```
gradlew deploy
```

## 第三方依赖

平台使用了很多第三方依赖库。可以在下列仓库找到相应的源码：

-   [eclipselink](https://github.com/cuba-platform/eclipselink)
-   [vaadin](https://github.com/cuba-platform/vaadin)
-   [vaadin-dragdroplayouts](https://github.com/cuba-platform/vaadin-dragdroplayouts)
-   [vaadin-aceeditor](https://github.com/cuba-platform/vaadin-aceeditor)

我们的工件仓库也有这些所有的依赖库，所以如果项目中要使用 CUBA 平台的话，不需要构建这些依赖库。
