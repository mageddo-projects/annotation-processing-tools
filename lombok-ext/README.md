Multiline string support for Java < 15 

Main.java
```java
/*
SELECT 
  NAME, COLOR
FROM FRUIT;
*/
@TextBlock
final String sql = TextBlocks.lazyInit();
System.out.println(sql);
```

Output
```
SELECT 
  NAME, COLOR
FROM FRUIT;
```

Configuring
```groovy
annotationProcessor("com.mageddo.lombok:lombok-ext:2.3.4")
compileOnly("com.mageddo.lombok:lombok-ext:2.3.4")
```
