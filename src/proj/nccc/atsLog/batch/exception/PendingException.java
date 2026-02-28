package proj.nccc.atsLog.batch.exception;

public class PendingException extends Exception
{

	private static final long serialVersionUID = 1L;
	private String returnCode;

	public PendingException()
	{
	}

	public PendingException(String message)
	{
		super( message );
	}

	public PendingException(Throwable cause)
	{
		super( cause );
	}

	public PendingException(String message, Throwable cause)
	{
		super( message , cause );
	}
	
	public PendingException(String message, String returnCode)
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
