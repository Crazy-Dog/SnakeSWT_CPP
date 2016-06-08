package snake;
/**
 * Main menu class
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class StartMenu extends Composite {
  public static final int SORT_BY_SCORE = 1;
  public static final int SORT_BY_TURNS_COUNT = 0;
  private Button newGameBtn;
  private Button newGameBotBtn;
  private Button generateReplays;
  private Button replayBtn;
  private Button javaSortByTurnsCount;
  private Button javaSortByScore;
  private Button scalaSortByTurnsCount;
  private Button scalaSortByScore;
  private Button showStatisticsBtn;
  private Button notationSubstitutionBtn;
  private Button exitBtn;
  
  private Shell shell;

  public StartMenu(Shell shell) {
    super(shell, SWT.NULL);
    this.shell = shell;

    setLayout(new GridLayout(1, false)); // setting layout
    setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    setBackground(new Color(shell.getDisplay(), 127, 255, 212));

    initBtn();
  }

  public void initBtn() {
    newGameBtn = new Button(this, SWT.PUSH);
    newGameBtn.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    newGameBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    newGameBtn.setText("New Game");
    newGameBtn.addListener(SWT.Selection, event -> Snake.handleNewGameBtn(event, "Human"));
    newGameBtn.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    newGameBtn.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    newGameBotBtn = new Button(this, SWT.PUSH);
    newGameBotBtn.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    newGameBotBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    newGameBotBtn.setText("Computer plays");
    newGameBotBtn.addListener(SWT.Selection, event -> Snake.handleNewGameBtn(event, "Computer"));
    newGameBotBtn.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    newGameBotBtn.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    generateReplays = new Button(this, SWT.PUSH);
    generateReplays.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    generateReplays.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    generateReplays.setText("Replays generator");
    generateReplays.addListener(SWT.Selection, event -> {
      for (int i = 0; i < 500; i++) {
        int randLevel = (int) (Math.random() * 5 + 1);
        GameLogicThread tmp = new GameLogicThread(this.getShell(), null, randLevel);
        tmp.generateReplay();
      }
    });
    generateReplays.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    generateReplays.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    replayBtn = new Button(this, SWT.PUSH);
    replayBtn.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    replayBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    replayBtn.setText("Replay");
    replayBtn.addListener(SWT.Selection, event -> Snake.handleNewGameBtn(event, "Replay"));
    replayBtn.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    replayBtn.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    javaSortByTurnsCount = new Button(this, SWT.PUSH);
    javaSortByTurnsCount.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    javaSortByTurnsCount.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    javaSortByTurnsCount.setText("JavaSort(ByTurnsCount)");
    javaSortByTurnsCount.addListener(SWT.Selection, event -> {
      System.out.println("Start sorting");
      long time = System.currentTimeMillis();
      JavaSorter test = new JavaSorter("ByTurnsCount");
      try {
        test.sort();
      } catch (Exception e) {
        e.printStackTrace();
      }
      time = System.currentTimeMillis() - time;
      System.out.println("sorting time(in millis): " + time);
    });
    javaSortByTurnsCount.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    javaSortByTurnsCount.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    javaSortByScore = new Button(this, SWT.PUSH);
    javaSortByScore.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    javaSortByScore.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    javaSortByScore.setText("JavaSort(ByScore)");
    javaSortByScore.addListener(SWT.Selection, event -> {
      System.out.println("Start sorting");
      long time = System.currentTimeMillis();
      JavaSorter test = new JavaSorter("ByScore");
      try {
        test.sort();
      } catch (Exception e) {
        e.printStackTrace();
      }
      time = System.currentTimeMillis() - time;
      System.out.println("sorting time(in millis): " + time);
    });
    javaSortByScore.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    javaSortByScore.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    scalaSortByTurnsCount = new Button(this, SWT.PUSH);
    scalaSortByTurnsCount.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    scalaSortByTurnsCount.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    scalaSortByTurnsCount.setText("ScalaSort(By Turns Count");
    scalaSortByTurnsCount.addListener(SWT.Selection, event -> {
      System.out.println("Start sorting");
      long time = System.currentTimeMillis();
      ScalaSorter test = new ScalaSorter();
      try {
        test.sort(SORT_BY_TURNS_COUNT);
      } catch (Exception e) {
        e.printStackTrace();
      }
      time = System.currentTimeMillis() - time;
      System.out.println("sorting time(in millis): " + time);
    });
    scalaSortByTurnsCount.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    scalaSortByTurnsCount.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    scalaSortByScore = new Button(this, SWT.PUSH);
    scalaSortByScore.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    scalaSortByScore.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    scalaSortByScore.setText("ScalaSort(By Score)");
    scalaSortByScore.addListener(SWT.Selection, event -> {
      System.out.println("Start sorting");
      long time = System.currentTimeMillis();
      ScalaSorter test = new ScalaSorter();
      try {
        test.sort(SORT_BY_SCORE);
      } catch (Exception e) {
        e.printStackTrace();
      }
      time = System.currentTimeMillis() - time;
      System.out.println("sorting time(in millis): " + time);
    });
    scalaSortByScore.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    scalaSortByScore.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    showStatisticsBtn = new Button(this, SWT.PUSH);
    showStatisticsBtn.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    showStatisticsBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    showStatisticsBtn.setText("Statictics");
    showStatisticsBtn.addListener(SWT.Selection, event -> Snake.handleShowStatisticsBtn(event));
    showStatisticsBtn.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    showStatisticsBtn.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));
    
    notationSubstitutionBtn = new Button(this, SWT.PUSH);
    notationSubstitutionBtn.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    notationSubstitutionBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    notationSubstitutionBtn.setText("Notation Substitution");
    notationSubstitutionBtn.addListener(SWT.Selection, event -> {
      FileDialog dialog = new FileDialog(shell);
      dialog.setFilterExtensions(new String[] {"*.out"});
      String name = dialog.open();
      if (name != null) {
        NotationSubstitution notation = new NotationSubstitution();
        notation.generateCode(name);
      }
    });
    notationSubstitutionBtn.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    notationSubstitutionBtn.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    exitBtn = new Button(this, SWT.PUSH);
    exitBtn.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    exitBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    exitBtn.setText("Exit");
    exitBtn.addListener(SWT.Selection, event -> Snake.exitApp());
    exitBtn.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    exitBtn.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));
  }
}
