package application;

import abs.Monkey;
import abs.selector.CanAccessFirstLadderSelector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.TimerTask;

/**
 * 这个类是可视化版本的猴子过河
 */
public class Application {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        String choice = JOptionPane.showInputDialog("1. 手动输入参数\n"
                + "2. 读取文件");

        JPanel window;

        switch (choice) {
            case "1":
                window = new InputPanel(frame);

                break;
            case "2":
                window = new SelectFilePanel(frame);
                // set size
                // this size is for file selector
                frame.setSize(600, 100);
                break;
            default:
                return;
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(window);
        frame.setVisible(true);
        frame.setResizable(false);

    }

}

class InputPanel extends JPanel implements ActionListener {

    private int monkeyLast = 0;

    private int time = 0;

    private JLabel monkeyLastText = new JLabel("剩余猴子数" + monkeyLast);

    private JLabel timeUsedText = new JLabel("已使用时间" + time);

    private int n;

    private int h;

    private int t;

    private int N;

    private int k;

    private int mV;

    private MonkeyPasser passer = null;

    private JFrame parent;

    InputPanel(JFrame parent) {
        this.parent = parent;

        this.n = Integer.parseInt(JOptionPane.showInputDialog("n = ?"));
        this.h = Integer.parseInt(JOptionPane.showInputDialog("h = ?"));
        this.t = Integer.parseInt(JOptionPane.showInputDialog("t = ?"));
        this.N = Integer.parseInt(JOptionPane.showInputDialog("N = ?"));
        this.k = Integer.parseInt(JOptionPane.showInputDialog("k = ?"));
        this.mV = Integer.parseInt(JOptionPane.showInputDialog("MV = ?"));

        this.parent.setSize(600, 800);
        add(monkeyLastText);
        add(timeUsedText);
        createMonkeys();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (passer != null) {
            draw(g);
        }


    }

    private void createMonkeys() {
        passer = MonkeyPasser.randomMonkeyPasser(n, h, t, N, k, mV);

        // start passing river
        new Thread(new Runnable() {
            @Override
            public void run() {
                passer.passRiver(CanAccessFirstLadderSelector.getSelector());
            }
        }).start();

        parent.setSize(600, 800);
        repaint();

        // set a timer to refresh all things
        java.util.Timer paintTimer = new java.util.Timer();
        paintTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (passer != null) {
                    monkeyLast = passer.getMonkeyLast();
                    time = passer.getTimeUsed();
                    monkeyLastText.setText("剩余猴子数" + monkeyLast);
                    timeUsedText.setText("已使用时间" + time);
                    if (monkeyLast == 0) {
                        paintTimer.cancel();
                        JOptionPane.showMessageDialog(null,
                                "猴子已经全部过河完毕\n"
                                        + "总耗时：" + time + "秒\n"
                                        + "吞吐率：" + passer.throughput()
                                        + "\n公平性：" + passer.fairness(),
                                "提示", JOptionPane.PLAIN_MESSAGE);
                    }
                }

                repaint();
            }
        }, 500, 1000);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void draw(Graphics g) {

        int width = getWidth(); // total width
        int height = getHeight(); // total height

        int leftX = 100;
        int rightX = 500;

        // river bank
        g.drawLine(leftX, 100, leftX, 700);
        g.drawLine(rightX, 100, rightX, 700);

        // ladders
        // 每把梯子宽30
        int startY = 120;
        int endY = 680;
        int ladderInterval = ((endY - startY) - (n * 30)) / (n - 1);
        int barInterval = (rightX - leftX) / (h - 1);
        int y = startY;
        for (int i = 0; i < n; i++) {
            String ladderDirectionI = this.passer.getLadderDirectionByNumber(i);
            if (Monkey.Direction.L2R.equals(ladderDirectionI)) {
                // ->
                int x = leftX;
                int y2 = y + 30;
                // draw ladder
                g.drawLine(leftX, y, rightX, y);
                for (int level = 1; level <= this.h; level++) {
                    g.drawLine(x, y, x, y2);

                    if (this.passer.isLevelOfLadderOccupied(i, level)) {
                        // draw monkey dot
                        g.fillOval(x - 4, y + 15 - 4, 8, 8);
                    }

                    x += barInterval;
                }
                y = y2;
                g.drawLine(leftX, y, rightX, y);
                y += ladderInterval;
            } else if (Monkey.Direction.R2L.equals(ladderDirectionI)) {
                // <-
                int x = rightX;
                int y2 = y + 30;
                // draw ladder
                g.drawLine(rightX, y, leftX, y);
                for (int level = this.h; level >= 1; level--) {
                    g.drawLine(x, y, x, y2);

                    if (this.passer.isLevelOfLadderOccupied(i, this.h - level + 1)) {
                        // draw monkey dot
                        g.fillOval(x - 4, y + 15 - 4, 8, 8);
                    }

                    x -= barInterval;
                }
                y = y2;
                g.drawLine(leftX, y, rightX, y);
                y += ladderInterval;
            } else {
                int x = leftX;
                int y2 = y + 30;
                // draw ladder
                g.drawLine(leftX, y, rightX, y);
                for (int level = 1; level <= this.h; level++) {
                    g.drawLine(x, y, x, y2);
                    x += barInterval;
                }
                y = y2;
                g.drawLine(leftX, y, rightX, y);
                y += ladderInterval;
            }


        }


    }
}

class SelectFilePanel extends JPanel implements ActionListener {
    private final JLabel label = new JLabel("读取文件路径：");

    private JTextField path = new JTextField(30);

    private JButton select = new JButton("选择文件");

    private JButton confirm = new JButton("确定");

    private JFileChooser fileChooser = new JFileChooser();

    private String selectedPath = null;

    private int monkeyLast = 0;

    private int time = 0;

    private JLabel monkeyLastText = new JLabel("剩余猴子数" + monkeyLast);

    private JLabel timeUsedText = new JLabel("已使用时间" + time);

    private int n = 0;

    private int h = 0;

    private MonkeyPasser passer = null;

    private JFrame parent;

    SelectFilePanel(JFrame parent) {
        this.parent = parent;

        add(label);
        add(path);
        add(select);
        add(confirm);
        path.setEnabled(false);
        select.addActionListener(this);
        confirm.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == select) {
            selectFile();
        } else if (e.getSource() == confirm) {
            clickConfirm();
            newComponents();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (passer != null) {
            draw(g);
        }

    }

    private void newComponents() {
        add(monkeyLastText);
        add(timeUsedText);
    }

    private void selectFile() {
        if (fileChooser.showOpenDialog(null) == 0) {
            String filePath = fileChooser.getSelectedFile().getPath();
            path.setText(filePath);
            selectedPath = filePath;
            System.out.println(filePath);
        }
    }

    private void clickConfirm() {
        if (selectedPath == null) {
            JOptionPane.showMessageDialog(null,
                    "请选择文件",
                    "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                passer = MonkeyPasser.passerByMonkeyFile(selectedPath);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null,
                        "文件不存在",
                        "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // when file exists
            // now use passer to get all details
            this.n = this.passer.getN();
            this.h = this.passer.getH();

            // start passing river
            new Thread(new Runnable() {
                @Override
                public void run() {
                    passer.passRiver(CanAccessFirstLadderSelector.getSelector());
                }
            }).start();

            remove(label);
            remove(path);
            remove(select);
            remove(confirm);
            parent.setSize(600, 800);
            repaint();

            // set a timer to refresh all things
            java.util.Timer paintTimer = new java.util.Timer();
            paintTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (passer != null) {
                        monkeyLast = passer.getMonkeyLast();
                        time = passer.getTimeUsed();
                        monkeyLastText.setText("剩余猴子数" + monkeyLast);
                        timeUsedText.setText("已使用时间" + time);
                        if (monkeyLast == 0) {
                            paintTimer.cancel();
                            JOptionPane.showMessageDialog(null,
                                    "猴子已经全部过河完毕\n"
                                            + "总耗时：" + time + "秒\n"
                                            + "吞吐率：" + passer.throughput()
                                            + "\n公平性：" + passer.fairness(),
                                    "提示", JOptionPane.PLAIN_MESSAGE);
                        }
                    }

                    repaint();
                }
            }, 500, 1000);

        }
    }

    private void draw(Graphics g) {

        int width = getWidth(); // total width
        int height = getHeight(); // total height

        int leftX = 100;
        int rightX = 500;

        // river bank
        g.drawLine(leftX, 100, leftX, 700);
        g.drawLine(rightX, 100, rightX, 700);

        // ladders
        // 每把梯子宽30
        int startY = 120;
        int endY = 680;
        int ladderInterval = ((endY - startY) - (n * 30)) / (n - 1);
        int barInterval = (rightX - leftX) / (h - 1);
        int y = startY;
        for (int i = 0; i < n; i++) {
            String ladderDirectionI = this.passer.getLadderDirectionByNumber(i);
            if (Monkey.Direction.L2R.equals(ladderDirectionI)) {
                // ->
                int x = leftX;
                int y2 = y + 30;
                // draw ladder
                g.drawLine(leftX, y, rightX, y);
                for (int level = 1; level <= this.h; level++) {
                    g.drawLine(x, y, x, y2);

                    if (this.passer.isLevelOfLadderOccupied(i, level)) {
                        // draw monkey dot
                        g.fillOval(x - 4, y + 15 - 4, 8, 8);
                    }

                    x += barInterval;
                }
                y = y2;
                g.drawLine(leftX, y, rightX, y);
                y += ladderInterval;
            } else if (Monkey.Direction.R2L.equals(ladderDirectionI)) {
                // <-
                int x = rightX;
                int y2 = y + 30;
                // draw ladder
                g.drawLine(rightX, y, leftX, y);
                for (int level = this.h; level >= 1; level--) {
                    g.drawLine(x, y, x, y2);

                    if (this.passer.isLevelOfLadderOccupied(i, this.h - level + 1)) {
                        // draw monkey dot
                        g.fillOval(x - 4, y + 15 - 4, 8, 8);
                    }

                    x -= barInterval;
                }
                y = y2;
                g.drawLine(leftX, y, rightX, y);
                y += ladderInterval;
            } else {
                int x = leftX;
                int y2 = y + 30;
                // draw ladder
                g.drawLine(leftX, y, rightX, y);
                for (int level = 1; level <= this.h; level++) {
                    g.drawLine(x, y, x, y2);
                    x += barInterval;
                }
                y = y2;
                g.drawLine(leftX, y, rightX, y);
                y += ladderInterval;
            }


        }


    }

}









