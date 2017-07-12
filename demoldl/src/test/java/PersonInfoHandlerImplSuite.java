import com.hzgosun.PersonInfoHandler;
import com.hzgosun.PersonInfoHandlerImpl;
import com.sydney.Image2Byte2Image;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2017/7/8.
 */
public class PersonInfoHandlerImplSuite {
    @Test
    public void testAddPersonInfo(){
        PersonInfoHandler handler = new PersonInfoHandlerImpl();
        Map<String, String> person = new HashMap<>();
        person.put("name", "caonima");
        person.put("idcard", "452011111111111111");
        person.put("sex", "0");
        try {
            person.put("photo", String.valueOf(new Image2Byte2Image()
                    .image2byte("C:\\\\Users\\\\lenovo\\\\Desktop\\\\temp\\\\HBASECRUD.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        person.put("reason", "piao");
        person.put("tkey", "enhaenha");
        person.put("cman", "langligelang");
        person.put("cmtel", "l8069773748");
        person.put("eval", "45a5da5fd45a5dda2ad");
        for (int i = 2; i <= 5001; i++){
            person.put("idcard", "45201111111111111" + i);
            handler.addPersonInfo(person);
        }
    }
}
