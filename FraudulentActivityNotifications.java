import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 
 */
public class FraudulentActivityNotifications {


    /**
     * Optimize the window operations from the first pass.
     */
    static class Window {


        // **** ****
        PriorityQueue<Integer> smallers = new PriorityQueue<>((a, b) -> b - a); // decrementing
        PriorityQueue<Integer> largers  = new PriorityQueue<>();                // incrementing


        /**
         * Largers should have size() + 1 of smallers.
         */
        private void adjustSizes() {
            if (smallers.size() > largers.size())
                largers.offer(smallers.poll());
            else if (largers.size() > smallers.size() + 1)
                smallers.offer(largers.poll());
        }


        /**
         * 
         */
        public void insert(Integer x) {

            // **** insert x (where needed) ****
            if (largers.isEmpty() || x >= largers.peek())
                largers.offer(x);
            else
                smallers.offer(x);

            // **** ****
            adjustSizes();
        }


        /**
         * 
         */
        public void remove(Integer x) {

            // **** ****
            if (x >= largers.peek())
                largers.remove(x);
            else
                smallers.remove(x);

            // **** ****
            adjustSizes();
        }


        /**
         * Compute median from window.
         */
        public double median() {
            if (largers.size() > smallers.size())
                return largers.peek();
            else
                return (smallers.peek() + largers.peek()) /2.0;
        }


        /**
         * 
         */
        public String toString() {
            return smallers + " - " + largers;
        }
    }


    /*
     * Complete the 'activityNotifications' function below.
     * First pass.
     * Sorting once and keeping elements sorted in the queue / window.
     * Not fast enough!
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. INTEGER_ARRAY expenditure
     *  2. INTEGER d
     */
    public static int activityNotifications0(List<Integer> expenditure, int d) {

        // **** sanity check(s) ****
        if (expenditure.size() < d) return 0;

        // **** initialization ****
        double median               = 0.0;
        int nots                    = 0;
        LinkedList<Integer> window  = new LinkedList<Integer>();

        // **** populate window ****
        for (int i = 0; i < d; i++)
            window.add(expenditure.get(i));

        // **** sort window - O(n * log(n)) ****
        Collections.sort(window);

        // **** ****
        for (int i = d; i < expenditure.size(); i++) {

            // **** get the expenditure associated with the window ****
            Integer currentExpenditure = expenditure.get(i);


            // **** compute median of window ****
            if (d % 2 == 0)
                median = (window.get(d / 2 - 1) + window.get(d / 2)) / 2.0;
            else
                median = window.get(d / 2);         


            // **** count this notification (if needed) ****
            if (currentExpenditure >= 2.0 * median)
                nots++;

            // **** remove leftmost entry ****
            window.removeFirst();

            // ****  **** 
            ListIterator<Integer> sit   = window.listIterator();
            ListIterator<Integer> lit   = window.listIterator();
            int smallerValue            = 0;

            // **** insert current expenditure keeping window in sorted order **** 
            do {

                // **** ****
                int largerValue = lit.next();

                // **** insert before first element in window (if needed) ****
                if (smallerValue <= currentExpenditure && currentExpenditure <= largerValue) {
                    sit.add(currentExpenditure);
                    break;
                }

                // **** ****
                smallerValue = sit.next();
            } while (lit.hasNext());
        }

        // **** return number of notifications ****
        return nots;
    }






    
    /*
     * Complete the 'activityNotifications' function below.
     * Using priority queues.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. INTEGER_ARRAY expenditure
     *  2. INTEGER d
     */
    public static int activityNotifications1(List<Integer> expenditure, int d) {

        // **** sanity check(s) ****
        if (expenditure.size() < d) return 0;

        // **** initialization ****
        int nots        = 0;
        double med      = 0.0;
        Window window   = new Window();

        // **** populate window ****
        for (int i = 0; i < d; i++)
            window.insert(expenditure.get(i));

        // **** ****
        for (int i = d; i < expenditure.size(); i++) {

            // ???? ????
            // System.out.println("<<< expenditure: " + expenditure.toString());
            // System.out.println("<<< window: " + window);

            // **** get the expenditure associated with the window ****
            Integer currentExpenditure = expenditure.get(i);

            // **** compute median of window ****
            med = window.median();
            
            // ???? ????
            // System.out.println("<<< med: " + med);

            // **** count this notification (if needed) ****
            if (currentExpenditure >= 2.0 * med)
                nots++;

            // **** remove leftmost entry ****
            window.remove(expenditure.get(i - d));

            // ???? ????
            // System.out.println("<<< window: " + window);

            // **** **** 
            window.insert(currentExpenditure);

            // ???? ????
            // System.out.println("<<< window: " + window);
        }

        // **** return number of notifications ****
        return nots;
    }





    /**
     * @throws IOException
     * 
     */
    public static void main(String[] args) throws IOException {
        
        // **** open a buffered reader ****
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // **** read n and d ****
        String[] strs = br.readLine().trim().split(" ");

        // **** extract n and d ****
        int n = Integer.parseInt(strs[0]);
        int d = Integer.parseInt(strs[1]);

        // **** read expenditures ****
        List<Integer> expenditure = Stream.of(br.readLine().trim().split(" "))
                                        .map(Integer::parseInt)
                                        .collect(Collectors.toList());

        // **** close the buffered reader ****
        br.close();

        // ???? ????
        System.out.println("main <<< n: " + n + " d: " + d);
        System.out.println("main <<< expenditure: " + expenditure.toString());


        // // ???? verify operation of priority queues in the Window class ????
        // Window window = new Window();

        // window.largers.add(4);
        // window.largers.add(1);
        // window.largers.add(3);
        // window.largers.add(2);
        // window.largers.add(1);

        // window.smallers.add(4);
        // window.smallers.add(1);
        // window.smallers.add(3);
        // window.smallers.add(2);
        // window.smallers.add(1);

        // while (!window.largers.isEmpty())
        //     System.out.println("main <<<  larger: " + window.largers.poll());

        // while (!window.smallers.isEmpty())
        //     System.out.println("main <<< smaller: " + window.smallers.poll());


        // **** compute and display the number of notifications ****
        System.out.println("main <<< nots: " + activityNotifications0(expenditure, d));

        // **** compute and display the number of notifications ****
        System.out.println("main <<< nots: " + activityNotifications1(expenditure, d));
    }
}