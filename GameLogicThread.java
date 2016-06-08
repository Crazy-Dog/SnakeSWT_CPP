package snake;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

public class GameLogicThread implements Runnable {
  private final int DOT_SIZE = 10; // size of snake part
  private final int ALL_DOTS = 9000;
  private int score;
  private int delay = 200;
  private int level;
  private int firstPos = 29; // first random positions
  private String direction;

  private int dots;
  private int cherryX;
  private int cherryY;

  private int snakeX[] = new int[ALL_DOTS];
  private int snakeY[] = new int[ALL_DOTS];
  private ArrayList<SaveThisMoment> listOfMoments = new ArrayList<SaveThisMoment>();
  private int nextStateSnakeX;
  private int nextStateSnakeY;
  private int counter = 0;

  private Point size;
  private Shell shell;
  private Game game;
  private Thread thread;

  private boolean inGame = true;

  GameLogicThread(Shell shell, Game game, int level) {
    thread = new Thread();
    thread.start(); // necessary when implements Runnable

    this.shell = shell;
    this.game = game;
    this.level = level;

    // first cherry location
    int r = (int) (Math.random() * firstPos);
    cherryX = ((r * DOT_SIZE));
    r = (int) (Math.random() * firstPos);
    cherryY = ((r * DOT_SIZE));
    dots = 3;
    direction = "right";

    for (int snakePart = 0; snakePart < dots; snakePart++) {
      snakeX[snakePart] = 50 - snakePart * 10;
      snakeY[snakePart] = 50;
    }
    checkLevel();
  }

  // When an object implementing interface Runnable is used to create a thread, starting the thread
  // causes the object's run method to be called in that separately executing thread.
  public void run() {
    if (inGame) {
      switch (Snake.mode) {
        case "Human":
          checkCherry();
          direction = game.getDirection();
          move();
          listOfMoments.add(new SaveThisMoment(snakeX[0], snakeY[0], cherryX, cherryY, level));
          break;
        case "Computer":
          checkCherry();
          computerAction();
          move();
          listOfMoments.add(new SaveThisMoment(snakeX[0], snakeY[0], cherryX, cherryY, level));
          break;
        case "Replay":
          replayAction();
          move();
          break;
      }
    }
    if (!shell.isDisposed()) {
      shell.getDisplay().timerExec(delay, this); // after delay do runnable
      game.redraw(); // redraw composite(game window)
    }
  }

  @SuppressWarnings("static-access")
  public void generateReplay() {
    while (inGame) {
      checkCherry();
      computerAction();
      move();
      listOfMoments.add(new SaveThisMoment(snakeX[0], snakeY[0], cherryX, cherryY, level));
    }
    if (!inGame) {
      new FileStream1().writeArrayInFile(listOfMoments,
          new File("replays/" + System.currentTimeMillis() + "_replay.out"), score);
    }
  }

  private void move() {
    try {
      if (!shell.isDisposed())
        size = new Point(500, 400);
      // if the snake eats itself
      for (int snakePart = dots; snakePart > 0; snakePart--) {
        if ((snakePart > 3) && (snakeX[0] == snakeX[snakePart])
            && (snakeY[0] == snakeY[snakePart])) {
          throw new IOException();
        }
      }
      // check scope
      if (level > 5 || (snakeY[0] > size.y - DOT_SIZE) || snakeY[0] < 0
          || (snakeX[0] > size.x - DOT_SIZE) || snakeX[0] < 0)
        throw new IOException();

      for (int snakePart = dots; snakePart > 0; snakePart--) {
        snakeX[snakePart] = snakeX[(snakePart - 1)];
        snakeY[snakePart] = snakeY[(snakePart - 1)];
      }

      switch (direction) {
        case "left":
          snakeX[0] -= DOT_SIZE;
          break;
        case "right":
          snakeX[0] += DOT_SIZE;
          break;
        case "up":
          snakeY[0] -= DOT_SIZE;
          break;
        case "down":
          snakeY[0] += DOT_SIZE;
          break;
      }
    } catch (IOException ex) {
      inGame = false; // game ends
    }
  }

  protected void computerAction() {
    // logic of computer play
    if (snakeX[0] > cherryX && (direction != "right")) {
      direction = "left";
    }

    else if (snakeX[0] < cherryX && (direction != "left")) {
      direction = "right";
    }

    else if (snakeY[0] > cherryY && (direction != "down")) {
      direction = "up";
    }

    else if (snakeY[0] < cherryY && (direction != "up")) {
      direction = "down";
    }
  }

  protected void replayAction() {
    try {
      if (Snake.replayMode.getSizeOfList() == counter) {
        throw new IOException();
      }
      nextStateSnakeX = Snake.replayMode.getStep(counter).getSnakeX();
      nextStateSnakeY = Snake.replayMode.getStep(counter).getSnakeY();
      level = Snake.replayMode.getStep(counter).getLevel();

      if (snakeX[0] > nextStateSnakeX && (direction != "right")) {
        direction = "left";
      }

      if (snakeX[0] < nextStateSnakeX && (direction != "left")) {
        direction = "right";
      }

      if (snakeY[0] > nextStateSnakeY && (direction != "down")) {
        direction = "up";
      }

      if (snakeY[0] < nextStateSnakeY && (direction != "up")) {
        direction = "down";
      }

      if ((snakeX[0] == cherryX) && (snakeY[0] == cherryY)) {
        dots++;
        if (dots != 3 && (dots - 3) % 100 == 0) {
          level++;
          checkLevel();
        }
      }
      cherryX = Snake.replayMode.getStep(counter).getCherryX();
      cherryY = Snake.replayMode.getStep(counter).getCherryY();
      counter++;
    } catch (IOException e) {
      inGame = false;
    }

  }

  private void checkCherry() {
    if ((snakeX[0] == cherryX) && (snakeY[0] == cherryY)) {
      dots++;
      locateCherry();
      score += 1 + (dots - 3) / 5;
      // go to next level
      if (dots != 3 && (dots - 3) % 100 == 0) {
        level++;
        checkLevel();
      }
    }
  }

  public void locateCherry() {
    int randPosision = (int) (Math.random() * 400 / 10 - 1);
    cherryX = ((randPosision * DOT_SIZE));
    randPosision = (int) (Math.random() * 400 / 10 - 1);
    cherryY = ((randPosision * DOT_SIZE));
  }

  private void checkLevel() {
    // assignment delay depending on level
    switch (level) {
      case 2:
        delay = 160;
        break;
      case 3:
        delay = 120;
        break;
      case 4:
        delay = 80;
        break;
      case 5:
        delay = 40;
        break;
    }
  }

  /**
   * @return game status
   */
  public boolean getGameStatus() {
    return inGame;
  }

  /**
   * @return coordinates x of snake
   */
  public int[] getSnakeX() {
    return snakeX;
  }

  /**
   * @return coordinates y of snake
   */
  public int[] getSnakeY() {
    return snakeY;
  }

  /**
   * @return count of dots(snake parts)
   */
  public int getDotsCount() {
    return dots;
  }

  /**
   * @return coordinate x of cherry
   */
  public int getCherryX() {
    return cherryX;
  }

  /**
   * @return coordinate x of cherry
   */
  public int getCherryY() {
    return cherryY;
  }

  public int getDelay() {
    return delay;
  }

  public int getScore() {
    return (level / 5 + 1 + score);
  }

  public ArrayList<SaveThisMoment> getListOfMoments() {
    return listOfMoments;
  }
}
