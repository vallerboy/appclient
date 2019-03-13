package pl.fivarto.b2bplatform.appclient.models.forms;


import lombok.Data;


@Data
public class ProductUpdateForm {
    private String name;
    private float quantity;
    private String ean;
    private boolean isDisplay;
    private float priceNetto;
    private float vat;
    private String description;
    private String group;
    private String groupWho;
}
