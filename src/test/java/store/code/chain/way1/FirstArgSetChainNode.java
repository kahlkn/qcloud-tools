package store.code.chain.way1;

public class FirstArgSetChainNode implements ChainNode {

    @Override
    public void execute(ChainContext context) {

        context.setData(context.getArguments()[0]);
    }

}
