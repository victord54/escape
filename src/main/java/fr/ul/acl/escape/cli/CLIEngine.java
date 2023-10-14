package fr.ul.acl.escape.cli;

import fr.ul.acl.escape.engine.GameController;
import fr.ul.acl.escape.engine.GameInterface;

public class CLIEngine extends fr.ul.acl.escape.engine.Engine {
    /**
     * If the game should continue.
     */
    private boolean again = true;

    protected CLIEngine(GameInterface ui, GameController controller) {
        super(ui, controller);
    }

    @Override
    protected void tick(long now) {
        ui.render();
        controller.update(now);
    }

    @Override
    public void start() {
        do {
            tick(0);
        } while (again);
    }

    @Override
    public void stop() {
        again = false;
    }
}
