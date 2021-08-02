import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class ProteinSequenceAlignment {

	public static void main(String[] args) throws IOException {
		
		int gap = -5; // Linear gap penalty 
		
		String cost [][] = new String [25][25]; // BLOSUM62 scoring matrix
		
		writeBlosumToCostMatrix(cost); // writes match-mismatch values to cost array
		
		String twoString[] = takeStringsFromInputFile(); // picks random two strings(amino acid sequences) from input.txt file
		
		String x = twoString[0];
		String y = twoString[1];
		
		System.out.println("Score : " + calculatedScore(x, y, cost, gap));
		
	}
	
	public static int calculatedScore(String x, String y, String [][] arr, int gap) {
		
		int comparison [][] = new int [y.length() + 1][x.length() + 1];
		
		int fromLeft = 0; 
		int fromTop = 0;  
		int diagonal = 0; // match-mismatch
		
		comparison[0][0] = 0;
		
		for (int i = 1; i < comparison.length; i++) {
			for (int j = 1; j < comparison[i].length; j++) {
				
				comparison[i][0] = comparison[i-1][0] + gap; // first column is increasing by gap (0, -5, -10, -15, -20, -25 , ...)
				comparison[0][j] = comparison[0][j-1] + gap; // first row is increasing by gap (0, -5, -10, -15, -20, -25 , ...)
				
				fromLeft = 0;
				fromTop = 0;
				diagonal = 0;
				
				fromLeft = comparison[i][j-1] + gap;
				fromTop = comparison[i-1][j] + gap;

				for (int k = 0; k < arr.length; k++)
					for (int l = 0; l < arr[k].length; l++)
						if(arr[0][l].equals(String.valueOf(x.charAt(j-1))) && arr[k][0].equals(String.valueOf(y.charAt(i-1))) )
							diagonal = comparison[i-1][j-1] + Integer.parseInt(arr[k][l]); // cost[k][l] : match-mismatch value at blosum62
				
				// diagonal --> left --> top
				
				if(diagonal > fromLeft && diagonal > fromTop)
					comparison[i][j] = diagonal;
				if(diagonal == fromLeft && diagonal >= fromTop)
					comparison[i][j] = diagonal;
				if(diagonal == fromTop && diagonal >= fromLeft)
					comparison[i][j] = diagonal;
				
				if(fromLeft > diagonal && fromLeft >= fromTop)
					comparison[i][j] = fromLeft;
				
				if(fromTop > diagonal && fromTop > fromLeft)
					comparison[i][j] = fromTop;
				
				
			}
			
		}

		// comparison table can be displayed
		
		System.out.println("----------------- Comparison Matrix -----------------\n");
		for (int i = 0; i < comparison.length; i++) {
			for (int j = 0; j < comparison[i].length; j++) {
				System.out.print(comparison[i][j] + "	");
			}
			System.out.println();
		}
		System.out.println();
		
		return comparison[y.length()][x.length()];
		
	}
	
	public static void writeBlosumToCostMatrix(String [][] arr) throws IOException {
		
		String temp [] = new String [25];
		
		File file = new File("Blosum62.txt"); 
		  
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		
		int indexY = 0;
		int indexX = 1;
		
		arr[0][0] = " ";
		
		String st;
		st = br.readLine();
		
		while ((st = br.readLine()) != null) {
			
			temp = st.split(" ");			
			
			for (int i = 0; i < temp.length; i++) {
				
				if(!temp[i].equals("")) {
					
					arr[indexY][indexX] = temp[i];
					
					if(indexX < arr.length - 1)
						indexX++;
					else
						indexX = 0;
					
				}
			}
			
			if(indexY < arr.length - 1)
				indexY++;
			else
				indexY = 0;
			
		}
		
	}
	
	public static String [] takeStringsFromInputFile() throws IOException {
		
		String x = "", y = "";
		
		File file = new File("input.txt"); 
		  
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String inputs [] = new String [1000];
		
		int index = 0;
		
		String st;
		
		while ((st = br.readLine()) != null) {
			
			inputs[index] = st;
			++index;
			
		}
		
		int num1 = 0, num2 = 0;
		
		Random rnd = new Random();
		num1 = rnd.nextInt(index);
		
		x = inputs[num1];
		
		do {
			
			num2 = rnd.nextInt(index);
			
		}while(num1 == num2);
		
		y = inputs[num2];
		
		return new String[] {x, y};
	}
	
}
