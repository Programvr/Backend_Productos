package com.example.inventario.jsonapi;

public class JsonApiResponse<T> {
    private Data<T> data;

    public JsonApiResponse(String type, String id, T attributes) {
        this.data = new Data<>(type, id, attributes);
    }

    public Data<T> getData() {
        return data;
    }

    public static class Data<T> {
        private String type;
        private String id;
        private T attributes;

        public Data(String type, String id, T attributes) {
            this.type = type;
            this.id = id;
            this.attributes = attributes;
        }

        public String getType() { return type; }
        public String getId() { return id; }
        public T getAttributes() { return attributes; }
    }
}