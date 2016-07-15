package com.algernoon.leetcode;

/**
 * 
 * You are given two linked lists representing two non-negative numbers. 
 * The digits are stored in reverse order and each of their nodes contain a single digit. 
 * Add the two numbers and return it as a linked list.
 * 
 * Example:
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
 * Output: 7 -> 0 -> 8
 */
public class AddTwoNumberSolution {
	 // Definition for singly-linked list.  
    static class ListNode {  
        int val;  
        ListNode next;  
        ListNode(int x) {
            val = x;  
        }  
    }
    
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    	if (l1 == null) {
    		return l2;
		}
    	if (l2 == null) {
			return l1;
		}
    	
    	int carry = 0;
    	ListNode result =  new ListNode(0);
    	ListNode p1 = l1;
    	ListNode p2 = l2;
    	
    	while (true) {
    		int sum = 0;
    		if (p1 != null) {
    			sum += p1.val;
    			p1 = p1.next;
    		}
    		
    		if (p2 != null) {
    			sum += p2.val;
    			p2 = p2.next;
    		}
    		
    		carry = sum / 10;
    		result.val = sum;
		}
    }
    
    public static void main(String[] args) {
    	 ListNode l1 = new ListNode(2);
         ListNode x2 = new ListNode(4);
         ListNode x3 = new ListNode(3);
         l1.next = x2;
         x2.next = x3;
         
         ListNode l2 = new ListNode(5);
         ListNode y2 = new ListNode(6);
         ListNode y3 = new ListNode(4);
         l2.next = y2;
         y2.next = y3;
         
         ListNode result = addTwoNumbers(l1, l2);
    }
    
}
