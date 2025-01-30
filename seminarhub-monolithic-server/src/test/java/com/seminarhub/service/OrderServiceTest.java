package com.seminarhub.service;

import com.seminarhub.entity.OrderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    Long[] seminarNoArr = new Long[] {24970171L, 24970172L, 24970173L, 24970174L};
    boolean[] visited = new boolean[seminarNoArr.length];
    public List<String> seminarPermutationList;
    int[] ret;
    public void seminarNoPermutation(int level, int maxSize){
        if(level == maxSize){
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<ret.length;i++){
                sb.append(ret[i]+" ");
            }
            seminarPermutationList.add(sb.toString());
            return ;
        }
        for(int i=0; i<seminarNoArr.length; i++){
            if(visited[i] == false){
                visited[i] = true;
                ret[level] = i;
                seminarNoPermutation(level + 1, maxSize);
                visited[i] = false;
            }
        }
    }

    @DisplayName("placeOrder Test")
    @Transactional
    @Test
    public void testPlaceOrderForSeminars(){
        long member_no = 10000001L;
        seminarPermutationList = new ArrayList<>();
        for(int i=1; i<=seminarNoArr.length; i++){
            ret = new int[i];
            seminarNoPermutation(0, i);
        }

//        for(String v: seminarPermutationList){
//            System.out.println(v+" ");
//        }

        Random random = new Random();
        AtomicInteger countAll = new AtomicInteger();
        AtomicInteger successNumber = new AtomicInteger();
        AtomicInteger failedNumber = new AtomicInteger();
        final int executeNumber = 1000;
        final ExecutorService executorService = Executors.newFixedThreadPool(100);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);

        for(int i=0; i<executeNumber; i++){
            executorService.execute( () -> {
                try{
                    int randomIndex = random.nextInt(seminarPermutationList.size());
                    String[] info = seminarPermutationList.get(randomIndex).split(" ");
                    List<OrderDTO> orderDtoList = new ArrayList<>();
                    for(int j=0; j<info.length; j++){
                        orderDtoList.add(new OrderDTO(member_no, seminarNoArr[Integer.parseInt(info[j])], 1L));;
                    }
                    try{
                        orderService.placeOrderForSeminars(orderDtoList);
                        countAll.addAndGet(info.length);
                        successNumber.addAndGet(1);
                    }catch(Exception e){
                        failedNumber.addAndGet(1);
                        throw e;
                    }
                } catch(Exception e){
                    System.out.println(e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        try{
            //모든 스레드가 종료될때까지 대기
            countDownLatch.await();
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted while waiting for completion");
        } finally{
            executorService.shutdown();
        }
        System.out.println("countAll:"+countAll);
        System.out.println("successNumber:"+successNumber);
        System.out.println("failedNumber:"+failedNumber);
    }
}