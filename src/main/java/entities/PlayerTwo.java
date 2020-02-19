package entities;

import game_modes.Game;
import game_modes.GameNormal;
import game_modes.ReadAndWrite;
import other.Input;
import other.Map;
import java.io.IOException;
import java.util.ArrayList;
import static game_modes.Game.*;

public class PlayerTwo extends Player implements ReadAndWrite {

    public PlayerTwo(String name) {
        super(name);
    }

    @Override
    public boolean canMove(ArrayList<Chip> chips) {
        Chip chip = chips.get(0);   // У игрока 2 всего одна фишка
        boolean result = false;
        if (Map.cellIsFree(chip.getX()-1, chip.getY())) result = true;      // Свободна ли клетка слева
        else if (Map.cellIsFree(chip.getX()+1, chip.getY())) result = true; // Свободна ли клетка справа
        else if (Map.cellIsFree(chip.getX(), chip.getY()-1)) result = true; // Свободна ли клетка сверху
        else if (Map.cellIsFree(chip.getX(), chip.getY()+1)) result = true; // Свободна ли клетка снизу
        else if ((chip.getY() == 0) && (Map.cellIsFree(0, 1) || Map.cellIsFree(2, 1))) result = true; // Если фишка в самом верху, свободны ли клетки по диагонали вниз
        // Если выполнится хотя бы одно из условий, значит у игрока есть ходы
        return result;
    }

    @Override
    public void go(ArrayList<Chip> chips) {
        Chip chip = chips.get(0);   // У игрока 2 всего одна фишка
        boolean exit = false;
        int x = chip.getX();
        int y = chip.getY();
        while (!exit) {
            int direction = Input.getInputPlayer2();
            if (direction == 2) {          // Движение фишки вниз
                if (Map.cellIsFree(x, y + 1)) {
                    Map.setValue(x, y, Map.VALUE_EMPTY);
                    y++;
                    chip.setY(y);
                    Map.setValue(x, y, Map.VALUE_PLAYER_STAR);
                    exit = true;
                } else System.out.println("Нельзя туда ходить!");
            } else if (direction == 8) {    // Движение фишки вверх
                if (Map.cellIsFree(x, y - 1)) {
                    Map.setValue(x, y, Map.VALUE_EMPTY);
                    y--;
                    chip.setY(y);
                    Map.setValue(x, y, Map.VALUE_PLAYER_STAR);
                    exit = true;
                } else System.out.println("Нельзя туда ходить!");
            } else if (direction == 4) {    // Движение фишки влево
                if (Map.cellIsFree(x - 1, y)) {
                    Map.setValue(x, y, Map.VALUE_EMPTY);
                    x--;
                    chip.setX(x);
                    Map.setValue(x, y, Map.VALUE_PLAYER_STAR);
                    exit = true;
                } else System.out.println("Нельзя туда ходить!");
            } else if (direction == 6) {    // Движение фишки вправо
                if (Map.cellIsFree(x + 1, y)) {
                    Map.setValue(x, y, Map.VALUE_EMPTY);
                    x++;
                    chip.setX(x);
                    Map.setValue(x, y, Map.VALUE_PLAYER_STAR);
                    exit = true;
                } else System.out.println("Нельзя туда ходить!");
            } else if (direction == 1 && y == 0) {  // Движение фишки по диагонали влево
                if (Map.cellIsFree(0, 1)) {
                    Map.setValue(x, y, Map.VALUE_EMPTY);
                    x--; y++;
                    chip.setX(x); chip.setY(y);
                    Map.setValue(x, y, Map.VALUE_PLAYER_STAR);
                    exit = true;
                } else System.out.println("Нельзя туда ходить!");
            } else if (direction == 3 && y == 0) {  // Движение фишки по диагонали вправо
                if (Map.cellIsFree(2, 1)) {
                    Map.setValue(x, y, Map.VALUE_EMPTY);
                    x++; y++;
                    chip.setX(x); chip.setY(y);
                    Map.setValue(x, y, Map.VALUE_PLAYER_STAR);
                    exit = true;
                } else System.out.println("Нельзя туда ходить!");
            } else System.out.println("По диагонали можно ходить только с самой  верхней клетки.");
            // если направление получено, то записываем ход в лог файл
            if (exit) {
                String strToWrite = "turn " + Game.turn + " Player2 direction " + direction + "\n";
                if (gameMode == GAME_MODE_NORMAL) try {
                    GameNormal.writer.write(strToWrite);
                    GameNormal.writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (gameMode == GAME_MODE_NET_PLAYER2) {
                    ReadAndWrite.writeLineToFile(strToWrite);
                }
            }
        }
    }
}
