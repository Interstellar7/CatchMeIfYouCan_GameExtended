package other;

import game_modes.Game;
import game_modes.GameNormal;
import game_modes.ReadAndWrite;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static game_modes.Game.*;

/**
 * Класс в зависимости от выбранного режима игры считывает ход либо из файла, либо из консоли
 */
public class Input implements ReadAndWrite {

    /**
     * Получить ход игрока 1
     * @return  возвращает номер фишки
     */
    public static int getInputPlayer1() {
        int chipNumber = 0;
        // если обычный режим игры или сетевой для первого игрока - ввод с консоли
        if (gameMode == GAME_MODE_NORMAL || gameMode == GAME_MODE_NET_PLAYER1) {
            boolean exit = false;
            while (!exit) {
                System.out.print("Введите номер фишки, которой нужно ходить (1-3) > ");
                BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
                try {
                    String inp = bf.readLine();
                    if (inp.equals(COMMAND_STOP)) {
                        System.out.println("Игра остановлена");
                        if (gameMode == GAME_MODE_NORMAL) {
                            GameNormal.writer.write("The game was stopped");
                            GameNormal.writer.flush();
                        }
                        if (gameMode == GAME_MODE_NET_PLAYER1) {
                            ReadAndWrite.writeLineToFile("The game was stopped");
                        }
                        Game.exit();
                    }
                    chipNumber = Integer.parseInt(inp);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    //
                }
                if (1 <= chipNumber && chipNumber <= 3) exit = true;
                else System.out.println("Нужно ввести число от 1 до 3, либо команду " + COMMAND_STOP + " для выхода");
            }
        }
        // в режиме воспроизведения читаем из лог-файла
        if (gameMode == GAME_MODE_REPLAY) {
            String[] s = GameNormal.sc.nextLine().split(" ");
            if (s[s.length-1].equals("stopped")) {
                System.out.println("Игра остановлена");
                Game.exit();
            } else {
                chipNumber = Integer.parseInt(s[4]);
                try {
                    Thread.sleep(SLEEP_TIME_FOR_REPLAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // если режим сетевой игры для второго игрока, то считываем ход игрока 1 с лог-файла
        if (gameMode == GAME_MODE_NET_PLAYER2) {
            String[] s = ReadAndWrite.readLastLineFromFile().split(" ");
            String playerName = "";
            if (s.length > 1) playerName = s[2];
            while (!playerName.equals("Player1")) {
                if (s[s.length-1].equals("stopped")) {
                    System.out.println("Игра остановлена");
                    Game.exit();
                }
                System.out.println("Ожидаем ход игрока 1...");
                try {
                    Thread.sleep(NET_PLAYER_WAITING_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                s = ReadAndWrite.readLastLineFromFile().split(" ");
                if (s.length > 1) playerName = s[2];
            }
            chipNumber = Integer.parseInt(s[4]);
        }
        return chipNumber;
    }

    /**
     * Получить ход игрока 2
     * @return  возвращает номер направления
     */
    public static int getInputPlayer2() {
        int direction = 0;
        // если обычный режим игры или сетевой для второго игрока - ввод с консоли
        if (gameMode == GAME_MODE_NORMAL || gameMode == GAME_MODE_NET_PLAYER2) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            boolean exit = false;
            while (!exit) {
                System.out.print("Введите направление движения: 4 - влево, 6 - вправо, 8 - вверх, 2 - вниз, 1 и 3 - вниз по диагонали (только если фишка в самом верху) > ");
                try {
                    String inp = reader.readLine();
                    if (inp.equals(Game.COMMAND_STOP)) {
                        System.out.println("Игра остановлена");
                        if (gameMode == GAME_MODE_NORMAL) {
                            GameNormal.writer.write("The game was stopped");
                            GameNormal.writer.flush();
                        }
                        if (gameMode == GAME_MODE_NET_PLAYER2) {
                            ReadAndWrite.writeLineToFile("The game was stopped");
                        }
                        System.exit(0);
                    }
                    direction = Integer.parseInt(inp);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    //
                }
                if (direction < 1 || direction == 5 || direction == 7 || direction > 8) {
                    System.out.println("Нужно ввести число 1-4, 6, 8, либо команду " + Game.COMMAND_STOP + " для выхода");
                } else exit = true;
            }
        }
        // в режиме воспроизведения читаем из лог-файла
        if (gameMode == GAME_MODE_REPLAY) {
            String[] s = GameNormal.sc.nextLine().split(" ");
            if (s[s.length-1].equals("stopped")) {
                System.out.println("Игра остановлена");
                Game.exit();
            } else {
                direction = Integer.parseInt(s[4]);
                try {
                    Thread.sleep(SLEEP_TIME_FOR_REPLAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // если режим сетевой игры для первого игрока, то считываем ход игрока 2 с лог-файла
        if (gameMode == GAME_MODE_NET_PLAYER1) {
            String[] s = ReadAndWrite.readLastLineFromFile().split(" ");
            String playerName = "";
            if (s.length > 1) playerName = s[2];
            while (!playerName.equals("Player2")) {
                if (s[s.length-1].equals("stopped")) {
                    System.out.println("Игра остановлена");
                    Game.exit();
                }
                System.out.println("Ожидаем ход игрока 2...");
                try {
                    Thread.sleep(NET_PLAYER_WAITING_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                s = ReadAndWrite.readLastLineFromFile().split(" ");
                if (s.length > 1) playerName = s[2];
            }
            direction = Integer.parseInt(s[4]);
        }
        return direction;
    }
}
