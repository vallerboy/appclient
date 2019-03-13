package pl.fivarto.b2bplatform.appclient.controllers.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.fivarto.b2bplatform.appclient.models.dto.ProductDto;
import pl.fivarto.b2bplatform.appclient.models.services.ProductService;

import java.util.List;

@RestController
public class ApiController {

    @Value("${api.key}")
    String apiKeyFromConfig;

    final ProductService productService;

    @Autowired
    public ApiController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/product/update")
    public ResponseEntity getAllProducts(@RequestHeader("api-key") String apiKey,
                                         @RequestBody List<ProductDto> productDto){
        if(!apiKey.equals(apiKeyFromConfig)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad api-key");
        }

        productService.updateProducts(productDto);
        return ResponseEntity.ok().build();
    }




}
