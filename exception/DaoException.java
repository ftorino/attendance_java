package com.zk.exception;


public class DaoException extends RuntimeException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DaoException()
    {
    }

    public DaoException(String message)
    {
        super(message);
    }

    public DaoException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DaoException(Throwable cause)
    {
        super(cause);
    }
}
