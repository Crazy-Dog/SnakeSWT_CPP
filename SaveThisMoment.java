package snake;

import java.io.Serializable;

public class SaveThisMoment implements Serializable {
  // serialVersionUID is necessary, because this class will be written to file as an object
  private static final long serialVersionUID = 5143031257300009288L;

  private int snakeX;
  private int snakeY;
  private int cherryX;
  private int cherryY;
  private int level;

  SaveThisMoment() {}

  public SaveThisMoment(int snakeX, int snakeY, int cherryX, int cherryY, int level) {
    this.snakeX = snakeX;
    this.snakeY = snakeY;
    this.cherryX = cherryX;
    this.cherryY = cherryY;
    this.level = level;
  }

  SaveThisMoment(SaveThisMoment saveField) {
    this.snakeX = saveField.getSnakeX();
    this.snakeY = saveField.getSnakeY();
    this.cherryX = saveField.getCherryX();
    this.cherryY = saveField.getCherryY();
    this.level = saveField.getLevel();
  }

  /**
   * @return snakeX
   */
  public int getSnakeX() {
    return snakeX;
  }

  /**
   * @return snakeY
   */
  public int getSnakeY() {
    return snakeY;
  }

  /**
   * @return cherryX
   */
  public int getCherryX() {
    return cherryX;
  }

  /**
   * @return cherryY
   */
  public int getCherryY() {
    return cherryY;
  }

  /**
   * @return level
   */
  public int getLevel() {
    return level;
  }

  public void setSnakeX(int snakeX) {
    this.snakeX = snakeX;
  }

  public void setSnakeY(int snakeY) {
    this.snakeY = snakeY;
  }

  public void setCherryX(int cherryX) {
    this.cherryX = cherryX;
  }

  public void setCherryY(int cherryY) {
    this.cherryY = cherryY;
  }

  public void setLevel(int yPos) {
    this.level = yPos;
  }

  public SaveThisMoment clone() {
    SaveThisMoment newSaveField = new SaveThisMoment(this);
    return newSaveField;
  }
}
