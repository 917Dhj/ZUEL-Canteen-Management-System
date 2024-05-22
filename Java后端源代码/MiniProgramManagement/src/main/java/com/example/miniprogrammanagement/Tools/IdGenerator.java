package com.example.miniprogrammanagement.Tools;

public class IdGenerator {

    // 根据旧的id生成新的id，参数length是这个id的前面部分的位数
    public static int generateNewId(int oldId, int length) {
        String id = String.valueOf(oldId);
        String startId = id.substring(0, length); // id的前两位
        String endId = id.substring(length); // 去掉id的前两位剩下的部分
        int end = Integer.parseInt(endId) + 1; // 剩下的部分转换成int然后+1
        endId = String.valueOf(end); // 递增后的数字再转换成String赋值给endId
        String result = startId + endId; // 拼接id的前后部分
        return Integer.parseInt(result);
    }

    public static void main(String[] args) {
        // 测试示例
        int input = 1119;
        int result = generateNewId(input, 2);
        System.out.println("Input: " + input + ", Result: " + result);
    }
}
