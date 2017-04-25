package com.example.xpack.bestbuy;

import java.util.ArrayList;

public class MergeSort {




    private ArrayList<Products> inputArray;
    private String type;

    public ArrayList<Products> getSortedArray() {
        return inputArray;
    }

    public MergeSort(ArrayList<Products> inputArray,String type){
        this.inputArray = inputArray;
        this.type=type;
    }

    public void sortGivenArray(){

        divide(0, this.inputArray.size()-1);
        if(!type.equals("salePrice")){
            swap();

        }
    }

    public void divide(int startIndex,int endIndex){

        //Divide till you breakdown your list to single element
        if(startIndex<endIndex && (endIndex-startIndex)>=1){
            int mid = (endIndex + startIndex)/2;
            divide(startIndex, mid);
            divide(mid+1, endIndex);

            //merging Sorted array produce above into one sorted array
            merger(startIndex,mid,endIndex);
        }
    }

    public void merger(int startIndex,int midIndex,int endIndex){

        //Below is the mergedarray that will be sorted array Array[i-midIndex] , Array[(midIndex+1)-endIndex]
        ArrayList<Products> mergedSortedArray = new ArrayList<Products>();

        int leftIndex = startIndex;
        int rightIndex = midIndex+1;
        float w,s;
        while(leftIndex<=midIndex && rightIndex<=endIndex){
            if(type.equals("salePrice")){
                if(Float.parseFloat(inputArray.get(leftIndex).salePrice)<=Float.parseFloat(inputArray.get(rightIndex).salePrice)){
                    mergedSortedArray.add(inputArray.get(leftIndex));
                    leftIndex++;
                }else{
                    mergedSortedArray.add(inputArray.get(rightIndex));
                    rightIndex++;
                }
            }else{
                if(inputArray.get(leftIndex).customerReview.equals("null")){
                     w= 0;
                }else{
                     w=Float.parseFloat(inputArray.get(leftIndex).customerReview);
                }
                if(inputArray.get(rightIndex).customerReview.equals("null")){
                     s=0;
                }else{
                     s=Float.parseFloat(inputArray.get(rightIndex).customerReview);
                }

                if(w<=s){
                    mergedSortedArray.add(inputArray.get(leftIndex));
                    leftIndex++;
                }else{
                    mergedSortedArray.add(inputArray.get(rightIndex));
                    rightIndex++;
                }
            }
            }



        //Either of below while loop will execute
        while(leftIndex<=midIndex){
            mergedSortedArray.add(inputArray.get(leftIndex));
            leftIndex++;
        }

        while(rightIndex<=endIndex){
            mergedSortedArray.add(inputArray.get(rightIndex));
            rightIndex++;
        }

        int i = 0;
        int j = startIndex;
        //Setting sorted array to original one
        while(i<mergedSortedArray.size()){
            inputArray.set(j, mergedSortedArray.get(i++));
            j++;
        }
    }

    private void swap(){
        int last=this.inputArray.size()-1;
        Products t;
        for(int i=0;i<(this.inputArray.size()/2);i++){


            t=inputArray.get(last);
            inputArray.set(last,inputArray.get(i));
            inputArray.set(i,t);
            last--;
        }
    }
}