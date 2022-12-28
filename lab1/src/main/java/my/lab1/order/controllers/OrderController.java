package my.lab1.order.controllers;

import my.lab1.order.model.Order;
import my.lab1.order.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
public class OrderController
{

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderRepository repository;

    @GetMapping("/order/{id}")
    public Order get(@PathVariable("id") int orderId)
    {
        return repository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Заказ с id=" + orderId + " не найден"));
    }

    @GetMapping("/order")
    public List<Order> all()
    {
        return repository.findAll();
    }

    @PostMapping("/order")
    @RolesAllowed({"ROLE_ADMIN"})
    public int save(@RequestBody Order order)
    {
        repository.save(order);
        return order.getId();
    }

    @PutMapping("/order")
    @RolesAllowed({"ROLE_ADMIN"})
    public void update(@RequestBody Order order)
    {
        logger.info(order.toString());
        if (order.getId() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Поле id не заполнено");
        else
            repository.save(order);
    }

    @DeleteMapping("/order/{id}")
    @RolesAllowed({"ROLE_ADMIN"})
    public void delete(@PathVariable("id") int orderId)
    {
        try
        {
            repository.deleteById(orderId);
        }
        catch (EmptyResultDataAccessException erda)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Заказ с id=" + orderId + " не найден");
        }
    }

}
