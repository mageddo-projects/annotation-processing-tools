Graal Reflection Configuration Generator provides an easy way to
geenrate GraalVM reflection config files, configure the
plugin on your gradle project, annotate the desired class class with
`@Reflection` run the gradle build then you're done.

See [an example app](https://github.com/mageddo/graal-reflection-configuration-generator/tree/master/example)

#### Using

Gradle dependencies 
```groovy
dependencies {
  compileOnly("com.mageddo.nativeimage:reflection-config-generator:2.3.4")
  annotationProcessor("com.mageddo.nativeimage:reflection-config-generator:2.3.4")
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

## Generation reflection to external library jars
As annotation processing only can process source code, if you need to generate a reflection json file to an external 
jar, you can do as follows:

Download [reflection-config-generator latest release jar][1] then run something like:
```bash
$ java -cp "your.jar:reflection-config-generator.jar" nativeimage.core.thirdparty.Main com.acme /tmp/tmp.json

generating reflect..., package=com.acme,  options=[com.github.dockerjava.api.model, /tmp/tmp.json]
generated!, package=com.acme, objects=148, writtenTo=/tmp/tmp.json
```

See available options by running 

```bash
$ java -jar reflection-config-generator.jar --help
```

[1]: https://github.com/mageddo-projects/annotation-processing-tools/releases?q=reflection-config-generator&expanded=true

