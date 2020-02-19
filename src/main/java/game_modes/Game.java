package game_modes;

import entities.Chip;
import other.Map;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс Игра. Для каждого режима игры должны быть наследники со своей реализацией
 */
public abstract class Game {
    public static int turn = 0;                     // Номер хода для логгирования
    public static boolean endGame = false;          // Флаг окончания игры для выхода из главного цикла
    public static int winningPlayer = 0;            // Номер победившего игрока
    public static final int SLEEP_TIME_FOR_REPLAY = 2000;    // Пауза в режиме воспроизведения между ходами
    public static final int NET_PLAYER_WAITING_TIME = 2000;  // Ожидание действия игрока в режиме сетевой игры
    // Создаём игровые фишки
    public static Chip chipX1 = new Chip(0, Map.Y_SIZE-2);
    public static Chip chipX2 = new Chip(1, Map.Y_SIZE-1);
    public static Chip chipX3 = new Chip(2, Map.Y_SIZE-2);
    static ArrayList<Chip> chipsX = new ArrayList<>(3);     // Массив фишек Игрока 1
    public static Chip chipStar = new Chip(1, 2);
    static ArrayList<Chip> chipsStar = new ArrayList<>(1);  // Массив фишек Игрока 2 с одним элементом
    // Основные команды для консоли
    public static final String COMMAND_START = "start";            // Старт игры в обычном режиме
    public static final String COMMAND_REPLAY = "replay";          // Воспроизведения последней игры
    public static final String COMMAND_NET_PLAYER1 = "netstart1";  // Старт сетевой игры для первого игрока
    public static final String COMMAND_NET_PLAYER2 = "netstart2";  // Старт сетевой игры для второго игрока
    public static final String COMMAND_RULES = "rules";            // Команда вывода правил игры
    public static final String COMMAND_STOP = "stop";              // Выход из игры
    // Имена игроков по умолчанию
    static final String DEFAULT_PLAYER1_NAME = "Игрок1";
    static final String DEFAULT_PLAYER2_NAME = "Игрок2";
    // Режимы игры
    public static int gameMode = 0;
    public static final int GAME_MODE_NORMAL = 1;   // Обычный режим игры - два игрока за одним компьютером
    public static final int GAME_MODE_REPLAY = 2;   // Режим воспроизведения последней игры
    public static final int GAME_MODE_NET_PLAYER1 = 3;      // Игра по сети через сетевую папку для 1-го игрока
    public static final int GAME_MODE_NET_PLAYER2 = 4;      // Игра по сети через сетевую папку для 2-го игрока
    //

    static {
        chipsX.add(chipX1);
        chipsX.add(chipX2);
        chipsX.add(chipX3);
        chipsStar.add(chipStar);
    }

    public Game() {
    }

    /**
     *  Метод с главным циклом. Для каждого режима игры - своя реализация метода.
     */
    public abstract void runGame();

    /**
     * Инициализация игры и начало
     */
    public static void initGame() {
        System.out.println("Добро пожаловать в консольную игру \"Поймай меня, если сможешь!\"");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command = "";
        while (true) {
            System.out.println("Доступные команды:");
            System.out.printf("%s - правила игры\n", COMMAND_RULES);
            System.out.printf("%s - воспроизвести последнюю игру\n", COMMAND_REPLAY);
            System.out.printf("%s - старт игры в обычном режиме\n", COMMAND_START);
            System.out.printf("%s - сетевая игра (1-й игрок)\n", COMMAND_NET_PLAYER1);
            System.out.printf("%s - сетевая игра (2-й игрок)\n", COMMAND_NET_PLAYER2);
            System.out.printf("%s - выход из игры\n", COMMAND_STOP);
            System.out.print("> ");
            try {
                command = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (command.equals(COMMAND_RULES)) {
                // Выводим правила игры из файла
                ClassLoader classLoader = Game.class.getClassLoader();
                File file = new File(Objects.requireNonNull(classLoader.getResource("rules.txt")).getFile());
                try {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine())  System.out.println(scanner.nextLine());
                } catch (FileNotFoundException e) {
                    System.out.println("Проблемы с загрузкой файла \"rules.txt\"");
                }
                System.out.println();
            }
            if (command.equals(COMMAND_STOP)) {
                System.out.println("Игра остановлена");
                exit();
            }
            if (command.equals(COMMAND_START)) {
                gameMode = GAME_MODE_NORMAL;
            }
            if (command.equals(COMMAND_REPLAY)) {
                gameMode = GAME_MODE_REPLAY;
            }
            if (command.equals(COMMAND_NET_PLAYER1)) {
                gameMode = GAME_MODE_NET_PLAYER1;
            }
            if (command.equals(COMMAND_NET_PLAYER2)) {
                gameMode = GAME_MODE_NET_PLAYER2;
            }
            if (1 <= gameMode && gameMode <= 4) return;
        }
    }

    // Метод возвращает координату самой нижней фишки игрока 1 (для победы игрока 2)
    public int yCoordOfTheLowestChipX() {
        int max = 0;
        for (Chip x : chipsX) {
            if (x.getY() > max) max = x.getY();
        }
        return max;
    }

    public static void exit() {
        System.out.println("Выход из игры");
        System.exit(0);
    }
}
