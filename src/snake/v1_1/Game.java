package snake.v1_1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * <h1> Snake++</h1>
 * Snake++ is a simple snake game with added food items!
 * <p>
 * version 1.1 features three food items: the regular apple 
 * Mice and Poison. Normal game rules apply: Watch out for 
 * walls, don't eat yourself and score as many points as 
 * you can! 
 * 
 * @author 	Jessey Harrymanoharan
 * @version	1.1
 * @since 	2017-10-19 11:00:00
 */
public class Game {
	// Game GUI
	public static JFrame frame = new JFrame();
	public static JPanel panel = new JPanel();
	public static JTextPane areaField = new JTextPane();
	public static StyledDocument doc = areaField.getStyledDocument();
	public static KeyListener l;

	// Styles for field
	public static Style red = areaField.addStyle("red", null);
	public static Style green = areaField.addStyle("green", null);
	public static Style grey = areaField.addStyle("grey", null);
	public static Style magenta = areaField.addStyle("magenta", null);
	public static Style style = areaField.addStyle("Style", null);
	public static Style align = areaField.addStyle("align", null);
	
	// list to keep track of snake body positions
	public static LinkedList<int[]> snake = new LinkedList<int[]>();
	/**
	 * holds information about the food items on the board in the form {int state,
	 * int x, int y} where state represents the food item and x and y represent the
	 * position on the board
	 */
	public static ArrayList<int[]> food = new ArrayList<int[]>();

	// states of the game board
	public static final int EMPTY = 0;
	public static final int SNAKE = 1;
	public static final int APPLE = 2;
	public static final int MOUSE = 3;
	public static final int POISON = 4;

	// states of the game
	public static final int EATING_ERROR = -1;
	public static final int MOVE = 0;
	public static final int ATE_FOOD = 1;
	public static final int OUT_OF_BOUNDS = 2;
	public static final int ATE_BODY = 3;

	// field size information
	public static final int ROWS = 10;
	public static final int COLUMNS = 40;
	public static int[][] field = new int[ROWS][COLUMNS];

	// state variable holds the current state of the
	public static int state = MOVE;

	// holds the value of the last direction
	public static String lastMove = "";
	// holds the position of the last tail used for grow()
	public static int[] lastTail;

	// random object
	public static Random r = new Random();

	// time info
	public static int timer = 1000;
	public static int runTime = 0;
	private static int stepUp = 5000;

	// score value
	public static int score = 0;

	public static void main(String[] args) {
		initFrame();
		initMenu();
		initGame();
		draw();
		write("\nwasd or arrow keys to move", style);
		move();
		while (true) {
			state = eventHandler(snake.getFirst());
			if (state == MOVE) {
				updateField();
				draw();
				move();
				frame.pack();
			} else if (state == ATE_FOOD) {
				eat(snake.getFirst());
				updateField();
				draw();
				if (snake.size() == 0) {
					write("\nTHE SNAKE ATE POISON AND HAD\n", red);
					write("NO BODY PARTS TO GIVE UP!!\n", red);
					write("GAME OVER!", red);
					frame.pack();
					break;
				}
				move();
				frame.pack();
			} else if (state == OUT_OF_BOUNDS) {
				write("\nTHE SNAKE FELL OFF THE WORLD!!\n", red);
				write("GAME OVER!", red);
				frame.pack();
				break;
			} else if (state == ATE_BODY) {
				write("\nTHE SNAKE ATE ITSELF!!\n", red);
				write("GAME OVER!", red);
				frame.pack();
				break;
			} else {
				System.err.println("ERROR!, state: " + state);
				write("\nERROR! state: " + state, red);
				frame.pack();
				break;
			}
		}
	}

	private static void initMenu() {
		StyleConstants.setForeground(style, Color.green);
		StyleConstants.setFontSize(style, 24);
		write("\n\n", style);
		write("Snake++\n", style);

		StyleConstants.setForeground(style, Color.white);
		StyleConstants.setFontSize(style, 12);
		write("version 1.1\n", style);
		write("By: Jessey Harrymanoharan\n", style);
		write("\n", style);
		write("wasd or arrow keys to move", style);
		
		StyleConstants.setAlignment(align, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), align, false);
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		frame.pack();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		StyleConstants.setAlignment(align, StyleConstants.ALIGN_LEFT);
		doc.setParagraphAttributes(0, doc.getLength(), align, false);
	}

	/**
	 * initializes the frame and, it's components and other GUI functions adds the
	 * key listener to the frame
	 */
	private static void initFrame() {
		// add styles to the style objects
		StyleConstants.setForeground(red, Color.red);
		StyleConstants.setForeground(green, Color.green);
		StyleConstants.setForeground(grey, Color.gray);
		StyleConstants.setForeground(magenta, Color.magenta);
		areaField.setEditable(false);
		areaField.setBackground(Color.BLACK);
		areaField.setForeground(Color.WHITE);
		areaField.setFont(new Font("Courier New", Font.PLAIN, 12));
		areaField.setPreferredSize(new Dimension(300, 230));
		panel.add(areaField);
		panel.setBackground(Color.BLACK);
		frame.add(panel);
		frame.setFocusable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.addKeyListener(l = new KeyAdapter() {

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

	/**
	 * initializes the game <br>
	 * -adds values to the field <br>
	 * -creates the first apple
	 */
	public static void initGame() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				field[i][j] = EMPTY;
			}
		}

		snake.addFirst(new int[] { r.nextInt(ROWS), r.nextInt(COLUMNS) });
		field[snake.getFirst()[0]][snake.getFirst()[1]] = SNAKE;

		while (true) {
			int x = r.nextInt(ROWS);
			int y = r.nextInt(COLUMNS);

			if (field[x][y] == EMPTY) {
				food.add(new int[] { APPLE, x, y });
				field[x][y] = APPLE;
				break;
			}
		}
	}

	/**
	 * draws the field to the GUI should follow an updateField call
	 */
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
				} else if (field[i][j] == POISON) {
					write("P", magenta);
				} else if (field[i][j] == MOUSE) {
					write("M", grey);
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

	/**
	 * clears the document for the text pane
	 */
	private static void clearDoc() {
		try {
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e) {
		}
	}

	/**
	 * writes to the document for the text pane
	 * 
	 * @param s
	 *            the string you are adding to the doc
	 * @param style
	 *            the style used for the string
	 */
	private static void write(String s, Style style) {
		try {
			doc.insertString(doc.getLength(), s, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * handles the runtime elements of the game calls the updateSnake function to
	 * move the snake in the direction of the last pressed key
	 */
	private static void move() {
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
		// updateField();

		if (score >= 10 && (r.nextInt(40) == 0)) {
			addFood(MOUSE);
		}
		if (score >= 30 && (r.nextInt(50) == 0)) {
			addFood(POISON);
		}
	}

	private static void addFood(int f) {
		while (true) {
			int x = r.nextInt(ROWS);
			int y = r.nextInt(COLUMNS);

			if (field[x][y] == EMPTY) {
				food.add(new int[] { f, x, y });
				break;
			}
		}
	}

	/**
	 * 
	 * @param move
	 *            the last key pressed; represents the direction that the snake
	 *            moves
	 */
	public static void updateSnake(String move) {
		lastTail = snake.getLast();
		switch (move) {
		case "w":
			for (int i = snake.size() - 1; i >= 0; i--) {
				if (i == 0) {
					snake.set(i, new int[] { snake.get(i)[0] - 1, snake.get(i)[1] });
				} else {
					snake.set(i, snake.get(i - 1));
				}
			}
			break;
		case "a":
			for (int i = snake.size() - 1; i >= 0; i--) {
				if (i == 0) {
					snake.set(i, new int[] { snake.get(i)[0], snake.get(i)[1] - 1 });
				} else {
					snake.set(i, snake.get(i - 1));
				}
			}
			break;
		case "s":
			for (int i = snake.size() - 1; i >= 0; i--) {
				if (i == 0) {
					snake.set(i, new int[] { snake.get(i)[0] + 1, snake.get(i)[1] });
				} else {
					snake.set(i, snake.get(i - 1));
				}
			}
			break;
		case "d":
			for (int i = snake.size() - 1; i >= 0; i--) {
				if (i == 0) {
					snake.set(i, new int[] { snake.get(i)[0], snake.get(i)[1] + 1 });
				} else {
					snake.set(i, snake.get(i - 1));
				}
			}
			break;
		default:
			move();
			break;
		}
	}

	/**
	 * updates the contents of the field
	 */
	public static void updateField() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				field[i][j] = EMPTY;
			}
		}

		for (int i = 0; i < food.size(); i++) {
			field[food.get(i)[1]][food.get(i)[2]] = food.get(i)[0];
		}

		for (int i = 0; i < snake.size(); i++) {
			field[snake.get(i)[0]][snake.get(i)[1]] = SNAKE;
		}
	}

	/**
	 * determines the state of the game
	 * 
	 * @param head
	 *            the first element of the snake List
	 * @return the game state
	 */
	public static int eventHandler(int[] head) {
		if (head[0] < 0 || head[0] == ROWS || head[1] < 0 || head[1] == COLUMNS) {
			return OUT_OF_BOUNDS;
		} else if (field[head[0]][head[1]] == EMPTY) {
			return MOVE;
		} else {
			return eating(head);
		}
	}

	/**
	 * determines if and what the snake is eating and returns the respective state
	 * 
	 * @param head
	 *            the first element of the snake List
	 * @return a game state
	 */
	public static int eating(int[] head) {
		if (ateFood(head)) {
			return ATE_FOOD;
		} else if (ateBody(head)) {
			return ATE_BODY;
		} else if (head[0] == lastTail[0] && head[1] == lastTail[1]) {
			return MOVE;
		}
		return EATING_ERROR;
	}

	/**
	 * determines if the snake eats itself
	 * 
	 * @param head
	 *            the first element of the snake List
	 * @return true or false
	 */
	private static boolean ateBody(int[] head) {
		for (int i = snake.size() - 1; i > 0; i--) {
			if (head[0] == snake.get(i)[0] && head[1] == snake.get(i)[1]) {
				return true;
			}
		}
		return false;
	}

	private static boolean ateFood(int[] head) {
		if (field[head[0]][head[1]] == APPLE) {
			return true;
		} else if (field[head[0]][head[1]] == POISON) {
			return true;
		} else if (field[head[0]][head[1]] == MOUSE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * determines what food item the snake is eating and calls the appropriate
	 * function to handle it's removal
	 * 
	 * @param head
	 */
	public static void eat(int[] head) {
		int[] f;
		for (int i = 0; i < food.size(); i++) {
			f = food.get(i);
			if (f[1] == head[0] && f[2] == head[1]) {
				if (f[0] == APPLE) {
					eatApple(i);
				} else if (f[0] == MOUSE) {
					eatMouse(i);
				} else if (f[0] == POISON) {
					eatPoison(i);
				}
			}
		}
	}

	/**
	 * handler for when snake eats poison
	 * 
	 * @param i
	 *            the position of the poison on the list; used for removal
	 */
	private static void eatPoison(int i) {
		state = MOVE;
		score -= 10;

		food.remove(i);
		snake.removeLast();
		snake.removeLast();
	}

	/**
	 * handler for when snake eats a mouse
	 * 
	 * @param i
	 *            the position of the mouse on the list; used for removal
	 */
	private static void eatMouse(int i) {
		state = MOVE;
		score += 10;

		food.remove(i);
		snake.addLast(lastTail);
	}

	/**
	 * handler for when snake eats an apple
	 */
	public static void eatApple(int i) {
		state = MOVE;
		score++;

		snake.addLast(lastTail);

		food.remove(i);
		while (true) {
			int x = r.nextInt(ROWS);
			int y = r.nextInt(COLUMNS);

			if (field[x][y] == EMPTY) {
				food.add(new int[] { APPLE, x, y });
				break;
			}
		}
	}
}
