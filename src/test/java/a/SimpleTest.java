package a;

import com.zwk.copier.CopierFactory;
import com.zwk.copier.CopierHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/28 13:16
 */

public class SimpleTest {
    private static CopierFactory copierFactory = CopierFactory.createDefault();
    private static CopierHelper helper = new CopierHelper(copierFactory);

    public static void main(String[] args) throws Exception {
//        beanToMap();
//        mapToBean();
//        beanToBean();
//        beanToBeanD();
        mapToMap();

    }

    private static void beanToMap() throws Exception {
        User user = new User();
        user.setAge(18);
        user.setName("zs");
        user.setSex(1);
        Map copy = helper.copy(Map.class, user);
        System.out.println("copy = " + copy);
    }

    private static void mapToBean() throws Exception {
        Map<Object, Object> map = new HashMap<>();
        map.put("age", 18);
        map.put("name", "zs");
        map.put("sex", 1);

        User copy = helper.copy(User.class, map);
        System.out.println("copy = " + copy);
    }

    private static void beanToBean() throws Exception {
        User user = new User();
        user.setAge(18);
        user.setName("zs");
        user.setSex(1);

        User copy = helper.copy(User.class, user);
        System.out.println("copy = " + copy);
    }

    private static void beanToBeanD() throws Exception {
        User user = new User();
        user.setAge(18);
        user.setName("zs");
        user.setSex(1);//属性类型不同

        User1 copy = helper.copy(User1.class, user);
        System.out.println("copy = " + copy);
    }

    private static void mapToMap() throws Exception {
        Map<Object, Object> map = new HashMap<>();
        map.put("age", 18);
        map.put("name", "zs");
        map.put("sex", 1);

        Map copy = helper.copy(Map.class, map);
        System.out.println("copy = " + copy);
    }

}
