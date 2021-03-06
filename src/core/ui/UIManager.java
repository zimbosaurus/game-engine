package core.ui;

import java.util.ArrayList;

import core.IGraphic;
import core.IRenderer;
import core.obj.ObjectStorage;

/**
 * Handles and stores multiple {@link UserInterface} objects. One UI can be set as active
 * and that one will be rendered and updated. More work is needed.
 */
public class UIManager <R extends IRenderer<R>> extends ObjectStorage<UserInterface<R>, R> {

    /**
     * The UI that is active.
     */
    private UserInterface<R> active;

    /**
     * 
     */
    public UIManager() {
        super();

        getWindow().addResizeListener((size) -> {
            getInterfaces().forEach(i -> i.onResize(size));
        });
    }

    @Override
    protected boolean shouldPropagate(UserInterface<R> obj) {
        return obj == getActive();
    }

    @Override
    public IGraphic<R> getGraphic() {
        return getActive().getGraphic();
    }

    @Override
    public void onMount() {
    }

    @Override
    public void add(UserInterface<R> obj) {
        if (getObjects().size() < 1) setActive(obj);
        super.add(obj);
    }

    /**
     * Get an UI by class.
     * @param <C> the class of the ui
     * @param uiClass the class object
     * @return the ui of the class, or null
     */
    @SuppressWarnings("unchecked")
    public <C extends UserInterface<R>> C getByClass(Class<C> uiClass) {
        for (UserInterface<R> ui : getInterfaces()) {
            if (uiClass.isAssignableFrom(ui.getClass())) return (C) ui;
        }
        return null;
    }

    /**
     * Rebuild the UI components of every {@link UserInterface}.
     */
    public void reload() {
        getInterfaces().forEach(UserInterface::reload);
    }

    /**
     * Set an interface as active, show it, and hide the one that was active before.
     * @param ui the ui to show
     */
    public void show(UserInterface<R> ui) {
        getActive().setVisible(false);
        setActive(ui);
        getActive().setVisible(true);
    }

    /**
     * Set an interface as active, show it, and hide the one that was active before.
     * @param c the ui to show
     */
    public <C extends UserInterface<R>> void show(Class<C> c) {
        show(getByClass(c));
    }

    /**
     * @param ui
     */
    public void remove(UserInterface<R> ui) {
        getInterfaces().remove(ui);
    }

    /**
     * @return the active
     */
    public UserInterface<R> getActive() {
        return active;
    }

    /**
     * Set an ui as active in the manager. This will make it the one that is shown by
     * 
     * @param active the ui to set to active
     */
    protected void setActive(UserInterface<R> active) {
        this.active = active;
    }

    /**
     * @return the interfaces
     */
    protected ArrayList<UserInterface<R>> getInterfaces() {
        return getObjects();
    }

    
}