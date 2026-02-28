package proj.nccc.atsLog.batch.exception;

/**
 * 
 * 發生Exception
 *
 */
public class BatchException extends Exception
{

	private static final long serialVersionUID = 1L;
	private String returnCode;

	/** Creates a new instance of BatchException.java */
	public BatchException()
	{
	}

	/** Creates a new instance of BatchException.java */
	public BatchException(String message)
	{
		super( message );
	}

	/** Creates a new instance of BatchException.java */
	public BatchException(Throwable cause)
	{
		super( cause );
	}

	/** Creates a new instance of BatchException.java */
	public BatchException(String message, Throwable cause)
	{
		super( message , cause );
	}
	
	public BatchException(String message, String returnCode)
	{
		super(message);
		this.returnCode = returnCode;
	}	

	public String getReturnCode()
	{
		return returnCode;
	}

	public void setReturnCode(String returnCode)
	{
		this.returnCode = returnCode;
	}

}
