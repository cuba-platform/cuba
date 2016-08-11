# CUBA Platform

[CUBA Platform](https://www.cuba-platform.com) is a high level framework for rapid development of enterprise applications with a rich web interface.

The simplest way to start using the platform is to [download](https://www.cuba-platform.com/download) CUBA Studio and create a new project in it. A released version of the platform will be downloaded automatically from the artifact repository.

You can also build a snapshot version of the platform from the source code and use it in your project.

## Building from Source

In order to build the platform from source, you'll need to install the following:
* [Java 8 Development Kit (JDK)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Gradle](https://gradle.org) (tested on 2.13, but newer versions may also work)
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

## Third-party dependencies

The platform uses a number of forked third-party libraries. They can be found in the following source code repositories:

* [eclipselink](https://github.com/cuba-platform/eclipselink)
* [vaadin](https://github.com/cuba-platform/vaadin)
* [vaadin-dragdroplayouts](https://github.com/cuba-platform/vaadin-dragdroplayouts)
* [vaadin-googlemaps](https://github.com/cuba-platform/vaadin-googlemaps)
* [vaadin-aceeditor](https://github.com/cuba-platform/vaadin-aceeditor)
* [swingx-core](https://github.com/cuba-platform/swingx-core)
* [apache-poi](https://github.com/cuba-platform/apache-poi)

All dependencies are also located in our artifacts repository, so you don't have to build them from sources in order to build and use the platform.
