package snake

import scala.io.Source;
import scala.collection.mutable.Buffer
import scala.collection.Map
import scala.collection.immutable.ListMap
import scala.collection.mutable.MutableList
import scala.collection.mutable.LinkedHashMap
import scala.collection.JavaConverters._
import scala.Array._
import java.io._
import java.nio.file.StandardOpenOption;
import java.nio.file.{ Files, Paths }
import scala.collection.mutable.ArrayBuffer;

class Statistics {
  var path = "replays/";
  var listOfFiles = ArrayBuffer[File]();
  var filenames = Array[String]();
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

  var highScoreVal: Int = 0;
  var stepsCount: Int = 0;
  var mostCommonLevelArray = Array[Int](0, 0, 0, 0, 0);
  var replay = Array[SaveThisMoment]();
  /**
   * @return high score
   */
  def getHighScore(): Int = {
    highScoreVal = 0;
    return highScore(listOfFiles);
  }
  /**
   * counting high score
   */
  def highScore(some: ArrayBuffer[File]): Int = {
    if (some == Nil)
      return highScoreVal;
    FileStream.readFromFile(some.head);
    var score = FileStream.getReadedScore();
    if (score > highScoreVal)
      highScoreVal = score;
    highScore(some.drop(1));
  }
  /**
   * @return max turns count
   */
  def getMaxStepsCount(): Int = {
    stepsCount = 0;
    return maxStepsCount(listOfFiles);
  }
  /**
   * counting max turns count
   */
  def maxStepsCount(some: ArrayBuffer[File]): Int = {
    if (some == Nil)
      return stepsCount;
    var fileInputStream = new FileInputStream(some.head);
    var objectInputStream = (new ObjectInputStream(fileInputStream));
    var space = objectInputStream.readInt(); //unused int, that contains score
    var tmpStepsCount = objectInputStream.readInt();
    if (tmpStepsCount > stepsCount)
      stepsCount = tmpStepsCount;
    maxStepsCount(some.drop(1));
  }
  /**
   * @return average turns count
   */
  def getAverageStepsCount(): Int = {
    stepsCount = 0;

    return averageStepsCount(listOfFiles);
  }
  /**
   * counting average turns count
   */
  def averageStepsCount(some: ArrayBuffer[File]): Int = {
    if (some == Nil)
      return (stepsCount / listOfFiles.size);
    var fileInputStream = new FileInputStream(some.head);
    var objectInputStream = (new ObjectInputStream(fileInputStream));
    var space = objectInputStream.readInt(); //unused int, that contains score
    stepsCount += objectInputStream.readInt();
    objectInputStream.close();
    fileInputStream.close();
    averageStepsCount(some.drop(1));
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
  def mostCommonLevelOfPlay(some: ArrayBuffer[File]): Int = {
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