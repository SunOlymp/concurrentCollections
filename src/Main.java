import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    private static BlockingQueue<String> queueA = new ArrayBlockingQueue(100);
    private static BlockingQueue<String> queueB = new ArrayBlockingQueue(100);
    private static BlockingQueue<String> queueC = new ArrayBlockingQueue(100);

    public static void main(String[] args) {

        Thread mainGenerator = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queueA.put(text);
                    //System.out.println("Добавили в A");
                    queueB.put(text);
                    //System.out.println("Добавили в B");
                    queueC.put(text);
                    //System.out.println("Добавили в C");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        Thread aThread = new Thread(() -> {
            int maxCount = 0;
            String maxText = "";
            while (true) {
                try {
                    String text = queueA.take();
                    int count = countCharacter(text, 'a');
                    if (count > maxCount) {
                        maxCount = count;
                        maxText = text;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread bThread = new Thread(() -> {
            int maxCount = 0;
            String maxText = "";
            while (true) {
                try {
                    String text = queueB.take();
                    int count = countCharacter(text, 'b');
                    if (count > maxCount) {
                        maxCount = count;
                        maxText = text;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread cThread = new Thread(() -> {
            int maxCount = 0;
            String maxText = "";
            while (true) {
                try {
                    String text = queueC.take();
                    int count = countCharacter(text, 'c');
                    if (count > maxCount) {
                        maxCount = count;
                        maxText = text;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mainGenerator.start();
        aThread.start();
        bThread.start();
        cThread.start();
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int countCharacter(String text, char character) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == character) {
                count++;
            }
        }
        return count;
    }
}