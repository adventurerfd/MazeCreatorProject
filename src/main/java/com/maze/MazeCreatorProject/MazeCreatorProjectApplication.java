package com.maze.MazeCreatorProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.stream.IntStream;
@RestController
@SpringBootApplication
public class MazeCreatorProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(MazeCreatorProjectApplication.class, args);
	}

	@GetMapping("/getMaze")
	public String getMaze(@RequestParam(name = "mazeHeight", defaultValue = "11") Integer columnsNumber , @RequestParam(name = "mazeWidth", defaultValue = "11") Integer rowsNumber) {
		int colNum = columnsNumber;
		int rowNum = rowsNumber;
		System.out.println("col = " + columnsNumber + " row = " + rowsNumber);
		Maze maze = new Maze(columnsNumber, rowsNumber);
		maze.generateMaze();
		String fullBlock = "&#9632;" + "&#32;";
		String emptyBlock = "&#9633;";
		String mazeSrt = "<body style='line-height: 85%'>";
		mazeSrt += fullBlock.repeat(columnsNumber+2) + "<br>";
		for(int column = 0; column < columnsNumber; column++){
			mazeSrt += fullBlock;
			for (int row = 0; row < rowsNumber; row++){
				mazeSrt += String.valueOf(maze.maze[column][row]).replace("1",fullBlock).replace("0",emptyBlock) + " ";
			}
			mazeSrt += fullBlock;
			mazeSrt += "<br>";
		}
		mazeSrt += fullBlock.repeat(columnsNumber+2);
//		return String.format("Hello %s!", "<br>" + "col = " + colNum + " row = " + rowNum);
		return mazeSrt + "</body>";
	}

}
class Maze {
	final int MOVE_LEFT = 11;
	final int MOVE_RIGHT = 22;
	final int MOVE_UP = 33;
	final int MOVE_DOWN = 44;
	int columnsNumber, rowsNumber, x , y;
	int[][] maze;

	public Maze(Integer colNumber, Integer rowNumber) {
		columnsNumber = colNumber;
		rowsNumber = rowNumber;
		x = getRandomFrom(evenArray(columnsNumber-1));
		y = getRandomFrom(evenArray(rowsNumber-1));
		maze = generateEmptyMaze();
		System.out.println("startX = " + x + " startY = " + y);

	}

	public int[][] generateMaze(){
		while (!isMaze() ) {
			moveTractor();
		}
		System.out.println("Final " + "X = " + x + " Y = " + y );
		return maze;
	}
	private int[][] generateEmptyMaze(){
		int[][] mazeArray = new int[columnsNumber][rowsNumber];
		for(int column = 0; column < columnsNumber; column++)
			for (int row = 0; row < rowsNumber; row++)
				mazeArray[column][row] = 1;
		return mazeArray;
	}

	private boolean isMaze () {
		for(int x = 0; x < columnsNumber; x++){
			for (int y = 0; y < rowsNumber; y++){
				if (isEven(x) && isEven(y) && getField(x,y) == 1) {
					return false;
				}
			}
		}
		return true;
	}
	private int getField(int x, int y){
//		inX = x; inY = y;
		if (x < 0 || x >= columnsNumber || y < 0 || y >= rowsNumber) {
			return maze[y][x];
		}
		return maze[y][x];
	}
	private void setField(int x, int y){
		if (x < 0 || x >= columnsNumber || y < 0 || y >= rowsNumber) {
			return;
		}
		maze[y][x] = 0;
	}
	private static int[] evenArray(int number) {
		return IntStream.rangeClosed(1, number/2).map(x -> x*2).toArray();
	}
	private int getRandomFrom (int[] array) {
		int index = (int) Math.floor(Math.random() * array.length);
		return array[index];
	}
	private boolean isEven (int n) {
		return n % 2 == 0;
	}
	private void moveTractor() {
		ArrayList<Integer> directs = new ArrayList<Integer>();
		if(x >0) directs.add(MOVE_LEFT);
		if (x < columnsNumber - 2) directs.add(MOVE_RIGHT);
		if(y >0) directs.add(MOVE_UP);
		if (y < rowsNumber - 2) directs.add(MOVE_DOWN);
		int[] arr = directs.stream().mapToInt(i -> i).toArray();
		int direct = getRandomFrom(arr);
		switch (direct){
			case MOVE_LEFT:
				if (getField(x - 2, y) == 1) {
					setField(x - 1, y);
					setField(x - 2, y);
				}
				x -= 2;
				break;
			case MOVE_RIGHT:
				if (getField(x + 2, y) == 1) {
					setField(x + 1, y);
					setField(x + 2, y);
				}
				x += 2;
				break;
			case MOVE_UP:
				if (getField(x, y - 2) == 1) {
					setField(x, y - 1);
					setField(x, y - 2);
				}
				y -= 2;
				break;
			case MOVE_DOWN:
				if (getField(x, y + 2) == 1) {
					setField(x, y + 1);
					setField(x, y + 2);
				}
				y += 2;
				break;
		}

	}

}
