package calculator;

@SuppressWarnings("serial")
public class divZero extends ArithmeticException
{
    public divZero()
    {
        super();
    }

    public divZero(String message)
    {
        super(message);
    }
}