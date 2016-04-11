package snake;
/**
 * Main class of app
 */

import java.awt.Dimension;
import java.awt.Toolkit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snake {

  private final int WIDTH = 400;
  private final int HEIGHT = 400;

  private static Shell shell;
  
  private static StartMenu startMenu;  
  private static Helper helper; //Submenu(level section)
  private static Game game;
  
  public static String mode;

  public Snake(Display display) {

    initUI(display);
  }

  private void initUI(Display display) {
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); 
    shell = new Shell(display);

    shell.setLayout(new GridLayout(1, false)); // setting layout
    shell.setText("Snake"); 
    shell.setSize(WIDTH, HEIGHT); //size of app window
    shell.setLocation(dim.width / 2 - WIDTH / 2, dim.height / 2 - HEIGHT / 2);

    startMenu = new StartMenu(shell); //main menu of app


    shell.open();

    // This part needed for SWT app working
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }
  
  // handler for return to Start Menu
  public static void returnToMainMenu() {
    //if game is open(not application), close game window
    if ((game != null) && (!game.isDisposed())) {
      game.dispose();
    }
    //then open main menu
    startMenu = new StartMenu(shell);
    startMenu.forceFocus();
    shell.layout(true);
  }

  // handler for start game(level selecting)
  public static void handleNewGameBtn(Event event, String mode) {
    if ((startMenu != null) && (!startMenu.isDisposed())) {
      startMenu.dispose();
    }
    Snake.mode = mode; // set mode(Human or Computer)
    helper = new Helper(shell); // open window of level selecting
    helper.initLevelToStart(); //create radio buttons on that window
    helper.forceFocus();
    shell.layout(true);
  }

  // start game
  public static void startNewGame(int level) {
    if ((startMenu != null) && (!startMenu.isDisposed())) {
      startMenu.dispose();
    }
    if ((helper != null) && (!helper.isDisposed())) {
      helper.dispose();
    }
    game = new Game(shell, level);
    // set layout data for expansion on shell size
    game.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    game.forceFocus();
    shell.layout(true);
  }
  
  //start computer game
  public static void botPlays(int level) {
    if ((startMenu != null) && (!startMenu.isDisposed())) {
      startMenu.dispose();
    }
    if ((helper != null) && (!helper.isDisposed())) {
      helper.dispose();
    }
    game = new Game(shell, level);
    game.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    game.forceFocus();
    shell.layout(true);
  }
  
  //handlers for change start menu background
  public static void handleMouseEnter(Event event){
    startMenu.setBackground(new Color(shell.getDisplay(), 245, 255, 250));
  }
  public static void handleMouseExit(Event event){
    startMenu.setBackground(new Color(shell.getDisplay(), 127, 255, 212));
  }


  @SuppressWarnings("unused")
  public static void main(String[] args) {

    Display display = new Display();
    Snake snake = new Snake(display);
    display.dispose();
  }
}
