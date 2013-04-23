package prefuse.visual;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * Class implement the interaction between UserLabels and Prefuse
 *
 * @version 1.0
 * @author <a href="http://maxime-escourbiac.fr//">Maxime Escourbiac</a>
 */
public class UserLabelInteraction implements MouseListener {

    private JPopupMenu menu;
    private final UserLabel userLabel; //Necessary for showing correctly the menu

    /**
     * UserLabelInteraction class constructor
     *
     * @param userLabel Label which will interact with prefuse
     * @param deleteName String which will display in "erase" button
     * @param renameName String which will display in "rename" button
     * @param renameQuestionTitle Title of the rename box
     * @param renameQuestion Message of the rename box
     *
     */
    public UserLabelInteraction(final UserLabel userLabel, String deleteName, String renameName, final String renameQuestionTitle, final String renameQuestion) {
        this.userLabel = userLabel;
        menu = new JPopupMenu();
        menu.setSize(50, 20);
        JMenuItem item;
        //Add rename button
        item = new JMenuItem(renameName);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newText = (String) JOptionPane.showInputDialog(menu, renameQuestion, renameQuestionTitle, JOptionPane.QUESTION_MESSAGE, null, null, userLabel.getLabel().getText());
                if (newText != null) {
                    userLabel.getLabel().setText(newText);
                    userLabel.computeLabelSize();
                }
            }
        });
        menu.add(item);
        //Add a delete label button
        item = new JMenuItem(deleteName);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userLabel.getLabel().setVisible(false);
                UserLabel.RemoveUserLabel(userLabel);
            }
        });
        menu.add(item);
    }

    /**
     * Interaction with a right click event
     *
     * @param e Mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 3) {
            JLabel label = userLabel.getLabel();
            menu.show(label, label.getWidth(), label.getHeight());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
