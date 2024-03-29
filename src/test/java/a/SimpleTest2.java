package a;

import com.zwk.converter.CollectionToArrayConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/29 16:07
 */

public class SimpleTest2 {
    public static void main(String[] args) {
        CollectionToArrayConverter converter = new CollectionToArrayConverter();
        List list = new ArrayList(Arrays.asList(1, 2, 3, 4));
        int[] convert = (int[]) converter.convert(list, int.class);
        System.out.println("convert = " + convert);
//        list.toArray(new int[0]);
    }
}
