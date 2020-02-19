package game_modes;
import java.io.*;
import java.util.Scanner;

public interface ReadAndWrite {
    File netLog = new File("N:\\net_log.txt");  // Лог-файл сетевой игры

    static String readLastLineFromFile() {
        FileReader fileReader;
        Scanner sc;
        String line = "";
        try {
            fileReader = new FileReader(netLog);
            sc = new Scanner(fileReader);

            while (sc.hasNextLine()) {
                line = sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл " + netLog.getName() + " не найден");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return line;
    }

    static void writeLineToFile(String line) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(netLog, true);
            fileWriter.write(line);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
