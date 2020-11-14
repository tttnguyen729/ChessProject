/*
 * @version 1.0
 * @author Thomas Nguyen
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ChessProject 
{
	public static void main(String[] args) throws FileNotFoundException
	{
		String[] chessMoves = readFile("revised-tartakower.pgn", 13);
		ArrayList<String> moves = separatingMoves(chessMoves);
		
		ArrayList<String> blackMoves = separateSides(moves, "black");
		ArrayList<String> whiteMoves = separateSides(moves, "white");
		
		ArrayList<String> whiteKingMoves = isolatingKing(whiteMoves);
		ArrayList<String> blackKingMoves = isolatingKing(blackMoves);

		System.out.println(whiteKingMoves);
		System.out.println(blackKingMoves);
	}
	
	// Skips the header of chess file
	public static void skipHeader(Scanner chessFile, String filename, int headerLength) throws FileNotFoundException
	{
		for (int i = 0; i < headerLength; ++i)
		{
			chessFile.nextLine();
		}
	}
	
	// @return an array of chess lines
	public static String[] readFile(String filename, int headerLength) throws FileNotFoundException
	{
		Scanner chessFile = new Scanner(new File(filename));
		skipHeader(chessFile, filename, headerLength);
		
		// Counting the number of lines with moves
		int numLines = 0;
		while (chessFile.hasNextLine()) 
		{
			numLines++;
			chessFile.nextLine();
		}
		
		// Reconstructing the scanner
		chessFile = new Scanner(new File(filename));
		skipHeader(chessFile, filename, headerLength);
		
		// Constructing an array
		String[] moves = new String[numLines];
		for (int i = 0; i < moves.length; ++i)
		{
			moves[i] = chessFile.nextLine();
		}
		
		chessFile.close();
		return moves;
	}
	
	// Separates the lines by move
	public static ArrayList<String> separatingMoves(String[] lineMoves)
	{
		ArrayList<String> results = new ArrayList<String>();
		final int NUM_SPACES = 3;
		
		for (String lineMove : lineMoves)
		{
			// Priming the pump
			int index = 0;
			int counter = 0;
			String result = "";
			
			// Taking advantage of the PGN formatting
			while (index < lineMove.length())
			{
				if (lineMove.charAt(index) == ' ')
				{
					counter++;
				}
				
				if (counter == NUM_SPACES)
				{
					results.add(result);
					result = "";
					counter = 0;
				}
				result += lineMove.charAt(index);
				index++;
			}
		}
		return results;	
	}
	
	public static ArrayList<String> separateSides(ArrayList<String> moves, String side)
	{
		ArrayList<String> whiteMoves = new ArrayList<String>();
		ArrayList<String> blackMoves = new ArrayList<String>();

		for (int i = 0; i < moves.size(); ++i)
		{
			String move = moves.get(i);
			String[] mover = move.split(" ");
			
			// Accounting for the split difference between 
			// start/end of line and body
			if (mover.length == 3)
			{
				whiteMoves.add(mover[1]);
				blackMoves.add(mover[2]);
			}
			if (mover.length == 4)
			{
				whiteMoves.add(mover[2]);
				blackMoves.add(mover[3]);
			}
			
		}
		
		// Returning ArrayList based on color
		if (side.equals("black"))
		{
			return blackMoves;
		}
		else if (side.equals("white"))
		{
			return whiteMoves;
		}
		else 
		{
			return null;
		}
	}
	
	public static ArrayList<String> isolatingKing(ArrayList<String> moves)
	{
		ArrayList<String> kingMoves = new ArrayList<String>();
		
		// Checking for castling and explicit king moves
		for (String move : moves)
		{
			if (move.charAt(0) == 'K' || move.equals("O-O") || move.equals("O-O-O"))
			{
				kingMoves.add(move);
			}
		}
		return kingMoves;
	}
}
