package snake;
/**
 * help class, just a sub menu at now
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Helper extends Composite {
  private static int level;
  private static Button level1Button;
  private static Button level2Button;
  private static Button level3Button;
  private static Button level4Button;
  private static Button level5Button;

  public Helper(Shell shell) {
    super(shell, SWT.NULL);
  }

  public void initLevelToStart() {
    setLayout(new GridLayout(1, false));
    setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    // Group of radio buttons
    Group group1 = new Group(this, SWT.SHADOW_IN);
    group1.setText("What level would you like to start from?");
    group1.setLayout(new GridLayout(1, false));
    group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    level1Button = new Button(group1, SWT.RADIO);
    level1Button.setText("level 1");

    level2Button = new Button(group1, SWT.RADIO);
    level2Button.setText("level 2");

    level3Button = new Button(group1, SWT.RADIO);
    level3Button.setText("level 3");

    level4Button = new Button(group1, SWT.RADIO);
    level4Button.setText("level 4");

    level5Button = new Button(group1, SWT.RADIO);
    level5Button.setText("level 5");

    Button acceptButton = new Button(group1, SWT.PUSH);
    acceptButton.setText("Accept");
    acceptButton.addListener(SWT.Selection, event -> handleRadioBtns(event));
  }

  public static void handleRadioBtns(Event event) {
    if (level1Button.getSelection())
      level = 1;
    if (level2Button.getSelection())
      level = 2;
    if (level3Button.getSelection())
      level = 3;
    if (level4Button.getSelection())
      level = 4;
    if (level5Button.getSelection())
      level = 5;

    if (level != 0) {
      switch (Snake.mode) { // mode sets in snake class
        case "Human":
          Snake.startNewGame(level);
          break;
        case "Computer":
          Snake.botPlays(level);
          break;
      }
    }
  }
}
