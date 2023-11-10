package store.code.chain.way1;

import artoria.data.bean.BeanUtils;
import artoria.script.ScriptUtils;
import artoria.util.Assert;

import java.util.Map;

public class ScriptChainNode implements ChainNode {
    private final String scriptContent;
    private final String scriptName;

    public ScriptChainNode(String scriptName, String scriptContent) {
        Assert.notBlank(scriptContent, "Parameter \"scriptContent\" must not blank. ");
        Assert.notBlank(scriptName, "Parameter \"scriptName\" must not blank. ");
        this.scriptContent = scriptContent;
        this.scriptName = scriptName;
    }

    public String getScriptName() {

        return scriptName;
    }

    public String getScriptContent() {

        return scriptContent;
    }

    @Override
    public void execute(ChainContext context) {
        Map<String, Object> contextMap = BeanUtils.beanToMap(context);
        context.setData(ScriptUtils.eval(scriptName, scriptContent, contextMap));
    }

}
