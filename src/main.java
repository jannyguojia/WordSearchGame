import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class main {
    static int SIZE = 15;
    static char[][] board = new char[SIZE][SIZE];
    static Random rand = new Random();

    static List<String> wordList = Arrays.asList(
        "椰子", "柚子", "百香果", "草莓", "山竹", "桔子", "火龙果", "榴莲", "柠檬", "木瓜"
    );

    private static final String chineseChars = "阿姨啊矮爱爱好安静茶八把爸爸吧白百班搬半办法办公室帮忙帮助包饱报纸杯子北方北京被本鼻子比比较比赛必须变化表示表演别别人宾馆冰箱不客气不地放放心非常飞机分分钟服务员附近复习干净敢感冒刚才高高兴告诉哥哥个给跟根据更公共汽车公斤公司公园工作才菜菜单参加草层差长唱歌超市衬衫成绩城市吃迟到出出现出租车厨房除了穿船春词语次聪明从错打电话打篮球打扫大大家带担心蛋糕但是当然到地地铁地图第一点电脑电视电梯电影电子邮件东东西冬懂动物都读短段锻炼对对不起多多么多少饿而且儿子耳朵二发烧发现饭馆方便房间害怕看见汉语考试渴好好吃可爱号喝和河黑黑板很红后面护照花花园画坏欢迎还环境换黄回回答会会议教室冷离离开来蓝老老师了类";

    enum Direction {
        RIGHT(0, 1), DOWN(1, 0),
        DOWN_RIGHT(1, 1), DOWN_LEFT(1, -1),
        UP_RIGHT(-1, 1), UP_LEFT(-1, -1);

        int dx, dy;
        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    public static void main(String[] args) {
        for (char[] row : board)
            Arrays.fill(row, '\0');

        for (String word : wordList) {
            placeWord(word);
        }

        fillEmptySpaces();
        printBoard();
        saveBoardAsImage("word_search.png");
        System.out.println("已保存图片：word_search.png");
    }

    static void placeWord(String word) {
        List<Direction> directions = Arrays.asList(Direction.values());
        Collections.shuffle(directions);

        for (Direction dir : directions) {
            for (int tries = 0; tries < 100; tries++) {
                int row = rand.nextInt(SIZE);
                int col = rand.nextInt(SIZE);
                if (canPlaceWord(word, row, col, dir)) {
                    for (int i = 0; i < word.length(); i++) {
                        board[row + i * dir.dx][col + i * dir.dy] = word.charAt(i);
                    }
                    return;
                }
            }
        }
        System.out.println("无法放置单词：" + word);
    }

    static boolean canPlaceWord(String word, int row, int col, Direction dir) {
        for (int i = 0; i < word.length(); i++) {
            int r = row + i * dir.dx;
            int c = col + i * dir.dy;
            if (r < 0 || r >= SIZE || c < 0 || c >= SIZE) return false;
            if (board[r][c] != '\0' && board[r][c] != word.charAt(i)) return false;
        }
        return true;
    }

    static void fillEmptySpaces() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '\0') {
                    board[i][j] = chineseChars.charAt(rand.nextInt(chineseChars.length()));
                }
            }
        }
    }

    static void printBoard() {
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    static void saveBoardAsImage(String fileName) {
        int cellSize = 40;
        int margin = 50;
        int extraTextWidth = 350;
        int imgWidth = SIZE * cellSize + margin * 2 + extraTextWidth;
        int imgHeight = SIZE * cellSize + margin * 2;

        BufferedImage image = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imgWidth, imgHeight);

        // 棋盘字体和边框
        g.setFont(new Font("Serif", Font.BOLD, 28));
        g.setColor(Color.BLACK);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int x = margin + j * cellSize;
                int y = margin + i * cellSize;
                g.drawRect(x, y, cellSize, cellSize);
                String ch = String.valueOf(board[i][j]);
                FontMetrics fm = g.getFontMetrics();
                int textX = x + (cellSize - fm.stringWidth(ch)) / 2;
                int textY = y + (cellSize + fm.getAscent() - fm.getDescent()) / 2;
                g.drawString(ch, textX, textY);
            }
        }

        // 绘制右侧词语列表
        int textX = margin + SIZE * cellSize + 30;
        int textY = margin;
        g.setFont(new Font("Serif", Font.PLAIN, 22));
        g.setColor(Color.DARK_GRAY);
        g.drawString("请⚪出以下词语：", textX, textY);

        int wordsPerLine = 5;
        int lineHeight = 30;
        int line = 1;
        for (int i = 0; i < wordList.size(); i += wordsPerLine) {
            int end = Math.min(i + wordsPerLine, wordList.size());
            String lineText = String.join("，", wordList.subList(i, end));
            g.drawString(lineText, textX, textY + lineHeight * line);
            line++;
        }

        g.dispose();
        try {
            ImageIO.write(image, "png", new File(fileName));
        } catch (Exception e) {
            System.out.println("保存图片失败：" + e.getMessage());
        }
    }
}
