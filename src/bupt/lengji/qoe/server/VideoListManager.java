package bupt.lengji.qoe.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import bupt.lengji.qoe.database.DBManager;

public class VideoListManager {

	public JSONArray getVideoList(int type) {
		JSONArray jsonArray = new JSONArray();
		String sql = "select * from videos where type = " + String.valueOf(type);
		Connection conn = DBManager.getConn();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", rs.getString("id"));
				jsonObject.put("title", rs.getString("title"));
				jsonObject.put("description", rs.getString("description"));
				jsonObject.put("type", rs.getInt("type"));
				jsonObject.put("uhd", rs.getBoolean("uhd"));
				jsonObject.put("hd", rs.getBoolean("hd"));
				jsonObject.put("sd", rs.getBoolean("sd"));
				jsonArray.put(jsonObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBManager.closeConn(conn);
		return jsonArray;
	}

	public VideoListManager() {
	}

}
