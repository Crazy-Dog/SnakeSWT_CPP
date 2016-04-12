package snake;
/**
 * help class, just a sub menu at now
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Helper extends Composite {
  private static int level;
  private static Button lvl1Btn;
  private static Button lvl2Btn;
  private static Button lvl3Btn;
  private static Button lvl4Btn;
  private static Button lvl5Btn;

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
    lvl1Btn = new Button(group1, SWT.RADIO);
    lvl1Btn.setText("level 1");

    lvl2Btn = new Button(group1, SWT.RADIO);
    lvl2Btn.setText("level 2");

    lvl3Btn = new Button(group1, SWT.RADIO);
    lvl3Btn.setText("level 3");

    lvl4Btn = new Button(group1, SWT.RADIO);
    lvl4Btn.setText("level 4");

    lvl5Btn = new Button(group1, SWT.RADIO);
    lvl5Btn.setText("level 5");

    Button accept = new Button(group1, SWT.PUSH);
    accept.setText("Accept");
    accept.addListener(SWT.Selection, event -> handleRadioBtns(event));
  }

  public static void handleRadioBtns(Event event) {
    if (lvl1Btn.getSelection())
      level = 1;
    if (lvl2Btn.getSelection())
      level = 2;
    if (lvl3Btn.getSelection())
      level = 3;
    if (lvl4Btn.getSelection())
      level = 4;
    if (lvl5Btn.getSelection())
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
