package   com.pangugle.passport.limit;

/**
 * 
 * @author Administrator
 *
 */
public class InvalidLoginTokenException extends RuntimeException
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    public static final InvalidLoginTokenException mException = new InvalidLoginTokenException();
}
