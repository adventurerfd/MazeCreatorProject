package com.maze.MazeCreatorProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
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
	public String getMaze(@RequestParam(name = "mazeHeight", defaultValue = "10") Integer columnsNumber , @RequestParam(name = "mazeWidth", defaultValue = "10") Integer rowsNumber) {
		int colNum = columnsNumber;
		int rowNum = rowsNumber;
		System.out.println("col = " + columnsNumber + " row = " + rowsNumber);
		Maze maze = new Maze(columnsNumber, rowsNumber);

		/*for(int column = 0; column < columnsNumber; column++){
			for (int row = 0; row < rowsNumber; row++){
				System.out.print(maze.emptyMaze[column][row] + " ");
			}
			System.out.print("\n");
		}*/

		maze.generateMaze();

		String myHtml = """
				<!DOCTYPE html>
				<html lang="ru">
				<head>
					<meta charset="UTF-8">
					<title>Лабиринт</title>
				</head>
				<body>
					<!-- подготавливаем пустой холст, чтобы работать с ним из скрипта -->
					<canvas></canvas>
					
				</body>
				</html>
				""";

//		return String.format("Hello %s!", "<br>" + "col = " + colNum + " row = " + rowNum);
		return myHtml;
	}

}
class Maze {

	int columnsNumber, rowsNumber, x , y;
	int[][] emptyMaze;

	public Maze(Integer colNumber, Integer rowNumber) {
		columnsNumber = colNumber;
		rowsNumber = rowNumber;
		x = getRandomFrom(evenArray(columnsNumber));
		y = getRandomFrom(evenArray(rowsNumber));
		emptyMaze = generateEmptyMaze();
		System.out.println("startX = " + x + " startY = " + y);

	}

	public void generateMaze(){
		System.out.println("call generateMaze" );
		int i =0;
		while (!isMaze() && i < 5) {//&& i < 25
//			System.out.println("call moveTractor" );
			moveTractor();
			i++;

		}
		System.out.println("Final " + "X = " + x + " Y = " + y );
		for(int column = 0; column < columnsNumber; column++){
			for (int row = 0; row < rowsNumber; row++){
				System.out.print(emptyMaze[column][row] + " ");
			}
			System.out.print("\n");
		}
//		return maze;

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
//				System.out.println ("x= " + x + " y = " + y + " field = " + getField(x,y));
				if (isEven(x) && isEven(y) && getField(x,y) == 1) {
//					System.out.println("test columnsNumber = " + x +" "+isEven(x) + " y = " + y+ " " + isEven(y) + " field number " +getField(x,y));
					return false;
				}
			}
		}
		return true;
	}
	private int getField(int x, int y){
//		inX = x; inY = y;
		if (x < 0 || x >= columnsNumber || y < 0 || y >= rowsNumber) {
			return emptyMaze[y][x];
		}
//		System.out.println(x + " " + y + " getField result = " + emptyMaze[inX][inY]);
		return emptyMaze[y][x];
	}
	private void setField(int x, int y){
//		inX = x; inY = y;
//		System.out.println("call Set" );
		if (x < 0 || x >= columnsNumber || y < 0 || y >= rowsNumber) {
			return;
		}
		emptyMaze[y][x] = 0;
//		return 0;
	}
	private static int[] evenArray(int number) {
		return IntStream.rangeClosed(1, number/2).map(x -> x*2).toArray();
	}
	private int getRandomFrom (int[] array) {
		int index = (int) Math.floor(Math.random() * array.length);
//		System.out.println("test " + index);
		return array[index];
	}
	private boolean isEven (int n) {
		return n % 2 == 0;
	}
	private void moveTractor() {
//		System.out.println("call moveTractor " + x + " " + y);
//		int[] directs = new int[100];
		ArrayList<Integer> directs = new ArrayList<Integer>();

		if(x >0) directs.add(11);//[directs.length - 1] = 11; //left
		if (x < columnsNumber - 2) directs.add(22);//[directs.length - 1] = 22; //rigth
		if(y >0) directs.add(33);//[directs.length - 1] = 33; //up
		if (y < rowsNumber - 2) directs.add(44);//[directs.length - 1] = 44; //down
//		int[] Arr = new int[directs.size()];
//				Arr = directs.toArray(Arr);
		int[] arr = directs.stream().mapToInt(i -> i).toArray();
		int direct = getRandomFrom(arr);
//		System.out.println(Arrays.toString(arr) + "direct = " + direct);
		switch (direct){
			case 11:
				System.out.println("move left");
				if (getField(x - 2, y) == 1) {
					setField(x - 1, y);
					setField(x - 2, y);
				}
				x -= 2;
				break;
			case 22:
				System.out.println("move right");
				if (getField(x + 2, y) == 1) {
					setField(x + 1, y);
					setField(x + 2, y);
				}
				x += 2;
				break;
			case 33:
				System.out.println("move up");
				if (getField(x, y - 2) == 1) {
					setField(x, y - 1);
					setField(x, y - 2);
				}
				y -= 2;
				break;
			case 44:
				System.out.println("move down");
				if (getField(x, y + 2) == 1) {
					setField(x, y + 1);
					setField(x, y + 2);
				}
				y += 2;
				break;
		}

	}

}

/*class Tractor {
//	int x, y;

	public void moveTractor(int columnsNumber, int rowsNumber, int x, int y) {
		int[] directs = new int[4];
		if(x >0) directs[directs.length] = 11; //left
		if (x < columnsNumber - 2) directs[directs.length] = 22; //rigth
		if(y >0) directs[directs.length] = 33; //up
		if (y < rowsNumber - 2) directs[directs.length] = 44; //down
		};
}*/



