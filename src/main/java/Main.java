import game_modes.*;
import static game_modes.Game.*;

public class Main {
    public static void main(String[] args) {
        initGame();
        Game game = null;
        switch (gameMode) {
            case GAME_MODE_NORMAL :
            case GAME_MODE_REPLAY :
                game = new GameNormal(); break;
            case GAME_MODE_NET_PLAYER1 : game = new GameNetPlayer1(); break;
            case GAME_MODE_NET_PLAYER2 : game = new GameNetPlayer2(); break;
        }
        game.runGame();
    }
}
