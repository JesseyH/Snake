package snake.v0;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class ConsoleGame {
	
	public static LinkedList<int[]> snake = new LinkedList<int[]>();
	public static int[] apple = new int[] {0, 0};
	
	public static final int EMPTY = 0;
	public static final int SNAKE = 1;
	public static final int APPLE = 2;
	
	public static final int MOVE = 0;
	public static final int GROW = 1;
	public static final int OUT_OF_BOUNDS = 2;
	public static final int ATE_BODY = 3;
	
	public static final int ROWS = 10;
	public static final int COLUMNS = 40;
	public static int[][] field = new int[ROWS][COLUMNS];
	public static Scanner in = new Scanner(System.in);
	
	public static int state = MOVE;
	
	public static String lastMove = "";
	public static int[] lastTail;
	
	public static Random r = new Random();
	
	public static void main(String[] args) {
		initGame();
		draw();
		move();
		while (true) { 
			state = eventHandler(snake.getFirst());
			if (state == MOVE) {
				updateField();
				draw();
				move();
			} else if (state == GROW) {
				grow();
				updateField();
				draw();
				move();
			} else if (state == OUT_OF_BOUNDS) {
				System.out.println("THE SNAKE FELL OFF THE WORLD!!");
				System.out.println("GAME OVER");
				break;
			} else if (state == ATE_BODY) {
				System.out.println("THE SNAKE ATE ITSELF!!");
				System.out.println("GAME OVER");
				break;
			}
		}
		
	}

	public static void initGame() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				field[i][j] = EMPTY;
			}
		}
		
		snake.addFirst(new int[] {r.nextInt(ROWS), r.nextInt(COLUMNS)});
		field[snake.getFirst()[0]][snake.getFirst()[1]] = SNAKE;
		
		while(true) {
			int x = r.nextInt(ROWS);
			int y = r.nextInt(COLUMNS);
			
			if (field[x][y] == EMPTY) {
				apple = new int[] {x, y};
				field[x][y] = APPLE;
				break;
			}
		}
	}
	
	private static void draw() {
		System.out.print("+");
		for (int i = 0; i < COLUMNS; i++) {
			System.out.print("-");
		}
		System.out.print("+");
		System.out.println();
		for (int i = 0; i < ROWS; i++) {
			System.out.print("|");
			for (int j = 0; j < COLUMNS; j++) {
				if (field[i][j] == SNAKE) {
					System.out.print("\033[32mS\033[37m"); 
						// \033[32mS\033[37m for Linux
				} else if (field[i][j] == APPLE) {
					System.out.print("\033[31mA\033[37m"); 
						// \033[31mA\033[37m for Linux
				} else {
					System.out.print("~");
				}
			}
			System.out.print("|");
			System.out.println();
		}
		System.out.print("+");
		for (int i = 0; i < COLUMNS; i++) {
			System.out.print("-");
		}
		System.out.print("+");
		System.out.println();
	}
	
	private static void move(){	
		System.out.print("Make your move! (wasd) :");
		String move = in.next();
		lastMove = move;
		updateSnake(move);
	}
	
	public static void updateSnake(String move) {
		lastTail = snake.getLast();
		switch (move) {
		case "w": 	for (int i=snake.size()-1; i >= 0; i--) {
						if (i == 0) {
							snake.set(i,new int[] {snake.get(i)[0] - 1, snake.get(i)[1]});
						} else {
							snake.set(i, snake.get(i -1));
						} 
					}
					break;
		case "a": 	for (int i=snake.size()-1; i >= 0; i--) {
						if (i == 0) {
							snake.set(i,new int[] {snake.get(i)[0], snake.get(i)[1] - 1});
						} else {
							snake.set(i, snake.get(i -1));
						}
					}
					break;
		case "s": 	for (int i=snake.size()-1; i >= 0; i--) {
						if (i == 0) {
							snake.set(i,new int[] {snake.get(i)[0] + 1, snake.get(i)[1]});
						} else {
							snake.set(i, snake.get(i -1));
						}
					}
					break;
		case "d": 	for (int i=snake.size()-1; i >= 0; i--) {
						if (i == 0) {
							snake.set(i,new int[] {snake.get(i)[0], snake.get(i)[1] + 1});
						} else {
							snake.set(i, snake.get(i -1));
						}
					}
					break;
		default:	System.out.println("invalid input try again");
					move();
					break;
		}
	}
	
	public static void updateField() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				field[i][j] = EMPTY;
			}
		}
		
		field[apple[0]][apple[1]] = APPLE;
		for (int i=0; i < snake.size(); i++) {
			field[snake.get(i)[0]][snake.get(i)[1]] = SNAKE;
		}
	}
	
	public static int eventHandler(int[] head){
		if (head[0] < 0 || head[0] == ROWS || head[1] < 0 || head[1] == COLUMNS) {
			return OUT_OF_BOUNDS;
		} else if (head[0] == apple[0] && head[1] == apple[1]) {
			return GROW;
		} else if (ateBody(head)) {
			return ATE_BODY;
		} else {
			return MOVE;
		}
	}

	private static boolean ateBody(int[] head) {
		for (int i=snake.size()-1; i > 0; i--) {
			if (head[0] == snake.get(i)[0] && head[1] == snake.get(i)[1]) {
				return true;
			}
		}
		return false;
	}
	
	public static void grow() {
		snake.addLast(lastTail);
		while(true) {
			int x = r.nextInt(ROWS);
			int y = r.nextInt(COLUMNS);
			
			if (field[x][y] == EMPTY) {
				apple = new int[] {x, y};
				break;
			}
		}
	}
}