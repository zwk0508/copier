# copier

java属性复制，支持不同名属性复制 底层采用javacc + janino

## 同名属性类型复制

### 1 bean to map

```java
//创建默认CopierFactory
private static CopierFactory copierFactory = CopierFactory.createDefault();
//创建CopierHelper
private static CopierHelper helper = new CopierHelper(copierFactory);

private static void beanToMap() throws Exception {
    User user = new User();
    user.setAge(18);
    user.setName("zs");
    user.setSex(1);
    Map copy = helper.copy(Map.class, user);
    System.out.println("copy = " + copy);//copy = {sex=1, name=zs, age=18}
}
```

### 2 bean to bean

```java
//属性类型兼容
private static void beanToBean() throws Exception {
    User user = new User();
    user.setAge(18);
    user.setName("zs");
    user.setSex(1);

    User copy = helper.copy(User.class, user);
    System.out.println("copy = " + copy);//copy = User{name='zs', age=18, sex=1}
}

//属性类型存在不兼容情况
private static void beanToBeanD() throws Exception {
    User user = new User();
    user.setAge(18);
    user.setName("zs");
    user.setSex(1);//属性类型与User1不同 User1 为String类型

    User1 copy = helper.copy(User1.class, user);
    System.out.println("copy = " + copy);//copy = User1{name='zs', age=18, sex='null'}
}
```

### 3 map to bean

```java
//map 的value值与bean的属性值类型兼容
private static void mapToBean() throws Exception {
    Map<Object, Object> map = new HashMap<>();
    map.put("age", 18);//对应 User 的age字段类型为Integer
    map.put("name", "zs");//对应 User 的name字段类型为String
    map.put("sex", 1);////对应 User 的sex字段类型为Integer

    User copy = helper.copy(User.class, map);
    System.out.println("copy = " + copy);//copy = User{name='zs', age=18, sex=1}
}

//存在map 的value值与bean的属性值类型不兼容 会报错 ClassCastException
private static void mapToBean() throws Exception {
    Map<Object, Object> map = new HashMap<>();
    map.put("age", "18");//对应 User 的age字段类型为Integer 会报错 ClassCastException
    map.put("name", "zs");//对应 User 的name字段类型为String
    map.put("sex", 1);////对应 User 的sex字段类型为Integer

    User copy = helper.copy(User.class, map);
    System.out.println("copy = " + copy);
}

```

### 4 map to map

```java
private static void mapToMap() throws Exception {
    Map<Object, Object> map = new HashMap<>();
    map.put("age", 18);
    map.put("name", "zs");
    map.put("sex", 1);

    Map copy = helper.copy(Map.class, map);
    System.out.println("copy = " + copy);//copy = {name=zs, age=18, sex=1}
}
```

## 不同名属性类型复制

需要指定属性复制规则

```groovy
//复制规则 形式，可以有多个规则
//rule_id、key、value格式: 由字母、数字、下划线、$ 组成
rule_id {
    key = value
    key = value
}

//例如：
copyRuleId {
    userAge = age
    userName = name
    userSex = sex
}
```

### 初始化

```java
private static CopierFactory copierFactory;
private static CopierHelper helper;

static {
    copierFactory = CopierFactory.fromString(s);
    helper = new CopierHelper(copierFactory);
}

//复制规则
private static String s = "rule_id{ " +
        "    userAge = age " +
        "    userName = name " +
        "    userSex = sex " +
        "}";

```

### 1 bean to map

```java
private static void beanToMap() throws Exception {
    User user = new User();
    user.setAge(18);
    user.setName("zs");
    user.setSex(1);
    Map copy = helper.copy("rule_id", Map.class, user);
    System.out.println("copy = " + copy);//copy = {userSex=1, userName=zs, userAge=18}
}
```

### 2 bean to bean

```java
 private static void beanToBean() throws Exception {
    User user = new User();
    user.setAge(18);
    user.setName("zs");
    user.setSex(1);
    User2 copy = helper.copy("rule_id", User2.class, user);
    System.out.println("copy = " + copy);//copy = User2{userName='zs', userAge=18, userSex=1}
}
```

### 3 map to bean

```java
//属性类型不兼容会报错
private static void mapToBean() throws Exception {
    Map map = new HashMap();
    map.put("age", 18);
    map.put("name", "ls");
    map.put("sex", 2);
    User2 copy = helper.copy("rule_id", User2.class, map);
    System.out.println("copy = " + copy);//copy = User2{userName='ls', userAge=18, userSex=2}
}
```

### 3 map to map

```java
private static void mapToMap() throws Exception {
    Map map = new HashMap();
    map.put("age", 18);
    map.put("name", "ls");
    map.put("sex", 2);
    Map copy = helper.copy("rule_id", Map.class, map);
    System.out.println("copy = " + copy);//copy = {userSex=2, userName=ls, userAge=18}
}
```