package com.example.productos.jsonapi;

public class JsonApiError {
    private ErrorObject[] errors;

    public JsonApiError(String title, String detail, String status) {
        this.errors = new ErrorObject[] { new ErrorObject(title, detail, status) };
    }

    public ErrorObject[] getErrors() {
        return errors;
    }

    public static class ErrorObject {
        private String title;
        private String detail;
        private String status;

        public ErrorObject(String title, String detail, String status) {
            this.title = title;
            this.detail = detail;
            this.status = status;
        }

        public String getTitle() { return title; }
        public String getDetail() { return detail; }
        public String getStatus() { return status; }
    }
}