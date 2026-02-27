package proj.nccc.logsearch.qs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.edstw.persist.jdbc.JdbcPersistableBuilder;

public abstract class ProjNameMapQS extends ProjQS {

	/**
	 * @param list
	 * @return
	 */
	protected Map<String, String> toMap(List<ValueObject> list) {

		Map<String, String> map = new HashMap<String, String>();

		for (ValueObject obj : list) {
			map.put(obj.value, obj.label);

			// System.out.println("Name Map-> " + obj.value + "=" + obj.label);
		}

		return map;
	}

	protected class Builder implements JdbcPersistableBuilder {

		private String nameField;
		private String labelField;

		public Builder(String nameField, String labelField) {
			this.nameField = nameField;
			this.labelField = labelField;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.edstw.sql.ResultSetCallback#isStopProcess()
		 */
		public boolean isStopProcess() {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.edstw.sql.ResultSetCallback#processResultSet(java.sql.ResultSet)
		 */
		public Object processResultSet(ResultSet rs) throws SQLException {

			ValueObject obj = new ValueObject();
			obj.value = rs.getString(nameField);
			obj.label = rs.getString(labelField);

			if (obj.label != null)
				obj.label = obj.label.trim();

			return obj;
		}
	}

	protected class Builder2 implements JdbcPersistableBuilder {

		private String nameField1;
		private String nameField2;
		private String labelField;

		public Builder2(String nameField1, String nameField2, String labelField) {
			this.nameField1 = nameField1;
			this.nameField2 = nameField2;
			this.labelField = labelField;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.edstw.sql.ResultSetCallback#isStopProcess()
		 */
		public boolean isStopProcess() {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.edstw.sql.ResultSetCallback#processResultSet(java.sql.ResultSet)
		 */
		public Object processResultSet(ResultSet rs) throws SQLException {

			ValueObject obj = new ValueObject();
			obj.value = rs.getString(nameField1) + rs.getString(nameField2);
			obj.label = rs.getString(labelField);

			if (obj.label != null)
				obj.label = obj.label.trim();

			return obj;
		}
	}

	protected class Builder3 implements JdbcPersistableBuilder {

		private String nameField1;
		private String nameField2;
		private String nameField3;
		private String labelField;

		public Builder3(String nameField1, String nameField2, String nameField3, String labelField) {
			this.nameField1 = nameField1;
			this.nameField2 = nameField2;
			this.nameField3 = nameField3;
			this.labelField = labelField;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.edstw.sql.ResultSetCallback#isStopProcess()
		 */
		public boolean isStopProcess() {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.edstw.sql.ResultSetCallback#processResultSet(java.sql.ResultSet)
		 */
		public Object processResultSet(ResultSet rs) throws SQLException {

			ValueObject obj = new ValueObject();
			obj.value = rs.getString(nameField1) + "," + rs.getString(nameField2) + "," + rs.getString(nameField3);
			obj.label = rs.getString(labelField);

			if (obj.label != null)
				obj.label = obj.label.trim();

			return obj;
		}
	}

	protected class ValueObject {

		public String value;
		public String label;
	}

}
