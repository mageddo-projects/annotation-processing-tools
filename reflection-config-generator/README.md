Graal Reflection Configuration Generator provides an easy way to
geenrate GraalVM reflection config files, configure the
plugin on your gradle project, annotate the desired class class with
`@RuntimeReflection` run the gradle build then you're done.

See [an example app](https://github.com/mageddo/graal-reflection-configuration-generator/tree/master/example)

#### Using

Gradle dependencies 
```groovy
dependencies {
  compileOnly("com.mageddo.nativeimage:reflection-config-generator:2.1.1")
  annotationProcessor("com.mageddo.nativeimage:reflection-config-generator:2.1.1")
}
```

Consider the following example 
```java
package com.acme;

import nativeimage.Reflection;

@Reflection(declaredConstructors = true, declaredFields = true)
public class Fruit {
  private String name;
  private String color;   
}
```
it will automatically configure reflection for `Fruit` props when compiling using native-image generating a json like

```javascript
[
  {
    "name" : "com.acme.Fruit",
    "allDeclaredConstructors" : true,
    "allPublicConstructors" : false,
    "allDeclaredMethods" : false,
    "allPublicMethods" : false,
    "allPublicFields" : false,
    "allDeclaredFields" : true
  }
]
```
