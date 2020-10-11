import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;

public class Lab0x04 {

    private static Long[] cache = new Long[100];

    public static void main(String[] args) {
        long time, overhead;
        long maxTime = 1000;
        int repetitions = 100000000;

        long[][] times = new long[4][100];
        boolean[] continuing = new boolean[4];

        // Zero out arrays
        Arrays.fill(continuing, true);
        for(long[] arr : times)
            Arrays.fill(arr,0);

        // Get overhead to do for loop
        time = getCpuTime();
        for(int i = 0; i < repetitions; i++);
        overhead = getCpuTime() - time;


        System.out.println("\t\tFibRecur\t\t\tFibCache\t\t\tFibLoop\t\t\tFibMatrix");
        System.out.println("X\tN\tTime\tTx(X)/Tx(X/2)\tExpected\tTime\tTx(X)/Tx(X/2)\tExpected\tTime\tTx(X)/Tx(X/2)\tExpected\tTime\tTx(X)/Tx(X/2)\tExpected");
        for(int x = 0; fibMatrix(x) >= 0; x++) {
            System.out.print(x + "\t" + N(x) + "\t");

//            if(continuing[0]) {
//                time = getCpuTime();
//                for(int i = 0; i < repetitions; i++)
//                    fibRecur(x);
//                times[0][x] = (getCpuTime() - time - overhead) / repetitions;
//                System.out.print(times[0][x] + "\t" + (x == 0 ? "NA" : (double)times[0][x] / times[0][x/2]) + "\t" +(x == 0 ? "NA" :  Math.pow(2,x)/Math.pow(2,x/2)) + "\t");
//                if(times[0][x] > maxTime) continuing[0] = false;
//            } else System.out.print("NA\tNA\tNA\t");

            if(continuing[1]) {
                time = getCpuTime();
                for(int i = 0; i < repetitions; i++)
                    fibCache(x);
                times[1][x] = (getCpuTime() - time - overhead) / repetitions;
                System.out.print(times[1][x] + "\t" + (x == 0 ? "NA" : (double)times[1][x] / times[1][x/2]) + "\t" + (x == 0 ? "NA" : 2) + "\t");
                if(times[1][x] > maxTime) continuing[1] = false;
            } else System.out.print("NA\tNA\tNA\t");

//            if(continuing[2]) {
//                time = getCpuTime();
//                for(int i = 0; i < repetitions; i++)
//                    fibLoop(x);
//                times[2][x] = (getCpuTime() - time - overhead) / repetitions;
//                System.out.print(times[2][x] + "\t" + (x == 0 ? "NA" : (double)times[2][x] / times[2][x/2]) + "\t" + (x == 0 ? "NA" : 2) + "\t");
//                if(times[2][x] > maxTime) continuing[2] = false;
//            } else System.out.print("NA\tNA\tNA\t");
//
//            if(continuing[3]) {
//                time = getCpuTime();
//                for(int i = 0; i < repetitions; i++)
//                    fibMatrix(x);
//                times[3][x] = (getCpuTime() - time - overhead) / repetitions;
//                System.out.print(times[3][x] + "\t" + (x == 0 ? "NA" : (double)times[3][x] / times[3][x/2]) + "\t" + (x == 0 ? "NA" : Math.log(x)/Math.log(x/2)) + "\t");
//                if(times[3][x] > maxTime) continuing[3] = false;
//            } else System.out.print("NA\tNA\tNA\t");

            System.out.println();
        }
    }

    private static int N(int x) {
        return (int) Math.ceil(Math.log(x+1));
    }

    public static long myfibRecur(int x) {
        return x == 0 || x == 1 ? x : myfibRecur(x-2,0,1);
    }

    private static long myfibRecur(int x, int f1, int f2) {
        return x == 0 ? f1 + f2 : myfibRecur(x-1,f2,f1+f2);
    }

    public static long fibRecur(int x) {
        return x == 0 || x == 1 ? x : fibRecur(x-1) + fibRecur(x-2);
    }

    public static long fibCache(int x) {
        Arrays.fill(cache, null);
        return fibCacheHelper(x);
    }

    private static long fibCacheHelper(int x) {
        if(x==0 || x==1) return x;
        if(cache[x] == null)
            cache[x] = fibCacheHelper(x-1) + fibCacheHelper(x-2);
        return cache[x];
    }

    public static long fibLoop(int x) {
        int f1 = 1, f2 = 1;

        for(int i = 2; i < x; i++) {
            f2 = f1 + f2;
            f1 = f2 - f1;
        }

        return f2;
    }

    public static long fibMatrix(int x) {
        if(x == 0 || x == 1) return x;
        int exponent = x-1;
        long[][] squarePow = new long[][] {{1,1},{1,0}}, result = new long[][] {{1,0},{0,1}};

        while(exponent > 0) {
            if(exponent%2 == 1) {
                result = new long[][] {
                        {squarePow[0][0] * result[0][0] + squarePow[0][1] * result[1][0],squarePow[0][0] * result[0][1] + squarePow[0][1] * result[1][1]},
                        {squarePow[1][0] * result[0][0] + squarePow[1][1] * result[1][0],squarePow[1][0] * result[0][1] + squarePow[1][1] * result[1][1]}
                };
            }
            squarePow = new long[][] {
                    {squarePow[0][0] * squarePow[0][0] + squarePow[0][1] * squarePow[1][0],squarePow[0][0] * squarePow[0][1] + squarePow[0][1] * squarePow[1][1]},
                    {squarePow[1][0] * squarePow[0][0] + squarePow[1][1] * squarePow[1][0],squarePow[1][0] * squarePow[0][1] + squarePow[1][1] * squarePow[1][1]}
            };
            exponent /= 2;
        }

        return result[0][0];
    }

    /** Get CPU time in nanoseconds since the program(thread) started. */
    /** from: http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking#TimingasinglethreadedtaskusingCPUsystemandusertime **/
    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }
}
