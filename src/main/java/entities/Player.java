package entities;

import java.util.ArrayList;

/**
 * Заготовка Игрок
 */
abstract class Player {
    private String name;

    public Player(String name) {
        this.name = name;
    }

    /**
     * Метод проверяет есть ли у игрока свободные ходы
     * @param chips список фишек игрока
     * @return возвращает true если есть куда двигаться хоть одной фишкой
     */
    abstract boolean canMove(ArrayList<Chip> chips);

    /**
     * Метод реализует движение фишки, включая считывание с консоли или файла и проверку введённых данных
     * @param chips список фишек игрока
     */
    abstract void go(ArrayList<Chip> chips);

    public String getName() {
        return name;
    }
}
