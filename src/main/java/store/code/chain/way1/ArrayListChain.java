package store.code.chain.way1;

import artoria.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrayListChain extends AbstractChain {
    private final List<ChainNode> nodes;

    public ArrayListChain(ArrayList<ChainNode> nodes) {
        Assert.notNull(nodes, "Parameter \"nodes\" must not null. ");
        this.nodes = nodes;
    }

    public ArrayListChain() {

        this(new ArrayList<ChainNode>());
    }

    public ArrayListChain add(ChainNode node) {
        nodes.add(node);
        return this;
    }

    public ArrayListChain add(int index, ChainNode node) {
        nodes.add(index, node);
        return this;
    }

    public ArrayListChain addAll(Collection<ChainNode> collection) {
        nodes.addAll(collection);
        return this;
    }

    public ArrayListChain addAll(int index, Collection<ChainNode> collection) {
        nodes.addAll(index, collection);
        return this;
    }

    public ArrayListChain set(int index, ChainNode node) {
        nodes.set(index, node);
        return this;
    }

    public int size() {

        return nodes.size();
    }

    @Override
    protected Iterable<ChainNode> getNodes() {

        return nodes;
    }

}
