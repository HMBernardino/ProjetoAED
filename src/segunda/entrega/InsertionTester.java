package segunda.entrega;

import static primeira.entrega.Tools.out;
import primeira.entrega.Stopwatch;
import segunda.entrega.SumExecutionTimeTester;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

public class InsertionTester {

    public static void main(final String[] arguments) {
        out.println("Testing insertion sort:");

            	final In in = new In(arguments[0]);
            	
                final Double[] originalValues = ArrayUtils.toObject(in.readAllDoubles());
                final Double[] sortedValues = originalValues.clone();

                final Stopwatch stopwatchins = new Stopwatch();
                Insertion.sort(sortedValues);
                double tempoins = stopwatchins.elapsedTime();
                
                final Stopwatch stopwatchinsmoves = new Stopwatch();
                InsertionWithMoves.sort(sortedValues);
                double tempoinsmoves = stopwatchinsmoves.elapsedTime();
                
                out.println("inserção");
                out.println(tempoins);
                out.println("inserção with moves");
                out.println(tempoinsmoves);
                out.println();
                out.println();
                out.println("Original");
                for(int i = 0; i != originalValues.length; i++){
                out.println(originalValues[i]);
                }
               
                out.println("inserção");
                for(int i = 0; i != originalValues.length; i++){
                    out.println(sortedValues[i]);
                    }


        out.println("Finished all tests.");
    }
}