package game_modes;

import entities.PlayerOne;
import entities.PlayerTwo;
import other.Map;
import java.io.*;
import java.util.Scanner;

/**
 * Данный класс реализует 2 режима: обычный и режим воспроизведения предыдущей игры
 */
public class GameNormal extends Game {
    PlayerOne player1;      // Игроки
    PlayerTwo player2;
    public static Scanner sc;
    public static File log = new File("game_log.txt");  // Файл для записи лога игры
    public static FileWriter writer = null;
    public static FileReader fileReader = null;

    static {
        try {
            if (gameMode == GAME_MODE_NORMAL) writer = new FileWriter(log, false);
            if (gameMode == GAME_MODE_REPLAY) fileReader = new FileReader(log);
        } catch (IOException e) {
            System.out.println("Проблема с доступом к log-файлу");
            exit();
        }
    }

    @Override
    public void runGame() {
        String name1 = DEFAULT_PLAYER1_NAME;
        String name2 = DEFAULT_PLAYER2_NAME;
        System.out.println("Начинаем игру!");
        if (gameMode == GAME_MODE_NORMAL) {         // Читаем имена игроков с консоли
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.print("Введите имя игрока 1: ");
                name1 = reader.readLine();
                System.out.print("Введите имя игрока 2: ");
                name2 = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Читаем имена игроков из лог-файла
        if (gameMode == GAME_MODE_REPLAY) {
            sc = new Scanner(fileReader);
            sc.nextLine();
            String[] s = sc.nextLine().split(" ");
            name1 = s[s.length-1];
            s = sc.nextLine().split(" ");
            name2 = s[s.length-1];
            System.out.println("Имя Игрока 1 - " + name1);
            System.out.println("Имя Игрока 2 - " + name2);
        }
        player1 = new PlayerOne(name1);
        player2 = new PlayerTwo(name2);
        if (gameMode == GAME_MODE_NORMAL) {
            try {
                writer.write("Start game!\n");
                writer.write("Player1 name is " + name1 + "\n");
                writer.write("Player2 name is " + name2 + "\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Главный цикл игры
        Map.printMap();
        while(!endGame) {
            turn++;
            // Ход Игрока 1
            System.out.println("Ход " + Game.turn + ". Ходит игрок 1 - " + player1.getName());
            if (player1.canMove(chipsX)) {
                player1.go(chipsX);
                Map.printMap();
            }
            else System.out.println(player1.getName() + " пропускает ход, так как некуда ходить");
            if (chipStar.getY() > yCoordOfTheLowestChipX()) {      // Условие победы Игрока 2
                winningPlayer = 2;
                endGame = true;
            }
            // Ход Игрока 2
            if (!endGame) {
                System.out.println("Ход " + Game.turn + ". Ходит игрок 2 - " + player2.getName());
                if (player2.canMove(chipsStar)) {
                    player2.go(chipsStar);
                    Map.printMap();
                } else {                                      // Условие победы Игрока 1
                    winningPlayer = 1;
                    endGame = true;
                }
                if (chipStar.getY() > yCoordOfTheLowestChipX()) {      // Условие победы Игрока 2
                    winningPlayer = 2;
                    endGame = true;
                }
            }
        }
        if (winningPlayer == 1) {
            System.out.println("Игроку 2 некуда ходить!");
            System.out.println("ПОБЕДИЛ ИГРОК 1 " + player1.getName() + "! ПОЗДРАВЛЯЕМ С ПОБЕДОЙ!!!");
        }
        if (winningPlayer == 2) {
            System.out.println("Фишка игрока 2 оказалась ниже всех фишек первого игрока.");
            System.out.println("ПОБЕДИЛ ИГРОК 2 " + player2.getName() + "! ПОЗДРАВЛЯЕМ С ПОБЕДОЙ!!!");
        }
        System.out.println("Конец игры!");
        if (gameMode == GAME_MODE_NORMAL)
            try {
                if (winningPlayer == 1) {
                    writer.write("Winner is Player1 " + player1.getName() + "\n");
                    writer.flush();
                }
                if (winningPlayer == 2) {
                    writer.write("Winner is Player2 " + player2.getName() + "\n");
                    writer.flush();
                }
                writer.write("End of the game!\n");
                writer.flush();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
