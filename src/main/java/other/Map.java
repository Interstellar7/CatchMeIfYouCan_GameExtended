package other;

import game_modes.Game;

/**
 * Класс Карта. Сама карта предствалена в виде двумерного массива
 * Имеет статические методы - рисование в консоль, проверка ячейки
*/
public class Map {
    public static final int X_SIZE = 3;     // размер поля по оси X
    public static final int Y_SIZE = 5;     // размер поля по оси Y
    public static final int VALUE_EMPTY = 0;        // значение свободного поля
    public static final int VALUE_ENGAGED = 5;      // значение непроходимого поля (углы)
    public static final int VALUE_PLAYER_X = 1;     // поле занято фишкой игрока 1
    public static final int VALUE_PLAYER_STAR = 2;  // поле занято фишкой игрока 2

    private static int[][] map;      // сама игровая карта

    // инициируем карту
    static {
        map = new int[X_SIZE][Y_SIZE];
        for (int i = 0; i < X_SIZE; i++) {
            for (int j = 0; j < Y_SIZE; j++) {
                map[i][j] = VALUE_EMPTY;
            }
                // углы
            map[0][0] = VALUE_ENGAGED;
            map[X_SIZE-1][0] = VALUE_ENGAGED;
            map[0][Y_SIZE-1] = VALUE_ENGAGED;
            map[X_SIZE-1][Y_SIZE-1] = VALUE_ENGAGED;
                // игровые фишки
            map[Game.chipStar.getX()][Game.chipStar.getY()] = VALUE_PLAYER_STAR;
            map[Game.chipX1.getX()][Game.chipX1.getY()] = VALUE_PLAYER_X;
            map[Game.chipX2.getX()][Game.chipX2.getY()] = VALUE_PLAYER_X;
            map[Game.chipX3.getX()][Game.chipX3.getY()] = VALUE_PLAYER_X;
        }
    }

    // метод рисует в консоли текущую игровую карту
    public static void printMap() {
        for (int j = 0; j < Y_SIZE; j++) {
            for (int i = 0; i < X_SIZE; i++) {
                String out = "";
                if (map[i][j] == VALUE_EMPTY) out = "[ ]";
                if (map[i][j] == VALUE_ENGAGED) out = "   ";
                if (map[i][j] == VALUE_PLAYER_X) out = "[X]";
                if (map[i][j] == VALUE_PLAYER_STAR) out = "[*]";
                System.out.print(out);
                if (i < X_SIZE-1) System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    // проверка: свободна ли ячейка по переданным координатам
    public static boolean cellIsFree(int x, int y) {
        boolean result = true;
        if (x < 0 || x >= X_SIZE || y < 0 || y >= Y_SIZE) result = false;
        else if (map[x][y] != VALUE_EMPTY) result = false;
        return result;
    }

    public static void setValue(int x, int y, int value) {
        map[x][y] = value;
    }
}
