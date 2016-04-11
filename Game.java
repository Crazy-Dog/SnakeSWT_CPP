package snake;
/**
 * class of game logic
 * 
 * @author Crazy Dog :)
 */

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Game extends Composite {

  private final int DOT_SIZE = 10; // size of snake part
  private final int ALL_DOTS = 9000;
  private int delay = 200;
  private int level = 1;
  private int first_pos = 29; // first random positions

  private int snake_x[] = new int[ALL_DOTS];
  private int snake_y[] = new int[ALL_DOTS];

  private int dots;
  private int cherry_x;
  private int cherry_y;

  private boolean left = false;
  private boolean right = true;
  private boolean up = false;
  private boolean down = false;
  private boolean inGame = true;

  private Image ball; // part of snake
  private Image cherry;
  private Image head; // snake head

  private Shell shell;
  private Runnable runnable;

  public Game(Shell shell, int level) {
    super(shell, SWT.NULL);

    this.shell = shell;
    this.level = level;

    // first cherry location
    int r = (int) (Math.random() * first_pos);
    cherry_x = ((r * DOT_SIZE));
    r = (int) (Math.random() * first_pos);
    cherry_y = ((r * DOT_SIZE));

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

    Color col = new Color(shell.getDisplay(), 0, 0, 0);

    setBackground(col); // setting background of game field
    col.dispose();

    loadImages();

    switch (Snake.mode) {
      case "Human":
        initHumanGame();
        break;
      case "Computer":
        initComputerGame();
    }
  }

  private void loadImages() {

    ImageData iib = new ImageData("images/dot.png");
    ball = new Image(shell.getDisplay(), iib);

    ImageData iia = new ImageData("images/cherry.png");
    cherry = new Image(shell.getDisplay(), iia);

    ImageData iih = new ImageData("images/head.png");
    head = new Image(shell.getDisplay(), iih);
  }

  private void initComputerGame() {

    dots = 3;

    // first snake initialization (coordinates of respawn)
    for (int snake_part = 0; snake_part < dots; snake_part++) {
      snake_x[snake_part] = 50 - snake_part * 10;
      snake_y[snake_part] = 50;
    }

    // run the game
    runnable = new Runnable() {
      @Override
      public void run() {

        if (inGame) {
          checkCherry();
          computerAction();
          move();
        }
        shell.getDisplay().timerExec(delay, this); // after delay do runnable
        redraw(); // redraw composite(game window)
      }
    };
    shell.getDisplay().timerExec(delay, runnable); // after delay do runnable
  }

  protected void computerAction() {
    // logic of computer play
    if (snake_x[0] > cherry_x && (!right)) {
      left = true;
      up = false;
      down = false;
    }

    else if (snake_x[0] < cherry_x && (!left)) {
      right = true;
      up = false;
      down = false;
    }

    else if (snake_y[0] > cherry_y && (!down)) {
      up = true;
      right = false;
      left = false;
    }

    else if (snake_y[0] < cherry_y && (!up)) {
      down = true;
      right = false;
      left = false;
    }
  }

  private void initHumanGame() {
    // same with initComputerGame
    dots = 3;

    for (int z = 0; z < dots; z++) {
      snake_x[z] = 50 - z * 10;
      snake_y[z] = 50;
    }

    runnable = new Runnable() {
      @Override
      public void run() {

        if (inGame) {
          checkCherry();
          move();
        }

        shell.getDisplay().timerExec(delay, this);
        redraw();
      }
    };

    shell.getDisplay().timerExec(delay, runnable);
  };

  // handler of redraw action
  private void doPainting(Event e) {

    // Graphics class
    GC gc = e.gc;

    Color col = new Color(shell.getDisplay(), 0, 0, 0);
    gc.setBackground(col); // set background of game window
    col.dispose();

    // sets the receiver's anti-aliasing value to the parameter
    gc.setAntialias(SWT.ON);


    if (inGame) {
      drawObjects(e);
    } else {
      if (level > 5)
        gameResult(e, "You Are Win :)");
      else
        gameResult(e, "Game Over");
    }
  }

  private void drawObjects(Event e) {

    GC gc = e.gc;

    gc.drawImage(cherry, cherry_x, cherry_y); // draw cherry

    // draw snake
    for (int snake_part = 0; snake_part < dots; snake_part++) {
      if (snake_part == 0) {
        gc.drawImage(head, snake_x[snake_part], snake_y[snake_part]);
      } else {
        gc.drawImage(ball, snake_x[snake_part], snake_y[snake_part]);
      }
    }
  }

  private void gameResult(Event e, String result) {

    GC gc = e.gc;

    Font font = new Font(e.display, "Helvetica", 12, SWT.NORMAL);
    Color whiteCol = new Color(e.display, 255, 255, 255);

    gc.setForeground(whiteCol);
    gc.setFont(font);

    Point size = gc.textExtent(result);

    gc.drawText(result, (getSize().x - size.x) / 2, (getSize().y - size.y) / 2);

    font.dispose();
    whiteCol.dispose();

    shell.getDisplay().timerExec(-1, runnable);
  }

  private void checkCherry() {

    if ((snake_x[0] == cherry_x) && (snake_y[0] == cherry_y)) {
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
      // if the snake eats itself
      for (int snake_part = dots; snake_part > 0; snake_part--) {
        if ((snake_part > 3) && (snake_x[0] == snake_x[snake_part])
            && (snake_y[0] == snake_y[snake_part])) {
          throw new IOException();
        }
      }
      // check scope
      if (level > 5 || (snake_y[0] > getSize().y - DOT_SIZE) || snake_y[0] < 0
          || (snake_x[0] > getSize().x - DOT_SIZE) || snake_x[0] < 0)
        throw new IOException();


      for (int z = dots; z > 0; z--) {
        snake_x[z] = snake_x[(z - 1)];
        snake_y[z] = snake_y[(z - 1)];
      }

      if (left) {
        snake_x[0] -= DOT_SIZE;
      }

      if (right) {
        snake_x[0] += DOT_SIZE;
      }

      if (up) {
        snake_y[0] -= DOT_SIZE;
      }

      if (down) {
        snake_y[0] += DOT_SIZE;
      }
    } catch (IOException ex) {
      inGame = false; // game ends
    }
  }

  public void locateCherry() {

    int r = (int) (Math.random() * getSize().x / 10 - 1);
    cherry_x = ((r * DOT_SIZE));
    r = (int) (Math.random() * getSize().y / 10 - 1);
    cherry_y = ((r * DOT_SIZE));
  }

  // handler of key events
  private void onKeyDown(Event e) {
    int key = e.keyCode;

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
}

