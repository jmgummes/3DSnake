package back;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LevelSchemaLoader {
	private Connection db;

	public LevelSchemaLoader() throws SQLException {
		db = DriverManager.getConnection("jdbc:sqlite:levels.db");
	}

	public List<LevelSchema> loadLevelSchemas() throws SQLException {
		ResultSet levelsResultSet = db.createStatement()
				.executeQuery("select `id`, `name`, `width`, `height`, `snake_starting_x`, `snake_starting_y`,\n"
						+ "`snake_starting_angle`, `snake_starting_speed`, `snake_starting_length`, `starting_food_number`\n"
						+ "from levels");

		Map<Integer, LevelSchema> levelSchemas = new HashMap<Integer, LevelSchema>();
		while (levelsResultSet.next()) {
			levelSchemas.put(levelsResultSet.getInt("id"), new LevelSchema(levelsResultSet.getString("name"),
					levelsResultSet.getDouble("width"), levelsResultSet.getDouble("height"),
					levelsResultSet.getDouble("snake_starting_x"), levelsResultSet.getDouble("snake_starting_y"),
					levelsResultSet.getDouble("snake_starting_angle"),
					levelsResultSet.getDouble("snake_starting_speed"), levelsResultSet.getInt("snake_starting_length"),
					levelsResultSet.getInt("starting_food_number")));
		}

		ResultSet obstaclesResultSet = db.createStatement().executeQuery(
				"select `level_id`, `x`, `y`, `width`, `height`\n"
						+ "from obstacles");
		while (obstaclesResultSet.next()) {
          LevelSchema levelSchema = levelSchemas.get(obstaclesResultSet.getInt("level_id"));
          if(levelSchema == null)
            continue;
          levelSchema.addObstacle(obstaclesResultSet.getDouble("x"), obstaclesResultSet.getDouble("y"),
        		  obstaclesResultSet.getDouble("width"), obstaclesResultSet.getDouble("height"));
		}
		
		return new LinkedList<LevelSchema>(levelSchemas.values());
	}
}
