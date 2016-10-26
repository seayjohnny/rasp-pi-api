package raspipi.api.objects;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ResponseObject {
    private Integer Status;
    private Map<String, String> Header;
    private Map<String, Object> Body;

    public ResponseObject() {
        this.setStatus(0);
        this.setHeader(null);
        this.setBody(null);
    }

    public ResponseObject(Integer status){
        this.setStatus(status);
        this.setHeader(null);
        this.setBody(null);
    }

    public ResponseObject(Integer status, Map<String, String> header){
        this.setStatus(status);
        this.setHeader(header);
        this.setBody(null);
    }

    public ResponseObject(Integer status, Map<String, String> header, Map<String, Object> body){
        this.setStatus(status);
        this.setHeader(header);
        this.setBody(body);
    }

    public ResponseObject(HttpResponse response) {
        this.setStatus(response.getStatusCode());

        HttpHeaders httpHeaders = response.getHeaders();
        Iterator iterator = httpHeaders.keySet().iterator();
        Map<String, String> header = new HashMap<>();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            header.put(key, httpHeaders.getHeaderStringValues(key).toString());
        }
        this.setHeader(header);

        try {
            this.setBody(convertStreamToMap(response.getContent()));
        } catch (IOException e) {
            System.err.println(e.getMessage().toString());
        }
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public Map<String, String> getHeader() {
        return Header;
    }

    public void setHeader(Map<String, String> header) {
        Header = header;
    }

    public Map<String, Object> getBody() {
        return Body;
    }

    public void setBody(Map<String, Object> body) {
        Body = body;
    }

    private Map<String, Object> convertStreamToMap(java.io.InputStream is) {
        Map<String, Object> map = new HashMap<>();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");

        String string = s.hasNext() ? s.next() : "";
        string = string.substring(2, string.lastIndexOf('}'));
        String[] pairs = string.split(",");
        for(int i=0;i<pairs.length;i++){
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            String key = keyValue[0].substring(1, keyValue[0].lastIndexOf('"'));
            map.put(key, keyValue[1]);
        }

        return map;
    }
}
