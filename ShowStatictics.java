package snake;

/**
 * everything is very simple in this class
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ShowStatictics extends Composite {
  private int highScore = 0;
  private int maxStepsCount = 0;
  private int averageStepsCount = 0;
  private int mostCommonLevelOfPlay = 0;

  public ShowStatictics(Shell shell) {
    super(shell, SWT.BORDER);
    addListener(SWT.KeyDown, event -> {
      if (event.keyCode == SWT.ESC) {
        Snake.returnToMainMenu();
      }
    });
    Color blackColor = new Color(shell.getDisplay(), 0, 0, 0);
    setBackground(blackColor);
    blackColor.dispose();
    Statistics statistics = new Statistics();
    highScore = statistics.getHighScore();
    maxStepsCount = statistics.getMaxStepsCount();
    averageStepsCount = statistics.getAverageStepsCount();
    mostCommonLevelOfPlay = statistics.getMostCommonLevelOfPlay();

    addPaintListener(new PaintListener() {
      @Override
      public void paintControl(PaintEvent event) {
        GC graphicClass = event.gc;

        Font font = new Font(event.display, "Helvetica", 16, SWT.ITALIC | SWT.BOLD);
        Color whiteColor = new Color(event.display, 255, 255, 255);

        graphicClass.setForeground(whiteColor);
        graphicClass.setFont(font);
        Point size = graphicClass.textExtent("Statistics");

        graphicClass.drawText("Statistics", (getSize().x - size.x) / 2, 10);

        Font font1 = new Font(event.display, "Helvetica", 12, SWT.ITALIC | SWT.BOLD);
        graphicClass.setFont(font1);

        graphicClass.drawLine(10, 75, getSize().x - 60, 75);
        graphicClass.drawLine(10, 125, getSize().x - 60, 125);
        graphicClass.drawLine(10, 175, getSize().x - 60, 175);
        graphicClass.drawLine(10, 225, getSize().x - 60, 225);

        graphicClass.drawText("High Score: ", 10, 60);
        graphicClass.drawText("Maximum Steps Count: ", 10, 110);
        graphicClass.drawText("Average Steps Count: ", 10, 160);
        graphicClass.drawText("The Most Common Level Of Play: ", 10, 210);

        graphicClass.drawText(" " + highScore, getSize().x - 60, 60);
        graphicClass.drawText(" " + maxStepsCount, getSize().x - 60, 110);
        graphicClass.drawText(" " + averageStepsCount, getSize().x - 60, 160);
        graphicClass.drawText(" " + mostCommonLevelOfPlay, getSize().x - 60, 210);

        graphicClass.setAntialias(SWT.ON);
        font.dispose();
        whiteColor.dispose();
      }
    });
  }
}
