# jLogitow [![Build Status](https://travis-ci.org/trychen/jLogitow.svg?branch=master)](https://travis-ci.org/trychen/jLogitow) [![](https://www.jitpack.io/v/trychen/jLogitow.svg)](https://www.jitpack.io/#trychen/jLogitow)
A java library and a forge mod help program connect and communicate to Logitow Device

## Usage
The core class of this lib is `LogiTowBLEStack`, it contains all of the core function.

### Require

Software|Min Version|Note
----|---|----
Java|8|Compile with java 1.8.0_40
macOS|10.10|Compile based on OS X 10.11
Forge|Only 1.12.2|Only required when your and developing with forge
Windows||Implement soon

### Install
For Maven, add this in `pom.xml`:
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://www.jitpack.io</url>
	</repository>
</repositories>
...
<dependency>
    <groupId>com.github.trychen</groupId>
    <artifactId>jLogitow</artifactId>
    <version>1.0-BETA</version>
</dependency>
```

For Gradle, add this in `build.gradle`:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://www.jitpack.io' }
    }
}
dependencies {
    compile 'com.github.trychen:jLogitow:1.0-BETA'
}
```


### Quick Start
The following code can automatically connect to Logitow and add print when data recived

```java
if (!LogiTowBLEStack.isAvailable()) return;
LogiTowBLEStack.startScan(); // native lib will start another thread to handle
LogiTowBLEStack.addBlockDataConsumer(System.out::println);
```

### Structure
It provides a structuring data structure for handle the BlockData

### Forge Event
If your are developing with Forge 1.12.2, this lib provide 3 event called `LogitowBlockDataEvent`, `LogitowConnectedEvent` and `LogitowDisconnectedEvent`.
Using event to fetch data can ensure the compatibility between different mod. in some situations,  you should cancel the event if your are capturing a BlockData.
