package leightek.crackingcoding;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * PowerSet: all subsets of a set
 */
public class PowerSet {

    /**
     * Use Recursion to get all the subsets
     * @param set  the set with all items
     * @param index the number of the items
     * @return the PowerSet
     */
    public static ArrayList<ArrayList<Integer>> getSubsets(ArrayList<Integer> set, int index) {
        ArrayList<ArrayList<Integer>> allsubsets;

        if (set.size() == index) {// Base case - add empty set
            allsubsets = new ArrayList<ArrayList<Integer>>();
            allsubsets.add(new ArrayList<Integer>()); // Empty set
        } else {
            allsubsets = getSubsets(set, index + 1);
            int item = set.get(index);
            ArrayList<ArrayList<Integer>> moresubsets = new ArrayList<ArrayList<Integer>>();
            for (ArrayList<Integer> subset : allsubsets) {
                ArrayList<Integer> newsubset = new ArrayList<Integer>();
                newsubset.addAll(subset);
                newsubset.add(item);
                moresubsets.add(newsubset);
            }
            allsubsets.addAll(moresubsets);
        }

        return allsubsets;
    }

    /**
     * Use combinatorics to get all the subsets
     * @param set the set with all items
     * @return the PowerSet
     */
    public static ArrayList<ArrayList<Integer>> getSubsets(ArrayList<Integer> set) {
        ArrayList<ArrayList<Integer>> allsubsets = new ArrayList<ArrayList<Integer>>();
        int max = 1 << set.size(); // Compute 2^n

        for (int k = 0; k < max; k++) {
            ArrayList<Integer> subset = convertIntToSet(k, set);
            allsubsets.add(subset);
        }

        return allsubsets;
    }

    private static ArrayList<Integer> convertIntToSet(int x, ArrayList<Integer> set) {
        ArrayList<Integer> subset = new ArrayList<Integer>();
        int index = 0;

        for (int k = x; k > 0; k >>= 1) {
            if ((k & 1) == 1) {
                subset.add(set.get(index));
            }
            index++;
        }

        return subset;
    }

    public static void main(String[] args) {

        ArrayList<Integer> set = new ArrayList<>(Arrays.asList(1, 2, 3));
        int index = 0;

        // use recursion
        System.out.println("set = " + set + ", index =" + index + ", powersetByRecursion = " +
                PowerSet.getSubsets(set, index));

        // use combinatorics
        System.out.println("set = " + set + ", index =" + index + ", powersetByCombinatorics = " +
                PowerSet.getSubsets(set));
    }
}
