import javax.swing.*;
import java.awt.*;
class gui{
    public static void main(String args[])
    {
        JFrame frame=new JFrame("IMDb");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,600);

        JMenuBar mb=new JMenuBar();
        JMenu m1=new JMenu("File");
        JMenu m2=new JMenu("Edit");
        JMenu m3=new JMenu("View");
        mb.add(m1);mb.add(m2);mb.add(m3);

        JMenuItem m11=new JMenuItem("New Chat");
        JMenuItem m12=new JMenuItem("Open");
        JMenuItem m13=new JMenuItem("Exit");
        m1.add(m11);m1.add(m12);m1.add(m13);

        JMenuItem m21=new JMenuItem("Undo");
        JMenuItem m22=new JMenuItem("Redo");
        m2.add(m21);m2.add(m22);

        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Text");
        JTextField tf = new JTextField(10); // accepts upto 10 characters
        JButton send = new JButton("Send");
        JButton reset = new JButton("Reset");
        panel.add(label); // Components Added using Flow Layout
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);
        panel.add(reset);

        // Text Area at the Center
        JTextArea ta = new JTextArea();

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        //frame.setLayout(null);
        //frame.pack();
        frame.setVisible(true);
    }
}