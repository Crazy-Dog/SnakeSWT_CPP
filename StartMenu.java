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
  private Button newGameBtn;
  private Button continueBtn;
  private Button newGameBotBtn;
  private Button highScoreBtn;
  private Button exitBtn;

  public StartMenu(Shell shell) {
    super(shell, SWT.NULL);

    setLayout(new GridLayout(1, false)); // setting layout
    setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    setBackground(new Color(shell.getDisplay(), 127, 255, 212));

    initBtn();
  }

  public void initBtn() {
    newGameBtn = new Button(this, SWT.PUSH);
    newGameBtn.setBackground(new Color(this.getDisplay(), 16, 16, 16)); // it don't working, I think
    newGameBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    newGameBtn.setText("New Game");
    newGameBtn.addListener(SWT.Selection, event -> Snake.handleNewGameBtn(event, "Human"));
    newGameBtn.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    newGameBtn.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    continueBtn = new Button(this, SWT.PUSH);
    continueBtn.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    continueBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    continueBtn.setText("Continue");
    continueBtn.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    continueBtn.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    newGameBotBtn = new Button(this, SWT.PUSH);
    newGameBotBtn.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    newGameBotBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    newGameBotBtn.setText("Computer plays");
    newGameBotBtn.addListener(SWT.Selection, event -> Snake.handleNewGameBtn(event, "Computer"));
    newGameBotBtn.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    newGameBotBtn.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    highScoreBtn = new Button(this, SWT.PUSH);
    highScoreBtn.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    highScoreBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    highScoreBtn.setText("High Score");
    highScoreBtn.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    highScoreBtn.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));

    exitBtn = new Button(this, SWT.PUSH);
    exitBtn.setBackground(new Color(this.getDisplay(), 16, 16, 16));
    exitBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    exitBtn.setText("Exit");
    exitBtn.addListener(SWT.Selection, event -> Snake.exitApp());
    exitBtn.addListener(SWT.MouseEnter, event -> Snake.handleMouseEnter(event));
    exitBtn.addListener(SWT.MouseExit, event -> Snake.handleMouseExit(event));
  }


}
