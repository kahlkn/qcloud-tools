package store.code.fake;

import artoria.fake.FakeUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.test.bean.Menu;
import artoria.test.bean.User;
import artoria.time.DateUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Date;

public class FakeUtilsTest {
    private static Logger log = LoggerFactory.getLogger(FakeUtilsTest.class);

    @Test
    public void testFake() {
        User user = FakeUtils.fake(User.class);
        log.info(JSON.toJSONString(user, true));

        Menu menu = FakeUtils.fake(Menu.class);
        log.info(JSON.toJSONString(menu, true));

        for (int i = 0; i < 10; i++) {
            Date date = FakeUtils.fake(Date.class);
            log.info(DateUtils.format(date));
        }
    }

}
