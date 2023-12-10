package fr.ul.acl.escape.cli;

import fr.ul.acl.escape.engine.GameController;
import fr.ul.acl.escape.engine.GameInterface;

public class CLIEngine extends fr.ul.acl.escape.engine.Engine {
    /**
     * If the game should continue.
     */
    private boolean again = true;

    private int turn = 0;

    protected CLIEngine(GameInterface ui, GameController controller) {
        super(ui, controller);
    }

    @Override
    protected void tick(long now) {
        ui.render();
        controller.update(1_000_000_000, now * 1_000_000_000); // 1 tick = 1 second in CLI
    }

    @Override
    public void start() {
        do {
            tick(++turn);
        } while (again);
    }

    @Override
    public void stop() {
        again = false;
    }

    /**
     * @return the current turn
     */
    public int getTurn() {
        return turn;
    }
}
