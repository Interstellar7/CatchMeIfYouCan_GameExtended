package game_modes;

import entities.PlayerOne;
import entities.PlayerTwo;
import other.Map;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Сетевой режим для игрока 1
 */
public class GameNetPlayer1 extends Game implements ReadAndWrite {

    @Override
    public void runGame() {
        String name1 = DEFAULT_PLAYER1_NAME;
        String name2 = DEFAULT_PLAYER2_NAME;
        boolean exit = false;
        System.out.println("Начинаем игру!");
        try {
            FileWriter fileWriter = new FileWriter(netLog, false);
            fileWriter.write("Start network game!\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Читаем имя игрока с консоли
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Введите имя игрока 1: ");
            name1 = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Записываем имя игрока 1 в лог файл
        ReadAndWrite.writeLineToFile("Player1 name is " + name1 + "\n");
        // Читаем имя игрока 2 из лог файла
        do {
            String[] s = ReadAndWrite.readLastLineFromFile().split(" ");
            if (s[0].equals("Player2")) {
                name2 = s[3];
                exit = true;
            } else {
                System.out.println("Ожидаем, когда игрок 2 напишет своё имя...");
                try {
                    Thread.sleep(NET_PLAYER_WAITING_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (!exit);
        System.out.println("Имя игрока 2 - " + name2);
        // Создаём игроков
        PlayerOne player1 = new PlayerOne(name1);
        PlayerTwo player2 = new PlayerTwo(name2);

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

        // Окончание игры
        if (winningPlayer == 1) {
            System.out.println("Игроку 2 некуда ходить!");
            System.out.println("ПОБЕДИЛ ИГРОК 1 " + player1.getName() + "! ПОЗДРАВЛЯЕМ С ПОБЕДОЙ!!!");
        }
        if (winningPlayer == 2) {
            System.out.println("Фишка игрока 2 оказалась ниже всех фишек первого игрока.");
            System.out.println("ПОБЕДИЛ ИГРОК 2 " + player2.getName() + "! ПОЗДРАВЛЯЕМ С ПОБЕДОЙ!!!");
            try {
                Thread.sleep(NET_PLAYER_WAITING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ReadAndWrite.writeLineToFile("Winner is Player2 \"" + player2.getName() + "\"\n");
            ReadAndWrite.writeLineToFile("End of the game!\n");
        }
        System.out.println("Конец игры.");
    }

}
