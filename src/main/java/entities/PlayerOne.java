package entities;

import game_modes.Game;
import game_modes.GameNormal;
import game_modes.ReadAndWrite;
import other.Input;
import java.io.IOException;
import java.util.ArrayList;
import static game_modes.Game.*;
import static other.Map.*;

public class PlayerOne extends Player implements ReadAndWrite {

    public PlayerOne(String name) {
        super(name);
    }

    @Override
    public boolean canMove(ArrayList<Chip> chips) {
        boolean result = false;
        for (int i = 0; i < chips.size(); i++) {
            if (cellIsFree(chips.get(i).getX(), chips.get(i).getY()-1)) result = true;         // Можно ли ходить фишками вверх
            else if (i == 0 && chips.get(0).getY() == 1 && cellIsFree(1, 0)) result = true; // Если левая фишка вверху, можно ли ей сходить по диагонали вправо
            else if (i == 2 && chips.get(2).getY() == 1 && cellIsFree(1, 0)) result = true; // Если правая фишка вверху, можно ли ей сходить по диагонали влево
            // Если выполнится хотя бы одно из условий, значит у игрока есть ходы
        }
        return result;
    }

    @Override
    public void go(ArrayList<Chip> chips) {
        boolean exit = false;
        while (!exit) {
            int chipNumber = Input.getInputPlayer1(); // номер фишки, которой будет ходить игрок 1
            int x = chips.get(chipNumber - 1).getX();
            int y = chips.get(chipNumber - 1).getY();
            if (cellIsFree(x, y - 1)) {             // Двигам фишку вверх
                setValue(x, y, VALUE_EMPTY);
                y--;
                chips.get(chipNumber - 1).setY(y);
                setValue(x, y, VALUE_PLAYER_X);
                exit = true;
                String strToWrite = "turn " + Game.turn + " Player1 chip " + chipNumber + "\n";
                if (gameMode == GAME_MODE_NORMAL) try {
                    GameNormal.writer.write(strToWrite);
                    GameNormal.writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (gameMode == GAME_MODE_NET_PLAYER1) {
                    ReadAndWrite.writeLineToFile(strToWrite);
                }
            } else if ((chipNumber == 1 || chipNumber == 3) && y == 1 && cellIsFree(1, 0)) {  // Двигаем фишку по диагонали вверх
                setValue(x, y, VALUE_EMPTY);
                chips.get(chipNumber - 1).setX(1);
                chips.get(chipNumber - 1).setY(0);
                setValue(1, 0, VALUE_PLAYER_X);
                exit = true;
                String strToWrite = "turn " + Game.turn + " Player1 chip " + chipNumber + "\n";
                if (gameMode == GAME_MODE_NORMAL) try {
                    GameNormal.writer.write(strToWrite);
                    GameNormal.writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (gameMode == GAME_MODE_NET_PLAYER1) {
                    ReadAndWrite.writeLineToFile(strToWrite);
                }
            } else System.out.println("Нельзя ходить этой фишкой в данный момент!");
        }
    }
}
