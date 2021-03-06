package core.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;

import core.graphic.HoverGraphic;
import core.math.Vector2D;
import core.swing.SwingRenderer;

/**
 * A {@link Component} that is a button.
 */
public class ButtonComponent extends TextComponent {

    /**
     * 
     */
    private Runnable action;

    /**
     * @param pos the position of the button
     * @param text the text
     * @param action the action that will be invoked when the button is activated
     */
    public ButtonComponent(Vector2D pos, String text, Runnable action) {
        this(text, action);
        setPosition(pos);
    }

    /**
     * @param text the text
     * @param action the action that will be invoked when the button is activated
     */
    public ButtonComponent(String text, Runnable action) {
        super(text);
        setText(text);
        setAction(action);
        connectMouse();
        setBackgroundGraphic(new HoverGraphic(this, Color.green, Color.red));
    }

    /**
     * 
     * @param action
     */
    public ButtonComponent(Runnable action) {
        this("", action);
        setPadding(100, 100);
    }

    @Override
    public void render(SwingRenderer renderer, Vector2D pos) {
        super.render(renderer, pos);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (mouseHover())
        getAction().run();
    }

    /**
     * @return the action
     */
    public Runnable getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(Runnable action) {
        this.action = action;
    }
    
}