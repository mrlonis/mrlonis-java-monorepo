package com.mrlonis;

/**
 * This is the main entry point for the application. First, get the project to
 * work with a BST. After that, make it work for an AVL tree. Test by changing
 * the treeType to Constants.AVL.
 */

public class Driver {

	public static void main(String[] args) {
		System.out.println(Constants.TITLE);
		int treeType = Constants.AVL; // <== Change BST to AVL
		javax.swing.SwingUtilities.invokeLater(() -> new GUI(treeType));
	}
}