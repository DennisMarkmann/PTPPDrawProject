package mydraw;

// This example is from _Java Examples in a Nutshell_. (http://www.oreilly.com)
// Copyright (c) 1997 by David Flanagan
// This example is provided WITHOUT ANY WARRANTY either expressed or implied.
// You may study, use, modify, and distribute it for non-commercial purposes.
// For any commercial use, see http://www.davidflanagan.com/javaexamples

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/** The application class. Processes high-level commands sent by GUI */
public class Draw {

    /** main entry point. Just create an instance of this application class */
    public static void main(String[] args) {
        new Draw();
    }

    protected DrawGUI window;

    /** Application constructor: create an instance of our GUI class */
    public Draw() {
        this.window = new DrawGUI(this);
    }

    public void autoDraw() {
        // TODO autoDraw
    }

    public void clear() {
        // TODO clear
    }

    private Color convertColorNameToColor(String colorString) throws ColorException {
        if (colorString.equals("Black")) {
            return Color.black;
        }
        else if (colorString.equals("Green")) {
            return Color.green;
        }
        else if (colorString.equals("Red")) {
            return Color.red;
        }
        else if (colorString.equals("Blue")) {
            return Color.blue;
        }
        else if (colorString.equals("White")) {
            return Color.white;
        }
        else {
            throw new ColorException();
        }
    }

    /** This is the application method that processes commands sent by the GUI */
    public void doCommand(String command) {
        if (command.equals("clear")) { // clear the GUI window
            // It would be more modular to include this functionality in the GUI
            // class itself. But for demonstration purposes, we do it here.
            Graphics g = this.window.getGraphics();
            g.setColor(this.window.getBackground());
            g.fillRect(0, 0, this.window.getSize().width, this.window.getSize().height);
        }
        else if (command.equals("quit")) { // quit the application
            this.window.dispose(); // close the GUI
            System.exit(0); // and exit.
        }
    }

    public void drawOval(Point upper_left, Point lower_right) {
        // TODO drawOval
    }

    public void drawPolyLine(java.util.List<Point> points) {
        // TODO drawPolyline
    }

    public void drawRectangle(Point upper_left, Point lower_right) {
        // TODO drawRectangle
    }

    public String getBGColor() {
        return (this.window.getBackground()).toString();
    }

    public Image getDrawing() {
        return null;
        // TODO getDrawing
    }

    public String getFGColor() {
        return (this.window.color).toString();
    }

    public int getHeight() {
        return this.window.getHeight();
    }

    public int getWidth() {
        return this.window.getWidth();
    }

    public Image readImage(String filename) throws IOException {
        return null;
        // TODO readImage
    }

    public void setBGColor(String new_color) throws ColorException {
        Color color = this.convertColorNameToColor(new_color);
        this.window.setBackground(color);
    }

    public void setFGColor(String new_color) throws ColorException {
        if (new_color.equals("white")) {
            throw new ColorException("White is not an acceptabe color here.");
        }
        Color color = this.convertColorNameToColor(new_color);
        this.window.color = color;
    }

    public void setHeight(int height) {
        this.window.setSize(this.window.getWidth(), height);
    }

    public void setWidth(int width) {
        this.window.setSize(width, this.window.getHeight());
    }

    public void writeImage(Image img, String filename) throws IOException {
        // TODO writeImage
    }
}

/** This class implements the GUI for our application */
class DrawGUI extends Frame {

    private static final long serialVersionUID = -3535662418210773735L;
    Draw app; // A reference to the application, to send commands to.
    Color color;

    /**
     * The GUI constructor does all the work of creating the GUI and setting up event listeners. Note the use of local and
     * anonymous classes.
     */
    @SuppressWarnings("deprecation")
    public DrawGUI(Draw application) {
        super("Draw"); // Create the window
        this.app = application; // Remember the application reference
        this.color = Color.black; // the current drawing color

        // selector for drawing modes
        Choice shape_chooser = new Choice();
        shape_chooser.add("Scribble");
        shape_chooser.add("Rectangle");
        shape_chooser.add("Oval");

        // selector for drawing colors
        Choice color_chooser = new Choice();
        color_chooser.add("Black");
        color_chooser.add("Green");
        color_chooser.add("Red");
        color_chooser.add("Blue");

        // Create two buttons
        Button clear = new Button("Clear");
        Button quit = new Button("Quit");

        // Set a LayoutManager, and add the choosers and buttons to the window.
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        this.add(new Label("Shape:"));
        this.add(shape_chooser);
        this.add(new Label("Color:"));
        this.add(color_chooser);
        this.add(clear);
        this.add(quit);

        // Here's a local class used for action listeners for the buttons
        class DrawActionListener implements ActionListener {

            private String command;

            public DrawActionListener(String cmd) {
                this.command = cmd;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                DrawGUI.this.app.doCommand(this.command);
            }
        }

        // Define action listener adapters that connect the buttons to the app
        clear.addActionListener(new DrawActionListener("clear"));
        quit.addActionListener(new DrawActionListener("quit"));

        // this class determines how mouse events are to be interpreted,
        // depending on the shape mode currently set
        class ShapeManager implements ItemListener {

            // if this class is active, ovals are drawn
            class OvalDrawer extends RectangleDrawer {

                @Override
                public void doDraw(int x0, int y0, int x1, int y1, Graphics g) {
                    int x = Math.min(x0, x1);
                    int y = Math.min(y0, y1);
                    int w = Math.abs(x1 - x0);
                    int h = Math.abs(y1 - y0);
                    // draw oval instead of rectangle
                    g.drawOval(x, y, w, h);
                }
            }

            // if this class is active, rectangles are drawn
            class RectangleDrawer extends ShapeDrawer {

                int pressx, pressy;
                int lastx = -1, lasty = -1;

                public void doDraw(int x0, int y0, int x1, int y1, Graphics g) {
                    // calculate upperleft and width/height of rectangle
                    int x = Math.min(x0, x1);
                    int y = Math.min(y0, y1);
                    int w = Math.abs(x1 - x0);
                    int h = Math.abs(y1 - y0);
                    // draw rectangle
                    g.drawRect(x, y, w, h);
                }

                // mouse released => temporarily set second corner of rectangle
                // draw the resulting shape in "rubber-band mode"
                @Override
                public void mouseDragged(MouseEvent e) {
                    Graphics g = ShapeManager.this.gui.getGraphics();
                    // these commands set the rubberband mode
                    g.setXORMode(ShapeManager.this.gui.color);
                    g.setColor(ShapeManager.this.gui.getBackground());
                    if (this.lastx != -1) {
                        // first undraw previous rubber rect
                        this.doDraw(this.pressx, this.pressy, this.lastx, this.lasty, g);

                    }
                    this.lastx = e.getX();
                    this.lasty = e.getY();
                    // draw new rubber rect
                    this.doDraw(this.pressx, this.pressy, this.lastx, this.lasty, g);
                }

                // mouse pressed => fix first corner of rectangle
                @Override
                public void mousePressed(MouseEvent e) {
                    this.pressx = e.getX();
                    this.pressy = e.getY();
                }

                // mouse released => fix second corner of rectangle
                // and draw the resulting shape
                @Override
                public void mouseReleased(MouseEvent e) {
                    Graphics g = ShapeManager.this.gui.getGraphics();
                    if (this.lastx != -1) {
                        // first undraw a rubber rect
                        g.setXORMode(ShapeManager.this.gui.color);
                        g.setColor(ShapeManager.this.gui.getBackground());
                        this.doDraw(this.pressx, this.pressy, this.lastx, this.lasty, g);
                        this.lastx = -1;
                        this.lasty = -1;
                    }
                    // these commands finish the rubberband mode
                    g.setPaintMode();
                    g.setColor(ShapeManager.this.gui.color);
                    // draw the finel rectangle
                    this.doDraw(this.pressx, this.pressy, e.getX(), e.getY(), g);
                }
            }

            // if this class is active, the mouse is interpreted as a pen
            class ScribbleDrawer extends ShapeDrawer {

                int lastx, lasty;

                @Override
                public void mouseDragged(MouseEvent e) {
                    Graphics g = ShapeManager.this.gui.getGraphics();
                    int x = e.getX(), y = e.getY();
                    g.setColor(ShapeManager.this.gui.color);
                    g.setPaintMode();
                    g.drawLine(this.lastx, this.lasty, x, y);
                    this.lastx = x;
                    this.lasty = y;
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    this.lastx = e.getX();
                    this.lasty = e.getY();
                }
            }

            abstract class ShapeDrawer extends MouseAdapter implements MouseMotionListener {

                @Override
                public void mouseMoved(MouseEvent e) {
                    /* ignore */ }
            }

            DrawGUI gui;

            ScribbleDrawer scribbleDrawer = new ScribbleDrawer();
            RectangleDrawer rectDrawer = new RectangleDrawer();
            OvalDrawer ovalDrawer = new OvalDrawer();
            ShapeDrawer currentDrawer;

            public ShapeManager(DrawGUI itsGui) {
                this.gui = itsGui;
                // default: scribble mode
                this.currentDrawer = this.scribbleDrawer;
                // activate scribble drawer
                this.gui.addMouseListener(this.currentDrawer);
                this.gui.addMouseMotionListener(this.currentDrawer);
            }

            // user selected new shape => reset the shape mode
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getItem().equals("Scribble")) {
                    this.setCurrentDrawer(this.scribbleDrawer);
                }
                else if (e.getItem().equals("Rectangle")) {
                    this.setCurrentDrawer(this.rectDrawer);
                }
                else if (e.getItem().equals("Oval")) {
                    this.setCurrentDrawer(this.ovalDrawer);
                }
            }

            // reset the shape drawer
            public void setCurrentDrawer(ShapeDrawer l) {
                if (this.currentDrawer == l) {
                    return;
                }

                // deactivate previous drawer
                this.gui.removeMouseListener(this.currentDrawer);
                this.gui.removeMouseMotionListener(this.currentDrawer);
                // activate new drawer
                this.currentDrawer = l;
                this.gui.addMouseListener(this.currentDrawer);
                this.gui.addMouseMotionListener(this.currentDrawer);
            }
        }

        shape_chooser.addItemListener(new ShapeManager(this));

        class ColorItemListener implements ItemListener {

            // user selected new color => store new color in DrawGUI
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getItem().equals("Black")) {
                    DrawGUI.this.color = Color.black;
                }
                else if (e.getItem().equals("Green")) {
                    DrawGUI.this.color = Color.green;
                }
                else if (e.getItem().equals("Red")) {
                    DrawGUI.this.color = Color.red;
                }
                else if (e.getItem().equals("Blue")) {
                    DrawGUI.this.color = Color.blue;
                }
            }
        }

        color_chooser.addItemListener(new ColorItemListener());

        // Handle the window close request similarly
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                DrawGUI.this.app.doCommand("quit");
            }
        });

        // Finally, set the size of the window, and pop it up
        this.setSize(500, 400);
        this.setBackground(Color.white);
        this.show();
    }

}
