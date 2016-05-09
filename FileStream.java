package snake;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileStream {
  private static int readedScore;

  /**
   * This method writes list of objects(contents game information)
   */
  public static void writeArrayInFile(ArrayList<SaveThisMoment> listOfMoments, File file,
      int score) {
    if (file.exists()) {
      file.delete();
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    FileOutputStream fileOutputStream;
    try {
      fileOutputStream = new FileOutputStream(file, false);

      try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
        objectOutputStream.writeInt(score);
        objectOutputStream.writeInt(listOfMoments.size());

        for (SaveThisMoment step : listOfMoments) {
          objectOutputStream.writeObject(step);
        }
        objectOutputStream.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        System.out.println("error: cannot save to file");
        return;
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("error: IO error");
        return;
      }
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * @param file - container of replay
   * @return ArrayList<SaveThisMoment> that was read from file
   */
  public static ArrayList<SaveThisMoment> readFromFile(File file) {
    ArrayList<SaveThisMoment> listOfMoments = new ArrayList<SaveThisMoment>();
    try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
      SaveThisMoment moment = new SaveThisMoment();
      listOfMoments.clear();
      readedScore = objectInputStream.readInt();
      int sizeOfList = objectInputStream.readInt();
      for (int i = 0; i < sizeOfList; i++) {
        try {
          moment = (SaveThisMoment) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }

        listOfMoments.add(moment.clone());
      }
      objectInputStream.close();
    } catch (FileNotFoundException e) {
      System.out.println("error: cannot open file");
    } catch (IOException e) {
      System.out.println("error: IO error, cannot read");
    }
    return listOfMoments;
  }

  public static int getReadedScore() {
    return readedScore;
  }
}
