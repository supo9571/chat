package com.pangugle.framework.lucene;

import org.apache.lucene.search.ScoreDoc;

public class FieldSortHelper {
	
	/**
	 * 使用快速排序,按从高到低,使用sortArray进行比较，dataArray跟随变动位置
	 * @param dataArray
	 * @param sortArray
	 * @param left
	 * @param right
	 */
	public static void quickSort(ScoreDoc[] dataArray, Double[] sortArray, int left, int right)
	{
		if (left >= right) return; // 表示一组排序完成
		
		ScoreDoc docKey = dataArray[left];
        double sortKey = sortArray[left];
        int low = left, high = right;
        while (low < high) {
        	// from right to left scan
            while(low < high && sortArray[high] < sortKey) {
                --high;
            }
            sortArray[low] = sortArray[high];
            dataArray[low] = dataArray[high];
            
            // from left to right 
            while(low < high && sortArray[low] >= sortKey) {
                ++low;
            }
            sortArray[high] = sortArray[low];
            dataArray[high] = dataArray[low];
        }
        sortArray[low] = sortKey;
        dataArray[low] = docKey;
        
        quickSort(dataArray, sortArray, left, low - 1);
        quickSort(dataArray, sortArray, low + 1, right);
	}
	
//	private static int getMiddleValue(Double[] dataArray,int left,int right)
//	{
//	    int mid = left + ((right - left)>>1);
//	    if(dataArray[left] <= dataArray[right])
//	    {
//	        if(dataArray[mid] <  dataArray[left])
//	            return left;
//	        else if(dataArray[mid] > dataArray[right])
//	            return right;
//	        else
//	            return mid;
//	    }
//	    else
//	    {
//	        if(dataArray[mid] < dataArray[right])
//	            return right;
//	        else if(dataArray[mid] > dataArray[left])
//	            return left;
//	        else
//	            return mid;
//	    }
//	}

}
