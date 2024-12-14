import enit.ecomerce.search_product.product.Product;
import enit.ecomerce.search_product.product.ProductEntity;
import enit.ecomerce.search_product.repository.ProductRepository;
import enit.ecomerce.search_product.repository.ProducteEntityRepository;
import enit.ecomerce.search_product.service.InboxProductCreationService;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@Transactional
class InboxProductCreationServiceTest {

    @InjectMocks
    private InboxProductCreationService inboxProductCreationService;

    @Mock
    private ProducteEntityRepository productEntityRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    @Transactional
    void testTransactional_RollbackOnError() {
        // Arrange
        ProductEntity validProduct = new ProductEntity();
        validProduct.setId("1");
        validProduct.setName("Valid Product");
        validProduct.setPrice(100.0f);
        validProduct.setIndex(false);

        ProductEntity invalidProduct = new ProductEntity();
        invalidProduct.setId("2");
        invalidProduct.setName("Invalid Product");
        invalidProduct.setPrice(null); // This will cause an exception

        when(productEntityRepository.findUnindexedProducts()).thenReturn(List.of(validProduct, invalidProduct));

        doThrow(new IllegalArgumentException("Invalid product price"))
                .when(productRepository).save(any(Product.class));

        // Act
        try {
            inboxProductCreationService.treatInbox();
        } catch (Exception e) {
            // Exception expected
        }

        // Assert
        // Verify that the valid product was not saved because of rollback
        verify(productEntityRepository, never()).save(validProduct);
        verify(productRepository, times(1)).save(any(Product.class)); // Attempt to save was made
    }
}
