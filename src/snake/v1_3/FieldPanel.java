package snake.v1_3;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FieldPanel extends JPanel {
	private static int columns;
	private static int rows;

	private static JLabel[][] label;

	private static ImageIcon SNAKE_HEAD_W;
	private static ImageIcon SNAKE_HEAD_A;
	private static ImageIcon SNAKE_HEAD_S;
	private static ImageIcon SNAKE_HEAD_D;
	private static ImageIcon SNAKE_BODY_H;
	private static ImageIcon SNAKE_BODY_V;
	private static ImageIcon SNAKE_TURN_WD;
	private static ImageIcon SNAKE_TURN_DS;
	private static ImageIcon SNAKE_TURN_SA;
	private static ImageIcon SNAKE_TURN_AW;
	private static ImageIcon SNAKE_TAIL_W;
	private static ImageIcon SNAKE_TAIL_A;
	private static ImageIcon SNAKE_TAIL_S;
	private static ImageIcon SNAKE_TAIL_D;

	private static ImageIcon POISON;
	private static ImageIcon MOUSE;
	private static ImageIcon APPLE;
	private static ImageIcon FIELD;

	private void imageUpdater() {
		try {
			SNAKE_HEAD_W = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_head_w.png")));
			SNAKE_HEAD_A = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_head_a.png")));
			SNAKE_HEAD_S = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_head_s.png")));
			SNAKE_HEAD_D = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_head_d.png")));
			SNAKE_BODY_H = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_body_h.png")));
			SNAKE_BODY_V = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_body_v.png")));
			SNAKE_TURN_WD = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_turn_wd.png")));
			SNAKE_TURN_DS = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_turn_ds.png")));
			SNAKE_TURN_SA = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_turn_sa.png")));
			SNAKE_TURN_AW = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_turn_aw.png")));
			SNAKE_TAIL_W = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_tail_w.png")));
			SNAKE_TAIL_A = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_tail_a.png")));
			SNAKE_TAIL_S = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_tail_s.png")));
			SNAKE_TAIL_D = new ImageIcon(ImageIO.read(getClass().getResource("/images/snake_tail_d.png")));

			POISON = new ImageIcon(ImageIO.read(getClass().getResource("/images/poison.png")));
			MOUSE = new ImageIcon(ImageIO.read(getClass().getResource("/images/mouse.png")));
			APPLE = new ImageIcon(ImageIO.read(getClass().getResource("/images/apple.png")));
			FIELD = new ImageIcon(ImageIO.read(getClass().getResource("/images/field.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public FieldPanel(int rows, int columns) {

		imageUpdater();

		this.columns = columns;
		this.rows = rows;

		this.setLayout(new GridLayout(rows, columns));

		this.label = new JLabel[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				this.label[i][j] = new JLabel();
				this.label[i][j].setIcon(FIELD);

				this.add(label[i][j]);
			}
		}
	}

	@SuppressWarnings("static-access")
	public void update(LinkedList<int[]> snake, ArrayList<int[]> food, String lastMove) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				this.label[i][j].setIcon(FIELD);
			}
		}

		for (int i = 0; i < snake.size(); i++) {
			if (i == 0) {
				if (lastMove == "w") {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_HEAD_W);
				} else if (lastMove == "a") {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_HEAD_A);
				} else if (lastMove == "s") {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_HEAD_S);
				} else if (lastMove == "d") {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_HEAD_D);
				}
			} else if (i == snake.size() - 1) {
				if (snake.get(i - 1)[0] == snake.get(i)[0] && snake.get(i - 1)[1] == snake.get(i)[1] - 1) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TAIL_A);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] && snake.get(i - 1)[1] == snake.get(i)[1] + 1) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TAIL_D);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] + 1 && snake.get(i - 1)[1] == snake.get(i)[1]) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TAIL_S);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] - 1 && snake.get(i - 1)[1] == snake.get(i)[1]) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TAIL_W);
				}
			} else {
				if (snake.get(i - 1)[0] == snake.get(i)[0] && snake.get(i - 1)[1] == snake.get(i)[1] - 1
						&& snake.get(i + 1)[0] == snake.get(i)[0] && snake.get(i + 1)[1] == snake.get(i)[1] + 1) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_BODY_H);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] && snake.get(i - 1)[1] == snake.get(i)[1] + 1
						&& snake.get(i + 1)[0] == snake.get(i)[0] && snake.get(i + 1)[1] == snake.get(i)[1] - 1) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_BODY_H);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] - 1 && snake.get(i - 1)[1] == snake.get(i)[1]
						&& snake.get(i + 1)[0] == snake.get(i)[0] + 1 && snake.get(i + 1)[1] == snake.get(i)[1]) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_BODY_V);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] + 1 && snake.get(i - 1)[1] == snake.get(i)[1]
						&& snake.get(i + 1)[0] == snake.get(i)[0] - 1 && snake.get(i + 1)[1] == snake.get(i)[1]) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_BODY_V);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] + 1 && snake.get(i - 1)[1] == snake.get(i)[1]
						&& snake.get(i + 1)[0] == snake.get(i)[0] && snake.get(i + 1)[1] == snake.get(i)[1] + 1) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TURN_DS);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] + 1 && snake.get(i - 1)[1] == snake.get(i)[1]
						&& snake.get(i + 1)[0] == snake.get(i)[0] && snake.get(i + 1)[1] == snake.get(i)[1] - 1) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TURN_SA);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] && snake.get(i - 1)[1] == snake.get(i)[1] + 1
						&& snake.get(i + 1)[0] == snake.get(i)[0] + 1 && snake.get(i + 1)[1] == snake.get(i)[1]) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TURN_DS);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] && snake.get(i - 1)[1] == snake.get(i)[1] - 1
						&& snake.get(i + 1)[0] == snake.get(i)[0] + 1 && snake.get(i + 1)[1] == snake.get(i)[1]) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TURN_SA);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] - 1 && snake.get(i - 1)[1] == snake.get(i)[1]
						&& snake.get(i + 1)[0] == snake.get(i)[0] && snake.get(i + 1)[1] == snake.get(i)[1] + 1) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TURN_WD);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] - 1 && snake.get(i - 1)[1] == snake.get(i)[1]
						&& snake.get(i + 1)[0] == snake.get(i)[0] && snake.get(i + 1)[1] == snake.get(i)[1] - 1) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TURN_AW);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] && snake.get(i - 1)[1] == snake.get(i)[1] + 1
						&& snake.get(i + 1)[0] == snake.get(i)[0] - 1 && snake.get(i + 1)[1] == snake.get(i)[1]) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TURN_WD);
				} else if (snake.get(i - 1)[0] == snake.get(i)[0] && snake.get(i - 1)[1] == snake.get(i)[1] - 1
						&& snake.get(i + 1)[0] == snake.get(i)[0] - 1 && snake.get(i + 1)[1] == snake.get(i)[1]) {
					label[snake.get(i)[0]][snake.get(i)[1]].setIcon(SNAKE_TURN_AW);
				}
			}
		}

		for (int i = 0; i < food.size(); i++) {
			if (food.get(i)[0] == 2) {
				label[food.get(i)[1]][food.get(i)[2]].setIcon(APPLE);
			} else if (food.get(i)[0] == 3) {
				label[food.get(i)[1]][food.get(i)[2]].setIcon(MOUSE);
			} else if (food.get(i)[0] == 4) {
				label[food.get(i)[1]][food.get(i)[2]].setIcon(POISON);
			}
		}
	}
	// TESTER //
	/*
	 * public static void main(String[] args) { JFrame frame = new JFrame();
	 * FieldPanel label = new FieldPanel(20, 40); LinkedList<int[]> snake = new
	 * LinkedList<int[]>(); snake.add(new int[] {2, 1}); snake.add(new int[] {2,
	 * 2}); snake.add(new int[] {2, 3}); snake.add(new int[] {3, 3}); snake.add(new
	 * int[] {3, 4}); snake.add(new int[] {3, 5});
	 * 
	 * ArrayList<int[]> food = new ArrayList<int[]>(); food.add(new int[] {2, 5,
	 * 5}); food.add(new int[] {3, 5, 6}); food.add(new int[] {4, 5, 7});
	 * 
	 * String lastMove = "a";
	 * 
	 * frame.add(label); frame.setVisible(true);
	 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); frame.pack();
	 * 
	 * try { Thread.sleep(1000); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * label.update(snake, food, lastMove); }
	 */
}