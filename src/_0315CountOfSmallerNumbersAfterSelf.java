import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class _0315CountOfSmallerNumbersAfterSelf {

    /**
     * 1. Merge Sort
     *
     * The smaller numbers on the right of a number
     * are exactly those that jump from its right to its left during a stable sort,
     * which is counted in merge sort.
     *
     * Inversion: an inversion is a Pair of Numbers, in an array, where the left number is bigger than the right number
     * This problem is equivalent of 'count of inversions of each element'
     *
     * Breakdown: https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/445769/merge-sort-CLEAR-simple-EXPLANATION-with-EXAMPLES-O(n-lg-n)
     *
     * Time complexity: O(nlogn)
     * Space complexiity: O(n)
     *
     */
    public List<Integer> countSmaller2(int[] nums) {
        List<Integer> res = new ArrayList<Integer>();

        int[] count = new int[nums.length];
        int[] indexes = new int[nums.length];
        for(int i = 0; i < nums.length; i++){
            indexes[i] = i;
        }
        mergeSort(nums, indexes, 0, nums.length - 1, count);
        for(int i = 0; i < count.length; i++){
            res.add(count[i]);
        }
        return res;
    }

    private void mergeSort(int[] nums, int[] indexes, int start, int end, int[] count){
        if(end <= start){
            return;
        }
        int mid = (start + end) / 2;
        mergeSort(nums, indexes, start, mid, count);
        mergeSort(nums, indexes, mid + 1, end, count);

        merge(nums, indexes, start, end, count);
    }

    /**
     * rightmost: the number of elements in the right sorted part
     * that are smaller than the next element from the left sorted part
     *
     * Why are they smaller than the next element from the left sorted part?
     * Because they come before (the next element from the left sorted part) in the merged array.
     *
     */
    private void merge(int[] nums, int[] indexes, int start, int end, int[] count){
        int mid = (start + end) / 2;
        int left_index = start;
        int right_index = mid+1;
        int rightcount = 0;
        int[] new_indexes = new int[end - start + 1];

        int sort_index = 0;
        while(left_index <= mid && right_index <= end){
            if(nums[indexes[right_index]] < nums[indexes[left_index]]){
                new_indexes[sort_index] = indexes[right_index];
                rightcount++;
                right_index++;
            }else{
                new_indexes[sort_index] = indexes[left_index];
                count[indexes[left_index]] += rightcount;
                left_index++;
            }
            sort_index++;
        }
        while(left_index <= mid){
            new_indexes[sort_index] = indexes[left_index];
            count[indexes[left_index]] += rightcount;
            left_index++;
            sort_index++;
        }
        while(right_index <= end){
            new_indexes[sort_index++] = indexes[right_index++];
        }
        for(int i = start; i <= end; i++){
            indexes[i] = new_indexes[i - start];
        }
    }

    /**
     * 1. Merge Sort with Wrapper class
     *
     * Space complexity:
     * elements: O(n)
     * mergeSortAndCount call stack: O(logn)
     * mergedList: O(n)
     *
     */
    // Wrapper class for each and every value of the input array,
    // to store the original index position of each value, before we merge sort the array
    private class Element {
        int val;
        int index;

        public Element(int val, int index) {
            this.val = val;
            this.index = index;
        }
    }

    /**
     * Why result list -> array: before we fill in the array, we don't have random access
     *
     * elements: the array we want to sort, and we store each intermediate sorted status in it.
     * Why elements, why not just integer?
     * MergeSort tells us the number of smaller numbers on the right,
     * but as numbers move around, we lost the position info (where should we put them in the result list)
     *
     */
    public List<Integer> countSmaller(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new ArrayList<Integer>();
        }

        int n = nums.length;
        int[] result = new int[n];

        Element[] elements = new Element[n];
        for (int i = 0; i < n; ++i) {
            elements[i] = new Element(nums[i], i);
        }

        mergeSortAndCount(elements, 0, n - 1, result);

        // notice we don't care about the sorted array after merge sort finishes.
        // we only wanted the result counts, generated by running merge sort
        List<Integer> resultList = new ArrayList<Integer>();
        for (int i : result) {
            resultList.add(i);
        }
        return resultList;

        // we can't return Arrays.asList(result);
        // -----
        // incompatible types: inference variable T has incompatible bounds
        // equality constraints: Integer
        // lower bounds; int[]
        // where T is a type-variable: T extends Object declardd in method <T>asList(T..)
        // -----
        // Explanation:
        // Arrays.asList is expecting a variable number of Object.
        // int is not an Object, but int[] is, thus Arrays.asList(A) will create a List<int[]> with just one element.
    }

    private void mergeSortAndCount(Element[] nums, int start, int end, int[] result) {
        if (start >= end) {
            return;
        }

        int mid = start + (end - start) / 2;
        mergeSortAndCount(nums, start, mid, result);
        mergeSortAndCount(nums, mid + 1, end, result);

        // left subarray start...mid
        // right subarray mid+1...end
        int leftPos = start;
        int rightPos = mid + 1;
        List<Element> merged = new ArrayList<Element>();
        int numElemsRightArrayLessThanLeftArray = 0;
        while (leftPos <= mid && rightPos <= end) {
            if (nums[leftPos].val > nums[rightPos].val) {
                // this code block is exactly what the problem is asking us for:
                // a number from the right side of the original input array, is smaller
                // than a number from the left side
                //
                // within this code block,
                // nums[rightPos] is smaller than the start of the left sub-array.
                // Since left sub-array is already sorted,
                // nums[rightPos] must also be smaller than the entire remaining left sub-array
                ++numElemsRightArrayLessThanLeftArray;

                // continue with normal merge sort, merge
                merged.add(nums[rightPos]);
                ++rightPos;
            } else {
                // a number from left side of array, is smaller than a number from
                // right side of array
                result[nums[leftPos].index] += numElemsRightArrayLessThanLeftArray;

                // Continue with normal merge sort
                merged.add(nums[leftPos]);
                ++leftPos;
            }
        }

        // part of normal merge sort, if either left or right sub-array is not empty,
        // move all remaining elements into merged result
        while (leftPos <= mid) {
            result[nums[leftPos].index] += numElemsRightArrayLessThanLeftArray;

            merged.add(nums[leftPos]);
            ++leftPos;
        }

        while (rightPos <= end) {
            merged.add(nums[rightPos]);
            ++rightPos;
        }

        // The above 3 while loops can be written in one
//        while (leftPos <= mid || rightPos <= end) {
//            if (rightPos <= end && (leftPos > mid || nums[leftPos].val > nums[rightPos].val)) {
//                numElemsRightArrayLessThanLeftArray++;
//                merged.add(nums[rightPos]);
//                rightPos++;
//            } else {
//                // rightPos > end
//                // OR
//                // leftPos <= mid && nums[leftPos].val <= nums[rightPos].val
//                result[nums[leftPos].index] += numElemsRightArrayLessThanLeftArray;
//                merged.add(nums[leftPos]);
//                leftPos++;
//            }
//        }

        // part of normal merge sort
        // copy back merged result into array
        int pos = start;
        for (Element m : merged) {
            nums[pos] = m;
            ++pos;
        }
    }

    /**
     * 2. Binary Search Tree
     *
     * Time complexity: O(n ^ 2)
     * O(n ^ 2) for decreasing sequence
     * Almost bad as brute force
     *
     * Space complexity:
     *
     * Source:
     * https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/76580/9ms-short-Java-BST-solution-get-answer-when-building-BST
     *
     * Another version:
     * iterative insert
     * https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/76587/Easiest-Java-solution
     *
     */
    class TreeNode{
        int smallCount;
        int val;
        TreeNode left;
        TreeNode right;
        public TreeNode(int count, int val){
            this.smallCount = count;
            this.val = val;
        }
    }

    public List<Integer> countSmaller3(int[] nums) {
        TreeNode root = null;
        Integer[] ret = new Integer[nums.length];
        if(nums == null || nums.length == 0) return Arrays.asList(ret);
        for(int i=nums.length-1; i>=0; i--){
            root = insert(root, nums[i], ret, i, 0);
        }
        return Arrays.asList(ret);
    }

    public TreeNode insert(TreeNode root, int val, Integer[] ans, int index, int preSum){
        if(root == null){
            root = new TreeNode(0, val);
            ans[index] = preSum;
        }
        else if(root.val > val){
            root.smallCount++;
            root.left = insert(root.left, val, ans, index, preSum);
        }
        else{
            //only adding 1 on preSum if root.val is only smaller than val
            root.right = insert(root.right, val, ans, index,
                    root.smallCount + preSum + ( root.val < val ? 1: 0));
        }
        return root;
    }

    /**
     * 3. Binary Index Tree
     */

    /**
     * 4. Segment Tree (Not good)
     *
     * Worst case example
     * 1, 2, 3, 4, 5, 6, 7, 8, 9, 999999999
     *
     * Time complexity: O(n ^ 2 logMax)
     * Space complexity: O(n + logMax)
     *
     */
    class SegTreeNode {
        int min, max; // range [min, max]
        int count; // how many numbers in this range
        SegTreeNode left;
        SegTreeNode right;

        public int mid() {
            return ((max - min) / 2 + min);
        }

        public SegTreeNode(int min, int max) {
            this.min = min;
            this.max = max;
            count = 0;
        }
    }

    public List<Integer> countSmaller4(int[] nums) {
        List<Integer> list = new LinkedList<Integer>();

        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int i : nums) {
            min = min > i ? i : min;
            max = max < i ? i : max;
        }

        SegTreeNode root = new SegTreeNode(min, max);

        for (int i = nums.length - 1; i >= 0; i--) {
            list.add(0, find(nums[i], root));
            add(nums[i], root);
        }

        return list;
    }

    private int find(int x, SegTreeNode root) {
        if (root == null) {
            return 0;
        }

        if (x > root.max) {
            return root.count;
        } else {
            int mid = root.mid();
            if (x <= mid) {
                return find(x, root.left);
            } else {
                return find(x, root.left) + find(x, root.right);
            }
        }
    }

    private void add(int x, SegTreeNode root) {
        if (x < root.min || x > root.max) return;

        root.count++;
        if (root.max == root.min) return;

        int mid = root.mid();
        if (x <= mid) {
            if (root.left == null) {
                root.left = new SegTreeNode(root.min, mid);
            }
            add(x, root.left);
        } else {
            if (root.right == null) {
                root.right = new SegTreeNode(mid + 1, root.max);
            }
            add(x, root.right);
        }
    }


    /**
     * 4. Segment Tree (Could be good?)
     *
     * Source:
     * https://www.jiuzhang.com/solution/count-of-smaller-numbers-after-self/#tag-lang-java
     *
     * Possible more concise version?
     * https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/76657/3-ways-(Segment-Tree-Binary-Indexed-Tree-Binary-Search-Tree)-clean-python-code
     *
     *
     */
    public List<Integer> countSmaller41(int[] nums) {
        List<Integer> list = new ArrayList<Integer>();
        if(nums == null || nums.length == 0) return list;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int num : nums) {
            max = Math.max(max, num);
            min = Math.min(min, num);
        }

        min = Math.min(min, 0); // ????????????min > 0, ???????????????shift;
        SegmentTree tree = new SegmentTree(max - min + 1);
        for(int i = nums.length - 1; i >= 0 ; i--) {
            if(nums[i] == min) { // ???min??????????????????????????????0???
                list.add(0, 0);
            } else {
                list.add(0, tree.querySum(0, nums[i]-1 + Math.abs(min)));
            }
            tree.add(nums[i] + Math.abs(min), 1);
        }
        return list;
    }

    private class SegmentTreeNode {
        public int sum;
        public int start;
        public int end;
        public SegmentTreeNode left, right;
        public SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.sum = 0;
            this.left = null;
            this.right = null;
        }
    }

    private class SegmentTree {
        /**
         * Height: O(logn)  because when going down from the root to the leaves the size of the segments decreases approximately by half.
         *
         * Number of nodes:
         * 1 +2 + 4 + 2 ^ log2n = 2 ^ (log2n + 1) < 4n.
         */
        private SegmentTreeNode root;

        /**
         * Time complexity: O(n)
         * Space complexity: O(logn) used by call stack
         *
         * @param size
         */
        public SegmentTree(int size) {
            root = buildTree(0, size-1);
        }

        private SegmentTreeNode buildTree(int start, int end) {
            SegmentTreeNode node = new SegmentTreeNode(start, end);
            if(start == end) {
                return node;
            }

            int mid = start + (end - start) / 2;
            node.left = buildTree(start, mid);
            node.right = buildTree(mid+1, end);
            node.sum = node.left.sum + node.right.sum;
            return node;
        }

        public void add(int index, int value) {
            add(root, index, value);
        }

        private void add(SegmentTreeNode node, int index, int value) {
            if(node.start == node.end && node.end == index){
                node.sum += value;
                return;
            }

            int mid = node.start + (node.end - node.start) / 2;
            if(node.start <= index && index <= mid){
                add(node.left, index, value);
            }
            if(mid+1 <= index && index <= node.end) {
                add(node.right, index, value);
            }

            node.sum = node.left.sum + node.right.sum;
        }

        public int querySum(int start, int end) {
            return querySum(root, start, end);
        }

        private int querySum(SegmentTreeNode node, int start, int end) {
            if(node.start == start && node.end == end) {
                return node.sum;
            }

            int mid = node.start +(node.end - node.start) / 2;
            int leftsum = 0, rightsum = 0;
            if(start <= mid) {
                leftsum = querySum(node.left, start, Math.min(mid, end));
            }
            if(end > mid) {
                rightsum = querySum(node.right, Math.max(mid+1, start), end);
            }
            return leftsum + rightsum;
        }
    }

    /**
     * 5. Binary Index Tree
     */
}
