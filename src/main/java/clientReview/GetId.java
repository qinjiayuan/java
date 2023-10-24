package clientReview;

import java.util.Random;

public class GetId {
    public  String getid() {
        // 定义生成的字符串长度
        int length = 32;

        // 创建一个随机数生成器
        Random random = new Random();

        // 创建一个StringBuilder对象，用于构建字符串
        StringBuilder sb = new StringBuilder();

        // 循环生成字符串
        for (int i = 0; i < length; i++) {
            // 生成一个随机数
            int num = random.nextInt(2);
            // 根据随机数生成字符
            if (num == 0) {
                // 生成大写字母
                sb.append(Character.toUpperCase((char) (random.nextInt(26) + 65)));

            } else {
                // 生成数字
                sb.append(random.nextInt(10));
            }
        }

        // 输出生成的字符串
        return sb.toString();
    }
}