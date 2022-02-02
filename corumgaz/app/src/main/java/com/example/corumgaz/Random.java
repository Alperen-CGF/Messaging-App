
package com.example.corumgaz;
import java.util.Random;

class RandomName {
    public static String getSaltString(){
        String SALTCHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder salt=new StringBuilder();
        Random rnd = new Random();
        while (salt.length()<18){
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
