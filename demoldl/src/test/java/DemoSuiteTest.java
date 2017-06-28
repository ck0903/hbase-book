import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.util.Arrays;

public class DemoSuiteTest {
    public void forTestVariableLengthArgs(String... clos){
        System.out.println(clos);
        if (clos != null){
            for (String clo:clos){
                System.out.println(clo);
            }
        }else {
            System.out.println("it ' null..");
        }
    }

    @Test
    public void testVariableLengthArgs(){
        forTestVariableLengthArgs("1", "2");
        System.out.println("--------------------------");
        forTestVariableLengthArgs();
    }

    @Test
    public void testCopyArray(){
        int[] array1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int[] array6 = new int[6];
        System.arraycopy(array1, 1, array6, 0, array6.length);
        System.out.println(Arrays.toString(array6));
    }
}
