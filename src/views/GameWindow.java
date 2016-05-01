/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.GameWindowController;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author User
 */
public class GameWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Board square is empty
	 */
	public static final int SQUARE_EMPTY = 0;

	/**
	 * Board square contains a piece with white side up
	 */
	public static final int SQUARE_WHITE = 1;

	/**
	 * Board square contains a piece with black side up
	 */
	public static final int SQUARE_BLACK = 2;

	/**
	 * Board square is a valid option for a white piece
	 */
	public static final int SQUARE_WHITE_OPTION = 3;

	/**
	 * Board square is a valid option for a black piece
	 */
	public static final int SQUARE_BLACK_OPTION = 4;

	/**
	 * Number of available states SQUARE_NUMBER_OF_STATES
	 */
	public static final int SQUARE_NUMBER_OF_STATES = 5;

	/**
	 * Size of the Reversi Board
	 */
	public static final int BOARD_SIZE = 8;

	private final int ICON_SIZE = 44;
	private final ImageIcon iconEmpty;
	private final ImageIcon iconWhite;
	private final ImageIcon iconBlack;
	private final ImageIcon iconWhiteOption;
	private final ImageIcon iconBlackOption;

	private final GameWindowController controller;

	// GUI components
	private JButton jButtonStartGame;
	@SuppressWarnings("rawtypes")
	private JComboBox jComboBox;
	private JLabel jLabelStatusMessage;
	private JLabel jLabelScoreBlack;
	private JLabel jLabelScoreWhite;
	private JPanel jPanelScores;
	private JPanel jPanelBoard;
	private final JButton buttonGrid[][];
	private JPanel jPanelCombo;
	private JPanel jPanelStatus;

	
	/**
	 * GameWindow Builder
	 * 
	 * @param title
	 * 			is the title of the window.
	 * @param controller
	 * 			is the assigned controller.
	 * 
	 */
	public GameWindow(String title, GameWindowController controller) {
		// Configure JFrame title
		super(title);

		// Store controller
		this.controller = controller;

		// Load Icons
		this.iconEmpty = new ImageIcon(getClass().getResource("/ressources/Reversi_Empty.png"));
		this.iconWhite = new ImageIcon(getClass().getResource("/ressources/Reversi_White.png"));
		this.iconBlack = new ImageIcon(getClass().getResource("/ressources/Reversi_Black.png"));
		this.iconWhiteOption = new ImageIcon(getClass().getResource("/ressources/Reversi_WhiteOption.png"));
		this.iconBlackOption = new ImageIcon(getClass().getResource("/ressources/Reversi_BlackOption.png"));

		// Initialize most of the GUI components
		initComponents(); // code generated with builder

		// Finalize JFrame setup
		super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		super.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(windowEvent.getComponent(), "Are you sure you want to quit ?", "Exit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					controller.exit();
				}
			}
		});

		// Add action listener to [Start Game] button
		jButtonStartGame.addActionListener((java.awt.event.ActionEvent evt) -> {
			startButtonActionPerformed(evt);
		});

		// Create board JPanel (button grid)
		jPanelBoard.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
		jPanelBoard.setSize((BOARD_SIZE * (ICON_SIZE + 1)), (BOARD_SIZE * (ICON_SIZE + 4)));
		this.buttonGrid = new JButton[BOARD_SIZE][BOARD_SIZE];
		
		JButton button;
		
		int count = 0;
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				button = new JButton();
				buttonGrid[i][j] = button;
				button.setName("" + count++); // name = button number
				button.setBorder(null);
				button.setBorderPainted(false);
				button.setMargin(new Insets(0, 0, 0, 0));
				button.setBorderPainted(false);
				button.setContentAreaFilled(false);
				button.setOpaque(true);
				button.setIcon(iconEmpty);
				button.setFocusPainted(false);
				button.addActionListener((java.awt.event.ActionEvent evt) -> {
					boardButtonActionPerformed(evt);
				});

				jPanelBoard.add(button);
			}
		}

		super.pack();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanelStatus = new JPanel();
		jLabelStatusMessage = new JLabel();
		jPanelCombo = new JPanel();
		jComboBox = new JComboBox();
		jButtonStartGame = new JButton();
		jPanelScores = new JPanel();
		jLabelScoreBlack = new JLabel();
		jLabelScoreWhite = new JLabel();
		jPanelBoard = new JPanel();

		setResizable(false);

		jPanelStatus.setBackground(new java.awt.Color(0, 158, 11));

		jLabelStatusMessage.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelStatusMessage.setText("...");

		GroupLayout jPanelStatusLayout = new GroupLayout(jPanelStatus);
		jPanelStatus.setLayout(jPanelStatusLayout);
		jPanelStatusLayout.setHorizontalGroup(jPanelStatusLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanelStatusLayout
						.createSequentialGroup().addContainerGap().addComponent(jLabelStatusMessage,
								GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		jPanelStatusLayout.setVerticalGroup(jPanelStatusLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING,
						jPanelStatusLayout.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabelStatusMessage).addGap(122, 122, 122)));

		jComboBox.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

		jButtonStartGame.setText("Start Game");

		GroupLayout jPanelComboLayout = new GroupLayout(jPanelCombo);
		jPanelCombo.setLayout(jPanelComboLayout);
		jPanelComboLayout.setHorizontalGroup(jPanelComboLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanelComboLayout.createSequentialGroup().addContainerGap()
						.addComponent(jComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(18, 18, 18)
						.addComponent(jButtonStartGame).addContainerGap()));
		jPanelComboLayout.setVerticalGroup(jPanelComboLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanelComboLayout.createSequentialGroup().addContainerGap()
						.addGroup(jPanelComboLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(jComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(jButtonStartGame))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jLabelScoreBlack.setText("Score of Black is : 0");
		jLabelScoreBlack.setPreferredSize(new java.awt.Dimension(150, 14));
		jLabelScoreBlack.setOpaque(true);
		jLabelScoreBlack.setBackground(Color.BLACK);
		jLabelScoreBlack.setForeground(Color.LIGHT_GRAY);

		jLabelScoreWhite.setText("Score of White is : 0");
		jLabelScoreWhite.setPreferredSize(new java.awt.Dimension(150, 14));
		jLabelScoreWhite.setOpaque(true);
		jLabelScoreWhite.setBackground(Color.WHITE);
		jLabelScoreWhite.setForeground(Color.GRAY);

		GroupLayout jPanelScoresLayout = new GroupLayout(jPanelScores);
		jPanelScores.setLayout(jPanelScoresLayout);
		jPanelScoresLayout.setHorizontalGroup(jPanelScoresLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanelScoresLayout.createSequentialGroup().addContainerGap()
						.addComponent(jLabelScoreBlack, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(jLabelScoreWhite, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		jPanelScoresLayout.setVerticalGroup(jPanelScoresLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, jPanelScoresLayout.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(jPanelScoresLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(jLabelScoreBlack, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabelScoreWhite, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));

		jPanelBoard.setPreferredSize(new java.awt.Dimension(352, 352));

		GroupLayout jPanelBoardLayout = new GroupLayout(jPanelBoard);
		jPanelBoard.setLayout(jPanelBoardLayout);
		jPanelBoardLayout.setHorizontalGroup(
				jPanelBoardLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 352, Short.MAX_VALUE));
		jPanelBoardLayout.setVerticalGroup(
				jPanelBoardLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 352, Short.MAX_VALUE));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jPanelStatus)
				.addComponent(jPanelCombo)
				.addComponent(jPanelScores, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanelBoard, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								layout.createSequentialGroup()
										.addComponent(jPanelStatus, GroupLayout.PREFERRED_SIZE, 40,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jPanelCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jPanelScores, GroupLayout.PREFERRED_SIZE, 33,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jPanelBoard, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * 
	 * @param ae
	 */
	private void boardButtonActionPerformed(ActionEvent ae) {
		// Cast the ae source to JButton
		JButton buttonSourceEvent = (JButton) ae.getSource();
		// Compute the button coordinates
		int num = Integer.parseInt(buttonSourceEvent.getName());

		int row = num / BOARD_SIZE;
		int col = num % BOARD_SIZE;

		// Update controller
		controller.squareClicked(row, col);
	}

	
	/**
	 * 
	 * @param ae
	 */
	private void startButtonActionPerformed(ActionEvent ae) {
		controller.startGame();
	}

	/**
	 * Sets the content of a square (row, col) to state. Coordinates starts at 0
	 * and must be less than BOARD_SIZE. State is one of SQUARE_EMPTY,
	 * SQUARE_WHITE, SQUARE_BLACK, SQUARE_WHITE_OPTION, SQUARE_BLACK_OPTION.
	 * 
	 * @param row
	 *            square's row index
	 * @param col
	 *            square's column index
	 * @param state
	 *            square's state
	 */
	public void setSquareState(int row, int col, int state) {
		if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
			throw new IllegalArgumentException("Square coordinates are outside of board limits.");
		}
		switch (state) {
		case SQUARE_EMPTY:
			buttonGrid[row][col].setIcon(iconEmpty);
			break;
		case SQUARE_WHITE:
			buttonGrid[row][col].setIcon(iconWhite);
			break;
		case SQUARE_BLACK:
			buttonGrid[row][col].setIcon(iconBlack);
			break;
		case SQUARE_WHITE_OPTION:
			buttonGrid[row][col].setIcon(iconWhiteOption);
			break;
		case SQUARE_BLACK_OPTION:
			buttonGrid[row][col].setIcon(iconBlackOption);
			break;
		default:
			throw new IllegalArgumentException("Invalid square state.");
		}
	}

	/**
	 * Sets the content of the combo box listing available games.
	 * 
	 * @param games
	 *            array of String with the description of the games
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setGamesList(String[] games) {
		if (games != null && games.length > 0) {
			jComboBox.setModel(new DefaultComboBoxModel(games));
			jComboBox.setSelectedIndex(0);
		}
	}

	/**
	 * Sets the index of the selected item in the game list.
	 * 
	 * @param index
	 *            the index of the selected item
	 */
	public void setSelectedGame(int index) {
		if (index >= 0 && index < jComboBox.getItemCount()) {
			jComboBox.setSelectedIndex(index);
		}
	}

	/**
	 * Returns the index of the selected item in the game list.
	 * 
	 * @return index of the selected item
	 */
	public int getSelectedGame() {
		return jComboBox.getSelectedIndex();
	}

	/**
	 * Sets the text displayed in the status area.
	 * 
	 * @param text
	 *            the text to display
	 */
	public void setStatusMessage(String text) {
		if (text != null)
			jLabelStatusMessage.setText(text);
	}

	/**
	 * Sets the background color of the status area.
	 * 
	 * @param color
	 *            the background color
	 */
	public void setStatusColor(Color color) {
		jPanelStatus.setBackground(color);
	}

	/**
	 * Sets the text displayed in the black player status area.
	 * 
	 * @param text
	 *            the text to display
	 */
	public void setBlackState(String text) {
		if (text != null)
			jLabelScoreBlack.setText(text);
	}

	/**
	 * Sets the text displayed in the white player status area.
	 * 
	 * @param text
	 *            the text to display
	 */
	public void setWhiteState(String text) {
		if (text != null)
			jLabelScoreWhite.setText(text);
	}

	/**
	 * Enables/disables the game selection area (combo + button).
	 * 
	 * @param value
	 *            true = disable / false = enable
	 */
	public void setToDisabled(boolean value) {
		jComboBox.setEnabled(!value);
		jButtonStartGame.setEnabled(!value);
	}
}
