package segunda.entrega;

import static primeira.entrega.Tools.out;
import primeira.entrega.Stopwatch;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

public class InsertionTester {

    public static void main(final String[] arguments) {
        out.println("Testing insertion sort:");

            	final In in = new In(arguments[0]);
            	
                final Double[] originalValues = ArrayUtils.toObject(in.readAllDoubles());
                final Double[] systemSortedValues = originalValues.clone();
                final Double[] sortedValues = originalValues.clone();

                final Stopwatch stopwatchsystem = new Stopwatch();
                Arrays.sort(systemSortedValues);
                double tempo = stopwatchsystem.elapsedTime();
                
                final Stopwatch stopwatchins = new Stopwatch();
                Insertion.sort(sortedValues);
                double tempoins = stopwatchins.elapsedTime();
                
                final Stopwatch stopwatchinsmoves = new Stopwatch();
                InsertionWithMoves.sort(sortedValues);
                double tempoinsmoves = stopwatchinsmoves.elapsedTime();
                
                out.println("System Sorted");
                out.println(tempo);
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
                
                out.println("System Sorted");
                for(int i = 0; i != originalValues.length; i++){
                    out.println(systemSortedValues[i]);
                    }

                out.println("inserção");
                for(int i = 0; i != originalValues.length; i++){
                    out.println(sortedValues[i]);
                    }


        out.println("Finished all tests.");
    }
}

/*
 * Copyright 2017, Manuel Menezes de Sequeira.
 * 
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this code. If not, see http://www.gnu.org/licenses.
 */