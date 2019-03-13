package pl.fivarto.b2bplatform.appclient.models.dto;


import lombok.Data;

@Data
public class ProductDto {
        private  int externalId;
        private  String name;
        private  String ean;
        private  float vat;
        private  float count;
        private  float priceNetto;
        private  String group;
        private  String groupWho;
}
