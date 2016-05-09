package snake

import scala.io.Source;
import java.nio.file.StandardOpenOption;
import java.nio.file.{ Files, Paths }
import scala.collection.mutable.ArrayBuffer;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.File;

class ScalaSorter {
  var path = "replays/";
  var pathSorted = "";
  var defaultFileName = "";
  var listOfFiles = ArrayBuffer[File]();
  var sortedList = ArrayBuffer[File]();
  var filenames = Array[String]();
  def sort(sortMode: Int) = {
    var folder = (new File(path));
    // get all filenames with .out extension
    filenames = folder.list(new FilenameFilter() {
      def accept(folder: File, name: String): Boolean = {
        return name.endsWith(".out");
      }
    });
    //get all files with names from array "filenames"
    filenames.foreach(filename => {
      val file = (new File(path + filename));
      listOfFiles.append(file);
    });
    var i = 0;
    sortMode match {
      case 0 => { //sort by turns count
        pathSorted = "replays_sorted_by_steps_count_Scala/";
        defaultFileName = "replay_sorted_by_steps_count_Scala.out";
        sortedList = listOfFiles.sortWith(compareByTurnsCount(_, _));
      }
      case 1 => { //sort by score
        pathSorted = "replays_sorted_by_score_Scala/";
        defaultFileName = "replay_sorted_by_score.out";
        sortedList = listOfFiles.sortWith(compareByScore(_, _));
      }
    }
    // rewrite files into new directory(in sorted order)
    sortedList.foreach(file => {
      var tmp = FileStream.readFromFile(file);
      var tmpScore = FileStream.getReadedScore();
      FileStream.writeArrayInFile(tmp, new File(pathSorted + i + "_" +
        defaultFileName), tmpScore);
      i += 1;
    });

    // rewrite file with best game into new with name "best_game.out"
    var tmp = FileStream.readFromFile(sortedList.head);
    var tmpScore = FileStream.getReadedScore();
    FileStream.writeArrayInFile(tmp, new File(pathSorted + "best_game.out"), tmpScore);

    // rewrite file with worst game into new with name "worst_game.out"
    tmp = FileStream.readFromFile(sortedList.last);
    tmpScore = FileStream.getReadedScore();
    FileStream.writeArrayInFile(tmp, new File(pathSorted + "worst_game.out"), tmpScore);
  }

  def compareByScore(o1: File, o2: File): Boolean = {
    //read first file
    FileStream.readFromFile(o1);
    var score1 = FileStream.getReadedScore();

    //read second file
    FileStream.readFromFile(o2);
    var score2 = FileStream.getReadedScore();
    if (score1 > score2)
      return true;
    return false;
  }

  def compareByTurnsCount(o1: File, o2: File): Boolean = {
    var fileInputStream = new FileInputStream(o1);
    var objectInputStream = (new ObjectInputStream(fileInputStream));
    var space = objectInputStream.readInt(); //unused int, that contains score
    var steps1 = objectInputStream.readInt();

    fileInputStream = new FileInputStream(o2);
    objectInputStream = (new ObjectInputStream(fileInputStream));
    space = objectInputStream.readInt();
    var steps2 = objectInputStream.readInt();
    objectInputStream.close();
    fileInputStream.close();
    if (steps1 > steps2)
      return true;
    return false;
  }
}