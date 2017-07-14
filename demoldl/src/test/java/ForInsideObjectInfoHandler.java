import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/7/14.
 */
public interface ForInsideObjectInfoHandler {
    /**
     * 根据插入的人员类型Keys 列表，返回EigenValues 的列表以及rowkeys
     * @param pkeys 人员类型列表
     * @return 返回的是一行一行的数据，每行数据包含EigenValue 和 rowkeys
     */
    public List<Map<String, byte[]>> getEigenValuesAndPkeys(List<String> pkeys);
}
