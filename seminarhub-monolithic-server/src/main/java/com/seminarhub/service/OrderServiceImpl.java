package com.seminarhub.service;

import com.seminarhub.entity.OrderDTO;
import com.seminarhub.entity.SeminarDTO;
import com.seminarhub.exception.SeminarRegistrationFullException;
import com.seminarhub.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final SeminarService seminarService;

    public OrderServiceImpl(OrderRepository orderRepository, SeminarService seminarService) {
        this.orderRepository = orderRepository;
        this.seminarService = seminarService;
    }

    /***
     * @param orderDTOList
      [
          {
              "seminar_no": 100,
              "member_no": 1,
              "quantity": 1
          },
          {
              "seminar_no": 101,
              "member_no": 2,
              "quantity": 2
          }
      ]
     * @throws SeminarRegistrationFullException
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public void placeOrderForSeminars(List<OrderDTO> orderDTOList) throws SeminarRegistrationFullException {
        Collections.sort(orderDTOList, (dto1, dto2) -> {
            if(dto1.getSeminar_no() > dto2.getSeminar_no()) return 1;
            else if(dto1.getSeminar_no() < dto2.getSeminar_no()) return -1;
            else return 0;
        });
        for(int i=0; i<orderDTOList.size(); i++){
            try{
                System.out.println(orderDTOList.get(i).toString());
                placeOrderForSingleSeminar(orderDTOList.get(i));
            }catch(Throwable e){
                e.printStackTrace();
                throw e;
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void placeOrderForSingleSeminar(OrderDTO order) {
        SeminarDTO seminarDTO = seminarService.getSeminarBySeminarNoWithPessimisticLock(order.getSeminar_no());

        if (seminarDTO == null) {
            throw new SeminarRegistrationFullException("There are no Info Of Member || Seminar");
        }

        Long currentParticipateCnt = seminarDTO.getAvailable_seats();
        if ((currentParticipateCnt) <= 0) {
            throw new SeminarRegistrationFullException("SeminarInfo:" + seminarDTO.getName() + "is already " + currentParticipateCnt + "/" + seminarDTO.getMax_capacity() + " full. Registration failed.");
        }
        seminarService.decrementSeminarAvailableSeats(seminarDTO);
        orderRepository.insert(order);
    }

    @Override
    public int createOrder(OrderDTO order) {
        return orderRepository.insert(order);
    }

    @Override
    public OrderDTO getOrderById(int order_no) {
        return orderRepository.findOrderById(order_no);
    }

    @Override
    public List<OrderDTO> getOrdersByMemberNo(int member_no) {
        return orderRepository.findOrdersByMemberNo(member_no);
    }

    @Override
    public int updateOrder(OrderDTO order) {
        return orderRepository.update(order);
    }

    @Override
    public int softDeleteOrder(Long orderNo) {
        return orderRepository.softDelete(orderNo);
    }
}