package deokar.kaustubh.pawsrescuecenter.repo;

import deokar.kaustubh.pawsrescuecenter.model.Cart;
import deokar.kaustubh.pawsrescuecenter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {

    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);
}
