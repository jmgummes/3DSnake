
import java.sql.SQLException;
import java.util.List;

import back.LevelSchema;
import back.LevelSchemaLoader;
import front.TitleScreen;
import front.Window;

/**
 * This is the main class for running the game. Pretty basic.
 * 
 * @author Jim
 */
public class Main {

  /**
   * Runs the game.
   * 
   * @throws SQLException
   */
  public static void main(String[] args) throws SQLException {
    System.setProperty("sun.java2d.uiScale", "1.0");
    Window window = new Window();
    LevelSchemaLoader loader = new LevelSchemaLoader();
    List<LevelSchema> levelSchemas = loader.loadLevelSchemas();
    new TitleScreen(window, levelSchemas);
    window.setVisible(true);
  }

}
