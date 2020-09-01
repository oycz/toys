
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


// A solver to solve generalized k-sum problem
public class KSumSolver {

    public static void main(String args[]) {
        int testSize = 2000;
        int testRange = 10;
        int[] nums = new int[testSize];
        Random random = new Random();
        for(int i = 0; i < testSize; ++i) {
            nums[i] = random.nextInt(testRange * 2) - testRange;
        }
        KSumSolver solver = new KSumSolver();
        // 2 sum
        List<List<Integer>> twoSumResult = solver.kSum(nums, 0, 2);
        printListOfList(twoSumResult);
        // 3 sum
        List<List<Integer>> threeSumResult = solver.kSum(nums, 0, 3);
        printListOfList(threeSumResult);
        // 4 sum
        List<List<Integer>> fourSumResult = solver.kSum(nums, 0, 4);
        printListOfList(fourSumResult);
        // 10 sum
        List<List<Integer>> twentySumResult = solver.kSum(nums, 0, 10);
        printListOfList(twentySumResult);
    }

    private static void printListOfList(List<List<Integer>> l) {
        for(List<Integer> u: l) {
            System.out.print('[');
            for(int i: u) {
                System.out.print(i + ", ");
            }
            System.out.print(']');
        }
        System.out.println();
    }

    public List<List<Integer>> kSum(int[] nums, int target, int k) {
        int[] copyOfNums = Arrays.copyOf(nums, nums.length);
        Arrays.sort(copyOfNums);
        return kSumHelper(copyOfNums, target, 0, copyOfNums.length - 1, k);
    }

    private List<List<Integer>> kSumHelper(int[] nums, int target, int i, int j, int k) {
        if(k == 2) {
            return twoSumHelper(nums, target, i, j);
        } else {
            List<List<Integer>> res = new ArrayList<>();
            int m = i;
            while(j - m + 1 >= k) {
                List<List<Integer>> kMinusOneRes = kSumHelper(nums, target - nums[m], m + 1, j, k - 1);
                for(List<Integer> l: kMinusOneRes) {
                    l.add(nums[m]);
                    res.add(l);
                }
                while(j - m + 1 >= k && nums[m] == nums[m + 1]) {
                    ++m;
                }
                ++m;
            }
            return res;
        }
    }

    private List<List<Integer>> twoSumHelper(int[] nums, int target, int i, int j) {
        List<List<Integer>> res = new ArrayList<>();
        int l = i, r = j;
        while(l < r) {
            if(nums[l] + nums[r] < target) {
                ++l;
            } else if(nums[l] + nums[r] > target) {
                --r;
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(nums[l]);
                list.add(nums[r]);
                res.add(list);
                while(l < r && nums[l + 1] == nums[l]) {
                    ++l;
                }
                ++l;
            }
        }
        return res;
    }
}
