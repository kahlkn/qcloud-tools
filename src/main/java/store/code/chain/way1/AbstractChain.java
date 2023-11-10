package store.code.chain.way1;

import artoria.util.Assert;

public abstract class AbstractChain implements Chain {

    protected ChainContext buildContext(Object[] arguments) {

        return new ChainContextImpl(arguments);
    }

    protected Iterable<ChainNode> getNodes() {

        throw new UnsupportedOperationException();
    }

    protected Object getResult(ChainContext context) {

        return context.getData();
    }

    @Override
    public Object execute(Object... arguments) {
        ChainContext context = buildContext(arguments);
        Iterable<ChainNode> nodes = getNodes();
        Assert.notNull(nodes, "Chain nodes must not null. ");
        for (ChainNode node : nodes) {
            node.execute(context);
        }
        return getResult(context);
    }

    public static class ChainContextImpl implements ChainContext {
        private Object[] arguments;
        private Object data;

        public ChainContextImpl(Object[] arguments) {

            this.arguments = arguments;
        }

        public ChainContextImpl() {

        }

        @Override
        public Object[] getArguments() {

            return arguments;
        }

        public void setArguments(Object[] arguments) {

            this.arguments = arguments;
        }

        @Override
        public Object getData() {

            return data;
        }

        @Override
        public void setData(Object data) {

            this.data = data;
        }
    }

}
