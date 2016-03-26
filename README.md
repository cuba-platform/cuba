# CUBA Platform

[CUBA Platform](https://www.cuba-platform.com) is a high level framework for rapid development of enterprise applications with rich web interface.

The simplest way to start using the platform is to [download](https://www.cuba-platform.com/download) CUBA Studio and create a new project in it. A released version of the platform will be downloaded automatically from the artifact repository.

You can also build a snapshot version of the platform from the source code and use it in your project.

## Building from Source

In order to build the platform from source, you need to install the following:
* Java 8 Development Kit (JDK)
* [Gradle](https://gradle.org) (tested on 2.6, but newer versions may also work)
* [CUBA Gradle Plugin](https://github.com/cuba-platform/cuba-gradle-plugin)

Let's assume that you have cloned CUBA Gradle Plugin and CUBA into the following directories:
```
work/
    cuba/
    cuba-gradle-plugin/
```

Open terminal in the `work` directory and run the following command to build and install the plugin into your local Maven repository (`~/.m2`):
```
cd cuba-gradle-plugin
gradle install
```

After that, go to the CUBA directory and build and install it with the same command:
```
cd ../cuba
gradle install
```

## Using Snapshot Version

Edit the `build.gradle` file of your project. Change the `ext.cubaVersion` property and add `mavenLocal()` to the `repositories` section, for example:
```
buildscript {
    ext.cubaVersion = '6.2-SNAPSHOT'
    repositories {
        mavenLocal()
        maven { ...
```
That's all. Now you can generate IDE project files and build and deploy your application based on the snapshot version of the platform from your local repository:
 ```
 gradle idea
 gradle deploy
 ```