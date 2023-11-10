package store.code.chain.way1;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ObjectUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ChainTest {
    private static final Logger log = LoggerFactory.getLogger(ChainTest.class);
    private static final ArrayListChain chain = new ArrayListChain();

    static {
        chain.add(new FirstArgSetChainNode()).add(new ChainNode() {
            @Override
            public void execute(ChainContext context) {
                Object data = context.getData();
                context.setData((Integer) data + 1);
            }
        }).add(new ChainNode() {
            @Override
            public void execute(ChainContext context) {
                Object data = context.getData();
                context.setData((Integer) data + 100);
            }
        }).add(new ChainNode() {
            @Override
            public void execute(ChainContext context) {
                Object data = context.getData();
                context.setData((Integer) data + 200);
            }
        });
    }

    @Test
    public void test1() {
        Object result = chain.execute(10);
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result, 311));
    }

}
