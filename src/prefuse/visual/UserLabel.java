package prefuse.visual;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JLabel;
import prefuse.Display;

/**
 * <p>UserLabel provides to users adding multiples labels to comment a graph.<p>
 * <p>The tips to refresh theses items independently of prefuse system with a
 * timer which redraw each label<p>
 *
 * @version 1.0
 * @author <a href="http://maxime-escourbiac.fr//">Maxime Escourbiac</a>
 */
public class UserLabel {

    private static ArrayList<UserLabel> UserLabels;
    private static Boolean synchronised = true;
    private Font font;
    private FontMetrics fontMetrics;
    private static Display mainDisplay;
    private JLabel label;
    private Point2D origin;

    private UserLabel(String textLabel, String textErase, String textRename, String textRenameQuestionTitle, String textRenameQuestion, int x, int y, Display display) {

        font = new Font(null, Font.BOLD, 14);
        label = new JLabel(textLabel);
        mainDisplay = display;
        this.origin = mainDisplay.getAbsoluteCoordinate(new Point2D.Float(x, y), null);
        fontMetrics = mainDisplay.getFontMetrics(font);

        label.setLocation(x, y);
        label.setFont(font);
        label.setSize(computeTextSize(textLabel) + 5, 20); //Similar than computeLabelSize Method.
        label.setBackground(Color.white);
        label.setOpaque(true);
        label.setVisible(true);
        label.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point locationOnScreen = mainDisplay.getLocationOnScreen();
                int x = e.getXOnScreen() - locationOnScreen.x;
                int y = e.getYOnScreen() - locationOnScreen.y;

                //Test if the label is located in the main display.
                if (x > 0 && x < mainDisplay.getWidth() - 10 && y > 0 && y < mainDisplay.getHeight() - label.getHeight()) {
                    label.setLocation(x, y);
                    mainDisplay.repaint();
                    DrawLabels();
                    mainDisplay.code_repaint = 0;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        label.addMouseListener(new UserLabelInteraction(this, textErase, textRename, textRenameQuestionTitle, textRenameQuestion));

        mainDisplay.add(label);
        mainDisplay.repaint();
        mainDisplay.validate();
        if (UserLabels == null) {
            UserLabels = new ArrayList<UserLabel>();
        }

    }

    /**
     * Create an user label
     *
     * @param textLabel Text of label
     * @param textErase Text into the erase button
     * @param textRename Text into rename button
     * @param textRenameQuestionTitle Title of the inputbox for renanimg the
     * label
     * @param textRenameQuestion Message contained into the renaming box
     * @param x position of label
     * @param y position of label
     * @param display Container of label
     * @return The user label created
     */
    public static UserLabel createUserLabel(String textLabel, String textErase, String textRename, String textRenameQuestionTitle, String textRenameQuestion, int x, int y, Display display) {
        UserLabel userLabel = new UserLabel(textLabel, textErase, textRename, textRenameQuestionTitle, textRenameQuestion, x, y, display);
        UserLabels.add(userLabel);
        return userLabel;
    }

    /**
     * Draw all the available label
     */
    public static void DrawLabels() {
        if (UserLabels != null && synchronised == true) {
            if (UserLabels.size() > 0) {
                synchronised = false;
                Point2D originDisplays[] = new Point2D[UserLabels.size()];
                Point2D labelCenters[] = new Point2D[UserLabels.size()];
                for (int i = 0; i < UserLabels.size(); ++i) {
                    originDisplays[i] = UserLabels.get(i).getOriginScreenCoordinate();
                    labelCenters[i] = UserLabels.get(i).computeImpactPoint();
                }

                Graphics graphics = mainDisplay.getGraphics();
                for (int i = 0; i < UserLabels.size(); ++i) {
                    graphics.drawLine((int) labelCenters[i].getX(), (int) labelCenters[i].getY(), (int) originDisplays[i].getX(), (int) originDisplays[i].getY());
                }
                synchronised = true;
            }
        }
    }

    /**
     * Get all the user label available
     *
     * @return The arraylist contains all user label available
     */
    public static ArrayList<UserLabel> getUserLabels() {
        return UserLabels;
    }

    /**
     * Remove all user label
     */
    public static void ClearUserLabels() {
        if (UserLabels != null) {
            UserLabels.clear();
        }
    }

    /**
     * Remove a specific UserLabel
     *
     * @param label User label to remove
     */
    public static void RemoveUserLabel(UserLabel label) {
        UserLabels.remove(label);
    }

    /**
     * Tests if this list has no elements.
     *
     * @return True if the userlabel's list has no element, false otherwise
     */
    public static boolean isEmpty() {
        return (UserLabels == null) ? true : UserLabels.isEmpty();
    }

    /**
     * Compute the impact point between the line and the label
     *
     * @return impact point computed
     */
    public Point2D computeImpactPoint() {
        Point2D impactPoint;
        Point2D originDisplay = getOriginScreenCoordinate();
        double h = (double) label.getHeight();
        double w = (double) label.getWidth();
        double tempY, tempX;
        Point2D labelCenter = new Point2D.Double((double) label.getX() + w / 2., (double) label.getY() + h / 2.);
        double delta = (originDisplay.getY() - labelCenter.getY()) / (originDisplay.getX() - labelCenter.getX());

        if (Math.abs(delta * w) > h) {
            tempX = ((labelCenter.getX() - originDisplay.getX()) > 0.) ? -h / (2. * Math.abs(delta)) : h / (2. * Math.abs(delta));
            tempY = ((labelCenter.getY() - originDisplay.getY()) > 0.) ? -h / 2. : h / 2.;
            impactPoint = new Point2D.Double(labelCenter.getX() + tempX, labelCenter.getY() + tempY);
        } else {
            tempX = ((labelCenter.getX() - originDisplay.getX()) > 0.) ? -w / 2. : w / 2.;
            tempY = ((labelCenter.getY() - originDisplay.getY()) > 0.) ? -Math.abs(delta) * w / 2. : (Math.abs(delta) * w / 2.);
            impactPoint = new Point2D.Double(labelCenter.getX() + tempX, labelCenter.getY() + tempY);
        }
        return impactPoint;
    }

    /**
     * Transform the original coordinate into onscreen coordinate
     *
     * @return onscreen coordinate of the origin
     */
    public Point2D getOriginScreenCoordinate() {
        return mainDisplay.getScreenCoordinate(origin, null);
    }

    /**
     * Compute and set the corrct size of the label. The size is depending of
     * many parameters like Text font and text size. This method may be called
     * after changing the text of the label.
     */
    public void computeLabelSize() {
        label.setSize(computeTextSize(label.getText()) + 5, 20);
    }

    /**
     * JLabel used by user label
     *
     * @return JLabel used by user label
     */
    public JLabel getLabel() {
        return label;
    }

    private int computeTextSize(String str) {
        int size = 0;
        char text[] = str.toCharArray();
        for (char c : text) {
            size += fontMetrics.charWidth(c);
        }
        return size;
    }
}
