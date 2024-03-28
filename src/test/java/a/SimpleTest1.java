package a;

import com.zwk.copier.CopierFactory;
import com.zwk.copier.CopierHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/28 19:46
 */

public class SimpleTest1 {
    private static CopierFactory copierFactory;
    private static CopierHelper helper;

    private static String s = "rule_id{\n" +
            "    userAge = age\n" +
            "    userName = name\n" +
            "    userSex = sex\n" +
            "}";

    public static void main(String[] args) throws Exception {
        copierFactory = CopierFactory.fromString(s);
        helper = new CopierHelper(copierFactory);
//        beanToMap();
//        beanToBean();
//        mapToBean();
        mapToMap();
    }

    private static void beanToMap() throws Exception {
        User user = new User();
        user.setAge(18);
        user.setName("zs");
        user.setSex(1);
        Map copy = helper.copy("rule_id", Map.class, user);
        System.out.println("copy = " + copy);
    }

    private static void beanToBean() throws Exception {
        User user = new User();
        user.setAge(18);
        user.setName("zs");
        user.setSex(1);
        User2 copy = helper.copy("rule_id", User2.class, user);
        System.out.println("copy = " + copy);
    }

    private static void mapToBean() throws Exception {
        Map map = new HashMap();
        map.put("age", 18);
        map.put("name", "ls");
        map.put("sex", 2);
        User2 copy = helper.copy("rule_id", User2.class, map);
        System.out.println("copy = " + copy);
    }

    private static void mapToMap() throws Exception {
        Map map = new HashMap();
        map.put("age", 18);
        map.put("name", "ls");
        map.put("sex", 2);
        Map copy = helper.copy("rule_id", Map.class, map);
        System.out.println("copy = " + copy);
    }

}
