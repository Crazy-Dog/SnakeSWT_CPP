package snake;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Game extends Composite {
  private final int DOT_SIZE = 10; // size of snake part
  private final int ALL_DOTS = 9000;
  private int delay = 200;
  private int level = 1;
  private int firstPos = 29; // first random positions

  private int snakeX[] = new int[ALL_DOTS];
  private int snakeY[] = new int[ALL_DOTS];
  private int nextStateSnakeX;
  private int nextStateSnakeY;
  private int counter = 1;

  private int dots;
  private int cherryX;
  private int cherryY;
  private Point size;

  private boolean left = false;
  private boolean right = true;
  private boolean up = false;
  private boolean down = false;
  private boolean inGame = true;

  private Image ball; // part of snake
  private Image cherry;
  private Image head; // snake head

  private static Shell shell;
  private static Runnable runnable;
  private static ArrayList<SaveThisMoment> listOfMoments = new ArrayList<SaveThisMoment>();

  public Game(Shell shell, int level) {
    super(shell, SWT.NULL);

    Game.shell = shell;
    if (Game.shell.isDisposed())
      Game.shell.addListener(SWT.Close, event -> {
        stopTimer();
        Snake.exitApp();
      });
    this.level = level;

    checkLevel();
    initGame(); // game initialization
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

  private void initGame() {
    // add painting and key handlers
    addListener(SWT.Paint, event -> doPainting(event));
    addListener(SWT.KeyDown, event -> onKeyDown(event));

    // handler for closing window
    addListener(SWT.Dispose, event -> {

      ball.dispose();
      cherry.dispose();
      head.dispose();
    });

    Color color = new Color(shell.getDisplay(), 0, 0, 0);
    setBackground(color); // setting background of game field
    color.dispose();

    loadImages();

    // first cherry location
    int r = (int) (Math.random() * firstPos);
    cherryX = ((r * DOT_SIZE));
    r = (int) (Math.random() * firstPos);
    cherryY = ((r * DOT_SIZE));
    // same with initComputerGame
    dots = 3;

    for (int z = 0; z < dots; z++) {
      snakeX[z] = 50 - z * 10;
      snakeY[z] = 50;
    }


    switch (Snake.mode) {
      case "Human":
        initHumanGame();
        break;
      case "Computer":
        initComputerGame();
        break;
      case "Replay":
        initReplay();
        break;
    }
  }

  private void loadImages() {
    ImageData snakePartImg = new ImageData("images/dot.png");
    ball = new Image(shell.getDisplay(), snakePartImg);

    ImageData cherryImg = new ImageData("images/cherry.png");
    cherry = new Image(shell.getDisplay(), cherryImg);

    ImageData snakeHeadImg = new ImageData("images/head.png");
    head = new Image(shell.getDisplay(), snakeHeadImg);
  }

  private void initReplay() {
    cherryX = Snake.replayMode.getStep(counter).getCherryX();
    cherryY = Snake.replayMode.getStep(counter).getCherryY();

    runnable = new Runnable() {
      @Override
      public void run() {
        if (inGame) {

          move();
          replayAction();
        }
        if (!shell.isDisposed()) {
          shell.getDisplay().timerExec(delay, this);
          redraw();
        }
      }
    };
    shell.getDisplay().timerExec(delay, runnable);
  }

  private void initHumanGame() {
    runnable = new Runnable() {
      @Override
      public void run() {
        if (inGame) {
          checkCherry();
          move();
          listOfMoments.add(new SaveThisMoment(snakeX[0], snakeY[0], cherryX, cherryY, level));
        }
        if (!shell.isDisposed()) {
          shell.getDisplay().timerExec(delay, this);
          redraw();
        }
      }
    };
    shell.getDisplay().timerExec(delay, runnable);
  }

  private void initComputerGame() {
    // run the game
    runnable = new Runnable() {
      @Override
      public void run() {
        if (inGame) {
          checkCherry();
          computerAction();
          move();
          listOfMoments.add(new SaveThisMoment(snakeX[0], snakeY[0], cherryX, cherryY, level));
        }
        if (!shell.isDisposed()) {
          shell.getDisplay().timerExec(delay, this); // after delay do runnable
          redraw(); // redraw composite(game window)
        }
      }
    };
    shell.getDisplay().timerExec(delay, runnable); // after delay do runnable
  }

  /**
   * replay mode displaying
   */
  protected void replayAction() {
    try {
      if (Snake.replayMode.getSizeOfList() == counter) {
        throw new IOException();
      }
      nextStateSnakeX = Snake.replayMode.getStep(counter).getSnakeX();
      nextStateSnakeY = Snake.replayMode.getStep(counter).getSnakeY();
      level = Snake.replayMode.getStep(counter).getLevel();

      if (snakeX[0] > nextStateSnakeX && (!right)) {
        left = true;
        up = false;
        down = false;
      }

      if (snakeX[0] < nextStateSnakeX && (!left)) {
        right = true;
        up = false;
        down = false;
      }

      if (snakeY[0] > nextStateSnakeY && (!down)) {
        up = true;
        right = false;
        left = false;
      }

      if (snakeY[0] < nextStateSnakeY && (!up)) {
        down = true;
        right = false;
        left = false;
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

  protected void computerAction() {
    // logic of computer play
    if (snakeX[0] > cherryX && (!right)) {
      left = true;
      up = false;
      down = false;
    }

    else if (snakeX[0] < cherryX && (!left)) {
      right = true;
      up = false;
      down = false;
    }

    else if (snakeY[0] > cherryY && (!down)) {
      up = true;
      right = false;
      left = false;
    }

    else if (snakeY[0] < cherryY && (!up)) {
      down = true;
      right = false;
      left = false;
    }
  }

  // handler of redraw action
  private void doPainting(Event event) {
    // Graphics class
    GC graphicClass = event.gc;

    Color color = new Color(shell.getDisplay(), 0, 0, 0);
    graphicClass.setBackground(color); // set background of game window
    color.dispose();

    // sets the receiver's anti-aliasing value to the parameter
    graphicClass.setAntialias(SWT.ON);

    if (inGame) {
      drawObjects(event);
    } else {
      if (Snake.mode == "Replay") {
        gameResult(event, "Replay ends");
      } else {
        if (level > 5)
          gameResult(event, "You Are Win :)");
        else
          gameResult(event, "Game Over");
      }
    }
  }

  private void drawObjects(Event event) {

    GC graphicClass = event.gc;

    graphicClass.drawImage(cherry, cherryX, cherryY); // draw cherry

    // draw snake
    for (int snakePart = 0; snakePart < dots; snakePart++) {
      if (snakePart == 0) {
        graphicClass.drawImage(head, snakeX[snakePart], snakeY[snakePart]);
      } else {
        graphicClass.drawImage(ball, snakeX[snakePart], snakeY[snakePart]);
      }
    }
  }

  private void gameResult(Event event, String result) {
    GC graphicClass = event.gc;

    Font font = new Font(event.display, "Helvetica", 12, SWT.NORMAL);
    Color whiteColor = new Color(event.display, 255, 255, 255);

    graphicClass.setForeground(whiteColor);
    graphicClass.setFont(font);

    Point size = graphicClass.textExtent(result);

    graphicClass.drawText(result, (getSize().x - size.x) / 2, (getSize().y - size.y) / 2);

    font.dispose();
    whiteColor.dispose();

    stopTimer();
  }

  private void checkCherry() {
    if ((snakeX[0] == cherryX) && (snakeY[0] == cherryY)) {
      dots++;
      locateCherry();
      // go to next level
      if (dots != 3 && (dots - 3) % 100 == 0) {
        level++;
        checkLevel();
      }
    }
  }

  // snake moving
  private void move() {
    try {
      if (!shell.isDisposed())
        size = getSize();
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

      if (left) {
        snakeX[0] -= DOT_SIZE;
      }

      if (right) {
        snakeX[0] += DOT_SIZE;
      }

      if (up) {
        snakeY[0] -= DOT_SIZE;
      }

      if (down) {
        snakeY[0] += DOT_SIZE;
      }
    } catch (IOException ex) {
      inGame = false; // game ends
    }
  }

  public void locateCherry() {
    int randPosision = (int) (Math.random() * getSize().x / 10 - 1);
    cherryX = ((randPosision * DOT_SIZE));
    randPosision = (int) (Math.random() * getSize().y / 10 - 1);
    cherryY = ((randPosision * DOT_SIZE));
  }

  // handler of key events
  private void onKeyDown(Event event) {
    int key = event.keyCode;

    if ((key == SWT.ARROW_LEFT) && (!right)) {
      left = true;
      up = false;
      down = false;
    }

    if ((key == SWT.ARROW_RIGHT) && (!left)) {
      right = true;
      up = false;
      down = false;
    }

    if ((key == SWT.ARROW_UP) && (!down)) {
      up = true;
      right = false;
      left = false;
    }

    if ((key == SWT.ARROW_DOWN) && (!up)) {
      down = true;
      right = false;
      left = false;
    }

    if (key == SWT.ESC) {
      shell.getDisplay().timerExec(-1, runnable); // stop timer
      Snake.returnToMainMenu();
    }
  }

  @SuppressWarnings("static-access")
  public static void stopTimer() {
    shell.getDisplay().timerExec(-1, runnable); // stop timer
    if (Snake.mode != "Replay")
      new FileStream().writeArrayInFile(listOfMoments,
          new File("replays/" + System.currentTimeMillis() + "_replay.out"));
  }
}

