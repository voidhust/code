public class TwoSum { 
    public int[] twoSum(int[] nums, int target) {
    	int[] res=new int[2]; 
    	Map<Integer,Integer> indexMap=new HashMap<Integer,Integer>();
    	for(int i=0;i<nums.length;i++){
    		if(target%2==0&&nums[i]==target/2&&indexMap.get(target/2)!=null){
    			int secondIndex=indexMap.get(target/2);
    			res[0]=Math.min(i, secondIndex);
    			res[1]=Math.max(i, secondIndex);
    			return res;
    		}
    		indexMap.put(nums[i], i);
    	} 
    	for(int i=0;i<nums.length;i++){
    		if(indexMap.containsKey(target-nums[i])&&indexMap.get(target-nums[i])!=i){
    			int secondIndex=indexMap.get(target-nums[i]);
    			res[0]=Math.min(i, secondIndex);
    			res[1]=Math.max(i, secondIndex);
    			break;
    		}
    	}
		return res; 
    }
}
