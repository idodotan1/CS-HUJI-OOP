/**
 * The class used to create new renderers
 */
public class RendererFactory {
    /**
     * A constructor for the renderer factory
     */
    public RendererFactory() {
    }

    /**
     * The factory to create the renderer
     * @param type The type of the renderer(void/console)
     * @param size The size of the board
     * @return a new renderer according to the type given
     */
    public Renderer buildRenderer(String type, int size) {
        return switch (type){
            case "console" -> new ConsoleRenderer(size);
            case "void" -> new VoidRenderer();
            default -> null;
        };
    }
}
