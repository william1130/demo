package proj.nccc.atsLog.batch.dao.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HouseKeeping extends ProjEntity{
	
	private static final long serialVersionUID = -2896339047722544188L;
	
	private String itemCategory;
	private int keepDays;
	private String tablePath;
	private String fieldFile;
	private String logicalRule;
	private String status;
	private String name;
	private String datePattern;
	private Date expireDate;
}
