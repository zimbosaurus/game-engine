package core;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.JFrame;

import core.io.resources.ImageResource;
import core.math.Vector2D;
import core.swing.SwingRenderer;
import core.util.FrameUtil;

/**
 * A concrete subtype of {@link Window} that uses the {@link SwingRenderer}.
 */
public class SwingWindow extends Window<SwingRenderer> {

    /**
     * The {@link GamePanel} is the <i>canvas</i> on which the graphics are drawn.
     * <p>This panel is added as a child component to the frame.
     */
    private GamePanel panel;

    /**
     * The game this window belongs to.
     */
    private Game game;

    /**
     * The frame is the root component of the window. A JFrame is actual window that has the title bar and buttons and such.
     */
    private JFrame frame;

    /**
     * The title of the window.
     */
    private String title;

    /**
     * The dimensions of the window.
     */
    final private Dimension size;

    /**
     * 
     */
    private ArrayList<Consumer<Vector2D>> resizeListeners;

    /**
     * Temp variable for the consumer to be invoked in paint(Graphics)
     */
    private Consumer<SwingRenderer> renderConsumer;

    /**
     * Create a window.
     * 
     * @param title the title
     * @param size the size
     */
    public SwingWindow(String title, Dimension size) {
        this.title = title;
        this.size = size;
    }

    @Override
    public void build(Game game) {

        this.game = game;

        frame = new JFrame(title);

        GamePanel panel = new GamePanel(this);
        initializePanel(panel);

        frame.setSize(size);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(getPanel());

        frame.validate();

        resizeListeners = new ArrayList<>();

        setResizeCallback();
    }

    @Override
    public void run() {
        frame.setVisible(true);
        panel.setVisible(true);
    }

    @Override
    public void render(Consumer<SwingRenderer> renderConsumer) {
        // Calling repaint on the panel will call the paint(g) method below with a graphics object that will be used to render things
        getPanel().repaint();
        this.renderConsumer = renderConsumer;
    }

    /**
     * Paint graphics. This method is called by {@link #render(SwingRenderer)}.
     * 
     * @param g the graphics component
     */
    public void paint(Graphics2D g) {
        SwingRenderer renderer = getRenderer();
        renderer.setGraphics(g);

        if (renderConsumer != null)
            renderConsumer.accept(renderer);
    }

    /**
     * @param panel
     */
    private void initializePanel(GamePanel panel) {
        setPanel(panel);
        getPanel().initialize(getGame());
        this.keyInput = panel.getKeyInput();
        this.mouseInput = panel.getMouseInput();
    }

    /**
     * 
     */
    private void setResizeCallback() {
        getPanel().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Vector2D s = new Vector2D(e.getComponent().getSize());
                onResize(s);
            }
        });
    }

    @Override
    public void maximize() {
        FrameUtil.maximize(frame);
    }

    @Override
    public void fullscreen() {
        FrameUtil.setFullscreen(frame);
    }


    @Override
    public Dimension getSize() {
        return getFrame().getSize();
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        getFrame().setTitle(title);
    }

    @Override
    public void setIcon(File icon) {
        ImageResource img = ImageResource.create(icon);
        img.loadImage();
        getFrame().setIconImage(img.getBufferedImage());
    }

    @Override
    public void onResize(Vector2D size) {
        resizeListeners.forEach(c -> c.accept(size));
    }

    @Override
    public void addResizeListener(Consumer<Vector2D> consumer) {
        resizeListeners.add(consumer);
    }

    @Override
    public void removeResizeListener(Consumer<Vector2D> consumer) {
        resizeListeners.remove(consumer);
    }

    /**
     * @param panel the panel to set
     */
    public void setPanel(GamePanel panel) {
        this.panel = panel;
    }

    /**
     * @return the panel
     */
    public GamePanel getPanel() {
        return panel;
    }

    /**
     * @return the frame
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * @return the game
     */
    public Game getGame() {
        return game;
    }

}