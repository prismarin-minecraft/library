# Prismarin Java Library

Here you can find utilities and extensions that can help your projects.

The project is written in JDK 17, also it is built in different small modules which will be combined into one big module at the end.

You can also specifically take only one module as dependency.

## Installation

This library is pushed into different small modules but also in one big module.
 
### Maven

To use the latest version, use this dependency and repository
in your pom.xml 

```xml
<repository>
    <id>prismarin-minecraft</id>
    <url>https://nexus.prismar.in/repository/prismarin-minecraft/</url>
</repository>
```

```xml
<dependency>
    <groupId>in.prismar.library</groupId>
    <artifactId>{MODULE}</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope> <!-- Only if you want to build a fat jar -->
</dependency>
```

Replace `{MODULE}` with:

<ul>
<li>common</li>
<li>authentication</li>
<li>file</li>
<li>vault</li>
<li>web</li>
<li>spigot</li>
</ul>

### Author

Made by Maga
