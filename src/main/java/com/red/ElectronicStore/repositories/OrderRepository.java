package com.red.ElectronicStore.repositories;

import com.red.ElectronicStore.entities.Order;
import com.red.ElectronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findByUser(User user);
}
