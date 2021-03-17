public class Main {
    public static void main(String[] args) {
        String plainText = "HELLOWORLDABCDEFGHELLOWORLD";
        int len = plainText.length();
//        StringBuilder cipherText = new StringBuilder();
        //移位密匙
        int k1 = 3;
        //仿射密匙
        int[] k2 = {11, 3};
        //置换密匙
        int[] k3 = {3, 0, 1, 5, 4, 2, 6, 7};
        //维吉尼亚密匙
        int[] k4 = {2, 8, 15, 7, 4, 17, 23, 12};

        //移位密码， 仿射密码， 置换密码， 维吉尼亚密码

        System.out.println("初始明文: \n" + plainText + "\n");
        //加密
        System.out.println("加密------------------->");
        //1.移位加密
        String str_1 = transEncrypt(plainText, k1);
        System.out.println("移位加密 ：\n" + str_1);
        //2.仿射加密
        String str_2 = affineEncrypt(str_1, k2);
        System.out.println("仿射加密 ：\n" + str_2);
        //3.置换加密
        String str_3 = permutEncrypt(str_2, k3);
        System.out.println("置换加密 ：\n" + str_3);
        //4.维吉尼亚加密
        String str_4 = vegeneEncrypt(str_3, k4);
        System.out.println("维吉尼亚加密 ：\n" + str_4);


        //解密
        System.out.println("解密------------------->");
        //1.维吉尼亚解密
        String string_1 = vegeneDecrypt(str_4, k4);
        System.out.println("维吉尼亚解密： \n" + string_1);
        //2.置换解密
        String string_2 = permutDecrypt(string_1, k3);
        System.out.println("置换解密： \n" + string_2);
        //3.仿射解密
        String string_3 = affineDecrypt(string_2, k2);
        System.out.println("仿射解密： \n" + string_3);
        //4.移位解密
        String string_4 = transDecrypt(string_3, k1);
        System.out.println("移位解密： \n" + string_4);

        System.out.println("\n最终结果： \n" + string_4);


    }

    //移位密码——加密
    public static String transEncrypt(String str, int key){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < str.length(); i++){
            stringBuilder.append( (char)((str.charAt(i) -65 + key) % 26 + 65) );
        }
        return stringBuilder.toString();
    }
    //移位密码——解密
    public static String transDecrypt(String str, int key){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < str.length(); i++){
            stringBuilder.append( (char)((str.charAt(i)-65 - key) % 26 + 65)  );
        }
        return stringBuilder.toString();
    }


    //仿射密码——加密
    public static String affineEncrypt(String str, int[] key){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < str.length(); i++){
            stringBuilder.append( (char)((str.charAt(i) * key[0] + key[1])%26 + 65) );
        }
        return stringBuilder.toString();
    }
    //仿射密码——解密
    public static String affineDecrypt(String str, int[] key){
        StringBuilder stringBuilder = new StringBuilder();
        //乘法逆元 默认key[0] = 11, 对应逆元为19
        int inverse = 19;
        for (int i = 0; i < str.length(); i++){
            stringBuilder.append( (char)(((str.charAt(i)-key[1]) * inverse) % 26 + 65) );
        }
        return stringBuilder.toString();
    }


    //置换密码——加密
    public static String permutEncrypt(String str, int[] key){
        StringBuilder stringBuilder = new StringBuilder();
//        length = length + key.length - length % key.length;
//        System.out.println(str.length());
        int length = str.length();
        if (length % key.length != 0) {
            for(int i = 0; i < (key.length - length % key.length); i++){
                str += '0';
            }
        }
//        System.out.println("Size: "+ str.length());
        int j = 0, temp = str.length();
        for(int i = 0; i < temp; i++){
            int index = key[j];
            stringBuilder.append( str.charAt(index) );
            j++;
            if (j > key.length-1) {
                j %= key.length;
                str = str.substring(key.length);
//                System.out.println("aa : "+str.length());
                //若长度小于key.length， 补全末尾
/*                if(str.length() < key.length){
                    int temp = str.length();
                    for(int n = 0; n < key.length - temp; n++){
                        str += ' ';
                        System.out.println(str.length());
                    }
                }*/
            }
        }
//        System.out.println("----->" + stringBuilder.toString());
        return stringBuilder.toString();
    }
    //置换密码——解密
    public static String permutDecrypt(String str, int[] key){
//        System.out.println("In " + str.length());
        int[] subKey = new int[key.length];
        for(int i = 0; i < key.length; i++){
            for(int j = 0; j < key.length; j++){
                if(key[j] == i){
                    subKey[i] = j;
                    break;
                }
            }
        }
        return permutEncrypt(str, subKey).replaceAll("0", "");
    }


    //维吉尼亚密码——加密
    public static String vegeneEncrypt(String str, int[] key){
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0, length = str.length();
        for(int i = 0; i < length; i++){
            if(str.charAt(index) == 48){
                stringBuilder.append((char)( ((str.charAt(index) - 48) + key[index])%10 + 48));
            }else {
                stringBuilder.append((char) (((str.charAt(index) - 65) + key[index]) % 26 + 65));
            }
            index++;
            if(index > key.length - 1){
                index %= key.length;
                str = str.substring(key.length);
            }
        }
        return stringBuilder.toString();
    }
    //维吉尼亚密码——解密
    public static String vegeneDecrypt(String str, int[] key){
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0, length = str.length();
        for(int i = 0; i < length; i++){
            if(str.charAt(index) < 65){
                stringBuilder.append((char)( ((str.charAt(index) - 48 + 26*5) - key[index])%10 + 48));
            }else {
                stringBuilder.append((char) (((str.charAt(index) - 65 + 26*5) - key[index]) % 26 + 65));
            }
            index++;
            if(index > key.length - 1){
                index %= key.length;
                str = str.substring(key.length);
            }
        }
        return stringBuilder.toString();
    }

    public static int[] string2ASCII(String str){
        int[] array = new int[str.length()];
        for(int i = 0; i < str.length(); i++){
            array[i] = (int)str.charAt(i);
        }
        return array;
    }

    public static String ascii2String(int[] array){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < array.length; i++){
            stringBuilder.append((char)array[i]);
        }
        return stringBuilder.toString();
    }
}
