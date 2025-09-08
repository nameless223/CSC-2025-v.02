// expected output:
// y is 7043.7

public class EquationY 
{
    public static void main(String[] args)
     {
        double x = 5;
        
        double y = (12.3 * (Math.pow(5, 4)) - 9.1 * (Math.pow(5, 3)) + 19.3 * (Math.pow(5, 2)) - (4.6 * 5) + 34.2);

        System.out.println("y is " + y);
    }
}
