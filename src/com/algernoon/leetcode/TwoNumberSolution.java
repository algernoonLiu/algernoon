package com.algernoon.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Given nums = [2, 7, 11, 15], target = 9,
 * Because nums[0] + nums[1] = 2 + 7 = 9,
 * return [0, 1].
 */
public class TwoNumberSolution {
	
    public static int[] twoSum(int[] nums, int target) {
    	if(nums == null){
    		return null;
    	}
        int len = nums.length;
        for (int i = 0; i < len; i++){
            for (int j = i + 1; j < len; j++){
                if(nums[i] + nums[j] == target){
                	return new int[]{i, j};
                }
            }
        }
        return null;
    }
    
    public static int[] twoSum2(int[] nums, int target) {
    	if(nums == null){
    		return null;
    	}
    	int len = nums.length;
    	int[] newnums = Arrays.copyOf(nums, len), result = new int[2];
    	Arrays.sort(newnums);		
    	
    	int low = 0, high = len - 1, result0 = 0, result1 = 0;
    	while (low < high) {
			if (newnums[low] + newnums[high] < target) {
				low++;
			}else if (newnums[low] + newnums[high] > target) {
				high--;
			}else {
				result0 = newnums[low];
				result1 = newnums[high];
				break;
			}	
		}
    	
    	for (int i = 0; i < len; i++) {
			if (nums[i] == result0) {
				result[0] = i;
			}else if (nums[i] == result1) {
				result[1] = i;
			}
		}
    	return result;
    }
    
    public static int[] twoSum3(int[] nums, int target) {
    	if(nums == null){
    		return null;
    	}
    	int len = nums.length;
    	Map<Integer, Integer> map = new HashMap<>();
    	for (int i = 0; i < len; i++) {
			map.put(nums[i], i);
		}
    	for (int i = 0; i < len; i++) {
    		int gap = target - nums[i];
    		if (map.get(gap) != null && map.get(gap)!= i){
    			return new int[]{i, map.get(gap)};
    		}
		}
    	return null;
    }
    
    public static void main(String[] args) {
    	int[] nums = {2, 7, 11, 15,18};
    	int[] twoSum = twoSum(nums, 20);
    	System.out.println(twoSum != null ? Arrays.toString(twoSum) : "返回null");
	}
}