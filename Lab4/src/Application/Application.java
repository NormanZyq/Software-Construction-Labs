package Application;

import Application.AtomStructure.AtomStructure;
import Application.AtomStructure.Electron;
import Application.SocialNetworkCircle.SocialNetworkCircle;
import Application.SocialNetworkCircle.SocialPerson;
import Application.TrackGame.TrackArranger;
import Application.TrackGame.TrackGame;
import MyException.AtomStructureException.ElectronDoesNotExistExistException;
import MyException.CircularOrbitExceotion.NoSuchLevelOfTrackException;
import MyException.CircularOrbitExceotion.ObjectDoesNotExistException;
import MyException.CircularOrbitExceotion.OrbitFileParseException;
import MyException.CircularOrbitExceotion.TrackDidExistException;
import MyException.SocialPersonDoesNotExistExistException;
import MyException.TrackGameException.AthleteDidExistException;
import MyException.TrackGameException.AthleteDoesNotExistException;
import abs.CircularOrbit;
import abs.Parser.AtomStructureParser;
import abs.Parser.FileParser;
import abs.Parser.SocialNetworkCircleParser;
import abs.Parser.TrackGameParser;
import abs.PhysicalObject;
import abs.Position;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Application {
    private Logger log = Logger.getLogger(Application.class);

    public static void main(final String[] args) {

        JFrame application = new JFrame(); // creates a new JFrame

        // create the panel with the user's input
        JPanel panel;

        // set size
        application.setSize(600, 100);  // this size is for file selector

        Outer:
        while (true) {
            // obtain user's choice
            String input = JOptionPane.showInputDialog(
                    "输入1进入Track Game\n"
                            + "输入2进入Social Network Circle\n"
                            + "输入3进入Atom Structure\n");

            if (input == null) {
                return;
            }

            switch (input) {
                case "1":
                    panel = new TrackGamePanel(application);
                    break Outer;

                case "2":
                    panel = new SocialNetworkPanel(application);
                    break Outer;

                case "3":
                    panel = new AtomStructurePanel(application);
                    break Outer;

                default:
                    JOptionPane.showMessageDialog(null, "请输入合法选择。");
                    break;
            }
        }

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.add(panel);
        application.setVisible(true);
    }
}

abstract class MyPanel extends JPanel implements ActionListener {
}

@SuppressWarnings("Duplicates")
class TrackGamePanel extends MyPanel {

    private final JFrame parent;
    private final JLabel label = new JLabel("读取文件路径：");
    private Logger log = Logger.getLogger(TrackGamePanel.class);
    private boolean hasArrangement = false;
    private JTextField path = new JTextField(30);

    private JButton select = new JButton("选择文件");

    private JButton confirm = new JButton("确定");

    private JFileChooser fileChooser = new JFileChooser();

    private String selectedPath = null;

    private TrackGame trackGame;

    private JLabel athleteCount = new JLabel();

    private JLabel a = new JLabel("安排比赛：");

    private JButton btnRandom = new JButton("随机安排");

    private JButton btnFasterLater = new JButton("强者后出场");

    private JButton btnWriteToFile = new JButton("将安排写入文件");

    private JButton btnExchange = new JButton("交换两个运动员的组");

    // 通用按钮
    private JButton btnAddTrack = new JButton("添加跑道");

    private JButton btnAddObject = new JButton("添加运动员");

    private JButton btnRemoveTrack = new JButton("删除跑道");

    private JButton btnRemoveObject = new JButton("删除运动员");
    private Graphics g;

    TrackGamePanel(JFrame parent) {
        this.parent = parent;
//        setLayout(new FlowLayout());

        add(label);
        add(path);
        add(select);
        add(confirm);
        path.setEnabled(false);
        select.addActionListener(this);
        confirm.addActionListener(this);


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;

        int width = getWidth(); // total width
        int height = getHeight() - 200; // total height

        if (trackGame != null) {
            drawTrackGame(width, height);
        }

    }

    private void drawTrackGame(int width, int height) {


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == select) {
            if (fileChooser.showOpenDialog(null) == 0) {
                String filePath = fileChooser.getSelectedFile().getPath();
                path.setText(filePath);
                selectedPath = filePath;
                System.out.println(filePath);
            }
        } else if (e.getSource() == confirm) {

            if (selectedPath == null) {
                JOptionPane.showMessageDialog(null, "请选择文件", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    trackGame = (TrackGame) FileParser.parseFile(new TrackGameParser(), new File(selectedPath));
                    System.out.println("ok");
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, "文件不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (OrbitFileParseException e1) {
                    JOptionPane.showMessageDialog(null, "文件内容不符合Track Game文件的规定\n错误详情：" + e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                parent.setSize(400, 600);
                remove(label);
                remove(path);
                remove(select);
                remove(confirm);

                athleteCount.setText("当前有" + trackGame.getAthletes().size() + "名运动员被读入。");      // set text of the label

                add(athleteCount);
                add(a);

                add(btnRandom);
                add(btnFasterLater);
                add(btnWriteToFile);
                add(btnExchange);

                btnRandom.addActionListener(this);
                btnFasterLater.addActionListener(this);
                btnWriteToFile.addActionListener(this);
                btnExchange.addActionListener(this);

                // 通用功能
                add(btnAddTrack);
                add(btnRemoveTrack);
                add(btnAddObject);
                add(btnRemoveObject);
                btnAddTrack.addActionListener(this);
                btnRemoveTrack.addActionListener(this);
                btnAddObject.addActionListener(this);
                btnRemoveObject.addActionListener(this);


            }
        } else if (e.getSource() == btnRandom) {
            // 随机安排
            trackGame.arrange(TrackArranger.randomArranger());
            JOptionPane.showMessageDialog(null, "安排完成", "成功",
                    JOptionPane.INFORMATION_MESSAGE);
            System.out.println("random arrange ok");
            hasArrangement = true;
        } else if (e.getSource() == btnFasterLater) {
            // 强者后出场
            trackGame.arrange(TrackArranger.fasterLaterArranger());
            System.out.println("faster-later arrange ok");
            hasArrangement = true;
            JOptionPane.showMessageDialog(null, "安排完成", "成功",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == btnWriteToFile) {
            // 判断是否有安排过比赛
            if (!hasArrangement) {
                JOptionPane.showMessageDialog(null, "当前没有安排，不能写入文件。", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // 写入文件
            try {
                String filename = trackGame.writeArrangementToFile();
                JOptionPane.showMessageDialog(null, "写入文件成功，文件名是" + filename, "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "写入文件失败，请重试", "失败", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnExchange) {
            // 判断是否有安排过比赛
            if (!hasArrangement) {
                JOptionPane.showMessageDialog(null, "当前没有安排，不能交换出场。至少需要执行上面两种安排方法的任意一种。", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 交换出场
            String name1 = JOptionPane.showInputDialog(null, "请输入第一个运动员的姓名：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            String name2 = JOptionPane.showInputDialog(null, "请输入第二个运动员的姓名：\n", "请输入", JOptionPane.PLAIN_MESSAGE);

            if (name1 == null || name2 == null) return;

            if (trackGame.exchangePosition(name1, name2)) {
                JOptionPane.showMessageDialog(null, "成功");
            } else {
                JOptionPane.showMessageDialog(null, "所输入的运动员不存在", "失败", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnAddObject) {
            String name = JOptionPane.showInputDialog(null, "运动员姓名：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            if (name == null) return;

            int number;
            String numberString = JOptionPane.showInputDialog(null, "运动员编号：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            // 处理number的非法输入
            if (numberString == null) {
                return;
            } else {
                try {
                    number = Integer.parseInt(numberString);
                    if (number <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "编号必须是正整数", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            int age = 0;
            String ageString = JOptionPane.showInputDialog(null, "运动员年龄：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            // 处理年龄的非法输入
            if (ageString == null) {
                return;
            } else {
                try {
                    age = Integer.parseInt(ageString);
                    if (age <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "年龄必须是整数", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            String country = JOptionPane.showInputDialog(null, "运动员国籍：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            if (country == null) return;

            double score;
            String scoreString = JOptionPane.showInputDialog(null, "运动员成绩：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            // 处理成绩的非法输入
            if (scoreString == null) {
                return;
            } else {
                try {
                    score = Double.parseDouble(scoreString);
                    if (score <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "成绩必须是数", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            try {
                trackGame.addAthlete(PhysicalObject.newAthlete(name, number, country, age, score));
            } catch (AthleteDidExistException e1) {
                log.info("运动员出现一次重名");
                JOptionPane.showMessageDialog(null, "添加失败！错误详情：" + e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnAddTrack) {
            // 增加跑道
            String input1 = JOptionPane.showInputDialog(null, "添加几号跑道：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            if (input1 == null || input1.equals("")) return;

            try {
                int level = Integer.parseInt(input1);
                trackGame.addTrack(level, level);
                JOptionPane.showMessageDialog(null, "添加成功", "成功", JOptionPane.PLAIN_MESSAGE);
                repaint();
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "输入内容不合法", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (TrackDidExistException e1) {
                JOptionPane.showMessageDialog(null, "添加失败，该编号的跑道已存在", "错误", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnRemoveObject) {
            // 移除运动员
            String name = JOptionPane.showInputDialog(null, "请输入要移除的运动员名称\n", "请输入", JOptionPane.PLAIN_MESSAGE);

            try {
                trackGame.removeAthlete(name);
                JOptionPane.showMessageDialog(null, "移除成功", "成功", JOptionPane.PLAIN_MESSAGE);
                repaint();
            } catch (AthleteDoesNotExistException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "移除失败，错误详情：" + e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnRemoveTrack) {
            String input1 = JOptionPane.showInputDialog(null, "删除几号跑道：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            if (input1 == null || input1.equals("")) return;

            try {
                int level = Integer.parseInt(input1);
                trackGame.removeTrack(level);
                JOptionPane.showMessageDialog(null, "删除成功", "成功", JOptionPane.PLAIN_MESSAGE);
                repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "输入内容不合法", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (NoSuchLevelOfTrackException e1) {
                JOptionPane.showMessageDialog(null, "移除失败，该编号的跑道不存在", "错误", JOptionPane.ERROR_MESSAGE);

            }
        }
    }
}

class AtomStructurePanel extends MyPanel {

    private final JLabel label = new JLabel("读取文件路径：");

    private JFrame parent;

    private JTextField path = new JTextField(30);

    private JButton select = new JButton("选择文件");

    private JButton confirm = new JButton("确定");

    private JFileChooser fileChooser = new JFileChooser();

    private String selectedPath = null;

    private JButton btnTrans = new JButton("跃迁");

    private AtomStructure atomStructure = null;

    private Graphics g = null;

    // 通用按钮
    private JButton btnAddTrack = new JButton("添加轨道");

    private JButton btnAddObject = new JButton("添加物体");

    private JButton btnRemoveTrack = new JButton("删除轨道");

    private JButton btnRemoveObject = new JButton("删除电子");

    AtomStructurePanel(JFrame parent) {
        this.parent = parent;

        add(label);
        add(path);
        add(select);
        add(confirm);
        path.setEnabled(false);
        select.addActionListener(this);
        confirm.addActionListener(this);
    }

    // draws a cascade of shapes starting from the top-left corner
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;

        int width = getWidth(); // total width
        int height = getHeight() - 200; // total height

        if (atomStructure != null) {
            drawAtomStructure(width, height);
        }
    }

    private void drawAtomStructure(int width, int height) {
        int tracks = this.atomStructure.trackCount();

        // define center dot's size and draw it
        int centerR = 9, centerD = 2 * centerR;
        g.fillOval(width / 2 - centerR, height / 2 - centerR + 200, centerD, centerD);

        // define dots' size
        final int dotR = 4, dotD = 2 * dotR;

        // draw tracks
        for (int i = 1; i <= tracks; i++) {
            if (atomStructure.getTrack(i) != null) {
                int r = i * 40;
                int d = 2 * r;
                int x = width / 2 - r;
                int y = height / 2 - r;
                y += 200;
                g.setColor(Color.BLACK);
                g.drawOval(x, y, d, d);

                // draw dots(electron)
//                Random random = new Random(System.currentTimeMillis());   // to generate random angle
                for (Electron e : atomStructure.getObjectsByLevel(i)) {
                    g.setColor(Color.RED);
                    double radians = Math.toRadians(atomStructure.getPosition(e).getAngle());
                    double a = r * Math.cos(radians);
                    double b = r * Math.sin(radians);
                    int x1 = (int) Math.round(x + r + a);
                    int y1 = (int) Math.round(y + (r - b));

                    int x2 = x1 - dotR;
                    int y2 = y1 - dotR;

                    g.fillOval(x2, y2, dotD, dotD);

                }
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == select) {
            if (fileChooser.showOpenDialog(null) == 0) {
                String filePath = fileChooser.getSelectedFile().getPath();
                path.setText(filePath);
                selectedPath = filePath;
                System.out.println(filePath);
            }
        } else if (e.getSource() == confirm) {
            if (selectedPath == null) {
                JOptionPane.showMessageDialog(null, "请选择文件", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    atomStructure = (AtomStructure) CircularOrbit.parseFile(new AtomStructureParser(), selectedPath);
                    parent.setSize(600, 800);
                    remove(label);
                    remove(path);
                    remove(select);
                    remove(confirm);
                    add(btnTrans);
                    btnTrans.addActionListener(this);

                    // 通用功能
                    add(btnAddTrack);
                    add(btnRemoveTrack);
                    add(btnAddObject);
                    add(btnRemoveObject);
                    btnAddTrack.addActionListener(this);
                    btnRemoveTrack.addActionListener(this);
                    btnAddObject.addActionListener(this);
                    btnRemoveObject.addActionListener(this);

                    repaint();
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "文件不存在", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (OrbitFileParseException ex) {
                    JOptionPane.showMessageDialog(null, "文件内容不符合Atom Structure规定！\n错误详情：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == btnTrans) {
            // 跃迁

            String input1 = JOptionPane.showInputDialog(null, "从哪一个轨道跃迁：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            String input2 = JOptionPane.showInputDialog(null, "跃迁到哪一个轨道：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            String input3 = JOptionPane.showInputDialog(null, "跃迁几个电子：\n", "请输入", JOptionPane.PLAIN_MESSAGE);

            int from = Integer.parseInt(input1);
            int to = Integer.parseInt(input2);
            int num = Integer.parseInt(input3);

            try {
                atomStructure.electronTransit(from, to, num);
                repaint();
            } catch (NoSuchLevelOfTrackException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "跃迁失败！错误详情：" + e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnAddObject) {
            // 增加电子
            String input1 = JOptionPane.showInputDialog(null, "加入到哪一个轨道：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            if (input1 == null) {
                return; // if no input, then return
            }
            try {
                int level = Integer.parseInt(input1);

                atomStructure.addToTrack(PhysicalObject.newElectron(""), level, level, System.currentTimeMillis() % 360);
                JOptionPane.showMessageDialog(null, "添加成功", "成功", JOptionPane.PLAIN_MESSAGE);
                repaint();
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "输入内容不合法", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (NoSuchLevelOfTrackException ex) {
                JOptionPane.showMessageDialog(null, "添加失败，可能是轨道不存在，请先添加该轨道", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnRemoveObject) {
            // 移除电子
            String input1 = JOptionPane.showInputDialog(null, "删除哪一个轨道的电子：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            if (input1 == null) return; // if no input, then return
            try {
                int level = Integer.parseInt(input1);
                atomStructure.removeElectronFromTrack(level);
                JOptionPane.showMessageDialog(null, "成功移除了" + level + "上的一个电子", "成功", JOptionPane.PLAIN_MESSAGE);
                repaint();
            } catch (NumberFormatException exFormat) {
                exFormat.printStackTrace();
                JOptionPane.showMessageDialog(null, "输入内容不合法", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (NoSuchLevelOfTrackException exTrack) {
                exTrack.printStackTrace();
                JOptionPane.showMessageDialog(null, "移除失败，错误详情：" + exTrack.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            } catch (ElectronDoesNotExistExistException e1) {
                JOptionPane.showMessageDialog(null, "该轨道上已经没有电子了", "失败", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (e.getSource() == btnAddTrack) {
            // 增加轨道
            String input1 = JOptionPane.showInputDialog(null, "请输入增加第几条轨道：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            if (input1 == null) return; // if no input, then return

            try {
                int level = Integer.parseInt(input1);
                atomStructure.addTrack(level, level);
                JOptionPane.showMessageDialog(null, "成功添加" + level + "号轨道", "成功", JOptionPane.PLAIN_MESSAGE);
                repaint();
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "输入内容不合法", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (TrackDidExistException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "添加失败！错误详情：" + e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnRemoveTrack) {
            // 移除轨道
            String input1 = JOptionPane.showInputDialog(null, "请输入移除第几条轨道：\n", "请输入", JOptionPane.PLAIN_MESSAGE);
            if (input1 == null) return; // if no input, then return

            try {
                int level = Integer.parseInt(input1);
                atomStructure.removeTrack(level);
                JOptionPane.showMessageDialog(null, "成功删除" + level + "号轨道", "成功", JOptionPane.PLAIN_MESSAGE);
                repaint();
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "输入内容不合法", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (NoSuchLevelOfTrackException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "移除失败！错误详情：" + e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

/**
 * 包含哪些功能和按钮：
 * 1. 通过输入两个人名和权重添加关系和删除关系
 * 2. 计算某个人的信息扩散度
 * 3. 通过输入计算两个人的逻辑、物理距离
 */
@SuppressWarnings("Duplicates")
class SocialNetworkPanel extends MyPanel {

    private final JLabel label = new JLabel("读取文件路径：");

    private JFrame parent;

    private JTextField path = new JTextField(30);

    private JButton select = new JButton("选择文件");

    private JButton confirm = new JButton("确定");

    private JFileChooser fileChooser = new JFileChooser();

    private String selectedPath = null;

    private JButton btnAddRelation = new JButton("添加关系");

    private JButton btnRemoveRelation = new JButton("移除关系");

    private JButton btnCalculateRelationWhat = new JButton("计算信息扩散度");

    private JButton btnCalculateDistance = new JButton("计算两人距离");

    private SocialNetworkCircle circle = null;

    private Graphics g = null;

//    private Random random = new Random(System.currentTimeMillis());

    public SocialNetworkPanel(JFrame parent) {
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;

        int width = getWidth(); // total width
        int height = getHeight() - 200; // total height

        if (circle != null) {
            drawSocialNetworkCircle(width, height);
        }

    }

    private String[] getUserInputNames(String tip1, String tip2) {
        if (tip1 == null) tip1 = "请输入第一个人名：";
        if (tip2 == null) tip2 = "请输入第二个人名：";

        String input1 = JOptionPane.showInputDialog(null, tip1 + "\n", "请输入", JOptionPane.PLAIN_MESSAGE);
        String input2 = JOptionPane.showInputDialog(null, tip2 + "\n", "请输入", JOptionPane.PLAIN_MESSAGE);

        return new String[]{input1, input2};
    }

    public void drawSocialNetworkCircle(int width, int height) {
        int tracks = circle.trackCount();

        // define center dot's size and draw it
        int centerR = 9, centerD = 2 * centerR;
        int centerX = width / 2 - centerR;
        int centerY = height / 2 - centerR + 200;
        g.fillOval(centerX, centerY, centerD, centerD);

        // define dots' size
        final int dotR = 4, dotD = 2 * dotR;

        // draw tracks
        for (int i = 1; i <= tracks; i++) {
            int r = i * 40;
            int d = 2 * r;
            int x = width / 2 - r;
            int y = height / 2 - r;
            y += 200;
            g.setColor(Color.BLACK);
            g.drawOval(x, y, d, d);

            // draw dots(person)
            for (SocialPerson p : circle.getObjectsByLevel(i)) {
                // draw dot
                g.setColor(Color.RED);
                double radians = Math.toRadians(circle.getPosition(p).getAngle());
                double a = r * Math.cos(radians);
                double b = r * Math.sin(radians);

                int x1 = (int) Math.round(x + r + a);
                int y1 = (int) Math.round(y + (r - b));

                int x2 = x1 - dotR;
                int y2 = y1 - dotR;

                g.fillOval(x2, y2, dotD, dotD);
                g.setColor(Color.BLUE);
                g.drawString(p.getName(), x2, y2);
                g.setColor(Color.RED);
            }
        }

        // draw relations
        g.setColor(Color.ORANGE);
        Set<String> names = circle.getPeopleNameSet();
        for (String name : names) {
            // get position
            if (!name.equals(circle.getCentralUser().getName())) {
                // not the center user
                Position sourcePosition = circle.getPosition(name);
                int[] sourceXY = calculateXY(sourcePosition.getLevel(), sourcePosition.getAngle(), width, height);

                Map<String, Double> relations = circle.getRelationByPerson(name);

                for (String targetName : relations.keySet()) {
                    // get targetPosition
                    Position targetPosition = circle.getPosition(targetName);
                    int[] targetXY = calculateXY(targetPosition.getLevel(), targetPosition.getAngle(), width, height);

                    g.drawLine(sourceXY[0], sourceXY[1], targetXY[0], targetXY[1]);
                }
            } else {
                // this is the center user
                Map<String, Double> relations = circle.getRelationByPerson(name);

                for (String targetName : relations.keySet()) {
                    // get targetPosition
                    Position targetPosition = circle.getPosition(targetName);
                    int[] targetXY = calculateXY(targetPosition.getLevel(), targetPosition.getAngle(), width, height);

                    g.drawLine(centerX + 9, centerY + 9, targetXY[0], targetXY[1]);
                }
            }

        }
    }

    private int[] calculateXY(int level, double angle, int width, int height) {
        int r = level * 40;
        int x = width / 2 - r;
        int y = height / 2 - r;
        y += 200;

        double radians = Math.toRadians(angle);
        double a = r * Math.cos(radians);
        double b = r * Math.sin(radians);

        int x1 = (int) Math.round(x + r + a);
        int y1 = (int) Math.round(y + (r - b));

        return new int[]{x1, y1};
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == select) {
            if (fileChooser.showOpenDialog(null) == 0) {
                String filePath = fileChooser.getSelectedFile().getPath();
                path.setText(filePath);
                selectedPath = filePath;
                System.out.println(filePath);
            }
        } else if (e.getSource() == confirm) {
            if (selectedPath == null) {
                JOptionPane.showMessageDialog(null, "请选择文件", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    circle = (SocialNetworkCircle) CircularOrbit.parseFile(new SocialNetworkCircleParser(), selectedPath);
                    parent.setSize(600, 800);

                    remove(label);
                    remove(path);
                    remove(select);
                    remove(confirm);

                    add(btnAddRelation);
                    add(btnRemoveRelation);
                    add(btnCalculateRelationWhat);
                    add(btnCalculateDistance);

                    btnAddRelation.addActionListener(this);
                    btnRemoveRelation.addActionListener(this);
                    btnCalculateRelationWhat.addActionListener(this);
                    btnCalculateDistance.addActionListener(this);

                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "文件不存在", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (OrbitFileParseException e1) {
                    JOptionPane.showMessageDialog(null, "文件内容不符合Social Network Circle的规定！\n错误详情：" + e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == btnAddRelation) {
            // add relation
            String[] inputs = getUserInputNames("请输入关系的起点", "请输入关系的终点");
            String input3 = JOptionPane.showInputDialog(null, "请输入关系亲密程度\n", "请输入", JOptionPane.PLAIN_MESSAGE);

            double weight;
            try {
                weight = Double.parseDouble(input3);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "有不合法的输入", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                circle.addRelation(inputs[0], inputs[1], weight);
                JOptionPane.showMessageDialog(null, "添加成功", "成功", JOptionPane.PLAIN_MESSAGE);
            } catch (ObjectDoesNotExistException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "添加失败，错误详情：" + e1.getMessage(), "失败", JOptionPane.ERROR_MESSAGE);
            }

            repaint();
        } else if (e.getSource() == btnRemoveRelation) {
            // remove relation
            String[] inputs = getUserInputNames("请输入关系起点人名", "请输入关系终点人名");

            try {
                circle.removeRelation(inputs[0], inputs[1]);
                JOptionPane.showMessageDialog(null, "删除成功", "成功", JOptionPane.PLAIN_MESSAGE);
                repaint();
            } catch (SocialPersonDoesNotExistExistException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "删除失败，输入的人名不存在", "失败", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnCalculateRelationWhat) {
            String input = JOptionPane.showInputDialog(null, "请输入要计算谁的信息扩散度\n", "请输入", JOptionPane.PLAIN_MESSAGE);
//            JOptionPane.showMessageDialog(null, , "计算结果", JOptionPane.PLAIN_MESSAGE);

        } else if (e.getSource() == btnCalculateDistance) {
            String[] inputs = getUserInputNames(null, null);

            int logic = circle.getLogicalDistance(inputs[0], inputs[1]);
            double physical = circle.getPhysicalDistance(inputs[0], inputs[1]);

            if (logic == -1) {
                JOptionPane.showMessageDialog(null, inputs[0] + "到" + inputs[1] + "不连通", "计算结果", JOptionPane.PLAIN_MESSAGE);
            }

            JOptionPane.showMessageDialog(null,
                    inputs[0] + "到" + inputs[1] + "\n逻辑距离为" + logic + "\n物理距离为" + physical,
                    "计算结果", JOptionPane.PLAIN_MESSAGE);

        }
    }
}
