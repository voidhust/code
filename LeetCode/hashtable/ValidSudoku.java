public class ValidSudoku {

    public boolean isValidSudoku(char[][] board) {
        int[][] rowsMap=new int[9][9];
        int[][] colsMap=new int[9][9];
        int[][] gridsMap=new int[9][9];
        for(int row=0;row<9;row++){
        	for(int col=0;col<9;col++){
        		if(board[row][col]=='.') continue;
        		int value=board[row][col]-'1';//1-9变换成0~8
        		//验证行
        		if(rowsMap[row][value]==1){
        			return false;
        		}else{
        			rowsMap[row][value]=1;
        		}
        		//验证列
        		if(colsMap[col][value]==1){
        			return false;
        		}else{
        			colsMap[col][value]=1;
        		}
        		//验证单元格
        		int index=(row/3)*3+col/3;
        		if(gridsMap[index][value]==1){
        			return false;
        		}else{
        			gridsMap[index][value]=1;
        		}
        	}
        }
        return true;
    }

}
