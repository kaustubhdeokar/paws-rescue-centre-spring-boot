package deokar.kaustubh.pawsrescuecenter.repo;

import deokar.kaustubh.pawsrescuecenter.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
