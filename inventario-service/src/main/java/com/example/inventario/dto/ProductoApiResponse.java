package com.example.inventario.dto;

public class ProductoApiResponse {
    private Data data;

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    public static class Data {
        private String type;
        private String id;
        private ProductoResponse attributes;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public ProductoResponse getAttributes() { return attributes; }
        public void setAttributes(ProductoResponse attributes) { this.attributes = attributes; }
    }
}