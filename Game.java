package snake;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Game extends Composite {
  private int level;

  private String direction = "right";

  private Image ball; // part of snake
  private Image cherry;
  private Image head; // snake head

  private static Shell shell;
  private static GameLogicThread gameThread;

  @SuppressWarnings("static-access")
  public Game(Shell shell, int level) {
    super(shell, SWT.NULL);

    Game.shell = shell;
    if (Game.shell.isDisposed())
      Game.shell.addListener(SWT.Close, event -> {
        stopTimer();
        Snake.exitApp();
      });
    gameThread = new GameLogicThread(this.shell, this, level);

    initGame(); // game initialization
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


    shell.getDisplay().timerExec(gameThread.getDelay(), gameThread);
  }

  private void loadImages() {
    ImageData snakePartImg = new ImageData("images/dot.png");
    ball = new Image(shell.getDisplay(), snakePartImg);

    ImageData cherryImg = new ImageData("images/cherry.png");
    cherry = new Image(shell.getDisplay(), cherryImg);

    ImageData snakeHeadImg = new ImageData("images/head.png");
    head = new Image(shell.getDisplay(), snakeHeadImg);
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

    if (gameThread.getGameStatus()) {
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
    graphicClass.drawImage(cherry, gameThread.getCherryX(), gameThread.getCherryY()); // draw cherry

    // draw snake
    for (int snakePart = 0; snakePart < gameThread.getDotsCount(); snakePart++) {
      if (snakePart == 0) {
        graphicClass.drawImage(head, gameThread.getSnakeX()[snakePart],
            gameThread.getSnakeY()[snakePart]);
      } else {
        graphicClass.drawImage(ball, gameThread.getSnakeX()[snakePart],
            gameThread.getSnakeY()[snakePart]);
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

  // handler of key events
  private void onKeyDown(Event event) {
    int key = event.keyCode;

    if ((key == SWT.ARROW_LEFT) && (direction != "right")) {
      direction = "left";
    }

    if ((key == SWT.ARROW_RIGHT) && (direction != "left")) {
      direction = "right";
    }

    if ((key == SWT.ARROW_UP) && (direction != "down")) {
      direction = "up";
    }

    if ((key == SWT.ARROW_DOWN) && (direction != "up")) {
      direction = "down";
    }

    if (key == SWT.ESC) {
      shell.getDisplay().timerExec(-1, gameThread); // stop timer
      Snake.returnToMainMenu();
    }
  }

  @SuppressWarnings("static-access")
  public static void stopTimer() {
    shell.getDisplay().timerExec(-1, gameThread); // stop timer
    if (Snake.mode != "Replay")
      new FileStream().writeArrayInFile(gameThread.getListOfMoments(),
          new File("replays/" + System.currentTimeMillis() + "_replay.out"), gameThread.getScore());
  }

  public String getDirection() {
    return direction;
  }
}

