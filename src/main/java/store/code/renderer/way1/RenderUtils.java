package store.code.renderer.way1;

import artoria.io.StringBuilderWriter;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Template render tools.
 * @author Kahle
 */
public class RenderUtils {
    private static Logger log = LoggerFactory.getLogger(RenderUtils.class);
    private static Renderer renderer;

    public static Renderer getRenderer() {
        if (renderer != null) { return renderer; }
        synchronized (RenderUtils.class) {
            if (renderer != null) { return renderer; }
            RenderUtils.setRenderer(new SimpleRenderer());
            return renderer;
        }
    }

    public static void setRenderer(Renderer renderer) {
        Assert.notNull(renderer, "Parameter \"renderer\" must not null. ");
        log.info("Set template renderer: {}", renderer.getClass().getName());
        RenderUtils.renderer = renderer;
    }

    public static void render(Object data, Writer output, String name, String charset) {

        getRenderer().render(data, output, name, null, charset);
    }

    public static void render(Object data, Writer output, String name, Reader input) {

        getRenderer().render(data, output, name, input, null);
    }

    public static void render(Object data, OutputStream output, String name, String charset) {

        getRenderer().render(data, output, name, null, charset);
    }

    public static void render(Object data, OutputStream output, String name, InputStream input, String charset) {

        getRenderer().render(data, output, name, input, charset);
    }

    public static String renderToString(Object data, String name, String charset) {
        StringBuilderWriter output = new StringBuilderWriter();
        getRenderer().render(data, output, name, null, charset);
        return output.toString();
    }

    public static String renderToString(Object data, String name, Reader input) {
        StringBuilderWriter output = new StringBuilderWriter();
        getRenderer().render(data, output, name, input, null);
        return output.toString();
    }

    public static String renderToString(Object data, String name, InputStream input, String charset) {
        StringBuilderWriter output = new StringBuilderWriter();
        getRenderer().render(data, output, name, input, charset);
        return output.toString();
    }

}
