package store.code.chain.way1;

import artoria.data.Dict;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ObjectUtils;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class Chain1Test {
    private static final Logger log = LoggerFactory.getLogger(Chain1Test.class);
    private static final ArrayListChain chain = new ArrayListChain();

    static {
        String scriptName = "javascript";
        chain.add(new FirstArgSetChainNode())
                .add(new ScriptChainNode(scriptName, "data.a = data.a + 1;" +
                        "data.b = data.b + 1;" +
                        "data.c = null; " +
                        "data;"))
                .add(new ScriptChainNode(scriptName, "data.d = 1;" +
                        "data.e = 2;" +
                        "data.f = 3;" +
                        "data.c = data.a + data.b; " +
                        "data;"))
                .add(new ScriptChainNode(scriptName, "data.delete(\"d\");" +
                        "data.delete(\"e\");" +
                        "data;"))
        ;
    }

    @Test
    public void test1() {
        Dict dict = Dict.of("a", 1).set("b", 2).set("c", 3);
        Dict result = Dict.of((Map<?, ?>) chain.execute(dict));
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result.getInteger("a"), 2));
        assertTrue(ObjectUtils.equals(result.getInteger("b"), 3));
        assertTrue(ObjectUtils.equals(result.getInteger("c"), 5));
        assertNull(result.get("d"));
        assertNull(result.get("e"));
        assertTrue(ObjectUtils.equals(result.getInteger("f"), 3));
    }

}
