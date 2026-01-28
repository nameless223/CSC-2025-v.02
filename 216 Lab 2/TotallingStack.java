import java.util.*;

/*
 * This is a stack of Coins which also keeps track of the total value
 * of the stack contents.
 */
class TotallingStack extends Stack<Mint.Coin> {
    // Update the stack value, then call the parent to perform the push.
    public Mint.Coin push(Mint.Coin c) {
        m_value += c.getValue();
        return super.push(c);
    }

    // Use the parent to pop, then remove that from the total value.
    public Mint.Coin pop() {
        Mint.Coin ret = super.pop();
        m_value -= ret.getValue();
        return ret;
    }

    // Return the total value of the coins on the stack.
    public int getValue() { return m_value; }

    // Value of the coins.
    private int m_value = 0;

    // Don't do this.
    public int search(Object o) {
        throw new UnsupportedOperationException("Stacks are not searchable");
    }


    public static void main(String[] args)
    {
        System.out.println("How many frobs do you wish to pay?");
        Scanner scanner = new Scanner(System.in);
        int frobs = scanner.nextInt();


        TotallingStack stack = new TotallingStack();

        while(stack.getValue() < frobs) 
        {
            stack.push(Mint.largest());
            
        }

        while(stack.isEmpty() == false && stack.getValue() > frobs)
        {
            while (stack.isEmpty() == false && stack.peek() == Mint.smallest())
            {
                stack.pop();
            }

            if(stack.isEmpty() == false)
            {
                Mint.Coin popped = stack.pop();
                Mint.Coin smaller = Mint.smaller(popped);
                stack.push(Mint.smaller(popped));
                while(stack.getValue() < frobs)
                {
                    stack.push(smaller);
                }
                
            }

            

            
        }

        if(stack.isEmpty() == true)
        {
            System.out.println("It is impossible to pay this amount in Destablian coins.");
        }
        else
        {
            System.out.println("Coins to pay " + frobs + " frob: ");
            System.out.println(stack);
            // for(int i = stack.size(); i > 0; --i)
            // {
            //     System.out.println("  " + stack.pop().getName());
            // }
            
            
        }

    }

}


