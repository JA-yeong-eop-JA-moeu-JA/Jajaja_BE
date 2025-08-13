package com.jajaja.domain.order.service;

import com.jajaja.domain.order.dto.response.*;
import com.jajaja.domain.order.entity.Order;
import com.jajaja.domain.order.repository.OrderRepository;
import com.jajaja.domain.team.entity.Team;
import com.jajaja.domain.team.entity.enums.TeamStatus;
import com.jajaja.global.apiPayload.code.status.ErrorStatus;
import com.jajaja.global.apiPayload.exception.custom.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderRepository orderRepository;

    @Override
    public PagingOrderListResponseDto getMyOrders(Long memberId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByMemberId(memberId, pageable);
        List<OrderListDto> orderListDtos = orderPage.getContent().stream()
                .map(order -> {
                    TeamStatus teamStatus = Optional.ofNullable(order.getTeam())
                            .map(Team::getStatus)
                            .orElse(null);
                    LocalDateTime teamCreatedAt = Optional.ofNullable(order.getTeam())
                            .map(Team::getCreatedAt)
                            .orElse(null);
                    List<OrderItemDto> items = order.getOrderProducts().stream()
                            .map(orderProduct -> OrderItemDto.of(orderProduct, teamStatus, teamCreatedAt))
                            .collect(Collectors.toList());
                    return OrderListDto.of(order, items);
                })
                .collect(Collectors.toList());
        return PagingOrderListResponseDto.of(orderPage, orderListDtos);
    }

    @Override
    public OrderDetailResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ErrorStatus.ORDER_NOT_FOUND));
        TeamStatus teamStatus = Optional.ofNullable(order.getTeam())
                .map(Team::getStatus)
                .orElse(null);
        LocalDateTime teamCreatedAt = Optional.ofNullable(order.getTeam())
                .map(Team::getCreatedAt)
                .orElse(null);
        List<OrderItemDto> items = order.getOrderProducts().stream()
                .map(orderProduct -> OrderItemDto.of(orderProduct, teamStatus, teamCreatedAt))
                .collect(Collectors.toList());
        return OrderDetailResponseDto.of(order, items);
    }
}
