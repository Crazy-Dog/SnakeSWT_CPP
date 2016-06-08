package snake

import java.io.{ File, PrintWriter }
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._

class NotationSubstitution {
  def generateCode(replayName: String): Unit = {
    val listOfSteps = FileStream.readFromFile(new File(replayName)).asScala
    val highScoreVal = FileStream.getReadedScore(new File(replayName))
    val stepsCount = "Common steps count: " + (FileStream.getReadedStepsCount()-1)
    val result = ArrayBuffer[(String, String, String)]()
    val helloMsg = "Hi, It is a modified notation"
    val ps = "P.S.Level of the game can be increased if it is collected 100 cherries(But it's impossible I think)"
    val highScore = "High score of this game: "
    val numberOfStep = "Step: "
    val snakeMove = "Snake moves to "
    val cherryCoordinates = "Current coordinates of cherry: "
    val currentLevel = "Current level: "
    val output = new PrintWriter(new File("output.txt"))

    for (moment <- listOfSteps) {
      moment match {
        case moment => result.+=((snakeMove + "(" + moment.getSnakeX.toString() + ", " + moment.getSnakeY.toString() + ")", (cherryCoordinates + "(" +
          moment.getCherryX.toString() + ", " + moment.getCherryY.toString() + ")"),
          (currentLevel + moment.getLevel.toString())))
        case _ => ("","","")
      }
    }
    result -= result.last

    output.println(helloMsg)
    output.println(ps)
    output.println(highScore + highScoreVal)
    output.println(stepsCount)
    result.foreach { x =>
      {
        output.println(numberOfStep + result.indexOf(x))
        output.println(x._1)
        output.println(x._2)
        output.println(x._3)
      }
    }
    output.flush()
    output.close()
  }
}