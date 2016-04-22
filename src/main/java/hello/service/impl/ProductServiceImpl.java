package hello.service.impl;

import hello.domain.Product;
import hello.repository.ProductRepository;
import hello.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CounterService counterService;

    public Product create(Product request) {
        return productRepository.save(request);
    }

    public Page<Product> list(Pageable pageable) {
        counterService.increment("product.list");
        return productRepository.findAll(pageable);
    }

    public Page<Product> listByCategoryId(Long categoryId, Pageable pageable) {
        counterService.increment("product.list");
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Product get(Long id) {
        counterService.increment("product.get");
        return productRepository.findOne(id);
    }

    public Product update(Long id, Product request) {
        counterService.increment("product.update");
        Product product = productRepository.findOne(id);
        modelMapper.map(request, product);
        BeanUtils.copyProperties(request, product);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        counterService.increment("product.delete");
        productRepository.delete(id);
    }
}
