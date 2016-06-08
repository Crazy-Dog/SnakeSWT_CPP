package snake

import java.io.File
import java.io.FileInputStream
import java.io.FilenameFilter
import java.io.ObjectInputStream

import scala.Array.canBuildFrom
import scala.collection.JavaConverters.asScalaBufferConverter

class Statistics {
  var path = "replays/";
  var filenames = Array[String]();
  var folder = (new File(path));
  // get all filenames with .out extension
  filenames = folder.list(new FilenameFilter() {
    def accept(folder: File, name: String): Boolean = {
      return name.endsWith(".out");
    }
  });
  val listOfFiles = (for (filename <- filenames) yield (new File(path + filename))).toList;
  val mostCommonLevelArray = Array[Int](0, 0, 0, 0, 0);
  var replay = Array[SaveThisMoment]();
  /**
   * @return high score
   */
  def getHighScore(): Int = {
    getMax(for (file <- listOfFiles) yield (FileStream.getReadedScore(file)))
  }
  /**
   * @return max turns count
   */
  def getMaxStepsCount(): Int = {
    getMax(for (file <- listOfFiles) yield (stepsCount(file)))
  }
  /**
   * @return average turns count
   */
  def getAverageStepsCount(): Int = {
    ((for (file <- listOfFiles) yield (stepsCount(file))).sum) / listOfFiles.length
  }
  /**
   * @return steps count readed from file
   */
  def stepsCount(file: File): Int = {
    val objectInputStream = (new ObjectInputStream(new FileInputStream(file)));
    val space = objectInputStream.readInt(); //unused int, that contains score
    val tmpStepsCount = objectInputStream.readInt();
    tmpStepsCount
  }
  def getMax(values: List[Int]): Int = {
    values.reduce(_ max _)
  }

  /**
   * @return most common level of play
   */
  def getMostCommonLevelOfPlay(): Int = {
    replay = FileStream.readFromFile(listOfFiles.head).asScala.toArray;
    var tmp = mostCommonLevelArray(replay.head.getLevel() - 1) + 1;
    mostCommonLevelArray.update(replay.head.getLevel() - 1, tmp);

    return mostCommonLevelOfPlay(listOfFiles.drop(1));
  }
  /**
   * counting most common level of play
   */
  def mostCommonLevelOfPlay(some: List[File]): Int = {
    if (some == Nil) {
      var tmpVal = mostCommonLevelArray.sortWith(_ > _).head;

      return (mostCommonLevelArray.indexOf(tmpVal) + 1);
    }
    replay = FileStream.readFromFile(some.head).asScala.toArray;
    var tmp = mostCommonLevelArray(replay.head.getLevel() - 1) + 1;
    mostCommonLevelArray.update(replay.head.getLevel() - 1, tmp);

    mostCommonLevelOfPlay(some.drop(1));
  }
}