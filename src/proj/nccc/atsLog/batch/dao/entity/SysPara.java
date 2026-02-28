package proj.nccc.atsLog.batch.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysPara extends ProjEntity{
	private static final long serialVersionUID = -1057239101776023227L;
	
	private String type;
	private String code;
	private String description;
	private String value;
	private String valueExt1;
	private String valueExt2;
	private String valueExt3;

}