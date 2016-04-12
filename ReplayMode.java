package snake;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.*;

public class ReplayMode {
  private ArrayList<SaveThisMoment> listOfMoments = new ArrayList<SaveThisMoment>();
  private static int sizeOfList;
  private SaveThisMoment moment;
  private boolean stateOfOpening;

  public ReplayMode(Shell shell) {
    FileDialog dialog = new FileDialog(shell);
    dialog.setFilterExtensions(new String[] {"*.out"});
    String name = dialog.open();
    if (name != null) {
      stateOfOpening = true;
      listOfMoments = FileStream.readFromFile(new File(name));
      sizeOfList = listOfMoments.size();
      moment = listOfMoments.get(0);
    } else {
      stateOfOpening = false;
      Snake.returnToMainMenu();
    }
  }

  /**
   * @param index
   * @return moment of game saved in file
   */
  public SaveThisMoment getStep(int index) {
    moment = listOfMoments.get(index);
    return moment;
  }

  /**
   * @return size of array
   */
  public int getSizeOfList() {
    return sizeOfList;
  }

  public boolean getStateOfOpening() {
    return stateOfOpening;
  }
}
