package com.example.demo.OAuthLogin;

public class RandomUtil {
    
    private RandomUtil(){}

    public static Integer getRandomInteger(Integer min,Integer max){/* 랜덤하게 숫자를 뽑는 함수 */
        return min+(int)(Math.random()*((max-min)+1L));
    }

    public static String getRandomString(Integer length){/* 랜덤한 문자열을 length 길이로 생성하기 위한 함수 */
        String chars="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()";
        StringBuilder sb=new StringBuilder();
        Integer charLength=(int)chars.length();
        for(int i=0;i<length;i++){
            int index=(int)getRandomInteger(0,charLength-1);
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

}
