package snake.v0;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GUIWindows {
	public static JFrame frame = new JFrame();
	public static JPanel panel = new JPanel();
	public static JTextPane areaField = new JTextPane();
	public static StyledDocument doc = areaField.getStyledDocument();
	
	public static Style red = areaField.addStyle("red", null);
	public static Style green = areaField.addStyle("green", null);
	
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
	
	public static int timer = 1000;
	public static int runTime = 0;
	private static int stepUp = 5000;
	
	public static int score = 0;
	
	public static void main(String[] args) {
		initFrame();
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
				write("\nTHE SNAKE FELL OFF THE WORLD!!\n", red);
				write("GAME OVER!", red);
				System.out.println("THE SNAKE FELL OFF THE WORLD!!");
				System.out.println("GAME OVER");
				frame.pack();
				break;
			} else if (state == ATE_BODY) {
				write("\nTHE SNAKE ATE ITSELF!!\n", red);
				write("GAME OVER!", red);
				System.out.println("THE SNAKE ATE ITSELF!!");
				System.out.println("GAME OVER");
				frame.pack();
				break;
			}
		}
		
	}

	private static void initFrame() {
	    StyleConstants.setForeground(red, Color.red);
	    StyleConstants.setForeground(green, Color.green);
		//areaField.setPreferredSize(new Dimension(400, 200));
		areaField.setEditable(false);
		areaField.setBackground(Color.BLACK);
		areaField.setForeground(Color.WHITE);
		areaField.setFont(new Font("Courier New", Font.PLAIN, 12));
		panel.add(areaField);
		panel.setBackground(Color.BLACK);
		frame.add(panel);
		frame.setFocusable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.pack();
		frame.addKeyListener(new KeyAdapter(){

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
					lastMove = "w";
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
					lastMove = "a";
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
					lastMove = "s";
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
					lastMove = "d";
				}
			}
		});
		frame.setVisible(true);
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
		clearDoc();
		write("score: " + score + "\n", null);
		write("+", null);
		for (int i = 0; i < COLUMNS; i++) {
			write("-", null);
		}
		write("+\n", null);
		for (int i = 0; i < ROWS; i++) {
			write("|", null);
			for (int j = 0; j < COLUMNS; j++) {
				if (field[i][j] == SNAKE) {
					write("S", green);
				} else if (field[i][j] == APPLE) {
					write("A", red);
				} else {
					write("~", null);
				}
			}
			write("|\n", null);
		}
		write("+", null);
		for (int i = 0; i < COLUMNS; i++) {
			write("-", null);
		}
		write("+", null);
		frame.pack();
	}
	
	private static void clearDoc() {
		try {
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e) {
		}
	}
	
	private static void write(String s, Style style) {
		try {
			doc.insertString(doc.getLength(), s, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private static void move(){	
		try {
			Thread.sleep(timer);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		runTime += timer;
		if (runTime > stepUp) {
			timer *= 0.9;
			stepUp += 5000; 
		}
		updateSnake(lastMove);
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
		default:	move();
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
		state = MOVE;
		score ++;
		
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
