package com.example.vishvender.scantosolve;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Created by vishvender on 21/8/16.
 */
public class EvaluatePicture {
    public static int[][] readPixelsOfOriginalImage(Bitmap img){
        int width=img.getWidth();
        int height=img.getHeight();
        int pixel[][]=new int[img.getHeight()][img.getWidth()];
        int r,g,b;
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                int pix=img.getPixel(j,i);
                r= (int) (Color.red(pix)*0.299);
                b=(int)(Color.blue(pix)*0.587);
                g=(int)(Color.green(pix)*0.114);
                int sum=r+g+b;
                if(sum<100){
                    pixel[i][j]=1;
                }
                else{
                    pixel[i][j]=0;
                }
            }
        }
        return pixel;
    }
    public static Bitmap[] segmentPictures(Bitmap img,int[][] pixel){
        int i=0,j=1;
        int height=img.getHeight();
        int width=img.getWidth();
        int x,y=0;
        Bitmap series[]=new Bitmap[10];
        int k=0;
        while(i<width){
            x=startofimage(i,width,height,pixel);
            if(x==i){
                break;
            }
            Log.e("tu hi h hr jagah",x+" "+img.getWidth());
            y=endofimage(x,width,height,pixel);
            String filename="picno"+j+".png";
            Log.e("new part", x+"  "+y);
            Bitmap cropped=Bitmap.createBitmap(img,x,0,y-x,img.getHeight());
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filename);
                cropped.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
            series[k++]=cropped;
            i=y;
            j++;
        }
        return series;
    }
    public static int startofimage(int x,int width,int height,int[][] pixel){
        for(int i=x;i<width;i++){
            int flag=0;
            for(int j=0;j<height;j++){
                if(pixel[j][i]==1){
                    flag=1;
                    break;
                }
            }
            if(flag==1){
                x=i;
                if(x-10>1)	x=x-10;
                else x=1;
                break;
            }
        }
        return x;
    }
    public static int endofimage(int x,int width,int height,int[][] pixel){
        for(int i=x+10;i<width;i++){
            int flag=0;
            for(int j=0;j<height;j++){
                if(pixel[j][i]==1){
                    flag=0;
                    break;
                }
                else{
                    flag=1;
                }
            }
            if(flag==1){
                x=i+11;
                break;
            }
        }
        return x;
    }
    public static Bitmap setPadding(Bitmap imag){
        Bitmap scrap=null;
        int widthPadding=(imag.getHeight()-imag.getWidth())/2;
        Log.e("wp  ",widthPadding+"");
        scrap=Bitmap.createBitmap(widthPadding*2+imag.getWidth(),imag.getHeight(),Bitmap.Config.ARGB_8888);
        int white=0xffffffff;
        for(int i=0;i<scrap.getHeight();i++){
            for(int j=0;j<widthPadding;j++){
                scrap.setPixel(j,i,white);
            }
        }
        for(int i=0;i<scrap.getHeight();i++){
            for(int j=widthPadding;j<widthPadding+imag.getWidth();j++){
                scrap.setPixel(j,i,imag.getPixel(j-widthPadding,i));
            }
        }
        for(int i=0;i<scrap.getHeight();i++){
            for(int j=widthPadding+imag.getWidth();j<widthPadding*2+imag.getWidth();j++){
                scrap.setPixel(j,i,white);
            }
        }
        scrap=Bitmap.createScaledBitmap(scrap,28,28,true);
        return scrap;
    }
    public static String[] recognizepics(Bitmap series[], double[][] digit_weight_hidden,double[][] digit_weight_output,double[][] operator_weight_hidden,double[][] operator_weight_output){
        int k=0;
        String []expectedDigit=new String[2];
        expectedDigit[0]="1111";
        expectedDigit[1]="2222";
        String exp="";
        while(k<3){
            Bitmap img= series[k];
            img=setPadding(img);
            Log.e("dim   ",img.getWidth()+" "+img.getHeight());
            int[] pixels=readPixels(img);
            if(k%2!=1){
                double[] res=evaluate_DIGIT(pixels,digit_weight_hidden,digit_weight_output);
                double max=0;
                int pos=0;
                for(int i=0;i<10;i++){
                    Log.e("yes",i+" "+res[i]);
                    if(res[i]>max){
                        pos=i;
                        max=res[i];
                    }
                }
                exp+=String.valueOf(pos)+"";
            }
            else{
                double[] res=evaluate_OPERATOR(pixels,operator_weight_hidden,operator_weight_output);
                double max=0;
                int pos=0;
                for(int i=0;i<4;i++){
                    if(res[i]>max){
                        pos=i;
                        max=res[i];
                    }
                }
                String a=null;
                if(pos==0) a="+";
                else if(pos==1) a="-";
                else if(pos==2) a="x";
                else if(pos==3) a="/";
                exp+=a+"";
            }
            k++;
        }
        expectedDigit[0]=exp;
        Log.e("expression",expectedDigit[0]);
        int a=Integer.parseInt(""+expectedDigit[0].charAt(0));
        int b=Integer.parseInt(""+expectedDigit[0].charAt(2));
        char op=expectedDigit[0].charAt(1);
        int result=0;
        if(op=='+') result=a+b;
        else if(op=='-') result=a-b;
        else if(op=='x') result=a*b;
        else if(op=='/') result=a/b;
        System.out.println(result);
        expectedDigit[1]=result+"";
        return expectedDigit;
    }
    public static double[][] readCSVfile(BufferedReader bf, int rows, int cols) throws IOException {
        double[][] weight=new double[rows][cols];
        String line=null;
        int j=0;
        while((line=bf.readLine())!=null){
            weight[j++]=getDoubleArrayFromStringArray(line.split(","));
        }
        return weight;
    }
    public static double[] getDoubleArrayFromStringArray(String []array){
        double[] weight=new double[array.length];
        for(int i=0;i<array.length;i++){
            weight[i]=Double.parseDouble(array[i]);
        }
        return weight;
    }
    public static int[] readPixels(Bitmap imag){
        int r,g,b;
        int mask=0x000000ff;
        int pixel[]=new int[785];
        pixel[0]=1;
        for(int i=0;i<28;i++){
            for(int j=0;j<28;j++){
                int pix=imag.getPixel(j,i);
                r= (int) (Color.red(pix)*0.299);
                b=(int)(Color.blue(pix)*0.587);
                g=(int)(Color.green(pix)*0.114);
                int sum=r+g+b;
                int newpix=Color.rgb(sum,sum,sum);
                imag.setPixel(j,i,newpix);
                if(sum<150){
                    pixel[j+28*i+1]=1;
                }
                else{
                    pixel[j+28*i+1]=0;
                }
            }
        }
        return pixel;
    }
    public static double[] evaluate_DIGIT(int[] pix,double[][] wh,double[][] wo){
        double[] y=new double[101];
        y[0]=1;
        for(int i=0;i<100;i++){
            double sum=0;
            for(int j=0;j<785;j++){
                sum=sum+(wh[j][i])*(pix[j]);
            }
            y[i+1]=Sigmoid(sum);
        }
        double[] z=new double[10];
        for(int i=0;i<10;i++){
            double sum=0;
            for(int j=0;j<101;j++){
                sum=sum+(wo[j][i])*(y[j]);
            }
            z[i]=Sigmoid(sum);
        }
        return z;
    }
    public static double[] evaluate_OPERATOR(int[] pix,double[][] wh,double[][] wo){
        double[] y=new double[51];
        y[0]=1;
        for(int i=0;i<50;i++){
            double sum=0;
            for(int j=0;j<785;j++){
                sum=sum+(wh[j][i])*(pix[j]);
            }
            y[i+1]=Sigmoid(sum);
        }
        double[] z=new double[4];
        for(int i=0;i<4;i++){
            double sum=0;
            for(int j=0;j<51;j++){
                sum=sum+(wo[j][i])*(y[j]);
            }
            z[i]=Sigmoid(sum);
        }
        return z;
    }
    public static double Sigmoid(double z){
        return 1/(1+Math.exp(-z));
    }
}
