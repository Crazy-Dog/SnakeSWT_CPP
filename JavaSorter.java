package snake;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("rawtypes")
public class JavaSorter implements Comparator {
  String path = "replays/";
  String mode;
  File folder;
  private String sortedPath;
  private String defaultFileName;

  public JavaSorter(String mode) {
    folder = new File(path);
    this.mode = mode;
    switch (mode) {
      case "ByTurnsCount":
        sortedPath = "replays_sorted_by_steps_count_Java/";
        defaultFileName = "replay_sorted_by_steps_count_Java.out";
        break;
      case "ByScore":
        sortedPath = "replays_sorted_by_score_Java/";
        defaultFileName = "replay_sorted_by_score.out";
        break;
    }
  }

  @Override
  public int compare(Object o1, Object o2) {
    File file1 = (File) o1;
    File file2 = (File) o2;

    switch (mode) {
      case "ByTurnsCount":
        int steps1 = 0;
        int steps2 = 0;
        @SuppressWarnings("unused")
        int space = 0;
        try (ObjectInputStream objectInputStream =
            new ObjectInputStream(new FileInputStream(file1))) {
          space = objectInputStream.readInt();
          steps1 = objectInputStream.readInt();
          objectInputStream.close();
        } catch (FileNotFoundException e) {
          System.out.println("error: cannot open file");
        } catch (IOException e) {
          System.out.println("error: IO error, cannot read");
        }
        try (ObjectInputStream objectInputStream =
            new ObjectInputStream(new FileInputStream(file2))) {
          space = objectInputStream.readInt();
          steps2 = objectInputStream.readInt();
          objectInputStream.close();
        } catch (FileNotFoundException e) {
          System.out.println("error: cannot open file");
        } catch (IOException e) {
          System.out.println("error: IO error, cannot read");
        }
        if (steps1 > steps2)
          return -1;
        else if (steps1 < steps2)
          return 1;
        break;
      case "ByScore":
        int score1;
        int score2;
        FileStream.readFromFile(file1);
        score1 = FileStream.getReadedScore();
        FileStream.readFromFile(file2);
        score2 = FileStream.getReadedScore();
        if (score1 > score2)
          return -1;
        else if (score1 < score2)
          return 1;
        break;
    }

    return 0;
  }

  @SuppressWarnings("unchecked")
  public void sort() throws FileNotFoundException {
    List<File> listOfFiles = new ArrayList<File>();
    // get all filenames with .out extension
    String[] files = folder.list(new FilenameFilter() {
      @Override
      public boolean accept(File folder, String name) {
        return name.endsWith(".out");
      }
    });
    for (int i = 0; i < files.length; i++) {
      File file = new File(path + files[i]);
      listOfFiles.add(file);
    }
    Collections.sort(listOfFiles, this);

    // rewrite file with best game into new with name "best_game.out"
    ArrayList<SaveThisMoment> tmp = FileStream.readFromFile(listOfFiles.get(0));
    int tmpScore = FileStream.getReadedScore();
    FileStream.writeArrayInFile(tmp, new File(sortedPath + "best_game.out"), tmpScore);
    tmp.clear();

    // rewrite file with worst game into new with name "worst_game.out"
    tmp = FileStream.readFromFile(listOfFiles.get(listOfFiles.size() - 1));
    tmpScore = FileStream.getReadedScore();
    FileStream.writeArrayInFile(tmp, new File(sortedPath + "worst_game.out"), tmpScore);
    tmp.clear();

    int i = 0;
    // rewrite files into new directory(in sorted order)
    for (File s : listOfFiles) {
      tmp = FileStream.readFromFile(s);
      tmpScore = FileStream.getReadedScore();
      FileStream.writeArrayInFile(tmp, new File(sortedPath + (i++) + "_" + defaultFileName),
          tmpScore);
      tmp.clear();
    }
  }
}
