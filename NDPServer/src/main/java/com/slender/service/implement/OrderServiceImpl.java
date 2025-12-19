package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.config.manager.JsonParserManager;
import com.slender.constant.other.RabbitMQConstant;
import com.slender.dto.order.OrderCreateRequest;
import com.slender.dto.order.OrderUpdateRequest;
import com.slender.entity.Order;
import com.slender.enumeration.order.OrderStatus;
import com.slender.exception.file.ExcelExportException;
import com.slender.exception.order.OrderNotFoundException;
import com.slender.exception.product.ProductNotFoundException;
import com.slender.mapper.OrderMapper;
import com.slender.model.message.OrderMessage;
import com.slender.repository.OrderRepository;
import com.slender.repository.ProductRepository;
import com.slender.repository.UserRepository;
import com.slender.service.interfase.OrderService;
import com.slender.vo.ListData;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final JsonParserManager jsonParserManager;
    @Override
    public ListData<Order> getList(Long uid, Boolean order) {
        List<Order> orders = orderRepository.getList(uid, order);
        return new ListData<>(orders.size(),orders);
    }

    @Override
    public void create(Long uid, OrderCreateRequest request) {
        BigDecimal totalAmount = productRepository.findById(request.getPid())
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                .orElseThrow(ProductNotFoundException::new);
        this.save(new Order(uid,request,totalAmount));
    }

    @Override
    public Order get(Long bid, Long uid) {
        final Order order = orderRepository.get(bid)
                .orElseThrow(OrderNotFoundException::new);
        if(!uid.equals(order.getUid())) throw new OrderNotFoundException();
        return order;
    }

    @Override
    public void delete(Long bid) {
        this.removeById(bid);
    }

    @Override
    public void update(Long bid, Long uid, OrderUpdateRequest request) {
        Order order = this.get(bid,uid);
        BigDecimal totalAmount=order.getQuantity().equals(request.getQuantity()) || request.getQuantity()==null ? null:
                productRepository.findById(order.getPid())
                        .map(product -> product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                        .orElseThrow(ProductNotFoundException::new);
        orderRepository.update(bid,request,totalAmount);
        if(request.getStatus() != null) {
            String message=jsonParserManager.format(new OrderMessage(bid,uid,request.getStatus()));
            rabbitTemplate.convertAndSend(RabbitMQConstant.ORDER_EXCHANGE, RabbitMQConstant.ORDER_STATUS_ROUTING_KEY, message);
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long bid, Long uid, OrderStatus status){
        Order order = this.get(bid,uid);
        OrderStatus oldStatus = order.getStatus();
        if(status==OrderStatus.PAID_SUCCESS && (oldStatus==OrderStatus.UNPAID || oldStatus==OrderStatus.PAID_FAIL)){
            userRepository.updateBalance(order.getUid(),order.getTotalAmount().negate());
            productRepository.updateRemain(order.getPid(),-order.getQuantity());
        }else if((status==OrderStatus.PAID_FAIL || status==OrderStatus.UNPAID) && oldStatus==OrderStatus.PAID_SUCCESS){
            userRepository.updateBalance(order.getUid(),order.getTotalAmount());
            productRepository.updateRemain(order.getPid(),order.getQuantity());
        }
        orderRepository.updateStatus(bid,status);
    }

    @Override
    public void export(HttpServletResponse response) {
        List<String> column = List.of("订单ID", "创建时间",  "用户ID", "商品ID", "地址ID",
                "数量", "总金额", "订单状态", "删除状态");
        List<Order> data = this.list();
        try(
            XSSFWorkbook excel=new XSSFWorkbook();
            ServletOutputStream out=response.getOutputStream()
        ){
            XSSFSheet sheet = excel.createSheet("orders");
            XSSFRow firstRow = sheet.createRow(0);

            int columnSize = column.size();
            for (int i = 0; i < columnSize; i++)
                firstRow.createCell(i).setCellValue(column.get(i));

            int len = data.size();
            for (int i = 0; i < len; i++) {
                XSSFRow row = sheet.createRow(i + 1);
                Order order = data.get(i);
                row.createCell(0).setCellValue(order.getBid());
                row.createCell(1).setCellValue(order.getCreateTime());
                row.createCell(2).setCellValue(order.getUid());
                row.createCell(3).setCellValue(order.getPid());
                row.createCell(4).setCellValue(order.getAid());
                row.createCell(5).setCellValue(order.getQuantity());
                row.createCell(6).setCellValue(order.getTotalAmount().doubleValue());
                row.createCell(7).setCellValue(order.getStatus().name());
                row.createCell(8).setCellValue(order.getDeleted().name());
            }
            response.setHeader("Content-Disposition", "attachment; filename=orders.xlsx");
            excel.write(out);
        }catch (IOException _){
            throw new ExcelExportException();
        }
    }
}
